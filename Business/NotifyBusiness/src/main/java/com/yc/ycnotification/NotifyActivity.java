package com.yc.ycnotification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yc.appprocesslib.AppStateMonitor;
import com.yc.appprocesslib.StateListener;

import com.yc.notificationlib.AppNotifyHelper;
import com.yc.notificationlib.NotificationParams;
import com.yc.notificationlib.NotificationUtils;
import com.yc.notifymessage.CustomNotification;
import com.yc.toastutils.ToastUtils;
import com.yc.ycnotification.notifyview.MyNotifyView;
import com.yc.ycnotification.notifyview.MyNotifyView2;
import com.yc.ycnotification.test.NotificationUtil;

public class NotifyActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_1;
    private TextView tv_2;
    private TextView tv_3;
    private TextView tv_4;
    private TextView tv_5;
    private TextView tv_6;
    private TextView tv_7;
    private TextView tv_8;
    private TextView tv_9;
    private TextView tv_10;
    private TextView tv_11;
    private TextView tv_12;
    private TextView tv_13;
    private TextView tv_14;
    private TextView tv_15;
    private TextView tv_16;
    private TextView tv_17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_main);
        AppStateMonitor.getInstance().init(this);
        init();
    }


    private void init() {
        initView();
        initListener();
        AppStateMonitor.getInstance().registerStateListener(new StateListener() {
            @Override
            public void onInForeground() {
                Log.i("AppStateMonitor:" ,"在前台");
            }

            @Override
            public void onInBackground() {
                Log.i("AppStateMonitor:" ,"在后台");
                tv_1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("AppStateMonitor:" ,"发送通知");
                        sendNotification1();
                    }
                },5000);
            }
        });
    }

    private void initView() {
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_5 = (TextView) findViewById(R.id.tv_5);
        tv_6 = (TextView) findViewById(R.id.tv_6);
        tv_7 = (TextView) findViewById(R.id.tv_7);
        tv_8 = (TextView) findViewById(R.id.tv_8);
        tv_9 = (TextView) findViewById(R.id.tv_9);
        tv_10 = (TextView) findViewById(R.id.tv_10);
        tv_11 = (TextView) findViewById(R.id.tv_11);
        tv_12 = (TextView) findViewById(R.id.tv_12);
        tv_13 = (TextView) findViewById(R.id.tv_13);
        tv_14 = (TextView) findViewById(R.id.tv_14);
        tv_15 = (TextView) findViewById(R.id.tv_15);
        tv_16= (TextView) findViewById(R.id.tv_16);
        tv_17= (TextView) findViewById(R.id.tv_17);
    }

    private void initListener() {
        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        tv_3.setOnClickListener(this);
        tv_4.setOnClickListener(this);
        tv_5.setOnClickListener(this);
        tv_6.setOnClickListener(this);
        tv_7.setOnClickListener(this);
        tv_8.setOnClickListener(this);
        tv_9.setOnClickListener(this);
        tv_10.setOnClickListener(this);
        tv_11.setOnClickListener(this);
        tv_12.setOnClickListener(this);
        tv_13.setOnClickListener(this);
        tv_14.setOnClickListener(this);
        tv_15.setOnClickListener(this);
        tv_16.setOnClickListener(this);
        tv_17.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_1) {
            cancelAllNotification();
        } else if (id == R.id.tv_2) {
            sendNotification1();
        } else if (id == R.id.tv_3) {
            sendNotification2();
        } else if (id == R.id.tv_4) {
            sendNotification3();
        } else if (id == R.id.tv_5) {
            sendNotification4();
        } else if (id == R.id.tv_6) {
            Intent intent = new Intent(this, ReminderReceiver.class);
            sendBroadcast(intent);
        } else if (id == R.id.tv_7) {
        } else if (id == R.id.tv_8) {
            sendNotification8();
        } else if (id == R.id.tv_9) {
            sendNotification9();
        } else if (id == R.id.tv_10) {
            sendNotification10();
        } else if (id == R.id.tv_11) {
            sendNotification11();
        } else if (id == R.id.tv_12) {
            sendNotification12();
        } else if (id == R.id.tv_13) {
            sendNotification13();
        } else if (id == R.id.tv_14) {
            sendNotification14();
        } else if (id == R.id.tv_15) {
            sendNotification15();
        } else if (id == R.id.tv_16) {
            sendNotification16();
        } else if (id == R.id.tv_17) {
            sendNotification17();
        }
    }

    private void cancelAllNotification() {
        NotificationUtils notificationUtils = new NotificationUtils(this);
        notificationUtils.clearNotification();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationUtils.getManager().deleteNotificationChannel("channel_1");
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            //判断通知是否是静默不重要的通知
//            boolean isNoImportance = notificationUtils.isNoImportance("channel_id");
//            //跳转设置中心
//            notificationUtils.openChannelSetting("channel_id");
//            //清空特定渠道的通知
//            notificationUtils.clearNotificationChannel("channel");
//            //清空所有的通知
//            notificationUtils.clearAllNotification();
//        }
    }


    private void sendNotification1() {
        ToastUtils.showRoundRectToast("发送最简单通知");
        //这三个属性是必须要的，否则异常
//        NotificationUtils notificationUtils = new NotificationUtils(
//                this,"channel_1","通知1");
//        notificationUtils.sendNotification(1,
//                "这个是标题","这个是内容",R.mipmap.ic_launcher);

        if (AppNotifyHelper.isOpenNotify(this)){
            NotificationUtil notificationUtil = new NotificationUtil();
            notificationUtil.showFloatNotify(this);
        } else {
            AppNotifyHelper.goToSetNotify(this);
        }
    }


    private void sendNotification2() {
        //处理点击Notification的逻辑
        //创建intent
//        Intent resultIntent = new Intent(this, NotifyIntentActivity.class);
//        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);           //添加为栈顶Activity
//        resultIntent.putExtra("what",2);
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,2,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//
//        // 定义Notification的各种属性
//        NotificationUtils notificationUtils = new NotificationUtils(
//                this,"channel_2","通知2");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel =
//                    notificationUtils.getNotificationChannel("channel_2");
//            if (notificationUtils.isNoImportance(notificationChannel)){
//                notificationUtils.openChannelSetting(notificationChannel);
//            }
//        }
//        NotificationParams notificationParams = new NotificationParams();
//        notificationParams.setContentIntent(resultPendingIntent);
//        notificationUtils
//                .setNotificationParams(notificationParams)
//                .sendNotificationCompat(2,"这个是标题2", "这个是内容2", R.mipmap.ic_launcher);


        NotificationUtil notificationUtil = new NotificationUtil();
        notificationUtil.showFloatNotify2(this);
    }


    private void sendNotification3() {
        long[] vibrate = new long[]{0, 500, 1000, 1500};
        //处理点击Notification的逻辑
        //创建intent
        Intent resultIntent = new Intent(this, NotifyIntentActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);           //添加为栈顶Activity
        resultIntent.putExtra("what",3);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,3,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        //发送pendingIntent

        NotificationUtils notificationUtils = new NotificationUtils(
                this,"channel_3","通知3");
        NotificationParams notificationParams = new NotificationParams();

        notificationParams
                //让通知左右滑的时候是否可以取消通知
                .setOngoing(true)
                //设置内容点击处理intent
                .setContentIntent(resultPendingIntent)
                //设置状态栏的标题
                .setTicker("来通知消息啦")
                //设置自定义view通知栏布局
                .setContent(getRemoteViews())
                //设置sound
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                //设置优先级
                .setPriority(Notification.PRIORITY_DEFAULT)
                //自定义震动效果
                .setVibrate(vibrate);
            //必须设置的属性，发送通知
            notificationUtils.setNotificationParams(notificationParams)
                    .sendNotification(3,"这个是标题3", "这个是内容3", R.mipmap.ic_launcher);
    }


    private void sendNotification4() {
        NotificationUtils notificationUtils = new NotificationUtils(this);
        NotificationParams notificationParams = new NotificationParams();
        notificationParams.setContent(getRemoteViews());
        notificationUtils.setNotificationParams(notificationParams);
        Notification notification = notificationUtils.getNotification("这个是标题4", "这个是内容4", R.mipmap.ic_launcher);
        notificationUtils.getManager().notify(4,notification);
    }


    private RemoteViews getRemoteViews() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_mobile_play);
        // 设置 点击通知栏的上一首按钮时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.btn_pre, getActivityPendingIntent(11));
        // 设置 点击通知栏的下一首按钮时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.btn_next, getActivityPendingIntent(12));
        // 设置 点击通知栏的播放暂停按钮时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.btn_start, getActivityPendingIntent(13));
        // 设置 点击通知栏的根容器时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.ll_root, getActivityPendingIntent(14));
        remoteViews.setTextViewText(R.id.tv_title, "标题");     // 设置通知栏上标题
        remoteViews.setTextViewText(R.id.tv_artist, "艺术家");   // 设置通知栏上艺术家
        return remoteViews;
    }


    /** 获取一个Activity类型的PendingIntent对象 */
    private PendingIntent getActivityPendingIntent(int what) {
        Intent intent = new Intent(this, NotifyIntentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);           //添加为栈顶Activity
        intent.putExtra("what", what);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, what, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }



    private void sendNotification8() {
        for(int a=0 ; a<3 ; a++){
            //这三个属性是必须要的，否则异常
            NotificationUtils notificationUtils = new NotificationUtils(this);
            notificationUtils.sendNotification(8,"这个是标题8","这个是内容8",R.mipmap.ic_launcher);

        }
    }


    private void sendNotification9() {
        NotificationUtils notificationUtils = new NotificationUtils(this);
        NotificationParams notificationParams = new NotificationParams();
        NotificationParams params = notificationParams
                //让通知左右滑的时候是否可以取消通知
                .setOngoing(true)
                //设置状态栏的标题
                .setTicker("有新消息呢9")
                //设置自定义view通知栏布局
                .setContent(getRemoteViews())
                //设置sound
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                //设置优先级
                .setPriority(Notification.PRIORITY_DEFAULT)
                //自定义震动效果
                .setFlags(Notification.FLAG_NO_CLEAR);

        //必须设置的属性，发送通知
        notificationUtils
                .setNotificationParams(params)
                .sendNotification(9,"有新消息呢9",
                "这个是标题9", R.mipmap.ic_launcher);
    }


    private void sendNotification10() {

        //处理点击Notification的逻辑
        //创建intent
        Intent resultIntent = new Intent(this, NotifyIntentActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);           //添加为栈顶Activity
        resultIntent.putExtra("what",10);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,10,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        //设置 Notification 的 flags = FLAG_NO_CLEAR
        //FLAG_NO_CLEAR 表示该通知不能被状态栏的清除按钮给清除掉,也不能被手动清除,但能通过 cancel() 方法清除
        //flags 可以通过 |= 运算叠加效果

        NotificationUtils notificationUtils = new NotificationUtils(this);
        NotificationParams notificationParams = new NotificationParams();
        notificationParams
                //让通知左右滑的时候是否可以取消通知
                .setOngoing(true)
                .setContentIntent(resultPendingIntent)
                //设置状态栏的标题
                .setTicker("有新消息呢10")
                //设置自定义view通知栏布局
                .setContent(getRemoteViews())
                .setDefaults(Notification.DEFAULT_ALL)
                //设置sound
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                //设置优先级
                .setPriority(Notification.PRIORITY_DEFAULT)
                //自定义震动效果
                .setFlags(Notification.FLAG_AUTO_CANCEL);
                //必须设置的属性，发送通知
        notificationUtils.setNotificationParams(notificationParams)
                .sendNotification(10,"有新消息呢10", "这个是标题10", R.mipmap.ic_launcher);
    }


    private void sendNotification11() {
        NotificationUtils notificationUtils = new NotificationUtils(this);
        NotificationParams notificationParams = new NotificationParams();
        notificationParams
                .setOngoing(false)
                .setTicker("来通知消息啦")
                .setContent(getRemoteViews())
                //.setSound(Uri.parse("android.resource://com.yc.ycnotification/" + R.raw.hah))
                .setSound(Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,"2"))
                .setPriority(Notification.PRIORITY_DEFAULT);
        notificationUtils.setNotificationParams(notificationParams).sendNotification(11,"我是伴有铃声效果的通知11", "美妙么?安静听~11", R.mipmap.ic_launcher);

    }



    private void sendNotification12() {
        //震动也有两种设置方法,与设置铃声一样,在此不再赘述
        long[] vibrate = new long[]{0, 500, 1000, 1500};
        NotificationUtils notificationUtils = new NotificationUtils(this);
        NotificationParams notificationParams = new NotificationParams();
        notificationParams
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setVibrate(vibrate);
        notificationUtils.setNotificationParams(notificationParams);
        notificationUtils.sendNotification(12,"我是伴有震动效果的通知", "颤抖吧,逗比哈哈哈哈哈~", R.mipmap.ic_launcher);

    }


    private void sendNotification13() {
        NotificationUtils notificationUtils = new NotificationUtils(this);
        NotificationParams notificationParams = new NotificationParams();
        notificationParams
                .setDefaults(Notification.DEFAULT_ALL)
                .setFlags(Notification.FLAG_ONLY_ALERT_ONCE);
        notificationUtils.setNotificationParams(notificationParams)
                .sendNotification(13,"仔细看,我就执行一遍","好了,已经一遍了~",R.mipmap.ic_launcher);

    }


    private void sendNotification14() {
        NotificationUtils notificationUtils = new NotificationUtils(this);
        NotificationParams notificationParams = new NotificationParams();
        notificationParams
                .setDefaults(Notification.DEFAULT_ALL)
                .setFlags(Notification.FLAG_ONLY_ALERT_ONCE);
        notificationUtils.setNotificationParams(notificationParams).sendNotification(14,"显示进度条14","显示进度条内容，自定定义",R.mipmap.ic_launcher);
    }

    private void sendNotification15() {
        NotificationUtils notificationUtils = new NotificationUtils(this);
        notificationUtils.sendNotification(15,"新消息来了","周末到了，不用上班了",R.mipmap.ic_launcher);


    }

    private void sendNotification16(){
        new CustomNotification<Void>()
                .setType(1)
                .setCollapsible(false)
                .setTimeOut(3000)
                .setPriority(100)
                .setNotificationView(new MyNotifyView(this))
                .show();
    }


    private void sendNotification17(){
        new CustomNotification<Void>()
                .setType(1)
                .setCollapsible(false)
                .setTimeOut(5000)
                .setPriority(60)
                .setNotificationView(new MyNotifyView2(this))
                .show();
    }
}
