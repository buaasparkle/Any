package fun.sure.any.websocket;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import fun.sure.any.R;
import fun.sure.any.common.activity.BaseActivity;

/**
 * Created by wangshuo on 26/10/2017.
 */

public class WebSocketTestActivity extends BaseActivity implements OnClickListener {

    private TextView logView;
    private Button startBtn;
    private Button stopBtn;

    private LogDispatcher logDispatcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        logView = (TextView) findViewById(R.id.log);
        startBtn = (Button) findViewById(R.id.start);
        stopBtn = (Button) findViewById(R.id.stop);
        startBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        setLogEnable(true);
    }

    private void setLogEnable(boolean enable) {
        startBtn.setEnabled(enable);
        stopBtn.setEnabled(!enable);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.websocket_activity_test;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LarkClient.getInstance().release();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.start) {
            doStart();
        } else if (id == R.id.stop) {
            doStop();
        }
    }

    private void doStart() {
        logDispatcher = new LogDispatcher();
        logDispatcher.start();
        setLogEnable(false);
    }

    private void doStop() {
        if (logDispatcher != null) {
            logDispatcher.quit();
        }
        setLogEnable(true);
    }

    private static class LogDispatcher extends Thread {

        private volatile boolean quit = false;

        public void quit() {
            this.quit = true;
            this.interrupt();
        }

        @Override
        public void run() {
            long time;
            while (true) {
                time = SystemClock.elapsedRealtime();

                try {
                    LarkClient.getInstance().sendEntry(String.valueOf(time));

                    Thread.sleep(3000);
                } catch (Exception e) {
                    if (this.quit) {
                        return;
                    }
                }

            }
        }
    }

}
