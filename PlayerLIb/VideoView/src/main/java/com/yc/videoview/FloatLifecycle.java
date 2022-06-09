package com.yc.videoview;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;

import com.yc.videoview.inter.ILifecycleListener;

/**
 * 用于控制悬浮窗显示周期
 * 使用了三种方法针对返回桌面时隐藏悬浮按钮
 * 1.startCount计数，针对back到桌面可以及时隐藏
 * 2.监听home键，从而及时隐藏
 * 3.resumeCount计时，针对一些只执行onPause不执行onStop的奇葩情况
 */

@RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class FloatLifecycle extends BroadcastReceiver implements Application.ActivityLifecycleCallbacks {

    private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    private static final long delay = 300;
    private final Handler mHandler;
    private final Class[] activities;
    private final boolean showFlag;
    private int startCount;
    private int resumeCount;
    private boolean appBackground;
    private final ILifecycleListener mLifecycleListener;


    public FloatLifecycle(Context applicationContext, boolean showFlag, Class[] activities,
                   ILifecycleListener lifecycleListener) {
        this.showFlag = showFlag;
        this.activities = activities;
        mLifecycleListener = lifecycleListener;
        mHandler = new Handler();
        ((Application) applicationContext).registerActivityLifecycleCallbacks(this);
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        applicationContext.registerReceiver(this, intentFilter);
    }


    private boolean needShow(Activity activity) {
        if (activities == null) {
            return true;
        }
        for (Class a : activities) {
            if (a.isInstance(activity)) {
                return showFlag;
            }
        }
        return !showFlag;
    }


    @Override
    public void onActivityResumed(Activity activity) {
        resumeCount++;
        if (needShow(activity)) {
            mLifecycleListener.onShow();
        }
        if (appBackground) {
            appBackground = false;
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        resumeCount--;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (resumeCount == 0) {
                    appBackground = true;
                    //mLifecycleListener.onPostHide();
                }
            }
        }, delay);

    }

    @Override
    public void onActivityStarted(Activity activity) {
        startCount++;
    }


    @Override
    public void onActivityStopped(Activity activity) {
        startCount--;
        if (startCount == 0) {
            //mLifecycleListener.onHide();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                //mLifecycleListener.onHide();
            }
        }
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }


    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }


}
