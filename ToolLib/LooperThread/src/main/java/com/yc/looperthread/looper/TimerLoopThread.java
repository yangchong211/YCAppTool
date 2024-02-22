package com.yc.looperthread.looper;

import android.util.Log;

import com.yc.looperthread.abs.AbsPollingThread;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用Timer实现定时性周期性任务轮训
 */
public class TimerLoopThread extends AbsPollingThread {

    private final ReentrantLock commitLock = new ReentrantLock();
    private Timer timer;
    @Override
    public void release() {
        super.release();
        //销毁的时候，释放timer，避免造成内存泄漏
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void doAction() {
        Log.v(TAG, "timer doAction-> " + count.getAndIncrement());
    }

    @Override
    public void startPolling() {
        timer = new Timer();
        timer.schedule(new CommitTimer(), 0, getSleepTime());
    }

    private class CommitTimer extends TimerTask {
        @Override
        public void run() {
            commitLock.lock();
            try {
                // 轮询的代码
                if (beginRead && isLoop()) {
                    doAction();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                commitLock.unlock();
            }
        }
    }
}
