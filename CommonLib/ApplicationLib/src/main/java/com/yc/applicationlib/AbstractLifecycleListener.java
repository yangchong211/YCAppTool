package com.yc.applicationlib;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : Application生命周期抽象类
 *     revise:
 * </pre>
 */
public abstract class AbstractLifecycleListener implements ApplicationListener {

    private final static String TAG = "ApplicationListener";

    @Override
    public void attachBaseContext(Context base) {
        if (BuildConfig.DEBUG){
            Log.d(TAG,"attachBaseContext");
        }
    }

    @Override
    public void onCreate(Application app) {
        if (BuildConfig.DEBUG){
            Log.d(TAG,"onCreate");
        }
    }

    @Override
    public void onTerminate(Application app) {
        if (BuildConfig.DEBUG){
            Log.d(TAG,"onTerminate");
        }
    }

    @Override
    public void onConfigurationChanged(Application app, Configuration config) {
        if (BuildConfig.DEBUG){
            Log.d(TAG,"onConfigurationChanged");
        }
    }

    @Override
    public void onLowMemory(Application app) {
        if (BuildConfig.DEBUG){
            Log.d(TAG,"onLowMemory");
        }
    }

    @Override
    public void onTrimMemory(Application app, int level) {
        if (BuildConfig.DEBUG){
            Log.d(TAG,"onTrimMemory level : " + level);
        }
    }
}
