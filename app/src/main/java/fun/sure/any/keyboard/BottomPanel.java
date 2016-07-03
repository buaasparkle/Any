package fun.sure.any.keyboard;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import fun.sure.any.R;
import fun.sure.any.common.util.KeyboardUtils;

/**
 * Created by wangshuo on 2016/7/1.
 */
public class BottomPanel extends LinearLayout implements View.OnClickListener {

    private static final String TAG = "BottomPanel";

    private enum LayoutState {
        INIT, KEYBOARD, PANEL
    }

    private EditText inputView;
    private TextView picView;

    private KeyboardHeightPanel khPanel;

    private LayoutState currentLayoutState = LayoutState.INIT;

    private int keyboardHeight;
    private int oldHeight = -1;

    public BottomPanel(Context context) {
        this(context, null);
    }

    public BottomPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.keyboard_bottom_panel, this);
        inputView = (EditText) findViewById(R.id.input);
        picView = (TextView) findViewById(R.id.pic);

        khPanel = (KeyboardHeightPanel) findViewById(R.id.kh_panel);

        inputView.setOnClickListener(this);
        picView.setOnClickListener(this);

        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (keyboardHeight == 0 && currentLayoutState == LayoutState.KEYBOARD) {
                    keyboardHeight = oldBottom - bottom;
                    removeOnLayoutChangeListener(this);
                    khPanel.refreshHeight(keyboardHeight);
                    Log.d(TAG, "[keyboardHeight] = " + keyboardHeight);
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "[onMeasure] width = " + width + ", height = " + height);

        handleBeforeMeasure(width, height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.input) {
            onInputClicked();
        } else if (id == R.id.pic) {
            onPicClicked();
        }
    }

    private void onInputClicked() {
        onLayoutStateChanged(LayoutState.KEYBOARD);
    }

    private void onPicClicked() {
        onLayoutStateChanged(LayoutState.PANEL);
    }

    public void hideSoftKeyboardOrPanel() {
        onLayoutStateChanged(LayoutState.INIT);
    }

    private void onLayoutStateChanged(LayoutState dstState) {
        if (dstState == LayoutState.INIT) {
            KeyboardUtils.hideSoftKeyboard(getContext(), inputView);
            khPanel.setVisibility(GONE);
            khPanel.setIsKeyboardShowing(false);
        } else if (dstState == LayoutState.KEYBOARD) {
            KeyboardUtils.showSoftKeyBoard(getContext(), inputView);
            khPanel.setIsKeyboardShowing(true);
        } else if (dstState == LayoutState.PANEL) {
            khPanel.setVisibility(VISIBLE);
            khPanel.setIsKeyboardShowing(false);
            KeyboardUtils.hideSoftKeyboard(getContext(), inputView);
        }
        currentLayoutState = dstState;
    }

    public void handleBeforeMeasure(final int width, int height) {
        // 由当前布局被键盘挤压，获知，由于键盘的活动，导致布局将要发生变化。

        Log.d(TAG, "onMeasure, width: " + width + " height: " + height);
        if (height < 0) {
            return;
        }

        if (oldHeight < 0) {
            oldHeight = height;
            return;
        }

        final int offset = oldHeight - height;

//        if (offset == 0) {
//            Log.d(TAG, "" + offset + " == 0 break;");
//            return;
//        }

        oldHeight = height;
        if (khPanel == null) {
            Log.w(TAG, "can't find the valid panel conflict layout, give up!");
            return;
        }

        // 检测到真正的 由于键盘收起触发了本次的布局变化

        if (offset > 0) {
            //键盘弹起 (offset > 0，高度变小)
            khPanel.handleHide();
        } else if (currentLayoutState == LayoutState.PANEL) {
            // 1. 总得来说，在监听到键盘已经显示的前提下，键盘收回才是有效有意义的。
            // 2. 修复在Android L下使用V7.Theme.AppCompat主题，进入Activity，默认弹起面板bug，
            // 第2点的bug出现原因:在Android L下使用V7.Theme.AppCompat主题，并且不使用系统的ActionBar/ToolBar，V7.Theme.AppCompat主题,还是会先默认绘制一帧默认ActionBar，然后再将他去掉（略无语）
            //键盘收回 (offset < 0，高度变大)
            if (khPanel.isVisible()) {
                // the panel is showing/will showing
                khPanel.handleShow();
            }
        }
    }
}
