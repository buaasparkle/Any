package fun.sure.any.webview.jsinterface.bean;

/**
 * Created by wangshuo on 24/01/2018.
 */

public class StrokePageVisibleBean extends BaseBean {

    public int strokePageId;
    public boolean visible;

    @Override
    public String toString() {
        return "strokePageId = " + strokePageId + ", visible = " + visible;
    }
}
