package com.yc.looperthread;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbsScheduledLoopThread implements IDoAction {

    static final String TAG = AbsScheduledLoopThread.class.getSimpleName();
    private final AtomicBoolean isStart = new AtomicBoolean(false);
    private ScheduledExecutorService scheduledExecutorService;
    private volatile boolean beginRead = false;

    @Override
    public void startThread() {
        //防止多次启动
        if (!isStart.get()) {
            Log.v(TAG, "startThread");
            scheduledPolling();
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
        scheduledExecutorService.shutdownNow();
        scheduledExecutorService = null;
        isStart.set(false);
    }

    private void scheduledPolling() {
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            //如果此执行器已关闭，则返回true。
            if (!scheduledExecutorService.isShutdown()) {
                // 轮询的代码
                if (beginRead && isLoop()) {
                    doAction();
                }
            }
        }, 0, getSleepTime(), TimeUnit.MILLISECONDS);
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
}
