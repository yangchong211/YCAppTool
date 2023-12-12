package com.yc.fpslib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Choreographer;
import android.view.FrameMetrics;
import android.view.Window;

import java.util.concurrent.atomic.AtomicInteger;

public final class PerformanceManager {

    private final Handler mMainHandler;
    private int mLastFrameRate;
    private final FrameRateRunnable mRateRunnable;
    private static final String TAG = "PerformanceManager";

    public static PerformanceManager getInstance() {
        return PerformanceManager.Holder.INSTANCE;
    }

    public void init(Context context) {
        if (context==null){
            throw new NullPointerException("context must be not null");
        }
    }


    /**
     * 定时任务
     * 1.使用线程，执行一个异步定时任务，每1000ms执行一次，用于统计1秒内的帧率。
     * 2.使用 Choreographer.getInstance().postFrameCallback(this) 注册 VSYNC 信号回调监听，当 VSYNC 信号返回时，会执行 doFrame 回调函数。
     * 3.在 doFrame 方法中，我们统计每秒内的执行次数，以及记录当前帧的时间，并注册一下次监听。
     */
    private class FrameRateRunnable implements Runnable, Choreographer.FrameCallback {
        
        private final AtomicInteger totalFramesPerSecond = new AtomicInteger(0);

        private FrameRateRunnable() {
            
        }

        public void run() {
            mLastFrameRate = totalFramesPerSecond.get();
            if (mLastFrameRate > 60) {
                mLastFrameRate = 60;
            }
            totalFramesPerSecond.set(0);
            mMainHandler.postDelayed(this, 1000L);
            Log.i(TAG,"fps runnable run");
        }

        /**
         * 在 doFrame 方法中，我们统计每秒内的执行次数，以及记录当前帧的时间
         * @param frameTimeNanos                nano帧时间
         */
        public void doFrame(long frameTimeNanos) {
            totalFramesPerSecond.getAndIncrement();
            //注册下一帧回调
            Choreographer.getInstance().postFrameCallback(this);
        }
    }


    @SuppressLint("SimpleDateFormat")
    private PerformanceManager() {
        //默认是60
        mLastFrameRate = 60;
        mMainHandler = new Handler(Looper.getMainLooper());
        mRateRunnable = new FrameRateRunnable();
    }

    /**
     * 开始
     */
    private void startMonitorFrameInfo() {
        mMainHandler.postDelayed(mRateRunnable, 1000L);
        //注册下一帧回调
        Choreographer.getInstance().postFrameCallback(mRateRunnable);
    }

    /**
     * 暂停
     */
    private void stopMonitorFrameInfo() {
        //移除下一帧回调
        Choreographer.getInstance().removeFrameCallback(mRateRunnable);
        mMainHandler.removeCallbacks(mRateRunnable);
    }

    private void startWindowFrameInfo(Activity activity) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && window!=null) {
            window.addOnFrameMetricsAvailableListener(listener,new Handler(Looper.getMainLooper()));
        } else {
            startMonitorFrameInfo();
        }
    }

    private void stopMonitorFrameInfo(Activity activity) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && window!=null) {
            window.removeOnFrameMetricsAvailableListener(listener);
        } else {
            stopMonitorFrameInfo();
        }
    }

    public void startMonitor() {
        PerformanceManager.getInstance().startMonitorFrameInfo();
    }

    public void stopMonitor() {
        PerformanceManager.getInstance().stopMonitorFrameInfo();
    }

    public void destroy() {
        //销毁
        stopMonitorFrameInfo();
    }

    @SuppressLint("NewApi")
    private final Window.OnFrameMetricsAvailableListener listener =  new Window.OnFrameMetricsAvailableListener() {
        @Override
        public void onFrameMetricsAvailable(Window window, FrameMetrics frameMetrics, int dropCountSinceLastInvocation) {
            //详细查看下 FrameMetrics 数据中定义了哪些渲染阶段

        }
    };

    private static class Holder {
        private static final PerformanceManager INSTANCE = new PerformanceManager();
        private Holder() {

        }
    }

}
