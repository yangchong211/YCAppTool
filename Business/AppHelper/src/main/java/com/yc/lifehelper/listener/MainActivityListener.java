package com.yc.lifehelper.listener;

import android.app.Activity;
import android.os.Bundle;

import com.yc.activitymanager.activity.AbsLifecycleListener;


public class MainActivityListener extends AbsLifecycleListener {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        super.onActivityCreated(activity, savedInstanceState);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        super.onActivityDestroyed(activity);
    }
}
