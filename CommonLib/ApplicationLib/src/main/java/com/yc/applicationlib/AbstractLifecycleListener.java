package com.yc.applicationlib;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractLifecycleListener implements ApplicationListener {

    @Override
    public void attachBaseContext(Context base) {

    }

    @Override
    public void onCreate(Application app) {

    }

    @Override
    public void onTerminate(Application app) {

    }

    @Override
    public void onConfigurationChanged(Application app, Configuration config) {

    }

    @Override
    public void onLowMemory(Application app) {

    }

    @Override
    public void onTrimMemory(Application app, int level) {

    }
}
