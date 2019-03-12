package com.ycbjie.library.base.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.ycbjie.library.arounter.ARouterUtils;
import com.ycbjie.library.base.config.AppConfig;
import com.ycbjie.library.constant.Constant;

import io.realm.Realm;
import io.realm.RealmConfiguration;

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

    @Override
    public void onCreate() {
        super.onCreate();
        initRealm(this);
        AppConfig.INSTANCE.initConfig(this);
        //在子线程中初始化
        InitializeService.start(this);
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
        Realm.getDefaultInstance().close();
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


    /**
     * 初始化数据库
     * @param application               application
     */
    public void initRealm(Application application){
        Realm.init(application);
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder()
                .name(Constant.REALM_NAME)
                .schemaVersion(Constant.REALM_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.getInstance(realmConfig);
        Realm.setDefaultConfiguration(realmConfig);
    }


}
