package com.yc.catonhelperlib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Choreographer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class PerformanceManager {

    private final Handler mMainHandler;
    private final String fpsFileName;
    private final SimpleDateFormat simpleDateFormat;
    private int mLastFrameRate;
    private int mLastSkippedFrames;
    private Context mContext;
    private final FrameRateRunnable mRateRunnable;
    private static final String TAG = "PerformanceManager";

    public static PerformanceManager getInstance() {
        return PerformanceManager.Holder.INSTANCE;
    }


    public void init(Context context) {
        if (context==null){
            throw new NullPointerException("context must be not null");
        }
        FloatPageManager.getInstance().init(context);
        mContext = context.getApplicationContext();
    }


    /**
     * 定时任务
     * 1.使用线程，执行一个异步定时任务，每1000ms执行一次，用于统计1秒内的帧率。
     * 2.使用 Choreographer.getInstance().postFrameCallback(this) 注册 VSYNC 信号回调监听，当 VSYNC 信号返回时，会执行 doFrame 回调函数。
     * 3.在 doFrame 方法中，我们统计每秒内的执行次数，以及记录当前帧的时间，并注册一下次监听。
     */
    private class FrameRateRunnable implements Runnable, Choreographer.FrameCallback {
        
        private int totalFramesPerSecond;

        private FrameRateRunnable() {
            
        }

        public void run() {
            mLastFrameRate = totalFramesPerSecond;
            if (mLastFrameRate > 60) {
                mLastFrameRate = 60;
            }
            mLastSkippedFrames = 60 - mLastFrameRate;
            totalFramesPerSecond = 0;
            mMainHandler.postDelayed(this, 1000L);
            Log.i(TAG,"fps runnable run");
        }

        /**
         * 在 doFrame 方法中，我们统计每秒内的执行次数，以及记录当前帧的时间
         * @param frameTimeNanos                nano帧时间
         */
        public void doFrame(long frameTimeNanos) {
            ++totalFramesPerSecond;
            //注册下一帧回调
            Choreographer.getInstance().postFrameCallback(this);
            //写入文件
            writeFpsDataIntoFile();
        }
    }


    @SuppressLint("SimpleDateFormat")
    private PerformanceManager() {
        fpsFileName = "fps.txt";
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //默认是60
        mLastFrameRate = 60;
        mMainHandler = new Handler(Looper.getMainLooper());
        mRateRunnable = new FrameRateRunnable();
    }

    /**
     * 开始
     */
    public void startMonitorFrameInfo() {
        mMainHandler.postDelayed(mRateRunnable, 1000L);
        //注册下一帧回调
        Choreographer.getInstance().postFrameCallback(mRateRunnable);
    }

    /**
     * 暂停
     */
    public void stopMonitorFrameInfo() {
        //移除下一帧回调
        Choreographer.getInstance().removeFrameCallback(mRateRunnable);
        mMainHandler.removeCallbacks(mRateRunnable);
    }

    public void startMonitor() {
        PerformanceManager.getInstance().startMonitorFrameInfo();
        RealTimeChartPage.openChartPage("Fps检测",1000, null);
    }

    public void stopMonitor() {
        PerformanceManager.getInstance().stopMonitorFrameInfo();
        RealTimeChartPage.closeChartPage();
    }

    public void destroy() {
        //销毁
        stopMonitorFrameInfo();
    }

    private void writeFpsDataIntoFile() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mLastFrameRate);
        stringBuilder.append(" ");
        stringBuilder.append(simpleDateFormat.format(
                new Date(System.currentTimeMillis())));
        String string = stringBuilder.toString();
        String filePath = getFilePath(mContext);
        Log.i(TAG,"fps data is : "+string);
        //Samsung SM-A5160 Android 11, API 30
        Log.i(TAG,"fps data file path : "+filePath);
        ///data/user/0/com.com.yc.ycandroidtool/cache/yc/
        FileManager.writeTxtToFile(string, getFilePath(mContext), fpsFileName);
    }

    public String getFpsFilePath() {
        //获取file
        return getFilePath(mContext) + fpsFileName;
    }

    public long getLastFrameRate() {
        return (long)this.mLastFrameRate;
    }

    private String getFilePath(Context context) {
        return context.getCacheDir() + File.separator + "yc/";
    }

    public int getLastSkippedFrames() {
        return mLastSkippedFrames;
    }

    private static class Holder {
        
        private static final PerformanceManager INSTANCE = new PerformanceManager();

        private Holder() {
        }
    }

}
