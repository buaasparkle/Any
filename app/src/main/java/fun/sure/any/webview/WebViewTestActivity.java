package fun.sure.any.webview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

import fun.sure.any.R;
import fun.sure.any.common.activity.BaseActivity;
import fun.sure.any.webview.jsinterface.InteractiveWebBrowserManager;
import fun.sure.any.webview.jsinterface.bean.WebAppEventBean;
import fun.sure.any.webview.jsinterface.bean.WebAppEventBean.StrokePageIds;

/**
 * Created by wangshuo on 15/01/2018.
 */

public class WebViewTestActivity extends BaseActivity implements OnClickListener {

    private static final String LOCAL_URL_PREFIX = "file:///android_asset/app.1.1/index.html";
    private static final String SUFFIX_PARAM =
            "#/?appVersion=1&appConfigId=abc&roomId=123&userId=123&userRole=student&mode=livecast&appId=1";

    private FrameLayout browswerContainer;
    private InteractiveBrowserView browserView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.live_destroy).setOnClickListener(this);
        findViewById(R.id.live_test).setOnClickListener(this);
        findViewById(R.id.live_test_local).setOnClickListener(this);

        browswerContainer = (FrameLayout) findViewById(R.id.live_browser_container);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.live_activity_webview_test;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.live_test_local) {
            testUrl(LOCAL_URL_PREFIX + SUFFIX_PARAM);
        } else if (id == R.id.live_destroy) {
            browserView.close();
        } else if (id == R.id.live_test) {
            browserView.emitJs(WebAppEventBean.createEvent("strokePageCreated", StrokePageIds.createFake()));
        }
    }

    private void testUrl(String url) {
        browserView = InteractiveWebBrowserManager.getInstance().createBrowswerView(this);
        browswerContainer.addView(browserView);
        browserView.loadUrl(url);
    }
}
