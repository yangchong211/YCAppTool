package com.yc.localelib.locale.observer;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.yc.localelib.locale.service.LocaleService;

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
        LocaleService.getInstance().refreshLocale(activity);
        LocaleService.getInstance().refreshLocale(activity.getApplication());
    }

    @Override
    public void onActivityStarted(Activity activity) {}

    @Override
    public void onActivityResumed(Activity activity) {
        LocaleService.getInstance().refreshLocale(activity);
        LocaleService.getInstance().refreshLocale(activity.getApplication());
    }

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivityStopped(Activity activity) {}

    @Override
    public void onActivityDestroyed(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
}