package com.yc.baseclasslib.handler;

import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * 快速创建HandlerThread对象
 */
public final class ThreadManager {

    private final Map<String, BaseHandlerThread> mHandlerThreadMap;
    private final Object mLock;
    public static final String NORMAL_THREAD = "NormalThread";

    private ThreadManager() {
        mLock = new Object();
        mHandlerThreadMap = new HashMap<>();
    }

    public static ThreadManager getInstance() {
        return InstanceHolder.mInstance;
    }

    private static class InstanceHolder {
        public static ThreadManager mInstance = new ThreadManager();
        private InstanceHolder() {
        }
    }

    public void postTaskThread(Runnable task) {
        postTaskThreadDelay(task, 0L);
    }

    public void postTaskThreadDelay(Runnable task, long delayMillis) {
        try {
            BaseHandlerThread normalHandlerThread = getHandlerThread(NORMAL_THREAD);
            if (normalHandlerThread == null || normalHandlerThread.getHandler() == null) {
                Log.e("ThreadManager", "[postTaskThreadDelay] can`t get thread with type = NormalThread");
                return;
            }

            normalHandlerThread.getHandler().postDelayed(task, delayMillis);
        } catch (Exception exception) {
            Log.e("ThreadManager", "Error happens in [postTaskThreadDelay] : "
                    + Log.getStackTraceString(exception));
        }
    }

    public BaseHandlerThread getHandlerThread(String type) {
        return getHandlerThread(type, false);
    }

    public BaseHandlerThread getHandlerThread(String type, boolean isDaemon) {
        synchronized(mLock) {
            BaseHandlerThread baseHandlerThread;
            try {
                BaseHandlerThread handlerThread;
                if (mHandlerThreadMap.containsKey(type) && mHandlerThreadMap.get(type) != null) {
                    handlerThread = mHandlerThreadMap.get(type);
                    if (handlerThread == null) {
                        return null;
                    }
                    handlerThread.setDaemon(isDaemon);
                    if (!handlerThread.isAlive()) {
                        handlerThread.start();
                    }
                } else {
                    handlerThread = new BaseHandlerThread(type, getPriority(type));
                    mHandlerThreadMap.put(type, handlerThread);
                }
                baseHandlerThread = handlerThread;
            } catch (Exception exception) {
                Log.e("ThreadManager", "error happens in [getHandlerThread] with type: "
                        + type + " errors :" + Log.getStackTraceString(exception));
                return null;
            }
            return baseHandlerThread;
        }
    }

    private static int getPriority(String type) {
        return TextUtils.equals(type, NORMAL_THREAD) ? 1 : 0;
    }
    
}
