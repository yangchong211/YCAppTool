package com.ns.yc.lifehelper.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.Utils;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.api.LogInterceptor;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;
import com.zhy.http.okhttp.OkHttpUtils;

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


    /**
     * 不建议使用这种单例模式
     * @return
     */
    /*public static synchronized BaseApplication getInstance() {
        if (null == instance) {
            instance = new BaseApplication();
        }
        return instance;
    }*/


    public static BaseApplication getInstance() {
        if (null == instance) {
            synchronized (BaseApplication.class){
                if(instance == null){
                    instance = new BaseApplication();
                }
            }
        }
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initUtils();
        initRealm();
        initOkHttpUtils();
        initLeakCanary();           //Square公司内存泄漏检测工具
        initBugly();                //初始化腾讯bug管理平台
        initRegisterActivityLifecycleCallbacks();       //栈管理
    }

    private void initUtils() {
        Utils.init(this);
    }

    /**
     * 初始化数据库
     */
    private Realm realm;
    private void initRealm() {
        Realm.init(this);
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
     * 监测Activity的生命周期事件
     */
    private void initRegisterActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.e("Activity生命周期","onActivityCreated");
                AppManager.getAppManager().addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.e("Activity生命周期","onActivityStarted");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.e("Activity生命周期","onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.e("Activity生命周期","onActivityPaused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.e("Activity生命周期","onActivityStopped");
                if(AppManager.activityStack!=null && AppManager.activityStack.size()>0){
                    AppManager.getAppManager().removeActivity(activity);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.e("Activity生命周期","onActivitySaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.e("Activity生命周期","onActivityDestroyed");
                /*if(AppManager.activityStack!=null && AppManager.activityStack.size()>0){
                    AppManager.getAppManager().finishActivity(activity);
                }*/
            }
        });
    }

}


