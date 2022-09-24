package com.yc.window.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import com.yc.window.FloatWindow;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/08/18
 *     desc  : activity生命周期监听
 *     revise:
 * </pre>
 */
public final class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private Activity mActivity;
    private FloatWindow mFloatWindow;

    public ActivityLifecycle(FloatWindow floatWindow, Activity activity) {
        mActivity = activity;
        mFloatWindow = floatWindow;
    }

    /**
     * 注册监听
     */
    public void register() {
        if (mActivity == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mActivity.registerActivityLifecycleCallbacks(this);
        } else {
            mActivity.getApplication().registerActivityLifecycleCallbacks(this);
        }
    }

    /**
     * 取消监听
     */
    public void unregister() {
        if (mActivity == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mActivity.unregisterActivityLifecycleCallbacks(this);
        } else {
            mActivity.getApplication().unregisterActivityLifecycleCallbacks(this);
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

    @Override
    public void onActivityStarted(Activity activity) {}

    @Override
    public void onActivityResumed(Activity activity) {}

    @Override
    public void onActivityPaused(Activity activity) {
        // 一定要在 onPaused 方法中销毁掉，如果放在 onDestroyed 方法中还是有一定几率会导致内存泄露
        if (mActivity != activity || !mActivity.isFinishing() || mFloatWindow == null || !mFloatWindow.isShowing()) {
            return;
        }
        mFloatWindow.dismiss();
    }

    @Override
    public void onActivityStopped(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (mActivity != activity) {
            return;
        }
        // 释放 Activity 的引用
        mActivity = null;

        if (mFloatWindow == null) {
            return;
        }
        mFloatWindow.recycle();
        mFloatWindow = null;
    }
}