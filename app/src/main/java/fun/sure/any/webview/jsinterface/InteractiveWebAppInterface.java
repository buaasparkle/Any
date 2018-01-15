package fun.sure.any.webview.jsinterface;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yuantiku.android.common.app.util.L;

import org.jivesoftware.smack.util.Base64;

import java.io.Serializable;

import fun.sure.any.common.util.GsonHelper;
import fun.sure.any.webview.jsinterface.bean.BaseBean;
import fun.sure.any.webview.jsinterface.bean.CreateStrokePageBean;
import fun.sure.any.webview.jsinterface.bean.ForumEnableBean;
import fun.sure.any.webview.jsinterface.bean.StrokePageVisibleBean;
import fun.sure.any.webview.jsinterface.bean.WebAppEventBean;
import fun.sure.any.webview.jsinterface.delegate.WebAppApiDelegate;

/**
 * Created by wangshuo on 23/01/2018.
 */

public class InteractiveWebAppInterface implements Serializable {

    private static final String TAG = InteractiveWebAppInterface.class.getSimpleName();

    private WebView webView;
    private Handler uiThreadHandler;

    @UiThread
    public InteractiveWebAppInterface(WebView webView) {
        this.webView = webView;
        uiThreadHandler = new Handler(Looper.getMainLooper());
    }

    //region delegate

    private WebAppApiDelegate webAppApiDelegate;

    public void setWebAppApiDelegate(WebAppApiDelegate webAppApiDelegate) {
        this.webAppApiDelegate = webAppApiDelegate;
    }

    //endregion

    @JavascriptInterface
    public void emit(String base64) {
        final WebAppEventBean bean = parseBean(base64, WebAppEventBean.class);
        run(new Runnable() {
            @Override
            public void run() {
                if (webAppApiDelegate != null) {
                    webAppApiDelegate.onEventEmit(bean);
                }
            }
        });
        end(bean);
    }

    @JavascriptInterface
    public void createStrokePage(String base64) {
        final CreateStrokePageBean bean = parseBean(base64, CreateStrokePageBean.class);
        run(new Runnable() {
            @Override
            public void run() {
                if (webAppApiDelegate != null) {
                    webAppApiDelegate.onCreateStrokePage(bean);
                }
            }
        });
    }

    @JavascriptInterface
    public void toggleStrokePageVisible(String base64) {
        final StrokePageVisibleBean bean = parseBean(base64, StrokePageVisibleBean.class);
        run(new Runnable() {
            @Override
            public void run() {
                if (webAppApiDelegate != null) {
                    webAppApiDelegate.onStrokePageVisibleToggled(bean);
                }
            }
        });
    }

    @JavascriptInterface
    public void toggleForumEnabled(String base64) {
        final ForumEnableBean bean = parseBean(base64, ForumEnableBean.class);
        run(new Runnable() {
            @Override
            public void run() {
                if (webAppApiDelegate != null) {
                    webAppApiDelegate.onForumEnableToggled(bean);
                }
            }
        });
    }
    //region utils

    private void end(final BaseBean bean) {
        if (bean != null) {
            evalJs(bean.callback, null, bean.getParam());
        }
    }

    /**
     * @param callback 回调函数名
     * @param error    错误信息
     * @param param    正常参数
     */
    public void evalJs(final String callback, final String error, final String param) {
        run(new Runnable() {
            @Override
            public void run() {
                String baseStr;
                if (error == null) {
                    baseStr = "[0," + param + "]";
                } else {
                    baseStr = "[\"" + error + "\"," + param + "]";
                }
                String base64Encode = Base64.encodeBytes(baseStr.getBytes());
                base64Encode = base64Encode.contains("\n")
                        ? base64Encode.replaceAll("\n", "") : base64Encode;
                webView.loadUrl(String.format("javascript:window.%s(\"%s\")", callback, base64Encode));
            }
        });
    }

    private void run(Runnable runnable) {
        uiThreadHandler.post(runnable);
    }

    private static <T extends BaseBean> T parseBean(String base64, Class<T> clazz) {
        try {
            String json = new String(Base64.decode(base64));
            L.d("js param : ", json);
            JsonObject jsonObject = GsonHelper.fromJson(json, JsonObject.class);
            T res = parseBean(jsonObject, clazz);
            if (jsonObject.has("callback")) {
                res.callback = jsonObject.get("callback").getAsString();
            }
            return res;
        } catch (Exception e) {
            L.e(TAG, e);
            return null;
        }
    }

    @NonNull
    private static <T extends BaseBean> T parseBean(JsonObject jsonObject,
            Class<T> clazz) {
        if (jsonObject == null) {
            return createEmptyBean(clazz);
        }
        JsonElement argumentsObject = jsonObject.get("arguments");
        if (argumentsObject == null || !argumentsObject.isJsonArray()) {
            return createEmptyBean(clazz);
        }
        if (argumentsObject.getAsJsonArray().size() < 1) {
            return createEmptyBean(clazz);
        }
        T res = GsonHelper.fromJson(argumentsObject.getAsJsonArray().get(0), clazz);
        if (res == null) {
            return createEmptyBean(clazz);
        } else {
            return res;
        }
    }

    @NonNull
    private static <T extends BaseBean> T createEmptyBean(Class<T> clazz) {
        return GsonHelper.fromJson("{}", clazz);
    }

    //endregion
}
