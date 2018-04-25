package com.ns.yc.lifehelper.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.main.view.MainActivity;


public class ReminderReceiver extends BroadcastReceiver {

    private static final String TAG = "ReminderReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "ReminderReceiver");
        //Calendar now = GregorianCalendar.getInstance();
        Notification.Builder mBuilder = new Notification.Builder(context)
                        .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setSmallIcon(R.drawable.ic_people)
                        .setContentTitle(intent.getStringExtra("title"))
                        .setContentText(intent.getStringExtra("content"))
                        .setAutoCancel(true);

        Log.i(TAG, "onReceive: intent" + intent.getClass().getName());
        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        //将该Activity添加为栈顶
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager != null) {
            mNotificationManager.notify(1, mBuilder.build());
        }
    }

}
