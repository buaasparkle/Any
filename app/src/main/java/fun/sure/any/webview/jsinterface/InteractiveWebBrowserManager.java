package fun.sure.any.webview.jsinterface;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

import fun.sure.any.webview.InteractiveBrowserView;

/**
 * Created by wangshuo on 24/01/2018.
 */

public class InteractiveWebBrowserManager {

    private static InteractiveWebBrowserManager instance;

    public static InteractiveWebBrowserManager getInstance() {
        if (instance == null) {
            synchronized (InteractiveWebBrowserManager.class) {
                if (instance == null) {
                    instance = new InteractiveWebBrowserManager();
                }
            }
        }
        return instance;
    }

    private List<View> destroyingBrowserList = new LinkedList<>();
    private Handler handler;

    private InteractiveWebBrowserManager() {
        handler = new Handler(Looper.getMainLooper());
    }

    public InteractiveBrowserView createBrowswerView(Context context) {
        InteractiveBrowserView browserView = new InteractiveBrowserView(context);
        InteractiveBrowserView.configBrowser(browserView.getWebView(), browserView.getWebAppInterface());
        return browserView;
    }

    public void prepareDestroy(final View browserView) {
        if (browserView == null) {
            return;
        }
        destroyingBrowserList.add(browserView);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                destroyBrowserView(browserView);
            }
        }, 5 * 1000);
    }

    public void destroyBrowserView(View browserView) {
        if (browserView == null) {
            return;
        }
        removeView(browserView);
    }

    public void destroyAll() {
        destroyingBrowserList.clear();
    }

    private void removeView(View view) {
        if (destroyingBrowserList.contains(view)) {
            destroyingBrowserList.remove(view);
        }
    }
}
