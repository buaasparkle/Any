package fun.sure.any.websocket;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Log;

import com.yuantiku.android.common.util.UnitUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import fun.sure.any.common.data.Action0;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.ws.WebSocket;
import okhttp3.ws.WebSocketCall;
import okhttp3.ws.WebSocketListener;
import okio.Buffer;

/**
 * Created by wangshuo on 26/10/2017.
 */

public class LarkClient {

    /**
     * 自定义的状态码(4000-4999)，close socket 的时候表明原因
     * https://tools.ietf.org/html/rfc6455#section-7.4.1
     */
    private enum StatusCode {

        FAILURE(4000, "web socket connect onFailure"), // 建立 web socket 连接时失败
        SEND_MSG_EXCEPTION(4001, "send message throws IO exception"), // 发送数据时出现异常
        INACTIVE_TIME_OUT(4002, "inactive more than 5 minutes"), // 长时间处于非激活状态
        ;

        int code;
        String reason;

        StatusCode(int code, String reason) {
            this.code = code;
            this.reason = reason;
        }
    }

    private static final String TAG = "LarkClient";

    private static LarkClient instance;

    public static LarkClient getInstance() {
        if (instance == null) {
            synchronized (LarkClient.class) {
                if (instance == null) {
                    instance = new LarkClient();
                }
            }
        }
        return instance;
    }

    private Timer timer;
    private long lastActiveTime;

    private WebSocketCall webSocketCall;
    private WebSocket webSocket;

    private boolean isConnecting;
    private boolean isConnected;

    private Queue<Action0> pendingQueue;
    private List<Action0> retryActionList;

    private HandlerThread handlerThread;
    private Handler handler;

    private LarkClient() {
        isConnected = false;
        isConnecting = false;

        pendingQueue = new LinkedBlockingQueue<>();
        retryActionList = new ArrayList<>();

        start(null);
    }

    public void start(final Action0 connectedCallback) {
        logThread("start");
        if (connectedCallback != null) {
            pendingQueue.add(connectedCallback);
        }

        if (isConnecting) {
            return;
        }

        isConnecting = true;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(0, TimeUnit.SECONDS)
                .readTimeout(0, TimeUnit.SECONDS)
                .writeTimeout(0, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder().url(getLarkUrl())
                .addHeader("Connection", "Upgrade")
                .addHeader("Origin", getLarkUrl()).build();
        webSocketCall = WebSocketCall.create(client, request);
        webSocketCall.enqueue(new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                // This method is called on the message reading thread
                // DO NOT use this callback to write to the web socket.
                // Start a new thread or use another thread in your application.
                logThread("onOpen");
                onWebSocketOpen(webSocket);
            }

            @Override
            public void onFailure(IOException e, Response response) {
                Log.d(TAG, Log.getStackTraceString(e));
                logThread("onFailure");
                close(StatusCode.FAILURE);
            }

            @Override
            public void onMessage(ResponseBody message) throws IOException {
                logThread("onMessage");
                logText(message.string());
            }

            @Override
            public void onPong(Buffer payload) {

            }

            @Override
            public void onClose(int code, String reason) {
                logThread("onClose");
                isConnecting = false;
                isConnected = false;
                retryActionList.addAll(pendingQueue);
                pendingQueue.clear();
            }
        });
        resetAutoCloseMonitor();
    }

    private String getLarkUrl() {
        return "ws://echo.websocket.org";
    }

    private void onWebSocketOpen(final WebSocket webSocket) {
        isConnecting = false;
        isConnected = true;
        this.webSocket = webSocket;
        startHandlerThread();
        postAction(new Action0() {
            @Override
            public void apply() {
                sendHeader("header");
                pendingQueue.addAll(retryActionList);
                while (!pendingQueue.isEmpty()) {
                    Action0 callback = pendingQueue.poll();
                    callback.apply();
                }
            }
        });
    }

    private void startHandlerThread() {
        closeHandlerThread();
        handlerThread = new HandlerThread("LarkClientThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    private void closeHandlerThread() {
        if (handlerThread != null) {
            handlerThread.quit();
            handlerThread = null;
        }
    }

    private void postAction(final Action0 action) {
        if (handler != null && action != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    logThread("postAction");
                    action.apply();
                }
            });
        }
    }

    public void sendHeader(String data) {
        logThread("sendHeader");
        sendData(data);
    }

    public void sendEntry(String data) {
        logThread("sendEntry");
        sendData(data);
    }

    private void sendData(final String data) {
        if (data == null) {
            return;
        }
        logThread("sendData");
        Action0 sendAction = new Action0() {
            @Override
            public void apply() {
                try {
                    logText(data);
                    webSocket.sendMessage(RequestBody.create(WebSocket.TEXT, data));
                    lastActiveTime = System.currentTimeMillis();
                } catch (IOException | IllegalStateException e) {
                    close(StatusCode.SEND_MSG_EXCEPTION);
                }
            }
        };

        if (isConnected) {
            postAction(sendAction);
        } else {
            start(sendAction);
        }
    }

    private void resetAutoCloseMonitor() {
        lastActiveTime = System.currentTimeMillis();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (System.currentTimeMillis() - lastActiveTime >= 5 * UnitUtils.MINUTE) {
                    close(StatusCode.INACTIVE_TIME_OUT);
                }
            }
        }, 0, 30 * UnitUtils.SECOND);
    }

    public void close(@NonNull StatusCode statusCode) {
        logThread("close");
        if (timer != null) {
            timer.cancel();
        }
        if (webSocketCall != null && isConnected) {
            webSocketCall.cancel();
            isConnected = false;
        }
        if (webSocket != null) {
            try {
                webSocket.close(statusCode.code, statusCode.reason);
            } catch (IOException | IllegalStateException e) {
                logText(Log.getStackTraceString(e));
            }
        }
        closeHandlerThread();
    }

    private void logThread(String where) {
        Log.d(TAG, "[Thread]" + where + " = " + Thread.currentThread().getName());
    }

    private void logText(String text) {
        Log.d(TAG, "[Text]" + text);
    }
}
