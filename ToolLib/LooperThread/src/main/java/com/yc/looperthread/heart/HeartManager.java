package com.yc.looperthread.heart;

import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

public class HeartManager {

    private static final String TAG = "HeartService";
    private static volatile HeartManager singleton = null;
    private final AtomicBoolean isInit = new AtomicBoolean(false);
    private HeartConfig heartConfig;
    private boolean crash = true;

    public static HeartManager getInstance() {
        if (singleton == null) {
            synchronized (HeartManager.class) {
                if (singleton == null) {
                    singleton = new HeartManager();
                }
            }
        }
        return singleton;
    }

    public void initHeart(HeartConfig config) {
        if (isInit.get()) {
            log("已经初始化过了");
        } else {
            heartConfig = config;
        }
    }

    private HeartManager() {

    }

    public void start() {
        HeartService.start(heartConfig.getContext());
    }

    public void stop() {
        HeartService.stop(heartConfig.getContext());
    }

    public void log(String msg) {
        Log.d(TAG, msg);
    }

    public boolean isCrash() {
        return crash;
    }

    public void setCrash(boolean crash) {
        this.crash = crash;
    }
}
