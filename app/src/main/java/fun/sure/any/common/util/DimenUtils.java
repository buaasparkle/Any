package fun.sure.any.common.util;

/**
 * Created by wangshuo on 04/09/2017.
 */

public class DimenUtils {

    private DimenUtils() {}

    public static int dip2px(float dip) {
        return (int) (dip * ApplicationHelper.getInstance().getResources().getDisplayMetrics().density);
    }

    public static int px2dip(float pxValue) {
        final float scale = ApplicationHelper.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
