package fun.sure.any.thread;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import fun.sure.any.R;
import fun.sure.any.common.activity.BaseActivity;

/**
 * Created by wangshuo on 30/01/2018.
 */

public class ThreadTestActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = "ThreadTest";

    private Thread thread;
    private boolean isRunning = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.thread_activity_test;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.start) {
            start();
        } else {
            stop();
        }
    }

    private void start() {
        if (isRunning) {
            return;
        }
        Log.e(TAG, "start");
        isRunning = true;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    Log.e(TAG, "run");
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        Log.e(TAG, "run: error", e);
                    }
                }
                Log.e(TAG, "run: quit");
            }
        });
        thread.start();
    }

    private void stop() {
        if (!isRunning) {
            return;
        }
        isRunning = false;
//        thread.interrupt();
        try {
            Log.e(TAG, "stop: join");
            thread.join(5000);
        } catch (InterruptedException e) {
            Log.e(TAG, "stop: error", e);
        }
        Log.e(TAG, "stop: end");
    }
}
