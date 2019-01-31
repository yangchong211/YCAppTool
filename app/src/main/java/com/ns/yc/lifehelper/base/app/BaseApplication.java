package com.ns.yc.lifehelper.base.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.ns.yc.lifehelper.BuildConfig;
import com.ns.yc.lifehelper.service.InitializeService;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.library.base.config.AppConfig;
import com.ycbjie.library.db.realm.RealmUtils;
import com.ycbjie.library.arounter.ARouterUtils;
import com.ycbjie.music.base.BaseAppHelper;

import cn.ycbjie.ycthreadpoollib.PoolThread;
import io.realm.Realm;


/**
 * <pre>
 *     @author      杨充
 *     blog         https://www.jianshu.com/p/53017c3fc75d
 *     time         2015/08/22
 *     desc         Application
 *     revise
 *     GitHub       https://github.com/yangchong211
 * </pre>
 */
public class BaseApplication extends Application {


    /**
     * 这个最先执行
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    /**
     * 程序启动的时候执行
     */
    @Override
    public void onCreate() {
        Log.d("Application", "onCreate");
        super.onCreate();
        AppConfig.INSTANCE.initConfig(this);
        BaseAppHelper.get().init(this);
        BaseLifecycleCallback.getInstance().init(this);
        //在子线程中初始化
        InitializeService.start(this);
    }



    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        Log.d("Application", "onTerminate");
        super.onTerminate();
        AppConfig.INSTANCE.closeRealm();
        AppConfig.INSTANCE.closeExecutor();
        ARouterUtils.destroy();
    }


    /**
     * 低内存的时候执行
     */
    @Override
    public void onLowMemory() {
        Log.d("Application", "onLowMemory");
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }


    /**
     * HOME键退出应用程序
     * 程序在内存清理的时候执行
     */
    @Override
    public void onTrimMemory(int level) {
        Log.d("Application", "onTrimMemory");
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN){
            Glide.get(this).clearMemory();
        }
        Glide.get(this).trimMemory(level);
    }


    /**
     * onConfigurationChanged
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("Application", "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }


}


