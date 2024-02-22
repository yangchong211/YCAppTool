package com.yc.looperthread.abs;

import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbsPollingThread implements IDoAction {

    protected static final String TAG = AbsPollingThread.class.getSimpleName();
    protected final AtomicLong count = new AtomicLong(0);
    private final AtomicBoolean isStart = new AtomicBoolean(false);
    protected volatile boolean beginRead = false;

    @Override
    public void startThread() {
        //防止多次启动
        if (!isStart.get()) {
            Log.v(TAG, "startThread");
            startPolling();
            isStart.set(true);
        }
    }

    @Override
    public void beginLoop() {
        Log.v(TAG, "beginLoop");
        //每次开启循环的时候都要检测一下线程是否需要跑起来
        startThread();
        beginRead = true;
    }

    @Override
    public void endLoop() {
        Log.v(TAG, "endRead");
        beginRead = false;
    }

    @Override
    public void release() {
        Log.v(TAG, "release");
        beginRead = false;
        isStart.set(false);
    }

    public abstract void startPolling();

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

}
