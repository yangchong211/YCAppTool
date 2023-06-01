package com.yc.monitorthread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.util.Log;


import java.util.HashMap;
import java.util.Locale;

public class ThreadBlockWatchDog {

    private static final String TAG = ThreadBlockWatchDog.class.getSimpleName();

    private static final int DEFAULT_BLOCK_TIMEOUT = 500;

    private boolean logThreadsWithoutStackTrace = false;

    private String namePrefix = "";
    private int timeoutInterval = DEFAULT_BLOCK_TIMEOUT;

    private Handler handler = null;
    private ThreadBlockedListener listener;
    private HandlerThread watchThread;
    private final Thread mayBlockedThread;

    private static final HashMap<Long, ThreadBlockWatchDog> instanceMap = new HashMap<>();

    public ThreadBlockWatchDog(Thread thread) {
        mayBlockedThread = thread;
        Log.d(TAG, String.format(Locale.CHINA, "new instance to watch thread [ID]:%d [NAME]:%s",
                thread == null ? 0 : thread.getId(), thread == null ? "NULL" : thread.getName()));
    }

    public void tick() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler.postAtTime(watchRunnable, SystemClock.uptimeMillis() + timeoutInterval);
        }
    }

    private final Runnable watchRunnable = new Runnable() {
        @Override
        public void run() {
            if (mayBlockedThread == null) {
                Log.e(TAG, "thread to watch is null");
                return;
            }
            if (mayBlockedThread.getState() != Thread.State.RUNNABLE) {
                Log.d(TAG, "watched thread state: " + mayBlockedThread.getState().toString());
                if (mayBlockedThread.getState() == Thread.State.NEW) {
                    handler.postAtTime(watchRunnable, SystemClock.uptimeMillis() + timeoutInterval);
                    return;
                }
                if (mayBlockedThread.getState() == Thread.State.TERMINATED) {
                    Log.d(TAG, "watched thread state: " + mayBlockedThread.getState() + "should stop watch");
                    return;
                }
            }
            ThreadBlockMessage error = ThreadBlockMessage.New(namePrefix, logThreadsWithoutStackTrace);
            listener.onThreadBlocked(error);
        }
    };

    public static ThreadBlockWatchDog getInstance(Thread thread) {
        ThreadBlockWatchDog instance = null;
        if (thread != null) {
            instance = instanceMap.get(thread.getId());
        }
        if (instance == null) {
            instance = new ThreadBlockWatchDog(thread);
        }
        if (thread != null) {
            instanceMap.put(thread.getId(), instance);
        }
        return instance;
    }

    public ThreadBlockWatchDog setTimeoutInterval(int timeoutInterval) {
        this.timeoutInterval = timeoutInterval;
        return this;
    }

    public ThreadBlockWatchDog setThreadBlockedListener(ThreadBlockedListener listener) {
        this.listener = listener;
        return this;
    }

    public ThreadBlockWatchDog setReportThreadNamePrefix(String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        namePrefix = prefix;
        return this;
    }

    public void setLogThreadsWithoutStackTrace(boolean logThreadsWithoutStackTrace) {
        this.logThreadsWithoutStackTrace = logThreadsWithoutStackTrace;
    }

    public synchronized void start() {
        if (watchThread == null) {
            watchThread = newWatchThread();
            handler.postAtTime(watchRunnable, SystemClock.uptimeMillis() + timeoutInterval);
            return;
        }
        if (watchThread.getState() == Thread.State.TERMINATED) {
            watchThread = newWatchThread();
            handler.postAtTime(watchRunnable, SystemClock.uptimeMillis() + timeoutInterval);
            return;
        }
        Log.w(TAG, "watch thread is already started, state[" + watchThread.getState().name() + "]");
    }

    private HandlerThread newWatchThread() {
        HandlerThread thread = new HandlerThread("ThreadBlock");
        watchThread = thread;
        thread.start();
        handler = new Handler(thread.getLooper());
        return thread;
    }

}