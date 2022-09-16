package com.yc.applicationlib;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.yc.toolutils.AppLogUtils;
import com.yc.toolutils.BuildConfig;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : Application生命周期抽象类
 *     revise:
 * </pre>
 */
public abstract class BaseApplicationHelper implements IApplicationHelper {

    private final static String TAG = "BaseApplicationHelper";

    protected Application mApplication;

    public BaseApplicationHelper(Application application) {
        this.mApplication = application;
    }

    @Override
    public void attachBaseContext(Context base) {
        if (BuildConfig.DEBUG){
            AppLogUtils.d(TAG,"attachBaseContext");
        }
    }

    @Override
    public void onCreate(Application app) {
        if (BuildConfig.DEBUG){
            AppLogUtils.d(TAG,"onCreate");
        }
    }

    @Override
    public void onTerminate(Application app) {
        if (BuildConfig.DEBUG){
            AppLogUtils.d(TAG,"onTerminate");
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
            AppLogUtils.d(TAG,"onLowMemory");
        }
    }

    @Override
    public void onTrimMemory(Application app, int level) {
        if (BuildConfig.DEBUG){
            AppLogUtils.d(TAG,"onTrimMemory level : " + level);
        }
    }
}
