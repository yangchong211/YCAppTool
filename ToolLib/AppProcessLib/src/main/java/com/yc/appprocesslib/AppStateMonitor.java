package com.yc.appprocesslib;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: 杨充
 * @email : yangchong211@163.com
 * @time : 2018/04/15
 * @desc : 前后台监听
 * @revise :
 * GitHub ：https://github.com/yangchong211/YCEfficient
 */
public class AppStateMonitor implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    public static final String TAG = "AppStateMonitor";
    private static final AppStateMonitor S_INSTANCE = new AppStateMonitor();
    private final ArrayList<StateListener> mStateListeners = new ArrayList<>();
    private final AtomicInteger mActiveActivitiesCount = new AtomicInteger(0);
    public static final int STATE_FOREGROUND = 1;
    public static final int STATE_BACKGROUND = 0;
    private int mState = STATE_BACKGROUND;
    private final Handler mHandler = BackgroundThread.get().getHandler();
    /**
     * 判断是否初始化
     */
    private boolean mInitialized = false;
    private Runnable mInitialReportTask;

    public static AppStateMonitor getInstance() {
        //静态单例
        return S_INSTANCE;
    }

    public void init(Context context) {
        if (mInitialized) {
            return;
        }
        Application application = (Application) context.getApplicationContext();
        application.registerActivityLifecycleCallbacks(this);
        sendDelayedMessage();
        mInitialized = true;
    }

    private void sendDelayedMessage() {
        mInitialReportTask = new Runnable() {
            @Override
            public void run() {
                if (mActiveActivitiesCount.get() == 0) {
                    onStateChanged(STATE_BACKGROUND);
                }
            }
        };
        mHandler.postDelayed(mInitialReportTask, 15 * 1000);
    }

    /**
     * 获取状态
     *
     * @return state状态
     */
    public int getState() {
        return mState;
    }

    /**
     * 判断是否在前台
     *
     * @return 返回true表示前台
     */
    public synchronized boolean isInForeground() {
        return this.mState == STATE_FOREGROUND;
    }

    /**
     * 判断是否在后台
     *
     * @return 返回true表示后台
     */
    public synchronized boolean isInBackground() {
        return this.mState == STATE_BACKGROUND;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TAG);
        stringBuilder.append("onActivityCreated: ");
        stringBuilder.append(activity);
        stringBuilder.append("savedInstanceState: ");
        stringBuilder.append(savedInstanceState);
        stringBuilder.append("intent: ");
        stringBuilder.append(activity.getIntent().clone());
        stringBuilder.append("callingPackage: ");
        stringBuilder.append(activity.getCallingPackage());
        stringBuilder.append("callingActivity: ");
        stringBuilder.append(activity.getCallingActivity());
        String msg = stringBuilder.toString();
        loggingAppState(msg);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        String msg = TAG + "onActivityStarted: " + activity;
        loggingAppState(msg);
        int old = mActiveActivitiesCount.getAndIncrement();
        if (old == 0) {
            onStateChanged(STATE_FOREGROUND);
        }

        if (mInitialReportTask != null) {
            mHandler.removeCallbacks(mInitialReportTask);
            mInitialReportTask = null;
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        String msg = TAG + "onActivityResumed: " + activity;
        loggingAppState(msg);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        String msg = TAG + "onActivityPaused: " + activity;
        loggingAppState(msg);
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        String msg = TAG + "onActivityStopped: " + activity;
        loggingAppState(msg);
        int newState = mActiveActivitiesCount.decrementAndGet();
        if (newState == 0) {
            onStateChanged(STATE_BACKGROUND);
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        String msg = TAG + "onActivitySaveInstanceState: " + activity + " outState:" + outState;
        loggingAppState(msg);
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        String msg = TAG + "onActivityDestroyed: " + activity;
        loggingAppState(msg);
    }

    @Override
    public void onTrimMemory(int level) {
        String msg = TAG + "onTrimMemory: level:" + level;
        loggingAppState(msg);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        String msg = TAG + "onConfigurationChanged: " + newConfig;
        loggingAppState(msg);
    }

    @Override
    public void onLowMemory() {
        String msg = TAG + "onLowMemory: ";
        loggingAppState(msg);
    }

    private void onStateChanged(int newState) {
        mState = newState;
        if (newState == STATE_BACKGROUND) {
            loggingAppState("App into background");
            dispatchOnInBackground();
        }
        if (newState == STATE_FOREGROUND) {
            loggingAppState("App into foreground");
            dispatchOnInForeground();
        }
    }

    private void dispatchOnInBackground() {
        Object[] callbacks = collectStateListeners();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((StateListener) callback).onInBackground();
            }
        }
    }

    private void dispatchOnInForeground() {
        Object[] callbacks = collectStateListeners();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((StateListener) callback).onInForeground();
            }
        }
    }

    private Object[] collectStateListeners() {
        Object[] callbacks = null;
        synchronized (mStateListeners) {
            if (mStateListeners.size() > 0) {
                callbacks = mStateListeners.toArray();
            }
        }
        return callbacks;
    }

    public void registerStateListener(StateListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (mStateListeners) {
            mStateListeners.add(listener);
        }
    }

    public void unregisterStateListener(StateListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (mStateListeners) {
            mStateListeners.remove(listener);
        }
    }

    private void loggingAppState(String msg) {
        if (msg != null) {
            Log.d("app auto closer msg : ", msg);
        }
    }

}
