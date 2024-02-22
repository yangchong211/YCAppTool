package com.yc.looperthread.heart;

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

/**
 * Service和Activity是运行在当前app所在进程的mainThread(UI主线程)里面，
 * <p>
 * 如果选择是开启服务还是绑定服务？
 * 如果只是想开个服务在后台运行的话，直接startService即可；
 * 如果需要相互之间进行传值或者操作的话，就应该通过bindService。
 * <p>
 * <p>
 * 调用context.startService() ->onCreate()- >onStartCommand()->Service running-->
 * 调用context.stopService() ->onDestroy()
 * <p>
 * 调用context.bindService()->onCreate()->onBind()->Service running-->
 * 调用>onUnbind() -> onDestroy()
 */

public class HeartService extends Service {


    /**
     * 开启心跳action
     */
    public static final String ACTION_HEART_BEAT = "com.yc.heart.ACTION_HEART_BEAT";
    /**
     * 关闭心跳action
     */
    public static final String ACTION_STOP = "com.yc.heart.ACTION_STOP";
    /**
     * 闲时时钟心跳间隔，设置为10分钟
     */
    private static final int TIME_NIGHT = 2 * 10 * 1000;
    /**
     * 延迟意图
     */
    private PendingIntent pendingIntent;
    /**
     * 闹钟服务
     */
    private AlarmManager alarmManager;

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
        try {
            Intent service = new Intent(context, HeartService.class);
            service.setAction(HeartService.ACTION_HEART_BEAT);
            context.startService(service);
            HeartManager.getInstance().log("start 启动服务");
        } catch (Exception e) {
            HeartManager.getInstance().log("start 启动服务异常：" + e.getMessage());
        }
    }

    /**
     * 停止服务
     *
     * @param context 上下文
     */
    public static void stop(Context context) {
        try {
            Intent service = new Intent(context, HeartService.class);
            service.setAction(HeartService.ACTION_STOP);
            context.startService(service);
            HeartManager.getInstance().log("stop 停止服务");
        } catch (Exception e) {
            HeartManager.getInstance().log("stop 停止服务：" + e.getMessage());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HeartManager.getInstance().log("onCreate 服务");
        Context context = getApplicationContext();
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, HeartReceiver.class);
        //开启心跳action
        intent.setAction(ACTION_HEART_BEAT);
        pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HeartManager.getInstance().log("onDestroy 服务");
    }

    /**
     * 每次通过startService()方法启动Service时都会被回调。服务启动时调用
     *
     * @param intent  intent
     * @param flags   flags
     * @param startId startId
     * @return onStartCommand方法返回值作用：
     * START_STICKY：粘性，service进程被异常杀掉，系统重新创建进程与服务，会重新执行onCreate()、onStartCommand(Intent)
     * START_STICKY_COMPATIBILITY：START_STICKY的兼容版本，但不保证服务被kill后一定能重启。
     * START_NOT_STICKY：非粘性，Service进程被异常杀掉，系统不会自动重启该Service。
     * START_REDELIVER_INTENT：重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，并将Intent的值传入。
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        HeartManager.getInstance().log("onStartCommand");
        if (intent == null) {
            //启动下一次心跳定时任务
            HeartManager.getInstance().log("启动下一次心跳定时任务");
            startAlarmHeartBeat();
            return START_STICKY;
        }
        String action = intent.getAction();
        if (ACTION_HEART_BEAT.equals(action)) {
            doAction();
        } else if (ACTION_STOP.equals(action)) {
            releaseService();
        } else {
            HeartManager.getInstance().log("其他action " + action);
        }
        return START_STICKY;
    }

    private void doAction() {
        HeartManager.getInstance().log("doAction 这里启动下一次心跳定时任务");
        startAlarmHeartBeat();
        HeartManager.OnHeartBeat onHeartBeat = HeartManager.getInstance().getOnHeartBeat();
        if (onHeartBeat != null) {
            onHeartBeat.heartBeat();
        }
    }

    /**
     * 启动下一次心跳定时任务
     */
    public void startAlarmHeartBeat() {
        if (alarmManager != null) {
            long triggerAtMillis = System.currentTimeMillis() + getDelayTime();
            HeartManager.getInstance().log("启动下一次心跳定时任务 " + triggerAtMillis);
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }
    }

    /**
     * 关闭时钟心跳定时任务
     */
    public void stopAlarmHeartBeat() {
        if (alarmManager != null) {
            HeartManager.getInstance().log("关闭时钟心跳定时任务");
            alarmManager.cancel(pendingIntent);
        }
    }

    /**
     * 关闭时钟心跳，停止服务，释放资源
     *
     */
    private void releaseService() {
        HeartManager.getInstance().log("关闭时钟心跳，停止服务，释放资源");
        //停止时钟心跳。先停止时钟
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
    private long getDelayTime() {
        if (!isDaytime()) { // 凌晨0-6点心跳频率降低
            return TIME_NIGHT;
        }
        return HeartManager.getInstance().getHeartConfig().getLooperTime();
    }

    /**
     * 是否为白天（凌晨0-6点）
     */
    private boolean isDaytime() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH", Locale.getDefault());
        String str = df.format(date);
        int a = Integer.parseInt(str);
        return a < 0 || a >= 6;
    }

}
