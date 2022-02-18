package com.yc.localelib.observer;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


public final class ActivityLanguages implements Application.ActivityLifecycleCallbacks {

    static void inject(Application application) {
        application.registerActivityLifecycleCallbacks(new ActivityLanguages());
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        MultiLanguages.updateAppLanguage(activity);
        MultiLanguages.updateAppLanguage(activity.getApplication());
    }

    @Override
    public void onActivityStarted(Activity activity) {}

    @Override
    public void onActivityResumed(Activity activity) {
        MultiLanguages.updateAppLanguage(activity);
        MultiLanguages.updateAppLanguage(activity.getApplication());
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