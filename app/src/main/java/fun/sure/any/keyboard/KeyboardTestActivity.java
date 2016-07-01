package fun.sure.any.keyboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import fun.sure.any.R;
import fun.sure.any.common.activity.BaseActivity;

/**
 * Created by wangshuo on 2016/7/1.
 */
public class KeyboardTestActivity extends BaseActivity {

    private ViewGroup container;
    private BottomPanel bottomPanel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        container = (ViewGroup) findViewById(R.id.container);
        bottomPanel = (BottomPanel) findViewById(R.id.panel);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomPanel.hideSoftKeyboardOrPanel();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.keyboard_activity_test;
    }
}
