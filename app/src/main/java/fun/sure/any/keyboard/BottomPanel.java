package fun.sure.any.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    private TextView picPanel;

    private LayoutState currentLayoutState = LayoutState.INIT;

    private int keyboardHeight;

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
        picPanel = (TextView) findViewById(R.id.pic_panel);

        inputView.setOnClickListener(this);
        picView.setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "[onMeasure] width = " + width + ", height = " + height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "[onLayout] changed = " + changed + ", " + l + ", " + t + ", " + r + ", " + b);
        super.onLayout(changed, l, t, r, b);
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
            if (picPanel.getVisibility() == VISIBLE) {
                picPanel.setVisibility(GONE);
            }
            KeyboardUtils.hideSoftKeyboard(getContext(), inputView);
        } else if (dstState == LayoutState.KEYBOARD) {
            if (currentLayoutState == LayoutState.INIT) {
                picPanel.setVisibility(GONE);
            } else {
                picPanel.setVisibility(GONE);
            }
            KeyboardUtils.showSoftKeyBoard(getContext(), inputView);
        } else if (dstState == LayoutState.PANEL) {
            KeyboardUtils.hideSoftKeyboard(getContext(), inputView);
            picPanel.setVisibility(VISIBLE);
        }
        currentLayoutState = dstState;
    }
}
