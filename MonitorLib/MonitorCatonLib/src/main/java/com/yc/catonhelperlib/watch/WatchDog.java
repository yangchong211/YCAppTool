package com.yc.catonhelperlib.watch;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

public final class WatchDog {

    private final static String TAG = "WatchDog";
    //一个标志
    private static final int TICK_INIT_VALUE = 0;
    private volatile int mTick = TICK_INIT_VALUE;
    //任务执行间隔
    private static final int DELAY_TIME = 4000;
    //UI线程Handler对象
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    //性能监控线程
    private final HandlerThread mWatchDogThread = new HandlerThread("WatchDogThread");
    //性能监控线程Handler对象
    private Handler mWatchDogHandler;
    private static WatchDog INSTANCE;

    public static WatchDog getInstance() {
        if (INSTANCE == null) {
            synchronized (HandlerBlockTask.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WatchDog();
                }
            }
        }
        return INSTANCE;
    }

    //定期执行的任务
    private final Runnable mDogRunnable = new Runnable() {
        @Override
        public void run() {
            if (null == mHandler) {
                Log.e(TAG, "handler is null");
                return;
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    //UI线程中执行
                    mTick++;
                }
            });
            try {
                //线程休眠时间为检测任务的时间间隔
                Thread.sleep(DELAY_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //当mTick没有自增时，表示产生了卡顿，这时打印UI线程的堆栈
            if (TICK_INIT_VALUE == mTick) {
                //mTick是0，这就表示上面的 mTick++ 并没有执行
                StringBuilder sb = new StringBuilder();
                Looper mainLooper = Looper.getMainLooper();
                StackTraceElement[] stackTrace = mainLooper.getThread().getStackTrace();
                for (StackTraceElement s : stackTrace) {
                    sb.append(s.toString()).append("\n");
                }
                Log.d(TAG, sb.toString());
            } else {
                //重新赋值为0
                mTick = TICK_INIT_VALUE;
            }
            mWatchDogHandler.postDelayed(mDogRunnable, DELAY_TIME);
        }
    };

    /**
     * 卡顿监控工作start方法
     */
    public void startWork(){
        mWatchDogThread.start();
        Looper looper = mWatchDogThread.getLooper();
        mWatchDogHandler = new Handler(looper);
        mWatchDogHandler.postDelayed(mDogRunnable, DELAY_TIME);
    }

}
