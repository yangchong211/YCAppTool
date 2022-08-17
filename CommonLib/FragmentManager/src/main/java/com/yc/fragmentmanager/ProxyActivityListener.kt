package com.yc.fragmentmanager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2018/11/9
 *     desc  : 代理类
 *     revise:
 * </pre>
 */
public class ProxyActivityListener extends FragmentLifecycleListener {

    private final Map<Class<Activity>, List<FragmentLifecycleListener>>
            mActivityLifecycleListeners = new HashMap<>();


    public void registerFragmentLifecycleListener(Class<Activity> clazz,
                                                  FragmentLifecycleListener lifecycleListener) {
        if (clazz == null || lifecycleListener == null) {
            return;
        }
        List<FragmentLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(clazz);
        if (lifecycleListeners == null) {
            lifecycleListeners = new ArrayList<>();
        }
        lifecycleListeners.add(lifecycleListener);
        mActivityLifecycleListeners.put(clazz, lifecycleListeners);
    }

    public boolean unregisterFragmentLifecycleListener(Class<Activity> clazz,
                                                       FragmentLifecycleListener lifecycleListener) {
        if (clazz == null || lifecycleListener == null) {
            return false;
        }
        synchronized (mActivityLifecycleListeners) {
            List<FragmentLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(clazz);
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
    public void onFragmentAttached(@NonNull @NotNull FragmentManager fm,
                                   @NonNull @NotNull Fragment f, @NonNull @NotNull Context context) {
        List<FragmentLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(f.getClass());
        if (lifecycleListeners == null || lifecycleListeners.size() == 0) {
            return;
        }

        List<FragmentLifecycleListener> callbacks = new ArrayList<>(lifecycleListeners);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onFragmentAttached(fm, f, context);
        }
    }

    @Override
    public void onFragmentCreated(@NonNull @NotNull FragmentManager fm, @NonNull @NotNull Fragment f, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        List<FragmentLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(f.getClass());
        if (lifecycleListeners == null || lifecycleListeners.size() == 0) {
            return;
        }

        List<FragmentLifecycleListener> callbacks = new ArrayList<>(lifecycleListeners);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onFragmentCreated(fm, f, savedInstanceState);
        }
    }

    @Override
    public void onFragmentStarted(@NonNull @NotNull FragmentManager fm, @NonNull @NotNull Fragment f) {
        List<FragmentLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(f.getClass());
        if (lifecycleListeners == null || lifecycleListeners.size() == 0) {
            return;
        }

        List<FragmentLifecycleListener> callbacks = new ArrayList<>(lifecycleListeners);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onFragmentStarted(fm, f);
        }
    }

    @Override
    public void onFragmentResumed(@NonNull @NotNull FragmentManager fm, @NonNull @NotNull Fragment f) {
        List<FragmentLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(f.getClass());
        if (lifecycleListeners == null || lifecycleListeners.size() == 0) {
            return;
        }

        List<FragmentLifecycleListener> callbacks = new ArrayList<>(lifecycleListeners);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onFragmentResumed(fm, f);
        }
    }

    @Override
    public void onFragmentPaused(@NonNull @NotNull FragmentManager fm, @NonNull @NotNull Fragment f) {
        List<FragmentLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(f.getClass());
        if (lifecycleListeners == null || lifecycleListeners.size() == 0) {
            return;
        }

        List<FragmentLifecycleListener> callbacks = new ArrayList<>(lifecycleListeners);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onFragmentPaused(fm, f);
        }
    }

    @Override
    public void onFragmentStopped(@NonNull @NotNull FragmentManager fm, @NonNull @NotNull Fragment f) {
        List<FragmentLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(f.getClass());
        if (lifecycleListeners == null || lifecycleListeners.size() == 0) {
            return;
        }

        List<FragmentLifecycleListener> callbacks = new ArrayList<>(lifecycleListeners);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onFragmentStopped(fm, f);
        }
    }

    @Override
    public void onFragmentDestroyed(@NonNull @NotNull FragmentManager fm, @NonNull @NotNull Fragment f) {
        List<FragmentLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(f.getClass());
        if (lifecycleListeners == null || lifecycleListeners.size() == 0) {
            return;
        }

        List<FragmentLifecycleListener> callbacks = new ArrayList<>(lifecycleListeners);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onFragmentDestroyed(fm, f);
        }
    }

    @Override
    public void onFragmentDetached(@NonNull @NotNull FragmentManager fm, @NonNull @NotNull Fragment f) {
        List<FragmentLifecycleListener> lifecycleListeners = mActivityLifecycleListeners.get(f.getClass());
        if (lifecycleListeners == null || lifecycleListeners.size() == 0) {
            return;
        }

        List<FragmentLifecycleListener> callbacks = new ArrayList<>(lifecycleListeners);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).onFragmentDetached(fm, f);
        }
    }
}
