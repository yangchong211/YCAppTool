
package com.yc.catonhelperlib.canary.monitor;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public final class HandlerThreadFactory {

    private static final HandlerThreadWrapper sLoopThread = new HandlerThreadWrapper("loop");
    private static final HandlerThreadWrapper sWriteLogThread = new HandlerThreadWrapper("writer");

    private HandlerThreadFactory() {
        throw new InstantiationError("Must not instantiate this class");
    }

    public static Handler getTimerThreadHandler() {
        return sLoopThread.getHandler();
    }

    public static Handler getWriteLogThreadHandler() {
        return sWriteLogThread.getHandler();
    }

    private static class HandlerThreadWrapper {
        private final Handler handler;

        public HandlerThreadWrapper(String threadName) {
            //创建一个有自带looper的线程
            HandlerThread handlerThread = new HandlerThread("BlockCanary-" + threadName);
            handlerThread.start();
            Looper looper = handlerThread.getLooper();
            //创建一个handler对象
            handler = new Handler(looper);
        }

        public Handler getHandler() {
            return handler;
        }
    }
}
