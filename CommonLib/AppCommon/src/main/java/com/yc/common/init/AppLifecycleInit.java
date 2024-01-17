package com.yc.common.init;

import android.app.Application;
import android.content.res.Configuration;

import com.yc.applicationlib.BaseApplicationHelper;
import com.yc.toolutils.AppLogUtils;

public class AppLifecycleInit extends BaseApplicationHelper {

    public AppLifecycleInit(Application application) {
        super(application);
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        AppLogUtils.d("AppLifecycle: " , "onConfigurationChanged init");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppLogUtils.d("AppLifecycle: " , "onCreate init");
    }

}
