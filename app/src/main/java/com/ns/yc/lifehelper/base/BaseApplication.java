package com.ns.yc.lifehelper.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.Utils;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.api.LogInterceptor;
import com.ns.yc.lifehelper.utils.LogUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.OkHttpClient;

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
    private RefWatcher refWatcher;

    public static synchronized BaseApplication getInstance() {
        if (null == instance) {
            instance = new BaseApplication();
        }
        return instance;
    }

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
        initRealm();
        initOkHttpUtils();
        initLeakCanary();                               //Square公司内存泄漏检测工具
        initBugly();                                    //初始化腾讯bug管理平台
        BaseAppManager.getInstance().init(this);        //栈管理
        BaseConfig.INSTANCE.initConfig();               //初始化配置信息
        initQQX5();                                     //初始化腾讯X5
        LogUtils.logDebug = true;                       //开启日志
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
     *
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
     * 初始化数据库
     */
    private Realm realm;
    private void initRealm() {
        File file = null;
        try {
            file = new File(Constant.ExternalStorageDirectory, Constant.DATABASE_FILE_PATH_FOLDER);
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
    public Realm getRealmHelper() {
        return realm;
    }


    /**
     * 初始化鸿洋大神网络请求框架
     */
    private void initOkHttpUtils() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //.addInterceptor(new BaseInterceptor())
                //.addInterceptor(interceptor)
                .addInterceptor(new LogInterceptor("TAG_APP",true))          //打印请求网络数据日志，上线版本就关掉
                .connectTimeout(50000L, TimeUnit.MILLISECONDS)                  //连接超时时间
                .readTimeout(10000L, TimeUnit.MILLISECONDS)                     //读数据超时时间
                .writeTimeout(10000L,TimeUnit.MICROSECONDS)                     //写数据超时时间
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * 初始化内存泄漏检测工具
     */
    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        refWatcher = LeakCanary.install(this);
    }

    /**
     * 获取RefWatcher对象
     * @param context
     * @return
     */
    public static RefWatcher getRefWatcher(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    /**
     * 初始化腾讯bug管理平台
     */
    private void initBugly() {
        /* Bugly SDK初始化
        * 参数1：上下文对象
        * 参数2：APPID，平台注册时得到,注意替换成你的appId
        * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
        * 注意：如果您之前使用过Bugly SDK，请将以下这句注释掉。
        */
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setAppVersion(AppUtils.getAppVersionName());
        strategy.setAppPackageName(AppUtils.getAppPackageName());
        strategy.setAppReportDelay(20000);                          //Bugly会在启动20s后联网同步数据
        CrashReport.initCrashReport(getApplicationContext(), "a3f5f3820f", false ,strategy);
        //Bugly.init(getApplicationContext(), "1374455732", false);
    }


    /**
     * 初始化腾讯X5
     */
    private void initQQX5() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {

            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
    }


}


