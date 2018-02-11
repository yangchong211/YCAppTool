package com.ns.yc.lifehelper.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.ns.yc.lifehelper.base.app.BaseApplication;
import com.ns.yc.lifehelper.base.BaseConfig;
import com.ns.yc.lifehelper.utils.AppUtil;
import com.ns.yc.lifehelper.utils.LogUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;


public class InitializeService extends IntentService {

    private static final String ACTION_INIT = "initApplication";

    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT);
        context.startService(intent);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * Used to name the worker thread, important only for debugging.
     */
    /*public InitializeService(String name) {
        super(name);
    }*/

    public InitializeService(){
        super("InitializeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT.equals(action)) {
                initApplication();
            }
        }
    }

    private void initApplication() {
        //initOkHttpUtils();                            //初始化鸿洋大神OkHttpUtils
        initBugly();                                    //初始化腾讯bug管理平台
        BaseConfig.INSTANCE.initConfig();               //初始化配置信息
        initQQX5();                                     //初始化腾讯X5
        LogUtils.logDebug = true;                       //开启日志
        //initChatIM();                                   //初始化环信，不要在子线程中初始化
    }


    /**
     * 初始化鸿洋大神网络请求框架
     * 项目刚开始时用鸿洋大神的框架【正式项目中用这个】，后来慢慢改成Retrofit
     */
    /*private void initOkHttpUtils() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //.addInterceptor(new BaseInterceptor())
                //.addInterceptor(interceptor)
                .addInterceptor(new LogInterceptor("TAG_APP",true))          //打印请求网络数据日志，上线版本就关掉
                .connectTimeout(50000L, TimeUnit.MILLISECONDS)                  //连接超时时间
                .readTimeout(10000L, TimeUnit.MILLISECONDS)                     //读数据超时时间
                .writeTimeout(10000L,TimeUnit.MICROSECONDS)                     //写数据超时时间
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }*/


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
