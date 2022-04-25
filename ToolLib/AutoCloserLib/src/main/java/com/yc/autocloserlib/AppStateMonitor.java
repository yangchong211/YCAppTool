package com.yc.autocloserlib;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: 杨充
 * @email  : yangchong211@163.com
 * @time   : 2018/04/15
 * @desc   : 前后台监听
 * @revise :
 */
public class AppStateMonitor implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    public static final String TAG = "ApplicationState";
    private static final AppStateMonitor sInstance = new AppStateMonitor();
    private final ArrayList<StateListener> mStateListeners = new ArrayList<>();

    private final AtomicInteger mActiveActivitiesCount = new AtomicInteger(0);

    public static final int STATE_FOREGROUND = 1;
    public static final int STATE_BACKGROUND = 0;

    private int mState = STATE_BACKGROUND;

    public static AppStateMonitor getInstance() {
        return sInstance;
    }

    private final Handler mHandler = BackgroundThread.get().getHandler();

    private boolean mInitialized = false;

    private Runnable mInitialReportTask;

    public int getState() {
        return mState;
    }

    public void init(Context context) {
        if (mInitialized) return;
        mInitialized = true;

        Application application = (Application) context.getApplicationContext();
        application.registerActivityLifecycleCallbacks(this);

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


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        String msg = "onActivityCreated: " + activity +
                ", savedInstanceState:" + savedInstanceState +
                ", intent:" + activity.getIntent().clone() +
                ", callingPackage:" + activity.getCallingPackage() +
                ", callingActivity:" + activity.getCallingActivity();
        loggingAppState(msg);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        String msg = "onActivityStarted: " + activity;
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
    public void onActivityResumed(Activity activity) {
        String msg = "onActivityResumed: " + activity;
        loggingAppState(msg);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        String msg = "onActivityPaused: " + activity;
        loggingAppState(msg);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        String msg = "onActivityStopped: " + activity;
        loggingAppState(msg);
        int newState = mActiveActivitiesCount.decrementAndGet();
        if (newState == 0) {
            onStateChanged(STATE_BACKGROUND);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        String msg = "onActivitySaveInstanceState: " + activity + " outState:" + outState;
        loggingAppState(msg);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        String msg = "onActivityDestroyed: " + activity;
        loggingAppState(msg);
    }

    @Override
    public void onTrimMemory(int level) {
        String msg = "onTrimMemory: level:" + level;
        loggingAppState(msg);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        String msg = "onConfigurationChanged: " + newConfig;
        loggingAppState(msg);
    }

    @Override
    public void onLowMemory() {
        String msg = "onLowMemory: ";
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

    public void registerStateListener(StateListener l) {
        if (l == null) {
            return;
        }
        synchronized (mStateListeners) {
            mStateListeners.add(l);
        }
    }

    public void unregisterStateListener(StateListener l) {
        if (l == null) {
            return;
        }
        synchronized (mStateListeners) {
            mStateListeners.remove(l);
        }
    }

    private void loggingAppState(String msg) {
        Log.d("app auto closer msg : ", msg);
    }

    public interface StateListener {
        void onInForeground();

        void onInBackground();
    }
}
