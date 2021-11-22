package com.yc.tracesdk;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

public class TraceService extends Service {
    private volatile Looper mServiceLooper;
    private volatile ServiceHandler mServiceHandler;
    private boolean mRedelivery;

    private boolean isStarted = false;

    private final static int DEFAULT_MSG = 0;
    /** 命令字段 */
    public final static String CMD_ACTION = "cmd_action";
    /** 空命令 */
    public final static int CMD_NONE = 0xDD000000;
    /** 开始采集位置信息的命令 */
    public final static int CMD_START = 0xDD000001;
    /** 停止采集位置信息的命令 */
    public final static int CMD_STOP = 0xDD000002;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                onHandleIntent((Intent) msg.obj);
            } catch (NullPointerException e) {
                //e.printStackTrace();
            }
        }
    }

    public TraceService() {
        super();
    }

    @Override
    public void onCreate() {
        LogHelper.log("LCService#onCreate");
        super.onCreate();
//
//        Notification notification = new Notification(R.drawable.ic_launcher,
//                "my_service_name",
//                System.currentTimeMillis());
//        PendingIntent p_intent = PendingIntent.getActivity(this, 0,
//                new Intent(this, MainActivity.class), 0);
//        notification.setLatestEventInfo(this, "MyServiceNotification", "MyServiceNotification is Running!", p_intent);
//        LogHelper.log(String.format("notification = %s", notification));
//        startForeground(1, notification);   // notification ID: 0x1982, you can name it as you will.

        HandlerThread thread = new HandlerThread("LCService[" + System.currentTimeMillis() + "]");
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        LogHelper.log("LCService#onStart");
        Message msg = mServiceHandler.obtainMessage(DEFAULT_MSG);
        msg.obj = intent;
        mServiceHandler.sendMessage(msg);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setIntentRedelivery(true);

        onStart(intent, startId);
        return START_NOT_STICKY;
        //return mRedelivery ? START_REDELIVER_INTENT : START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        LogHelper.log("LCService#onDestroy");
        mServiceLooper.quit();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void setIntentRedelivery(boolean enabled) {
        mRedelivery = enabled;
    }

    /**
     * 处理本次的{@code Intent}事件
     * 
     * @param intent
     *            本次事件的意图
     */
    private void onHandleIntent(Intent intent) {
        LogHelper.log("onHandleIntent thread id: " + Thread.currentThread().getId());
        int cmd = intent.getIntExtra(CMD_ACTION, CMD_NONE);
        switch (cmd) {
        case CMD_NONE:
            break;
        case CMD_START:
            startTrace();
            break;
        case CMD_STOP:
            stopTrace();
            mServiceHandler.removeMessages(DEFAULT_MSG);
            stopSelf();
            break;
        }
    }

    /**
     * 开始采集
     */
    private void startTrace() {

        synchronized (this) {

            if (isStarted) return;

            CellMonitor.getInstance(this).start();
            GpsMonitor.getInstance(this).start();

            if (TraceManager.getInstance(this).getLevel() == TraceManager.LEVEL_LOW) {
                WifiMonitor.getInstance(this).setEnableRegularScan(false);
            } else {
                WifiMonitor.getInstance(this).setEnableRegularScan(true);
            }
            if (Config.DEBUG) {
                WifiMonitor.getInstance(this).setEnableRegularScan(true);
            }
            WifiMonitor.getInstance(this).start();

            ExtraLocMonitor.getInstance(this).start();

            EnvMonitor.getInstance(this).start();

            isStarted = true;
        }
    }

    /**
     * 停止采集
     */
    private void stopTrace() {

        synchronized (this) {

            if (!isStarted) return;

            CellMonitor.getInstance(this).stop();
            GpsMonitor.getInstance(this).stop();
            WifiMonitor.getInstance(this).stop();
            ExtraLocMonitor.getInstance(this).stop();

            EnvMonitor.getInstance(this).stop();

            isStarted = false;
        }
    }
}
