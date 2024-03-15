package com.yc.networklib.wifi;

import android.util.Log;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

class ConnectWiFiThread extends AtomicInteger implements ThreadFactory {

    private static final String TAG = "NetWork-Thread";
    private final AtomicInteger POOL_NUMBER = new AtomicInteger(1);

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r) {
            @Override
            public void run() {
                try {
                    super.run();
                } catch (Throwable e) {
                    Log.e(TAG, "Thread run threw throwable", e);
                }
            }
        };
        t.setName(TAG + new AtomicInteger(1)
                + "-pool-" + POOL_NUMBER.getAndIncrement() +
                "-thread-");
        t.setDaemon(false);
        t.setPriority(Thread.NORM_PRIORITY);
        t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Log.e(TAG, "Thread run threw uncaughtException throwable", e);
            }
        });
        return t;
    }
}
