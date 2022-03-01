package com.yc.appstatuslib.backgroud;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.ArrayList;

public class AppStateMonitor implements ActivityLifecycleRegister.ActivityLifecycleListener {

    private static final String TAG = "AppStateMonitor";
    private static final AppStateMonitor sInstance = new AppStateMonitor();
    private final ArrayList<AppStateListener> mAppStateListeners = new ArrayList<>();
    private static final int STATE_FOREGROUND = 1;
    private static final int STATE_BACKGROUND = 0;
    private int mState = 0;
    private final ArrayList<Activity> mStartedActivities = new ArrayList<>();

    private AppStateMonitor() {

    }

    public static AppStateMonitor getInstance() {
        return sInstance;
    }

    public synchronized void init(Application application){
        ActivityLifecycleRegister.init(application);
    }

    public int getState() {
        return this.mState;
    }

    public synchronized boolean isInForeground() {
        return this.mState == STATE_FOREGROUND;
    }

    public synchronized boolean isInBackground() {
        return this.mState == STATE_BACKGROUND;
    }

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    public void onActivityStarted(Activity activity) {
        if (this.mStartedActivities.isEmpty()) {
            this.onStateChanged(STATE_FOREGROUND);
        }

        this.mStartedActivities.add(activity);
    }

    public void onActivityResumed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
        this.mStartedActivities.remove(activity);
        if (this.mStartedActivities.isEmpty()) {
            this.onStateChanged(STATE_BACKGROUND);
        }

    }

    public void onActivityDestroyed(Activity activity) {
    }

    private void onStateChanged(int newState) {
        this.mState = newState;
        if (newState == STATE_BACKGROUND) {
            this.dispatchOnInBackground();
        }
        if (newState == STATE_FOREGROUND) {
            this.dispatchOnInForeground();
        }
    }

    private void dispatchOnInBackground() {
        Object[] callbacks = this.collectAppStateListeners();
        if (callbacks != null) {
            Object[] data = callbacks;
            int length = callbacks.length;
            for(int i = 0; i < length; ++i) {
                Object callback = data[i];
                ((AppStateMonitor.AppStateListener)callback).onInBackground();
            }
        }

    }

    private void dispatchOnInForeground() {
        Object[] callbacks = this.collectAppStateListeners();
        if (callbacks != null) {
            Object[] data = callbacks;
            int length = callbacks.length;

            for(int i = 0; i < length; ++i) {
                Object callback = data[i];
                ((AppStateMonitor.AppStateListener)callback).onInForeground();
            }
        }

    }

    private Object[] collectAppStateListeners() {
        Object[] callbacks = null;
        synchronized(this.mAppStateListeners) {
            if (this.mAppStateListeners.size() > 0) {
                callbacks = this.mAppStateListeners.toArray();
            }
            return callbacks;
        }
    }

    public void registerAppStateListener(AppStateMonitor.AppStateListener l) {
        if (l != null) {
            synchronized(this.mAppStateListeners) {
                this.mAppStateListeners.add(l);
            }
        }
    }

    public void unregisterAppStateListener(AppStateMonitor.AppStateListener l) {
        if (l != null) {
            synchronized(this.mAppStateListeners) {
                this.mAppStateListeners.remove(l);
            }
        }
    }

    public interface AppStateListener {
        void onInForeground();

        void onInBackground();
    }


}
