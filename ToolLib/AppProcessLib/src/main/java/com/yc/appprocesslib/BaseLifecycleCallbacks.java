package com.yc.appprocesslib;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;


/**
 * @author: 杨充
 * @email : yangchong211@163.com
 * @time : 2018/04/15
 * @desc : ActivityLifecycleCallbacks
 * @revise :
 * GitHub ：https://github.com/yangchong211/YCEfficient
 */
public class BaseLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    public static final String TAG = "AppStateMonitor ";

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TAG);
        stringBuilder.append("onActivityCreated: ");
        stringBuilder.append(activity);
        stringBuilder.append("savedInstanceState: ");
        stringBuilder.append(savedInstanceState);
        stringBuilder.append("intent: ");
        stringBuilder.append(activity.getIntent().clone());
        stringBuilder.append("callingPackage: ");
        stringBuilder.append(activity.getCallingPackage());
        stringBuilder.append("callingActivity: ");
        stringBuilder.append(activity.getCallingActivity());
        String msg = stringBuilder.toString();
        loggingAppState(msg);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        String msg = TAG + "onActivityStarted: " + activity;
        loggingAppState(msg);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        String msg = TAG + "onActivityResumed: " + activity;
        loggingAppState(msg);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        String msg = TAG + "onActivityPaused: " + activity;
        loggingAppState(msg);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        String msg = TAG + "onActivityStopped: " + activity;
        loggingAppState(msg);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        String msg = TAG + "onActivitySaveInstanceState: " + activity + " outState:" + outState;
        loggingAppState(msg);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        String msg = TAG + "onActivityDestroyed: " + activity;
        loggingAppState(msg);
    }


    private void loggingAppState(String msg) {
        if (AppStateLifecycle.getInstance().mIsDebug && msg != null) {
            Log.d("app callback : ", msg);
        }
    }

}
