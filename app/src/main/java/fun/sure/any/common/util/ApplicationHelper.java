package fun.sure.any.common.util;

import android.app.Application;

/**
 * Created by wangshuo on 04/09/2017.
 */

public class ApplicationHelper {

    private static Application me;

    public static void init(Application application) {
        me = application;
    }

    public static Application getInstance() {
        return me;
    }
}
