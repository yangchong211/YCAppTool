package com.yc.looperthread.looper;

import android.util.Log;

import com.yc.looperthread.abs.AbsPollingThread;

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
        //固定速率计时器（ scheduleAtFixedRate() ）基于开始时间（因此每个迭代将在startTime + iterationNumber * delayTime处执行）。
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            //如果此执行器已关闭，则返回true。
            if (!scheduledExecutorService.isShutdown()) {
                // 轮询的代码
                if (beginRead && isLoop()) {
                    doAction();
                }
            }
        }, 0, getSleepTime(), TimeUnit.MILLISECONDS);


        //固定延迟计时器（ schedule() ）基于前一次执行（因此每次迭代将在lastExecutionTime + delayTime处执行）。
//        scheduledExecutorService.schedule(() -> {
//            //如果此执行器已关闭，则返回true。
//            if (!scheduledExecutorService.isShutdown()) {
//                // 轮询的代码
//                if (beginRead && isLoop()) {
//                    doAction();
//                }
//            }
//        },  getSleepTime(), TimeUnit.MILLISECONDS);
    }

}
