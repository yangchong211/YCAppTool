package com.yc.taskscheduler;

import android.os.Process;

import java.util.concurrent.atomic.AtomicInteger;


class ThreadFactory {

    static final class BackgroundRunnable implements Runnable {
        private Runnable runnable;
        BackgroundRunnable(Runnable runnable) {
            this.runnable = runnable;
        }
        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            runnable.run();
        }
    }

    static final java.util.concurrent.ThreadFactory TASKSCHEDULER_FACTORY = new java.util.concurrent.ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(new BackgroundRunnable(r), "TaskScheduler  #" + mCount.getAndIncrement());
        }
    };

    static final java.util.concurrent.ThreadFactory TIME_OUT_THREAD_FACTORY = new java.util.concurrent.ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(new BackgroundRunnable(r), "TaskScheduler timeoutThread #" + mCount.getAndIncrement());
        }
    };

    static final java.util.concurrent.ThreadFactory SCHEDULER_THREAD_FACTORY = new java.util.concurrent.ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(new BackgroundRunnable(r), "TaskScheduler scheduler #" + mCount.getAndIncrement());
        }
    };
}
