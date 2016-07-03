package fun.sure.any.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by wangshuo on 2016/7/3.
 */
public class KeyboardHeightPanel extends FrameLayout {

    private static final String TAG = "KeyboardHeightPanel";

    private boolean isHide;

    private final int[] processedMeasureWHSpec = new int[2];

    public KeyboardHeightPanel(Context context) {
        super(context);
    }

    public KeyboardHeightPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardHeightPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        processOnMeasure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(processedMeasureWHSpec[0], processedMeasureWHSpec[1]);
    }

    public void processOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isHide) {
            setVisibility(View.GONE);
            /**
             * The current frame will be visible nil.
             */
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY);
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY);
        }

        processedMeasureWHSpec[0] = widthMeasureSpec;
        processedMeasureWHSpec[1] = heightMeasureSpec;
    }

    public boolean isVisible() {
        return !isHide;
    }

    @Override
    public void setVisibility(int visibility) {
        if (filterSetVisibility(visibility)) {
            return;
        }
        super.setVisibility(visibility);
    }

    public boolean filterSetVisibility(final int visibility) {
        if (visibility == View.VISIBLE) {
            this.isHide = false;
        }

        if (visibility == getVisibility()) {
            return true;
        }

        /**
         * For handling Keyboard->Panel.
         *
         * Will be handled on {@link KPSwitchRootLayoutHandler#handleBeforeMeasure(int, int)} ->
         * {@link IPanelConflictLayout#handleShow()} Delay show, until the {@link KPSwitchRootLayoutHandler} discover
         * the size is changed by keyboard-show. And will show, on the next frame of the above
         * change discovery.
         */
        if (isKeyboardShowing() && visibility == View.VISIBLE) {
            return true;
        }

        return false;
    }

    private boolean mIsKeyboardShowing = false;

    public void setIsKeyboardShowing(final boolean isKeyboardShowing) {
        mIsKeyboardShowing = isKeyboardShowing;
    }

    public boolean isKeyboardShowing() {
        return mIsKeyboardShowing;
    }

    public boolean refreshHeight(final int aimHeight) {
        if (isInEditMode()) {
            return false;
        }
        Log.d(TAG, String.format("refresh Height %d %d", getHeight(), aimHeight));

        if (getHeight() == aimHeight) {
            return false;
        }

        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    aimHeight);
            setLayoutParams(layoutParams);
        } else {
            layoutParams.height = aimHeight;
            requestLayout();
        }

        return true;
    }

    public void handleHide() {
        isHide = true;
    }

    public void handleShow() {
        super.setVisibility(View.VISIBLE);
    }
}
