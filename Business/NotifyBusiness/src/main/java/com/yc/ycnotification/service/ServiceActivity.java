package com.yc.ycnotification.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;


import com.yc.ycnotification.R;
import com.yc.ycnotification.NotifyIntentActivity;

import java.util.List;

public class ServiceActivity extends AppCompatActivity {

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private TextView tv7;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_service);


        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        tv3 = findViewById(R.id.tv_3);
        tv4 = findViewById(R.id.tv_4);
        tv5 = findViewById(R.id.tv_5);
        tv6 = findViewById(R.id.tv_6);
        tv7 = findViewById(R.id.tv_7);

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyService.startRiderService(ServiceActivity.this);
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyService.stopRiderService(ServiceActivity.this);
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyService2.startRiderService(ServiceActivity.this);
            }
        });

        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyService2.stopRiderService(ServiceActivity.this);
            }
        });

        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
//                createChannel();
            }
        });
        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (mNotificationManager!=null){
                   mNotificationManager.cancel(DIALOG_TIME_SEC);
               }
            }
        });
        tv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNotificationManager!=null){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        mNotificationManager.deleteNotificationChannel(LOCAL_CHANNEL_ID);
                        test();
                    }
                }
            }
        });
    }

    private NotificationManager mNotificationManager;
    private static final String LOCAL_CHANNEL_ID = "RIDER_LOCAL_CHANNEL_ID";
    public static final int DIALOG_TIME_SEC = 2;

    private void send(){
        // 创建一个NotificationManager的引用
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(LOCAL_CHANNEL_ID,
                    "Channel Notifications", NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(mChannel);
        }
        Intent intent = new Intent(this, NotifyIntentActivity.class);
        //添加为栈顶Activity
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, LOCAL_CHANNEL_ID)
                .setContentTitle("Title")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle("您有一条新通知")
                .setContentText("这是一条逗你玩的消息")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent, true)
                .build();
        mNotificationManager.notify(DIALOG_TIME_SEC, notification);
    }


    private void createChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "chat";
            String channelName = "通知消息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);

            channelId = "email";
            channelName = "通知邮件";
            importance = NotificationManager.IMPORTANCE_DEFAULT;
            createNotificationChannel(channelId, channelName, importance);

            channelId = "subscribe";
            channelName = "通知订阅";
            importance = NotificationManager.IMPORTANCE_LOW;
            createNotificationChannel(channelId, channelName, importance);
        }
    }

    public void sendChatMsg() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = manager.getNotificationChannel("chat");
            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                startActivity(intent);
                Toast.makeText(this, "请手动将通知打开", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Notification notification = new NotificationCompat.Builder(this, "chat")
                .setContentTitle("收到聊天消息")
                .setContentText("在吗，找你有事情")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notification_music_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification_music_logo))
                .setAutoCancel(true)
                .build();
        manager.notify(1, notification);
    }

    public void sendSubscribeMsg() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "subscribe")
                .setContentTitle("收到订阅消息")
                .setContentText("告诉你个好消息！")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notification_music_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification_music_logo))
                .setAutoCancel(true)
                .build();
        manager.notify(2, notification);
    }

    public void sendEmailMsg() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "subscribe")
                .setContentTitle("收到邮件消息")
                .setContentText("逗比收到一封邮件")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notification_music_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification_music_logo))
                .setAutoCancel(true)
                .build();
        manager.notify(3, notification);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        if (mNotificationManager==null){
            mNotificationManager = (NotificationManager) getSystemService(
                    NOTIFICATION_SERVICE);
        }
        mNotificationManager.createNotificationChannel(channel);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void test(){
        if (mNotificationManager!=null){
            List<NotificationChannelGroup> notificationChannelGroups =
                    mNotificationManager.getNotificationChannelGroups();
            if (notificationChannelGroups!=null){
                for (int i=0 ; i<notificationChannelGroups.size() ; i++){
                    NotificationChannelGroup notificationChannelGroup = notificationChannelGroups.get(i);
                    if (notificationChannelGroup == null){
                        continue;
                    }
                    String id = notificationChannelGroup.getId();
                    CharSequence name = notificationChannelGroup.getName();
                    Log.d("notification group " , id + " , " + name);
                }
            }
        }


        if (mNotificationManager!=null){
            List<NotificationChannel> notificationChannels = mNotificationManager.getNotificationChannels();
            if (notificationChannels!=null){
                for (int i=0 ; i<notificationChannels.size() ; i++){
                    NotificationChannel notificationChannel = notificationChannels.get(i);
                    if (notificationChannel == null){
                        continue;
                    }
                    String id = notificationChannel.getId();
                    CharSequence name = notificationChannel.getName();
                    Log.d("notification channel " , id + " , " + name);
                }
            }
        }

    }

}
