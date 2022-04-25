
package com.yc.autocloserlib;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * @author: 杨充
 * @email  : yangchong211@163.com
 * @time   : 2018/04/15
 * @desc   : Shared singleton background thread for each process.
 * @revise :
 */
public final class BackgroundThread extends HandlerThread {

    private static BackgroundThread sInstance;
    private static Handler sHandler;

    private BackgroundThread() {
        super("APM-BackgroundThread", android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            synchronized(BackgroundThread.class){
                if (sInstance == null){
                    sInstance = new BackgroundThread();
                    sInstance.start();
                    final Looper looper = sInstance.getLooper();
                    sHandler = new Handler(looper);
                }
            }
        }
    }

    public static BackgroundThread get() {
        synchronized (BackgroundThread.class) {
            ensureThreadLocked();
            return sInstance;
        }
    }

    public Handler getHandler() {
        return sHandler;
    }
}
