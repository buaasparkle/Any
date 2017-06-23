package fun.sure.any.common;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.yuantiku.android.common.ape.api.ApetexApi;
import com.yuantiku.android.common.ape.constant.ApetexConst;
import com.yuantiku.android.common.app.YtkApp;
import com.yuantiku.android.common.app.YtkApp.YtkAppDelegate;
import com.yuantiku.android.common.base.BaseRuntime;
import com.yuantiku.android.common.ubb.YtkUbb;
import com.yuantiku.android.common.ubb.YtkUbb.YtkUbbDelegate;
import com.yuantiku.android.common.ubb.renderer.FElement;
import com.yuantiku.android.common.ubb.renderer.FFormulaSpan;
import com.yuantiku.android.common.ubb.renderer.FImageSpan;
import com.yuantiku.android.common.ubb.view.UbbView;
import com.yuantiku.android.common.util.UrlUtils;

/**
 * Created by wangshuo on 6/23/17.
 */

public class AppHelper {

    private static AppHelper instance;

    public static AppHelper getInstance() {
        if (instance == null) {
            synchronized (AppHelper.class) {
                if (instance == null) {
                    instance = new AppHelper();
                }
            }
        }
        return instance;
    }

    private AppHelper() {
    }

    private Application application;

    public void init(Application application) {
        this.application = application;
        initApp();
    }

    private void initApp() {
        YtkApp.init(ytkAppDelegate);
        YtkUbb.init(ytkUbbDelegate);
    }

    private YtkAppDelegate ytkAppDelegate = new YtkAppDelegate() {

        @Override
        public Context getAppContext() {
            return application.getApplicationContext();
        }

        @Override
        public String getWebUaName() {
            return "Any";
        }

        @Override
        public void logError(@NonNull String name, @NonNull String msg, @Nullable Throwable error) {
        }
    };

    private static YtkUbbDelegate ytkUbbDelegate = new YtkUbbDelegate() {

        @Override
        public int getUserId() {
//            return UserHelper.getUserId();
            return 0;
        }

        @Override
        public void sendLocalBroadcast(@NonNull String action, @Nullable Bundle args) {
            BaseRuntime.sendLocalBroadcast(action, args);
        }

        @Override
        public Bitmap getSvgFromStore(String url, float width, float height) {
//            return QuestionBitmapCache.getInstance().getSvgFromStore(url, width, height);
            return null;
        }

        @Override
        public String getImageUrl(String imageId, int width, int height) {
            if (UrlUtils.isAbsolutePath(imageId)) {
                return imageId;
            }
//            if (width > 0 || height > 0) {
//                return String.format("%s?width=%d&height=%d", TutorUrl.getStudentAvatarUrl(imageId),
//                        width, height);
//            } else {
//                return TutorUrl.getStudentAvatarUrl(imageId);
//            }
            return "";
        }

        @Override
        public String getFormulaUrl(String latex, int fontSize) {
            return String.format("%s?latex=%s&fontSize=%d&color=%s", ApetexApi.getFormulaUrl(),
                    UrlUtils.encodeUrl(latex), fontSize,
                    UrlUtils.encodeUrl(ApetexConst.COLOR_FORMULA));
        }

        @Override
        public void onClick(UbbView ubbView, int paragraphIndex, FElement element) {
            super.onClick(ubbView, paragraphIndex, element);
            if (element instanceof FImageSpan) {
                FImageSpan span = (FImageSpan) element;
                String imageId = span.getImageId();
                String url = imageId;
//                if (!UrlUtils.isAbsolutePath(imageId)) {
//                    url = TutorUrl.getStudentAvatarUrl(imageId);
//                }
//                ImgActivityUtils.toImageForTime((Activity) ubbView.getContext(),
//                        url, 0, true, true, QuestionRequestConst.LIFE_TIME);
            } else if (element instanceof FFormulaSpan) {
                FFormulaSpan span = (FFormulaSpan) element;
                String latex = span.getLatex();
                String url = getFormulaUrl(latex, (int) ubbView.getTextSize());
//                ImgActivityUtils.toImageForTime((Activity) ubbView.getContext(),
//                        url, 0, false, false, QuestionRequestConst.LIFE_TIME);
            } else {
                super.onClick(ubbView, paragraphIndex, element);
            }
        }

        @Override
        public boolean isAbovePanel(View v) {
            return true;
        }

        @Override
        public boolean isAboveWrapper(View v) {
            return true;
        }

        @Override
        public int getAboveWrapperHeight(View aboveWrapper) {
            return aboveWrapper.getMeasuredHeight();
        }
    };
}
