package com.yc.looperthread;

import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbsLoopThread extends Thread {

    static final String TAG = AbsLoopThread.class.getSimpleName();
    private volatile boolean beginRead = false;
    private final AtomicBoolean isStart = new AtomicBoolean(false);

    @Override
    public void run() {
        super.run();
        while (!interrupted()) {
            while (beginRead && isLoop()) {
                try {
                    doAction();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        Thread.sleep(getSleepTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 启动读卡线程
     */
    public void startThread() {
        //防止多次启动
        if (!isStart.get()) {
            Log.v(TAG, "startThread");
            start();
            isStart.set(true);
        }
    }


    /**
     * 开始循环
     */
    public void beginLoop() {
        startThread();
        Log.v(TAG, "beginLoop");
        //每次开启读卡的时候都要检测一下线程是否需要跑起来
        startThread();
        //一直寻卡
        beginRead = true;
    }

    /**
     * 暂停循环
     */
    public void endLoop() {
        Log.v(TAG, "endRead");
        beginRead = false;
    }

    /**
     * 释放线程。如果线程释放了，则没有办法再次start，除非是创建新的线程
     */
    public void release() {
        Log.v(TAG, "release");
        beginRead = false;
        //中断可以理解为线程的一个标志位，它表示了一个运行中的线程是否被其他线程进行了中断操作。
        interrupt();
        //使用stop方法强行终止线程（不推荐使用，可能发生不可预料的结果）
        //stop();
    }

    /**
     * 默认条件是true
     *
     * @return 布尔值
     */
    public boolean isLoop() {
        return true;
    }

    /**
     * 默认睡眠时间是100毫秒，子类可以重写该方法
     *
     * @return 时间
     */
    public long getSleepTime() {
        return 100;
    }

    /**
     * 循环体做的事情，子类必须实现
     */
    public abstract void doAction();
}
