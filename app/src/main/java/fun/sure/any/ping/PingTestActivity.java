package fun.sure.any.ping;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import fun.sure.any.R;
import fun.sure.any.common.activity.BaseActivity;

/**
 * Created by wangshuo on 24/02/2018.
 */

public class PingTestActivity extends BaseActivity {

    private static final String TAG = "netstat";
    private static final String[] colors = {
            "#FF0000",
            "#00FF00",
            "#0000FF",
    };
    private int i = 0;

    private Random random;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View pingView = findViewById(R.id.ping);
        pingView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ping();
            }
        });
        pingView.postDelayed(new Runnable() {
            @Override
            public void run() {
                pingView.setBackgroundColor(Color.parseColor(colors[i % colors.length]));
                i++;
                pingView.postDelayed(this, 500);
            }
        }, 500);
        random = new Random(System.currentTimeMillis());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ping_activity_test;
    }

    private void ping() {
        Runnable mRunnable = new Runnable() {
            public void run() {
//                startPing("www.baidu.com");
                int timeout = random.nextInt(500);
                int ttl = random.nextInt(30);
                PingConfig config = new PingConfig("www.baidu.com", timeout, ttl);
                PingResult result = NetStatHelper.netStatPing(config);
                Log.e(TAG, "run: result = " + result.toString());
            }
        };
        new Thread(mRunnable).start();
    }

    public void startPing(String ip) {
        try {
            int ttl = random.nextInt(30);
            String command = "ping -c 1 -t " + String.valueOf(ttl) + " " + ip;
            Log.e(TAG, "startPing: " + command);
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            reader.close();
            int status = process.waitFor();
            Log.e(TAG, "startPing: status = " + status);

//            if (status == 0) {
                Log.e(TAG, "startPing: \n" + buffer.toString());
//            } else {
//                Log.e(TAG, "Fail: IP addr not reachable, \n" + buffer.toString());
//            }
        } catch (IOException e) {
            Log.e(TAG, "Fail: IOException" + e.getMessage());
        } catch (InterruptedException e) {
            Log.e(TAG, "Fail: InterruptedException" + e.getMessage());
        }
    }
}
