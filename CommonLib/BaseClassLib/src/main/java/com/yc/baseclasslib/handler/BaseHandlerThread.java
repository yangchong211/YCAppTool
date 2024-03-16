package com.yc.baseclasslib.handler;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public final class BaseHandlerThread {

    private final HandlerThread mThread;
    private final Handler mHandler;

    public BaseHandlerThread(String name, int priority) {
        this.mThread = new HandlerThread(name, priority);
        this.mThread.start();
        this.mHandler = new Handler(this.mThread.getLooper());
    }

    public Looper getThreadLooper() {
        return this.mThread == null ? null : this.mThread.getLooper();
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public boolean isAlive() {
        if (mThread == null) {
            return false;
        }
        return mThread.isAlive();
    }

    public void setDaemon(boolean isDaemon) {
        if (this.mThread != null) {
            this.mThread.setDaemon(isDaemon);
        }
    }

    public void start() {
        if (this.mThread != null) {
            this.mThread.start();
        }
    }

}
