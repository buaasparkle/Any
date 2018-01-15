package fun.sure.any.webview;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.yuantiku.android.common.toast.ToastUtils;

import org.jivesoftware.smack.util.Base64;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import fun.sure.any.R;
import fun.sure.any.common.util.GsonHelper;
import fun.sure.any.webview.jsinterface.InteractiveWebBrowserManager;
import fun.sure.any.webview.jsinterface.InteractiveWebAppInterface;
import fun.sure.any.webview.jsinterface.bean.CreateStrokePageBean;
import fun.sure.any.webview.jsinterface.bean.ForumEnableBean;
import fun.sure.any.webview.jsinterface.bean.StrokePageVisibleBean;
import fun.sure.any.webview.jsinterface.bean.WebAppEventBean;
import fun.sure.any.webview.jsinterface.bean.WebAppEventBean.Biz;
import fun.sure.any.webview.jsinterface.delegate.WebAppApiDelegate;

/**
 * @author Jing
 * @date 20/07/2017
 */

public class InteractiveBrowserView extends FrameLayout implements WebAppApiDelegate {

    public static void configBrowser(WebView webView, InteractiveWebAppInterface webViewInterface) {
        if (webView == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 测试包开启WebView调试模式
            WebView.setWebContentsDebuggingEnabled(true);
        }
        webView.getSettings().setUserAgentString(webView.getSettings().getUserAgentString() + " YuanFuDao");

        webViewInterface = webViewInterface != null ?
                webViewInterface : new InteractiveWebAppInterface(webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(webViewInterface, "WebView");
        // http://stackoverflow.com/questions/28626433/android-webview-blocks-redirect-from-https-to-http
        // 允许从https重定向到http
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // 支持 Cross Domain
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
    }

    private WebView webView;
    private View progressView;

    public InteractiveBrowserView(Context context) {
        this(context, null);
    }

    public InteractiveBrowserView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InteractiveBrowserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.live_view_interactive_browser, this);
        progressView = findViewById(R.id.live_progress);
        webView = (WebView) findViewById(R.id.live_web_view);
        webView.setWebViewClient(new WebViewClient() {

            // http://dontcry2013.github.io/2016/03/11/android-webview-change-header/
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (isHttpScheme(url)) {
                    try {
                        URL newUrl = new URL(injectIsParams(url));
                        URLConnection connection = newUrl.openConnection();
                        return new WebResourceResponse(connection.getContentType(),
                                connection.getHeaderField("encoding"), connection.getInputStream());
                    } catch (IOException e) {
                    }
                }
                return null;
            }
        });
    }

    private boolean isHttpScheme(String url) {
        if (!TextUtils.isEmpty(url) && Uri.parse(url).getScheme() != null) {
            String scheme = Uri.parse(url).getScheme().trim();
            return scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https");
        }
        return false;
    }

    private String injectIsParams(String url) {
        return url;
    }

    public InteractiveWebAppInterface getWebAppInterface() {
        InteractiveWebAppInterface webAppInterface = new InteractiveWebAppInterface(webView);
        webAppInterface.setWebAppApiDelegate(this);
        return webAppInterface;
    }

    public void loadUrl(String url) {
        showProgress();
        webView.loadUrl(url);
    }

    private void showProgress() {
        progressView.setVisibility(VISIBLE);
    }

    private void dismissProgress() {
        progressView.setVisibility(GONE);
    }

    public WebView getWebView() {
        return webView;
    }

    @Override
    public void onEventEmit(WebAppEventBean bean) {
        Log.d(TAG, "onEventEmit: bean : " + bean.toString());
        ToastUtils.toast("emit: " + bean.eventName);
        switch (bean.eventName) {
            case "jsReady":
                onJsReady();
                break;
            case "ready":
                onReady();
                break;
            case "close": // webApp 主动关闭自身
                onClose();
                break;
            case "end":
                onEnd();
                break;
        }
    }

    private void onJsReady() {
        emitJs(WebAppEventBean.createEvent("bizCreated", Biz.createFake()));
    }

    private void onReady() {
        dismissProgress();
        emitJs(WebAppEventBean.createEvent("active", "{}"));
    }

    private void onClose() {
        close();
    }

    private void onEnd() {
        InteractiveWebBrowserManager.getInstance().destroyBrowserView(this);
    }

    @Override
    public void onCreateStrokePage(CreateStrokePageBean bean) {
        ToastUtils.toast(bean.toString());
    }

    @Override
    public void onStrokePageVisibleToggled(StrokePageVisibleBean bean) {
        ToastUtils.toast(bean.toString());
    }

    @Override
    public void onForumEnableToggled(ForumEnableBean bean) {
        ToastUtils.toast(bean.toString());
    }

    public <T> void emitJs(T event) {
        if (event == null) {
            return;
        }
        String jsonData = GsonHelper.toJson(event);
        String base64Encode = Base64.encodeBytes(jsonData.getBytes());
        base64Encode = base64Encode.contains("\n")
                ? base64Encode.replaceAll("\n", "") : base64Encode;
        webView.loadUrl(String.format("javascript:window.WebAppApi.emit(\"%s\")", base64Encode));
    }

    public void close() {
        InteractiveWebBrowserManager.getInstance().prepareDestroy(this);
        if (getParent() instanceof ViewGroup) {
            ((ViewGroup) getParent()).removeView(this);
        }
        emitJs(WebAppEventBean.createEvent("destroy", "{}"));
    }
}
