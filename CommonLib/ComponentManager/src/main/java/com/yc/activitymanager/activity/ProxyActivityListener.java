/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.yc.activitymanager.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     @author yangchong
 *     blog  : <a href="https://github.com/yangchong211">...</a>
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2018/11/9
 *     desc  : 代理类
 *     revise:
 * </pre>
 */
public class ProxyActivityListener extends AbsLifecycleListener {

    private final Map<Class<Activity>, List<AbsLifecycleListener>>
            mActivityLifecycleListeners = new HashMap<>();
    private final ActivityManager mActivityManager;

    ProxyActivityListener(ActivityManager manager) {
        this.mActivityManager = manager;
    }

    public void registerActivityLifecycleListener(Class<Activity> clazz,
                                                  AbsLifecycleListener lifecycleListener) {
        if (clazz == null || lifecycleListener == null) {
            return;
        }
        List<AbsLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(clazz);
        if (lifecycleListeners == null) {
            lifecycleListeners = new ArrayList<>();
        }
        lifecycleListeners.add(lifecycleListener);
        mActivityLifecycleListeners.put(clazz, lifecycleListeners);
    }

    public boolean unregisterActivityLifecycleListener(Class<Activity> clazz, AbsLifecycleListener lifecycleListener) {
        if (clazz == null || lifecycleListener == null) {
            return false;
        }
        synchronized (mActivityLifecycleListeners) {
            List<AbsLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(clazz);
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
    public void onActivityResumed(@Nullable Activity activity) {
        List<AbsLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(activity.getClass());
        if (lifecycleListeners == null || lifecycleListeners.size() == 0) {
            return;
        }

        List<AbsLifecycleListener> callbacks = new ArrayList<>(lifecycleListeners);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onActivityResumed(activity);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        List<AbsLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(activity.getClass());
        if (lifecycleListeners == null || lifecycleListeners.size() == 0) {
            return;
        }

        List<AbsLifecycleListener> callbacks = new ArrayList<>(lifecycleListeners);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onActivityStopped(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        List<AbsLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(activity.getClass());
        if (lifecycleListeners == null || lifecycleListeners.size() == 0) {
            return;
        }

        List<AbsLifecycleListener> callbacks = new ArrayList<>(lifecycleListeners);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onActivityPaused(activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        List<AbsLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(activity.getClass());
        if (lifecycleListeners == null || lifecycleListeners.size() == 0) {
            return;
        }

        List<AbsLifecycleListener> callbacks = new ArrayList<>(lifecycleListeners);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onActivitySaveInstanceState(activity, outState);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivityManager.remove(activity);
        List<AbsLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(activity.getClass());
        if (lifecycleListeners == null || lifecycleListeners.size() == 0) {
            return;
        }

        List<AbsLifecycleListener> callbacks = new ArrayList<>(lifecycleListeners);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onActivityDestroyed(activity);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        List<AbsLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(activity.getClass());
        if (lifecycleListeners == null || lifecycleListeners.size() == 0) {
            return;
        }

        List<AbsLifecycleListener> callbacks = new ArrayList<>(lifecycleListeners);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onActivityStarted(activity);
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivityManager.add(activity);
        List<AbsLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(activity.getClass());
        if (lifecycleListeners == null || lifecycleListeners.size() == 0) {
            return;
        }

        List<AbsLifecycleListener> callbacks = new ArrayList<>(lifecycleListeners);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onActivityCreated(activity, savedInstanceState);
        }
    }
}
