package fun.sure.any.notificationpolicy;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * Created by wangshuo on 29/09/2017.
 */

@RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR2)
public class AnyNotificationListenerService extends NotificationListenerService {

    private static final String TAG = AnyNotificationListenerService.class.getSimpleName();

//    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
//        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Log.d(TAG, "onNotificationPosted(1)");
        Log.d(TAG, sbn.toString());
        if (NotificationPolicyTestActivity.interruptNotification) {
//            notificationManager.cancel(sbn.getTag(), sbn.getId());
            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                cancelNotification(sbn.getKey());
            }
        }
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        super.onNotificationPosted(sbn, rankingMap);
        Log.d(TAG, "onNotificationPosted(2)");
        Log.d(TAG, sbn.toString());
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Log.d(TAG, "onNotificationRemoved(1)");
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap) {
        super.onNotificationRemoved(sbn, rankingMap);
        Log.d(TAG, "onNotificationRemoved(2)");
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d(TAG, "onListenerConnected");
    }

    @Override
    public void onNotificationRankingUpdate(RankingMap rankingMap) {
        super.onNotificationRankingUpdate(rankingMap);
        Log.d(TAG, "onNotificationRankingUpdate");
    }

    @Override
    public void onListenerHintsChanged(int hints) {
        super.onListenerHintsChanged(hints);
        Log.d(TAG, "onListenerHintsChanged");
    }

    @Override
    public void onInterruptionFilterChanged(int interruptionFilter) {
        super.onInterruptionFilterChanged(interruptionFilter);
        Log.d(TAG, "onInterruptionFilterChanged");
    }
}
