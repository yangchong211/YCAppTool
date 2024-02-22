package com.yc.looperthread.looper;

import android.util.Log;

import com.yc.looperthread.abs.AbsPollingThread;

/**
 * 使用while实现定时性周期性任务轮训
 */
public class WhileLoopThread extends AbsPollingThread {

    private Thread thread;


    @Override
    public void release() {
        super.release();
        //中断可以理解为线程的一个标志位，它表示了一个运行中的线程是否被其他线程进行了中断操作。
        try {
            thread.interrupt();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            thread = null;
        }
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (thread != null && !thread.isInterrupted()) {
                while (beginRead && isLoop()) {
                    try {
                        doAction();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            //thread.wait(getSleepTime());
                            //线程轮训的方式中延时操作阻塞了线程
                            Thread.sleep(getSleepTime());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };

    @Override
    public void doAction() {
        Log.v(TAG, "thread doAction-> " + count.getAndIncrement());
    }

    @Override
    public void startPolling() {
        thread = new Thread(runnable);
        thread.start();
    }

}
