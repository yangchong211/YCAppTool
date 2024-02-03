package com.yc.looperthread.heart;

import android.util.Log;

import com.yc.looperthread.abs.IDoAction;

import java.util.concurrent.atomic.AtomicBoolean;

public class HeartManager implements IDoAction {

    private static final String TAG = "HeartService";
    private static volatile HeartManager singleton = null;
    private final AtomicBoolean isInit = new AtomicBoolean(false);
    private HeartConfig heartConfig;

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


    @Override
    public void startThread() {
        HeartService.start(heartConfig.getContext());
    }

    @Override
    public void beginLoop() {

    }

    @Override
    public void endLoop() {

    }

    @Override
    public void release() {
        HeartService.stop(heartConfig.getContext());
    }

    @Override
    public void doAction() {

    }

    public void log(String msg) {
        Log.d(TAG, msg);
    }
}
