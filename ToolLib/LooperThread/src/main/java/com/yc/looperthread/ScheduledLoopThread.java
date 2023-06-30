package com.yc.looperthread;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 使用ScheduledExecutorService实现轮询
 */
public class ScheduledLoopThread extends AbsPollingThread {

    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void release() {
        super.release();
        scheduledExecutorService.shutdownNow();
        scheduledExecutorService = null;
    }

    @Override
    public void doAction() {
        Log.v(TAG, "scheduled doAction-> " + count.getAndIncrement());
    }

    @Override
    public void startPolling() {
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

}
