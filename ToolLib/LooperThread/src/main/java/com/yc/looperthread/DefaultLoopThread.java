package com.yc.looperthread;

import android.util.Log;

import java.util.concurrent.atomic.AtomicLong;

public class DefaultLoopThread extends AbsLoopThread {

    private final AtomicLong count = new AtomicLong(0);

    @Override
    public long getSleepTime() {
        return 1000;
    }

    @Override
    public void doAction() {
        Log.v(TAG, "doAction-> " + count.getAndIncrement());
    }
}
