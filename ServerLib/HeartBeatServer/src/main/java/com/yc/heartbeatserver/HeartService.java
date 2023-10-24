package com.yc.heartbeatserver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HeartService extends Service {

    public static final String ACTION_HEART_BEAT = "com.yc.heart.ACTION_HEART_BEAT";
    public static final String ACTION_STOP = "com.yc.heart.ACTION_STOP";
    //时钟心跳间隔
    private static final int TIME = 30 * 1000;
    //闲时时钟心跳间隔
    private static final int TIME_NIGHT = 10 * 60 * 1000;
    private PendingIntent pendingIntent;
    private AlarmManager manager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 发送心跳指令，启动服务
     *
     * @param context 上下文
     */
    public static void start(Context context) {
        Intent service = new Intent(context, HeartService.class);
        service.setAction(HeartService.ACTION_HEART_BEAT);
        context.startService(service);
    }

    /**
     * 停止服务
     *
     * @param context 上下文
     */
    public static void stop(Context context) {
        Intent service = new Intent(context, HeartService.class);
        service.setAction(HeartService.ACTION_STOP);
        context.startService(service);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Context context = getApplicationContext();
        Intent intent = new Intent(context, HeartReceiver.class);
        intent.setAction(ACTION_HEART_BEAT);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            startAlarmHeartBeat();
            return START_STICKY;
        }
        String action = intent.getAction();
        if (ACTION_HEART_BEAT.equals(action)) {
            doAction();
        } else if (ACTION_STOP.equals(action)) {
            releaseService();
        }
        return START_STICKY;
    }

    /**
     * 做具体的操作
     */
    private void doAction() {

    }

    /**
     * 启动下一次心跳定时任务
     */
    public void startAlarmHeartBeat() {
        long triggerAtMillis = System.currentTimeMillis() + getDelayTime();
        manager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
    }

    /**
     * 关闭时钟心跳定时任务
     */
    public void stopAlarmHeartBeat() {
        if (manager != null) {
            manager.cancel(pendingIntent);
        }
    }

    /**
     * 关闭时钟心跳，停止服务，释放资源
     */
    public void releaseService() {
        //停止时钟心跳
        stopAlarmHeartBeat();
        //关闭服务
        Intent service = new Intent(getApplicationContext(), HeartService.class);
        stopService(service);
    }


    /**
     * 通过不同策略获取心跳间隔
     *
     * @return 心跳间隔
     */
    private int getDelayTime() {
        if (!isDaytime()) { // 凌晨0-6点心跳频率降低
            return TIME_NIGHT;
        }
        return TIME;
    }

    /**
     * 是否为白天（凌晨0-6点不进行心跳和实体卡同步）
     */
    private boolean isDaytime() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH", Locale.getDefault());
        String str = df.format(date);
        int a = Integer.parseInt(str);
        return a < 0 || a >= 6;
    }

}
