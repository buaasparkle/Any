package fun.sure.any.websocket;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
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
        CLOSE(4001, "web socket connect onClose"), // websocket.close 之后 触发的回调
        SEND_MSG_EXCEPTION(4002, "send message throws IO exception"), // 发送数据时出现异常
        INACTIVE_TIME_OUT(4003, "inactive more than 5 minutes"), // 长时间处于非激活状态
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

    private HandlerThread handlerThread;
    private WorkHandler handler;

    //region work handler

    private class WorkHandler extends Handler {

        private static final int MSG_INIT = 0;
        private static final int MSG_CLOSE = MSG_INIT + 1;
        private static final int MSG_DATA = MSG_CLOSE + 1;
        private static final int MSG_ON_OPEN = MSG_DATA + 1;

        WorkHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INIT:
                    init();
                    break;

                case MSG_CLOSE:
                    StatusCode statusCode = (StatusCode) msg.obj;
                    close(statusCode);
                    break;

                case MSG_DATA:
                    if (msg.obj instanceof String) {
                        sendData((String) msg.obj);
                    }
                    break;

                case MSG_ON_OPEN:
                    if (msg.obj instanceof WebSocket) {
                        onWebSocketOpen((WebSocket) msg.obj);
                    }
                    break;
            }
        }
    }

    private void sendInitMsg() {
        Message message = handler.obtainMessage();
        message.what = WorkHandler.MSG_INIT;
        handler.sendMessage(message);
    }

    private void sendCloseMsg(StatusCode statusCode) {
        Message message = handler.obtainMessage();
        message.what = WorkHandler.MSG_CLOSE;
        message.obj = statusCode;
        handler.sendMessage(message);
    }

    private void sendDataMsg(String data) {
        Message message = handler.obtainMessage();
        message.what = WorkHandler.MSG_DATA;
        message.obj = data;
        handler.sendMessage(message);
    }

    private void sendOnOpenMsg(WebSocket webSocket) {
        Message message = handler.obtainMessage();
        message.what = WorkHandler.MSG_ON_OPEN;
        message.obj = webSocket;
        handler.sendMessage(message);
    }

    //endregion

    private LarkClient() {
        startHandlerThread();
        sendInitMsg();
    }

    public void release() {
        closeHandlerThread();
        instance = null;
    }

    private void startHandlerThread() {
        closeHandlerThread();
        handlerThread = new HandlerThread("LarkClientThread");
        handlerThread.start();
        handler = new WorkHandler(handlerThread.getLooper());
    }

    private void closeHandlerThread() {
        if (handlerThread != null) {
            handlerThread.quit();
            handlerThread = null;
        }
    }

    private void init() {
        logThread("init");
        isConnected = false;
        isConnecting = false;

        pendingQueue = new LinkedList<>();
    }

    private void start(final Action0 connectedCallback) {
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
                sendOnOpenMsg(webSocket);
            }

            @Override
            public void onFailure(IOException e, Response response) {
                Log.d(TAG, Log.getStackTraceString(e));
                logThread("onFailure");
                sendCloseMsg(StatusCode.FAILURE);
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
                sendCloseMsg(StatusCode.CLOSE);
            }
        });
        resetAutoCloseMonitor();
    }

    private String getLarkUrl() {
        return "ws://echo.websocket.org";
    }

    private void onWebSocketOpen(final WebSocket webSocket) {
        logThread("onWebSocketOpen");
        isConnecting = false;
        isConnected = true;
        LarkClient.this.webSocket = webSocket;
        sendHeader("header");
        while (!pendingQueue.isEmpty()) {
            Action0 callback = pendingQueue.poll();
            callback.apply();
        }
    }

    private void sendHeader(String data) {
        logThread("sendHeader");
        sendDataMsg(data);
    }

    public void sendEntry(String data) {
        logThread("sendEntry");
        sendDataMsg(data);
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
                    logThread("send message");
                    webSocket.sendMessage(RequestBody.create(WebSocket.TEXT, data));
                    lastActiveTime = System.currentTimeMillis();
                } catch (IOException | IllegalStateException e) {
                    sendCloseMsg(StatusCode.SEND_MSG_EXCEPTION);
                }
            }
        };

        logText("isConnected = " + isConnected);
        if (isConnected) {
            sendAction.apply();
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
                if (System.currentTimeMillis() - lastActiveTime >= 5 * 1000) {
                    logThread("timer run");
                    sendCloseMsg(StatusCode.INACTIVE_TIME_OUT);
                }
            }
        }, 0, 30 * 1000);
    }

    private void close(@NonNull StatusCode statusCode) {
        logThread("close");
        isConnecting = false;
        isConnected = false;

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
    }

    private void logThread(String where) {
        Log.d(TAG, "[Thread]" + where + " , id = " + Thread.currentThread().getId() +
                ", name = " + Thread.currentThread().getName());
    }

    private void logText(String text) {
        Log.d(TAG, "[Text]" + text);
    }
}
