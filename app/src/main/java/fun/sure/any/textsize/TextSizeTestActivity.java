package fun.sure.any.textsize;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yuantiku.android.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fun.sure.any.R;
import fun.sure.any.common.activity.BaseActivity;
import fun.sure.any.common.util.DimenUtils;
import fun.sure.any.common.util.KeyboardUtils;

/**
 * Created by wangshuo on 04/09/2017.
 */

public class TextSizeTestActivity extends BaseActivity implements View.OnClickListener {

    private static final String CANDIDATE_NUMBER = "0123456789";
    private static final String CANDIDATE_LETTER = "afghjmpyY";
    private static final String CANDIDATE_CHINESE = "中国";
    private static final String DEFAULT_CANDIDATE = CANDIDATE_NUMBER + CANDIDATE_LETTER + CANDIDATE_CHINESE;

    private EditText inputView;
    private EditText candidateView;
    private LinearLayout resultContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        inputView = (EditText) findViewById(R.id.input);
        candidateView = (EditText) findViewById(R.id.candidate);
        resultContainer = (LinearLayout) findViewById(R.id.result_container);

        Button runButton = (Button) findViewById(R.id.run);
        runButton.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.textsize_activity_test;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.run) {
            listTextSizeResult();
        }
    }

    private void listTextSizeResult() {
        KeyboardUtils.hideSoftKeyboard(this, inputView);
        KeyboardUtils.hideSoftKeyboard(this, candidateView);
        List<Integer> sizeList = parseSizeListFromInput();
        if (sizeList == null || sizeList.size() == 0) {
            Toast.makeText(this, "输入有误", Toast.LENGTH_SHORT).show();
            clearInput();
            return;
        }
        resultContainer.removeAllViews();
        resultContainer.addView(getTitleView());
        for (final Integer size : sizeList) {
            final TextView sizeView = new TextView(this);
            sizeView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
           sizeView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                        int oldRight, int oldBottom) {
                    sizeView.setText(String.format(Locale.getDefault(),
                            "%d, %d, %d, %s", size, sizeView.getMeasuredHeight(),
                            DimenUtils.px2dip(sizeView.getMeasuredHeight()),
                            getCandidateString()));
//                    sizeView.removeOnLayoutChangeListener(this);
                }
            });
            resultContainer.addView(sizeView);
        }
    }

    private List<Integer> parseSizeListFromInput() {
        String input = inputView.getText().toString().trim();
        if (StringUtils.isNotBlank(input)) {
            String[] textSizes = input.split("-");
            int start = Integer.valueOf(textSizes[0]);
            int end = (textSizes.length == 1 ? start : Integer.valueOf(textSizes[1])) + 1;
            List<Integer> sizeList = new ArrayList<>();
            for (int i = start; i < end; i++) {
                sizeList.add(i);
            }
            return sizeList;
        }
        return null;
    }

    private void clearInput() {
        inputView.setText("");
        candidateView.setText("");
    }

    private View getTitleView() {
        TextView titleView= new TextView(this);
        titleView.setText("字号sp，高度px，高度dp，内容");
        return titleView;
    }

    private String getCandidateString() {
        String candidate = candidateView.getText().toString();
        if (TextUtils.isEmpty(candidate)) {
            return DEFAULT_CANDIDATE;
        }
        return candidate;
    }
}
