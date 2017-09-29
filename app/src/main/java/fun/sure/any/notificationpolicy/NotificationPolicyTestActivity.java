package fun.sure.any.notificationpolicy;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicInteger;

import fun.sure.any.R;
import fun.sure.any.common.activity.BaseActivity;
import fun.sure.any.common.util.ApplicationHelper;

/**
 * Created by wangshuo on 28/09/2017.
 */

public class NotificationPolicyTestActivity extends BaseActivity implements OnClickListener {

    private AtomicInteger count;

    private NotificationManager notificationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        initView();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        count = new AtomicInteger(0);
    }

    private void initView() {
        RadioGroup switcher = (RadioGroup) findViewById(R.id.switcher);
        switcher.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @RequiresApi(api = VERSION_CODES.M)
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                onSwitcherChange(checkedId);
            }
        });
        Button notify = (Button) findViewById(R.id.send_notification);
        Button settings = (Button) findViewById(R.id.settings);
        notify.setOnClickListener(this);
        settings.setOnClickListener(this);

        // notification listener service
        RadioGroup listener = (RadioGroup) findViewById(R.id.listener);
        listener.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                onListenerChange(checkedId);
            }
        });
        RadioGroup interrupt = (RadioGroup) findViewById(R.id.interrupt);
        interrupt.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                onInterruptChange(checkedId);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.notification_policy_activity_test;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.send_notification) {
            sendNotification();
        } else if (id == R.id.settings) {
            goSettings();
        }
    }

    private void sendNotification() {
        NotificationCompat.Builder nb = new Builder(this);
        nb.setSmallIcon(ApplicationHelper.getInstance().getApplicationContext().getApplicationInfo().icon)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("通知")
                .setContentText("内容");
        notificationManager.notify(count.getAndIncrement(), nb.build());
    }

    @RequiresApi(api = VERSION_CODES.M)
    private void onSwitcherChange(@IdRes int checkedId) {
        if (checkedId == R.id.start) {
            startNotificationInterrupt();
        } else {
            stopNotificationInterrupt();
        }
    }

    @RequiresApi(api = VERSION_CODES.M)
    private void startNotificationInterrupt() {
        if (couldFilterInterruptionNotification()) {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
        }
    }

    @RequiresApi(api = VERSION_CODES.M)
    private void stopNotificationInterrupt() {
        if (couldFilterInterruptionNotification()) {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
        }
    }

    private boolean couldFilterInterruptionNotification() {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            if (!notificationManager.isNotificationPolicyAccessGranted()) {
                goSettings();
                return false;
            }
            return true;
        }
        return false;
    }

    private void goSettings() {
        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
        startActivity(intent);
    }

    /**
     * 华丽丽的分割线 === notification listener service
     */

    public static boolean interruptNotification = false;

    private void onListenerChange(@IdRes int checkedId) {
        if (checkedId == R.id.open) {
            if (!isNotificationListenGranted()) {
                startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            } else {
                Toast.makeText(getApplicationContext(), "监控器开关已打开", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (isNotificationListenGranted()) {
                startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            } else {
                Toast.makeText(getApplicationContext(), "监控器开关已打开", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNotificationListenGranted() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void onInterruptChange(@IdRes int checkedId) {
        interruptNotification = (checkedId == R.id.interrupt_open);
    }
}
