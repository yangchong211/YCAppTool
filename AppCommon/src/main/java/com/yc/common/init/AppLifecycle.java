package com.yc.common.init;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.yc.applicationlib.BaseApplicationHelper;
import com.yc.toolutils.AppLogUtils;

public class AppLifecycle extends BaseApplicationHelper {

    public AppLifecycle(Application application) {
        super(application);
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        AppLogUtils.d("AppLifecycle: " , "onConfigurationChanged");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppLogUtils.d("AppLifecycle: " , "onCreate");
    }

}
