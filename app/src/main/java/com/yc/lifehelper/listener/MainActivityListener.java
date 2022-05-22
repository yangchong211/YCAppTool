package com.yc.lifehelper.listener;

import android.app.Activity;
import android.os.Bundle;

import com.yc.baseclasslib.activity.ActivityLifecycleListener;


public class MainActivityListener extends ActivityLifecycleListener {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        super.onActivityCreated(activity, savedInstanceState);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        super.onActivityDestroyed(activity);
    }
}
