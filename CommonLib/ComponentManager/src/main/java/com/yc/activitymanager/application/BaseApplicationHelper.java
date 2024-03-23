package com.yc.activitymanager.application;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import com.yc.toolutils.system.AppLogUtils;
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
public class BaseApplicationHelper implements IApplicationHelper {

    private final static String TAG = "BaseApplicationHelper";

    protected Application mApplication;

    public BaseApplicationHelper(Application application) {
        this.mApplication = application;
    }

    @Override
    public void onCreate() {
        if (BuildConfig.DEBUG){
            AppLogUtils.d(TAG,"onCreate");
        }
    }

    @Override
    public void onTerminate() {
        if (BuildConfig.DEBUG){
            AppLogUtils.d(TAG,"onTerminate");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        if (BuildConfig.DEBUG){
            Log.d(TAG,"onConfigurationChanged");
        }
    }

    @Override
    public void onLowMemory() {
        if (BuildConfig.DEBUG){
            AppLogUtils.d(TAG,"onLowMemory");
        }
    }

    @Override
    public void onTrimMemory(int level) {
        if (BuildConfig.DEBUG){
            AppLogUtils.d(TAG,"onTrimMemory level : " + level);
        }
    }
}
