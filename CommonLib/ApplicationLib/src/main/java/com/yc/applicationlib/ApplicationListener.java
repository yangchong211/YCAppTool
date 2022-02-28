package com.yc.applicationlib;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

/**
 * Application生命周期接口定义
 */
public interface ApplicationListener {

    void attachBaseContext(Context base);

    void onCreate(final Application app);

    void onTerminate(final Application app);

    void onConfigurationChanged(final Application app, final Configuration config);

    void onLowMemory(final Application app);

    void onTrimMemory(final Application app, final int level);
}
