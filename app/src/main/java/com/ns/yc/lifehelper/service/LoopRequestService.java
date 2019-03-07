package com.ns.yc.lifehelper.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <pre>
 *     @author      杨充
 *     blog         https://www.jianshu.com/p/53017c3fc75d
 *     time         2017/09/12
 *     desc         轮询操作，测试实际开发中轮询操作逻辑。实际开发中比如抽奖活动，30秒一次轮询请求接口更新页面
 *     revise
 *     GitHub       https://github.com/yangchong211
 * </pre>
 */
public class LoopRequestService extends Service {

    /**
     * 是否已经绑定了推送设备id
     */
    private volatile int isBindDevId = BindDevId.NONE;
    /*public enum isBindDevIdEnum {
        NONE, RUN, SUCCESS
    }*/
    @Retention(RetentionPolicy.SOURCE)
    public @interface BindDevId {
        int NONE = 1;
        int ERROR = 2;
        int SUCCESS = 3;
    }

    private static final String ACTION = "LoopService";
    /**
     * 客户端执行轮询的时间间隔，该值由StartQueryInterface接口返回，默认设置为30s
     */
    public static final int LOOP_INTERVAL_SECS = 5;
    /**
     * 当前服务是否正在执行
     */
    private boolean isServiceRunning ;
    /**
     * 定时任务工具类
     */
    public Timer timer = new Timer();

    private Context context;

    public LoopRequestService() {
        isServiceRunning = false;
    }

    //-------------------------------使用闹钟执行轮询服务------------------------------------

    /**
     * 启动轮询服务
     */
    public static void startLoopService(Context context) {
        if (context == null){
            return;
        }
        quitLoopService(context);
        LogUtils.i("LoopService开启轮询服务，轮询间隔：" + LOOP_INTERVAL_SECS + "s");
        AlarmManager manager = (AlarmManager) context.getApplicationContext()
                .getSystemService(Context.ALARM_SERVICE);
        //开启服务service
        Intent intent = new Intent(context.getApplicationContext(), LoopRequestService.class);
        intent.setAction(LoopRequestService.ACTION);
        PendingIntent pendingIntent = PendingIntent.getService(context.getApplicationContext(),
                1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        /*
         * 闹钟的第一次执行时间，以毫秒为单位，可以自定义时间，不过一般使用当前时间。需要注意的是，本属性与第一个属性（type）密切相关，
         * 如果第一个参数对应的闹钟使用的是相对时间（ELAPSED_REALTIME和ELAPSED_REALTIME_WAKEUP），那么本属性就得使用相对时间（相对于系统启动时间来说），
         *      比如当前时间就表示为：SystemClock.elapsedRealtime()；
         * 如果第一个参数对应的闹钟使用的是绝对时间（RTC、RTC_WAKEUP、POWER_OFF_WAKEUP），那么本属性就得使用绝对时间，
         *      比如当前时间就表示为：System.currentTimeMillis()。
         */
        if (manager != null) {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                    LOOP_INTERVAL_SECS * 1000, pendingIntent);
        }
    }

    /**
     * 停止轮询服务
     */
    public static void quitLoopService(Context context) {
        if (context == null){
            return;
        }
        LogUtils.i("LoopService关闭轮询闹钟服务...");
        AlarmManager manager = (AlarmManager) context.getApplicationContext()
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context.getApplicationContext(), LoopRequestService.class);
        intent.setAction(LoopRequestService.ACTION);
        PendingIntent pendingIntent = PendingIntent.getService(context.getApplicationContext(),
                1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (manager != null) {
            manager.cancel(pendingIntent);
        }
        // 关闭轮询服务
        LogUtils.i("LoopService关闭轮询服务...");
        context.stopService(intent);
        //关闭服务后直接就会调用onDestroy()方法
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
    START_STICKY：
        如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。
        随后系统会尝试重新创建service，由于服务状态为开始状态，
        所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。
        如果在此期间没有任何启动命令被传 递到service，那么参数Intent将为null；
    START_NOT_STICKY：“非粘性的”。
        使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统不会自动重启该服务；
    START_REDELIVER_INTENT：重传Intent。
        使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，并将Intent的值传入；
    START_STICKY_COMPATIBILITY：
        START_STICKY的兼容版本，但不保证服务被kill后一定能重启。
    */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i("LoopService开始执行轮询服务... \n 判断当前用户是否已登录...");
        // 若当前网络不正常或者是用户未登陆，则不再跳转
        if (NetworkUtils.isConnected()) {
            // 判断当前长连接状态，若长连接正常，则关闭轮询服务
            LogUtils.i("LoopService当前用户已登录... \n 判断长连接是否已经连接...");
            if (isBindDevId == BindDevId.SUCCESS) {
                LogUtils.i("LoopService已经绑定id成功，退出轮询服务...");
                quitLoopService(context);
            } else {
                if (isServiceRunning) {
                    return START_STICKY;
                }
                // 启动轮询拉取消息
                startLoop();
            }
        } else {
            LogUtils.i("LoopService没有网络时，关闭轮询服务...");
            quitLoopService(context);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i("LoopService轮询服务退出，执行onDestroy()方法，inServiceRunning赋值false");
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
                LogUtils.i("LoopService执行轮询操作...轮询服务中请求getInstance接口...");
                //开始执行轮询的网络请求操作sendLoopRequest();
                //这里只是假设数据操作
                if (count==5){
                    isBindDevId = BindDevId.SUCCESS;
                    quitLoopService(context);
                }
                count++;
            }
        }, 0, LOOP_INTERVAL_SECS * 1000);
    }
}
