package com.yc.catonhelperlib.watch;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.util.Printer;

public final class HandlerBlockTask {

    /**
     * 1.代码中，使用了一个工作线程mBlockThread来监控UI线程的卡顿。每次Looper的loop方法对消息进行处理之前，我们添加一个定时监控器。
     * 2.如果UI线程中的消息处理时间小于我们设定的阈值BLOCK_TIME，则取消已添加的定时器。
     */

    private final static String TAG = "HandlerBlockTask";
    private static final int BLOCK_TIME = 1000;
    private final HandlerThread mBlockThread = new HandlerThread("blockThread");
    private Handler mHandler;
    private static HandlerBlockTask INSTANCE;

    public static HandlerBlockTask getInstance() {
        if (INSTANCE == null) {
            synchronized (HandlerBlockTask.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HandlerBlockTask();
                }
            }
        }
        return INSTANCE;
    }

    private final Runnable mBlockRunnable = new Runnable() {
        @Override
        public void run() {
            StringBuilder sb = new StringBuilder();
            Looper mainLooper = Looper.getMainLooper();
            StackTraceElement[] stackTrace = mainLooper.getThread().getStackTrace();
            for (StackTraceElement s : stackTrace) {
                sb.append(s.toString()).append("\n");
            }
            Log.d(TAG, sb.toString());
        }
    };

    public void startWork(){
        mBlockThread.start();
        mHandler = new Handler(mBlockThread.getLooper());
        //获取主线程looper
        Looper mainLooper = Looper.getMainLooper();
        mainLooper.setMessageLogging(new MyPrinter());
    }

    private class MyPrinter implements Printer{

        //这个是在Looper源码loop方法中找到
        private static final String START = ">>>>> Dispatching";
        private static final String END = "<<<<< Finished";

        @Override
        public void println(String x) {
            if (x.startsWith(START)) {
                //开始监控
                startMonitor();
            }
            if (x.startsWith(END)) {
                //移除监控
                removeMonitor();
            }
        }
    }

    private void startMonitor() {
        //开始监控，发送延迟消息
        mHandler.postDelayed(mBlockRunnable, BLOCK_TIME);
    }

    private void removeMonitor() {
        //移除监控
        mHandler.removeCallbacks(mBlockRunnable);
    }

}
