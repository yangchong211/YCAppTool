
package com.yc.catonhelperlib.canary.monitor;

import android.os.Handler;
import android.os.HandlerThread;

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
            HandlerThread handlerThread = new HandlerThread("BlockCanary-" + threadName);
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());
        }

        public Handler getHandler() {
            return handler;
        }
    }
}
