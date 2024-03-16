package com.yc.appgraylib.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.yc.appgraylib.AppGrayHelper;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/5/11
 *     desc   : 生命周期监听器
 *     revise :
 * </pre>
 */
public final class ActivityCallback implements Application.ActivityLifecycleCallbacks {

    public static void inject(Application application) {
        application.registerActivityLifecycleCallbacks(new ActivityCallback());
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        boolean globalGray = AppGrayHelper.getInstance().isGlobalGray();
        if (activity != null && activity.getWindow() != null && globalGray){
            if (AppGrayHelper.getInstance().getType() == 1){
                AppGrayHelper.getInstance().setGray1(activity.getWindow());
            } else {
                AppGrayHelper.getInstance().setGray2(activity.getWindow().getDecorView());
            }
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {}

    @Override
    public void onActivityResumed(Activity activity) {}

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivityStopped(Activity activity) {}

    @Override
    public void onActivityDestroyed(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
}