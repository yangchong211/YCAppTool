package com.yc.library.base.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import androidx.multidex.MultiDex;
import android.util.Log;

import com.yc.library.base.config.AppConfig;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/05/23
 *     desc  : 可以做一些公共处理逻辑
 *     revise:
 * </pre>
 */
public class LibApplication extends Application {

    private static LibApplication instance;

    public static LibApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AppConfig.INSTANCE.initConfig(this);
        //在子线程中初始化
        InitializeService.start(this);
        initSetTransfer();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        Log.d("Application", "onTerminate");
        super.onTerminate();
        AppConfig.INSTANCE.closeExecutor();
    }


    /**
     * 低内存的时候执行
     */
    @Override
    public void onLowMemory() {
        Log.d("Application", "onLowMemory");
        super.onLowMemory();
    }


    /**
     * HOME键退出应用程序
     * 程序在内存清理的时候执行
     */
    @Override
    public void onTrimMemory(int level) {
        Log.d("Application", "onTrimMemory");
        super.onTrimMemory(level);
    }


    /**
     * onConfigurationChanged
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("Application", "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    private void initSetTransfer() {
    }


}
