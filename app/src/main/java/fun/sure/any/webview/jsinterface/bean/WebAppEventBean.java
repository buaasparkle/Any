package fun.sure.any.webview.jsinterface.bean;

import com.yuantiku.android.common.data.BaseData;

/**
 * Created by wangshuo on 23/01/2018.
 */

public class WebAppEventBean extends BaseBean {

    public String eventName;
    public Object eventData;

    @Override
    public String toString() {
        return "eventName = " + eventName + "; eventData = " + eventData;
    }

    //region client emit events

    public static <T> WebAppEventBean createEvent(String eventName, T eventData) {
        WebAppEventBean bean = new WebAppEventBean();
        bean.eventName = eventName;
        bean.eventData = eventData;
        return bean;
    }

    public  static class Biz extends BaseData {
        private String key;
        private String payload;

        public static Biz createFake() {
            Biz biz = new Biz();
            biz.key = "1";
            biz.payload = "";
            return biz;
        }
    }

    public static class StrokePageIds extends BaseData {
        private int[] currentStrokePageIds;

        public static StrokePageIds createFake() {
            StrokePageIds ids = new StrokePageIds();
            ids.currentStrokePageIds = new int[] {1};
            return ids;
        }
    }

    //endregion
}
