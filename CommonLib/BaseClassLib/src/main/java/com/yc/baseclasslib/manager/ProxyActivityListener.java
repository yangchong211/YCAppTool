package com.yc.baseclasslib.manager;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 代理类
 *     revise:
 * </pre>
 */
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
            if (lifecycleListeners == null) {
                return false;
            }
            boolean result = lifecycleListeners.remove(lifecycleListener);
            if (lifecycleListeners.size() == 0) {
                mActivityLifecycleListeners.remove(clazz);
            }
            return result;
        }
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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
