package fun.sure.any.webview.jsinterface.delegate;

import fun.sure.any.webview.jsinterface.bean.CreateStrokePageBean;
import fun.sure.any.webview.jsinterface.bean.ForumEnableBean;
import fun.sure.any.webview.jsinterface.bean.StrokePageVisibleBean;
import fun.sure.any.webview.jsinterface.bean.WebAppEventBean;

/**
 * Created by wangshuo on 23/01/2018.
 */

public interface WebAppApiDelegate {

    void onEventEmit(WebAppEventBean bean);

    void onCreateStrokePage(CreateStrokePageBean bean);

    void onStrokePageVisibleToggled(StrokePageVisibleBean bean);

    void onForumEnableToggled(ForumEnableBean bean);
}
