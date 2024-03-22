package com.yc.apptool;

import android.app.Application;

import com.yc.apptool.thread.LogCallback;
import com.yc.toolutils.AppLogUtils;
import cn.ycbjie.ycthreadpoollib.PoolThread;

public class App extends Application {

    private static App instance;
    private PoolThread executor;

    public static synchronized App getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AppLogUtils.setShowLog(true);
        //初始化线程池管理器
        initThreadPool();
    }


    /**
     * 初始化线程池管理器
     */
    private void initThreadPool() {
        // 创建一个独立的实例进行使用
        executor = PoolThread.ThreadBuilder
                .createFixed(5)
                .setPriority(Thread.MAX_PRIORITY)
                .setCallback(new LogCallback())
                .build();
    }

    /**
     * 获取线程池管理器对象，统一的管理器维护所有的线程池
     * @return                      executor对象
     */
    public PoolThread getExecutor(){
        if(executor ==null){
            executor = PoolThread.ThreadBuilder
                    .createFixed(5)
                    .setPriority(Thread.MAX_PRIORITY)
                    .setCallback(new LogCallback())
                    .build();
        }
        return executor;
    }

}
