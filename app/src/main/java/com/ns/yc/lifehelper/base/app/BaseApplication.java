package com.ns.yc.lifehelper.base.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.ns.yc.lifehelper.comment.config.AppConfig;
import com.ns.yc.lifehelper.comment.Constant;
import com.ns.yc.lifehelper.inter.callback.LogCallback;
import com.ns.yc.lifehelper.service.InitializeService;

import java.io.File;

import cn.ycbjie.ycthreadpoollib.PoolThread;
import io.realm.Realm;
import io.realm.RealmConfiguration;


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


    private static BaseApplication instance;
    private PoolThread executor;
    private Realm realm;

    public static synchronized BaseApplication getInstance() {
        if (null == instance) {
            instance = new BaseApplication();
        }
        return instance;
    }

    public BaseApplication(){}

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
        instance = this;
        Utils.init(this);
        BaseLifecycleCallback.getInstance().init(this);
        initRealm();
        initThreadPool();
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
        if(realm!=null){
            realm.close();
            realm = null;
        }
        if(executor!=null){
            executor.close();
            executor = null;
        }
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

    /**
     * 初始化数据库
     */
    private void initRealm() {
        File file ;
        try {
            file = new File(Constant.ExternalStorageDirectory, Constant.DATABASE_FILE_PATH_FOLDER);
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        } catch (Exception e) {
            Log.e("异常",e.getMessage());
        }
        Realm.init(instance);
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder()
                .name(Constant.REALM_NAME)
                .schemaVersion(Constant.REALM_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(realmConfig);
    }


    /**
     * 获取Realm数据库对象
     * @return              realm对象
     */
    public Realm getRealmHelper() {
        return realm;
    }

    /**
     * 初始化线程池管理器
     */
    private void initThreadPool() {
        // 创建一个独立的实例进行使用
        executor = PoolThread.ThreadBuilder
                .createFixed(6)
                .setPriority(Thread.MAX_PRIORITY)
                .setCallback(new LogCallback())
                .build();
    }

    /**
     * 获取线程池管理器对象，统一的管理器维护所有的线程池
     * @return                      executor对象
     */
    public PoolThread getExecutor(){
        return executor;
    }


}


