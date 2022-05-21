
package com.yc.appprocesslib;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * @author: 杨充
 * @email : yangchong211@163.com
 * @time : 2018/04/15
 * @desc : Shared singleton background thread for each process.
 * @revise :
 * GitHub ：https://github.com/yangchong211/YCEfficient
 */
public final class BackgroundThread extends HandlerThread {

    private static BackgroundThread sInstance;
    private static Handler sHandler;

    private BackgroundThread() {
        super("BackgroundThread", android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            synchronized (BackgroundThread.class) {
                if (sInstance == null) {
                    //创建一个HandlerThread对象
                    sInstance = new BackgroundThread();
                    sInstance.start();
                    //获取该thread的独有looper对象
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
