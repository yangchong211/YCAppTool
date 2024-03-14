package com.yc.appprocesslib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Process;
import android.util.Log;


import java.util.concurrent.TimeUnit;

/**
 * @author: 杨充
 * @email  : yangchong211@163.com
 * @GitHub : https://github.com/yangchong211/YCEfficient
 * @time   : 2018/04/15
 * @desc   : app 进入后台一定时间后执行退出或者重启操作，有助于释放内存，减少用户电量消耗
 * @revise :
 */
public class AppAutoCloser implements Runnable {

    private static final String TAG = "AppAutoCloser";
    private static final int CONFIG_ARG_RESTART_DELAY = 30;
    @SuppressLint("StaticFieldLeak")
    private volatile static AppAutoCloser sInstance;
    private Context mContext;
    private boolean mInitialized;
    private final Handler mHandler = BackgroundThread.get().getHandler();
    private int mCloseType = CLOSE_TYPE_CLOSE;
    private static final int CLOSE_TYPE_CLOSE = 0;
    private static final int CLOSE_TYPE_RESTART = 1;
    private int mTime = -1;

    private AppAutoCloser() {

    }

    public synchronized static AppAutoCloser getInstance() {
        if (sInstance == null) {
            sInstance = new AppAutoCloser();
        }
        return sInstance;
    }

    public synchronized void init(Context context) {
        if (mInitialized){
            return;
        }
        if (context == null){
            throw new NullPointerException("context is not null");
        }
        mInitialized = true;
        mContext = context.getApplicationContext();
        AppStateMonitor.getInstance().init(mContext);
    }

    public void setTime(int time){
        this.mTime = time;
    }

    public void start() {
        if (!mInitialized){
            throw new NullPointerException("please init app auto closer lib at first");
        }
        if (mTime<0){
            mTime = CONFIG_ARG_RESTART_DELAY;
        }
        AppStateMonitor.getInstance().registerStateListener(new OnStateListener() {
            @Override
            public void onInForeground() {
                removeSchedule();
            }

            @Override
            public void onInBackground() {
                scheduleClose();
            }
        });
    }

    private synchronized void scheduleClose(long delay) {
        Log.i(TAG, "scheduleClose: delay=" + delay);
        mHandler.removeCallbacks(this);
        mHandler.postDelayed(this, delay);
    }

    @Override
    public void run() {
        if (mCloseType == CLOSE_TYPE_CLOSE) {
            closeNow();
        }
        if (mCloseType == CLOSE_TYPE_RESTART) {
            restartNow();
        }
    }

    private void scheduleClose() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mCloseType = CLOSE_TYPE_CLOSE;
                scheduleClose(TimeUnit.SECONDS.toMillis(mTime));
            }
        });
    }

    private void removeSchedule() {
        Log.i(TAG, "removeSchedule ");
        mHandler.removeCallbacks(this);
    }

    private void closeNow() {
        Log.i(TAG, "closeNow kii app");
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

    private void restartNow() {
        Log.i(TAG, "restartNow");
        try {
            String packageName = mContext.getPackageName();
            Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent == null) {
                return;
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            Process.killProcess(Process.myPid());
        } catch (Throwable e) {
            Log.e(TAG, "restartNow error: ", e);
        }
    }
}
