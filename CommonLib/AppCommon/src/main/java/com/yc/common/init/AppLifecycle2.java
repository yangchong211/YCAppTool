package com.yc.common.init;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.yc.applicationlib.BaseApplicationHelper;
import com.yc.toolutils.AppLogUtils;

public class AppLifecycle2 extends BaseApplicationHelper {

    public AppLifecycle2(Application application) {
        super(application);
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        AppLogUtils.d("AppLifecycle: " , "onConfigurationChanged2");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppLogUtils.d("AppLifecycle: " , "onCreate2");
    }

}
