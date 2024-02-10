package com.yc.looperthread.heart;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;


/**
 * <pre>
 *     @author 杨充
 *     blog         https://www.jianshu.com/p/53017c3fc75d
 *     time         2019/01/4
 *     desc         轮询操作，测试实际开发中轮询操作逻辑。实际开发中比如抽奖活动，10秒一次轮询请求接口更新页面
 *     revise       不建议在application创建handler，runable做轮询操作
 *     GitHub       https://github.com/yangchong211
 * </pre>
 */
public class LoopRequestService extends Service {


    private static final String ACTION = "LoopService";
    /**
     * 客户端执行轮询的时间间隔，该值由StartQueryInterface接口返回，默认设置为10s
     */
    private static final int LOOP_INTERVAL_SECS = 10;
    /**
     * 当前服务是否正在执行
     */
    private boolean isServiceRunning;
    /**
     * 定时任务工具类
     */
    private Timer timer = new Timer();

    private Context context;
    private static final int MAX_COUNT = 20;

    public LoopRequestService() {
        isServiceRunning = false;
    }

    //-------------------------------使用闹钟执行轮询服务------------------------------------

    /**
     * 启动轮询服务
     */
    public static void startLoopService(Context context) {
        if (context == null) {
            return;
        }
        quitLoopService(context);
        Log.i("LoopRequestService", "LoopService开启轮询服务，轮询间隔：" + LOOP_INTERVAL_SECS + "s");
        PollingManager.startPolling(context,LOOP_INTERVAL_SECS,
                LoopRequestService.class,LoopRequestService.ACTION);
    }

    /**
     * 程序退出，绑定设备id接口请求成功，或者没有网络等则调用该方法
     * 停止轮询服务
     */
    public static void quitLoopService(Context context) {
        if (context == null) {
            return;
        }
        Log.i("LoopRequestService", "LoopService关闭轮询闹钟服务...");
        PollingManager.stopPolling(context, LoopRequestService.class,LoopRequestService.ACTION);
    }

    /**
     * 该方法在开启service时会调用一次
     */
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    /**
     * START_STICKY：
     * 如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。
     * 随后系统会尝试重新创建service，由于服务状态为开始状态，
     * 所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。
     * 如果在此期间没有任何启动命令被传 递到service，那么参数Intent将为null；
     * START_NOT_STICKY：“非粘性的”。
     * 使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统不会自动重启该服务；
     * START_REDELIVER_INTENT：重传Intent。
     * 使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，并将Intent的值传入；
     * START_STICKY_COMPATIBILITY：
     * START_STICKY的兼容版本，但不保证服务被kill后一定能重启。
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LoopRequestService", "LoopService开始执行轮询服务... \n 判断当前用户是否已经绑定设备id...");
        // 判断当前长连接状态，若长连接正常请求绑定id接口成功，或者请求超过5次，则关闭轮询服务
        Log.i("LoopRequestService", "LoopService当前用户有网络... \n 判断长连接是否已经连接...");
        if (count > MAX_COUNT) {
            Log.i("LoopRequestService", "LoopService已经绑定id成功，退出轮询服务...");
            quitLoopService(context);
        } else {
            if (isServiceRunning) {
                return START_STICKY;
            }
            // 启动轮询拉取消息
            startLoop();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("LoopRequestService", "LoopService轮询服务退出，执行onDestroy()方法，inServiceRunning赋值false");
        isServiceRunning = false;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 启动轮询拉去消息
     */
    private int count = 0;

    private void startLoop() {
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isServiceRunning = true;
                Log.i("LoopRequestService", "LoopService执行轮询操作...轮询服务中请求 sendLoopRequest绑定设备 接口...");
                //开始执行轮询的网络请求操作sendLoopRequest();
                //这里只是假设数据操作，如果轮询绑定失败，最多只操作5次
                //当然只要请求成功，绑定设备成功，则退出服务
                if (count < MAX_COUNT) {
                    sendLoopRequest();
                }
                count++;
            }
        }, 0, LOOP_INTERVAL_SECS * 1000);
    }

    /**
     * 开始执行轮训操作逻辑
     */
    private void sendLoopRequest() {
        Log.i("LoopRequestService", "开始执行轮训操作逻辑..." + count);
    }

    public static class PollingManager {
        /**
         * 开启轮询
         */
        public static void startPolling(Context context, int seconds, Class<?> cls, String action) {
            // 先获取系统AlarmManger服务
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            // 得到要执行的对应的Service的意图
            Intent intent = new Intent(context, cls);
            intent.setAction(action);
            PendingIntent pendingIntent = PendingIntent.getService(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            /*
             * 闹钟的第一次执行时间，以毫秒为单位，可以自定义时间，不过一般使用当前时间。
             * 需要注意的是，本属性与第一个属性（type）密切相关，
             * 如果第一个参数对应的闹钟使用的是相对时间（ELAPSED_REALTIME和ELAPSED_REALTIME_WAKEUP），
             * 那么本属性就得使用相对时间（相对于系统启动时间来说），比如当前时间就表示为：SystemClock.elapsedRealtime()；
             * 如果第一个参数对应的闹钟使用的是绝对时间（RTC、RTC_WAKEUP、POWER_OFF_WAKEUP），
             * 那么本属性就得使用绝对时间，比如当前时间就表示为：System.currentTimeMillis()。
             */

            // 启动服务的起始时间
            long triggerAtTime = SystemClock.elapsedRealtime();

            //AlarmManager这个类提供对系统闹钟服务的访问接口。
            //注册的闹钟在设备睡眠的时候仍然会保留，可以选择性地设置是否唤醒设备，但是当设备关机和重启后，闹钟将会被清除。
            //应用退出到后台也会进行轮询操作，相比在activity或者application操作，退出后台则不会轮询操作
            // 根据系统的AlarmManger服务设置轮询间隔时间和要执行的Service
            //RTC_WAKEUP
            //ELAPSED_REALTIME
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime,
                    seconds * 1000, pendingIntent);
        }

        /**
         * 停止轮询
         */
        public static void stopPolling(Context context, Class<?> cls, String action) {
            // 获取AlarmManager系统服务
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            // 获取意图
            Intent intent = new Intent(context, cls);
            intent.setAction(action);
            PendingIntent pendingIntent = PendingIntent.getService(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            // 取消当前执行的Service
            alarmManager.cancel(pendingIntent);
            // 关闭轮询服务
            Log.i("LoopRequestService", "LoopService关闭轮询服务...");
            context.stopService(intent);
            //关闭服务后直接就会调用onDestroy()方法
        }
    }

}
