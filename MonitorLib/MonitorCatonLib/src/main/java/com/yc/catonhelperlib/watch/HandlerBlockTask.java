package com.yc.catonhelperlib.watch;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Printer;

import com.yc.toolutils.AppLogUtils;

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
    private long monitorTime;
    private boolean isOpen = true;
    private long nowTime = System.currentTimeMillis();
    private String stackMsg;

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

    public HandlerBlockTask(){
        monitorTime = BLOCK_TIME;
    }

    /**
     * 获取监控时间阈值
     * @return 时间
     */
    public long getMonitorTime() {
        return monitorTime;
    }

    /**
     * 设置监控时间阈值
     * @param monitorTime 时间
     */
    public HandlerBlockTask setMonitorTime(long monitorTime) {
        if(monitorTime < 20L){
            AppLogUtils.e(TAG,"setMonitorTime时间不能太小:"+monitorTime);
            return INSTANCE;
        }
        this.monitorTime = monitorTime;
        return INSTANCE;
    }

    /**
     * 是否开启检测
     * @return 结果
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * 设置是否要检测
     * @param open 是否检测
     */
    public HandlerBlockTask setOpen(boolean open) {
        isOpen = open;
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
            stackMsg = sb.toString();
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
            if(!isOpen){
                return;
            }
            if (x.startsWith(START)) {
                nowTime = SystemClock.uptimeMillis();
                //开始监控
                startMonitor();
            }
            if (x.startsWith(END)) {
                long cosTime = SystemClock.uptimeMillis() - nowTime;
                //移除监控
                removeMonitor();
                //计算出耗时
                if(cosTime > monitorTime){
                    String msg = "UI主线程耗时:"+cosTime+" 栈轨迹:"+ stackMsg;
                    AppLogUtils.e(TAG, msg);
                    onBlockNext(cosTime,stackMsg);
                }
            }
        }
    }

    private void startMonitor() {
        //开始监控，发送延迟消息
        mHandler.postDelayed(mBlockRunnable, getMonitorTime());
    }

    private void removeMonitor() {
        //移除监控
        mHandler.removeCallbacks(mBlockRunnable);
    }

    private void onBlockNext(long cos,String stackMsg){
        if(this.onBlockListener != null && mHandler != null){
            mHandler.post(() -> onBlockListener.onBlock(cos,stackMsg));
        }
    }

    private OnBlockListener onBlockListener;

    //供外部调用的set方法
    public HandlerBlockTask setOnFluentListener(OnBlockListener onBlockListener) {
        this.onBlockListener = onBlockListener;
        return INSTANCE;
    }


    public interface OnBlockListener {
        void onBlock(long cos,String stackMsg);
    }

}
