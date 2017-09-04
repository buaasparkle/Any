package fun.sure.any.common;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import fun.sure.any.common.util.ApplicationHelper;

/**
 * Created by wangshuo on 6/9/17.
 */

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationHelper.init(this);
        Fresco.initialize(this);
        AppHelper.getInstance().init(this);
    }
}
