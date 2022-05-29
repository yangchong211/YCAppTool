package com.yc.baseclasslib.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2018/11/9
 *     desc  : 父类Application
 *     revise:
 * </pre>
 */
public class BaseApplication extends Application {

    /**
     * 最先执行的是这个方法
     * Process.start->Application创建->attachBaseContext->onCreate
     *
     * @param base base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    /**
     * 在应用程序启动时调用
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 当App运行时设备配置发生更改时由系统调用。
     * 如果您覆盖此方法，则必须调用超类实现。
     *
     * @param newConfig config
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 此方法用于模拟过程环境。它永远不会在生产 Android 设备上调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * 当整个系统的内存不足时调用它，并且主动运行的进程应该减少它们的内存使用量。
     *
     * @param level level
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    /**
     * 当整个系统的内存不足时调用它，并且主动运行的进程应该减少它们的内存使用量
     * 实现此方法以释放您可能持有的任何缓存或其他不必要的资源。从这个方法返回后，系统会为你执行一次垃圾回收。
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


}
