package com.ns.yc.lifehelper.base.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.service.InitializeService;
import com.ns.yc.lifehelper.utils.AppUtil;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/18
 * 描    述：BaseApplication
 * 修订历史：
 * ================================================
 */
public class BaseApplication extends Application {


    private static BaseApplication instance;
    //private RefWatcher refWatcher;

    public static synchronized BaseApplication getInstance() {
        if (null == instance) {
            instance = new BaseApplication();
        }
        return instance;
    }

    public BaseApplication(){}

    /*public static BaseApplication getInstance() {
        if (null == instance) {
            synchronized (BaseApplication.class){
                if(instance == null){
                    instance = new BaseApplication();
                }
            }
        }
        return instance;
    }*/


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
        initUtils();
        //BaseAppManager.getInstance().init(this);        //栈管理
        //initLeakCanary();                               //Square公司内存泄漏检测工具
        initRealm();                                    //初始化Realm数据库
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
     * 初始化utils工具类
     */
    private void initUtils() {
        Utils.init(this);
    }


    /**
     * 初始化内存泄漏检测工具
     */
    /*private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        refWatcher = LeakCanary.install(this);
    }*/


    /**
     * 获取RefWatcher对象
     * @param context       上下文
     * @return              RefWatcher对象
     */
    /*public static RefWatcher getRefWatcher(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.refWatcher;
    }*/


    private Realm realm;
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

}


