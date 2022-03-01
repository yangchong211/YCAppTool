package com.yc.taskscheduler;

import android.os.Process;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


class MyThreadFactory {

    static final class BackgroundRunnable implements Runnable {
        private final Runnable runnable;

        BackgroundRunnable(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            runnable.run();
        }
    }

    static final ThreadFactory TASK_SCHEDULER_FACTORY = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(new BackgroundRunnable(r), "TaskScheduler  #" + mCount.getAndIncrement());
        }
    };

    static final ThreadFactory TIME_OUT_THREAD_FACTORY = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(new BackgroundRunnable(r), "TaskScheduler timeoutThread #" + mCount.getAndIncrement());
        }
    };

    static final ThreadFactory SCHEDULER_THREAD_FACTORY = new ThreadFactory() {

        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(new BackgroundRunnable(r), "TaskScheduler scheduler #" + mCount.getAndIncrement());
        }
    };
}
