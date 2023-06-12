package com.yc.ycthreadpool;

import com.yc.ycthreadpoollib.PoolThread;
import com.yc.ycthreadpoollib.ScheduleTask;

public class App {


    private static App instance;
    private PoolThread executor;

    public static synchronized App getInstance() {
        if (null == instance) {
            instance = new App();
        }
        return instance;
    }

    public void init() {

    }




}
