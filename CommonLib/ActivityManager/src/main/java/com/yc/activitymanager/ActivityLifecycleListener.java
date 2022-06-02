package com.yc.activitymanager;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2018/11/9
 *     desc  : activity生命周期
 *     revise:
 * </pre>
 */
public abstract class ActivityLifecycleListener implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(@Nullable Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@Nullable Activity activity) {

    }

    @Override
    public void onActivityResumed(@Nullable Activity activity) {

    }

    @Override
    public void onActivityPaused(@Nullable Activity activity) {

    }

    @Override
    public void onActivityStopped(@Nullable Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@Nullable Activity activity, @Nullable Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@Nullable Activity activity) {

    }
}
