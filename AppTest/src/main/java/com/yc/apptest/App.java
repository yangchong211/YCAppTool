package com.yc.apptest;

import android.app.Application;

import com.yc.looperthread.heart.HeartConfig;
import com.yc.looperthread.heart.HeartManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HeartConfig.Builder builder = HeartConfig.builder();
        HeartConfig heartConfig = builder.setContext(this).build();
        HeartManager.getInstance().initHeart(heartConfig);
    }
}
