package com.yc.ycnotification.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


import com.yc.ycnotification.NotifyHomeActivity;
import com.yc.ycnotification.R;

public class MyForegroundService extends Service {

    private final String channelId = "yc_delivery_service";
    public static final int NOTIFICATION_ID_SERVICE = 104;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Service","MyForegroundService onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Service","MyForegroundService onCreate");
        startForeground("yangchong") ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Service","MyForegroundService onDestroy");
        //如果设置成ture，则会移除通知栏
        stopForeground(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void startForeground(String title){
        Log.d("Service","MyForegroundService startForeground : " +title);
        try {
            createNotificationChannel(channelId, "测试");
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
            Intent intentMainActivity = new Intent(this, NotifyHomeActivity.class);
            intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pi = PendingIntent.getActivity(this, 0,
                    intentMainActivity, 0);
            builder.setContentIntent(pi)
                    .setSmallIcon(R.drawable.ic_notification_music_logo)
                    .setContentTitle("setContentTitle")
                    .setContentText(title);
            Notification notification = builder.build();
            ////把该service创建为前台service
            ////把该service创建为前台service
            startForeground(NOTIFICATION_ID_SERVICE, notification);
        } catch (Exception e) {
            Log.d("Service","MyForegroundService startForeground : " +e.getMessage());
        }
    }

    /**
     * Android8.0之后必须要有channel
     */
    private void createNotificationChannel(String channelId, CharSequence channelName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
            if (null == channel) {
                channel = new NotificationChannel(channelId, channelName,
                        NotificationManager.IMPORTANCE_DEFAULT);
            }
            notificationManager.createNotificationChannel(channel);
        }
    }

}
