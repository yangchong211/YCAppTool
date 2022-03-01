package com.yc.applicationlib.activity;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProxyActivityListener extends ActivityLifecycleListener {

    private final Map<Class<Activity>, List<ActivityLifecycleListener>> mActivityLifecycleListeners = new HashMap<>();
    private final ActivityManager mActivityManager;

    ProxyActivityListener(ActivityManager manager) {
        this.mActivityManager = manager;
    }

    public void registerActivityLifecycleListener(Class<Activity> clazz, ActivityLifecycleListener lifecycleListener) {
        if (clazz == null || lifecycleListener == null) {
            return;
        }
        List<ActivityLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(clazz);
        if (lifecycleListeners == null) {
            lifecycleListeners = new ArrayList<>();
        }
        lifecycleListeners.add(lifecycleListener);
        mActivityLifecycleListeners.put(clazz, lifecycleListeners);
    }

    public boolean unregisterActivityLifecycleListener(Class<Activity> clazz, ActivityLifecycleListener lifecycleListener) {
        if (clazz == null || lifecycleListener == null) {
            return false;
        }
        synchronized (mActivityLifecycleListeners) {
            List<ActivityLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(clazz);
            if (lifecycleListener == null) {
                return false;
            }
            boolean result = lifecycleListeners.remove(lifecycleListener);
            if (lifecycleListeners.size() == 0) {
                mActivityLifecycleListeners.remove(clazz);
            }
            return result;
        }
    }

    public void onActivityResumed(Activity activity) {
        List<ActivityLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(activity.getClass());
        if (lifecycleListeners == null || lifecycleListeners.size() == 0) {
            return;
        }

        List<ActivityLifecycleListener> callbacks = new ArrayList<>(lifecycleListeners);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onActivityResumed(activity);
        }
    }

    public void onActivityStopped(Activity activity) {
        List<ActivityLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(activity.getClass());
        if (lifecycleListeners == null || lifecycleListeners.size() == 0) {
            return;
        }

        List<ActivityLifecycleListener> callbacks = new ArrayList<>(lifecycleListeners);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onActivityStopped(activity);
        }
    }

    public void onActivityPaused(Activity activity) {
        List<ActivityLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(activity.getClass());
        if (lifecycleListeners == null || lifecycleListeners.size() == 0) {
            return;
        }

        List<ActivityLifecycleListener> callbacks = new ArrayList<>(lifecycleListeners);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onActivityPaused(activity);
        }
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        List<ActivityLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(activity.getClass());
        if (lifecycleListeners == null || lifecycleListeners.size() == 0) {
            return;
        }

        List<ActivityLifecycleListener> callbacks = new ArrayList<>(lifecycleListeners);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onActivitySaveInstanceState(activity, outState);
        }
    }

    public void onActivityDestroyed(Activity activity) {
        mActivityManager.remove(activity);
        List<ActivityLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(activity.getClass());
        if (lifecycleListeners == null || lifecycleListeners.size() == 0) {
            return;
        }

        List<ActivityLifecycleListener> callbacks = new ArrayList<>(lifecycleListeners);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onActivityDestroyed(activity);
        }
    }

    public void onActivityStarted(Activity activity) {
        List<ActivityLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(activity.getClass());
        if (lifecycleListeners == null || lifecycleListeners.size() == 0) {
            return;
        }

        List<ActivityLifecycleListener> callbacks = new ArrayList<>(lifecycleListeners);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onActivityStarted(activity);
        }
    }

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivityManager.add(activity);
        List<ActivityLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(activity.getClass());
        if (lifecycleListeners == null || lifecycleListeners.size() == 0) {
            return;
        }

        List<ActivityLifecycleListener> callbacks = new ArrayList<>(lifecycleListeners);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onActivityCreated(activity, savedInstanceState);
        }
    }
}
