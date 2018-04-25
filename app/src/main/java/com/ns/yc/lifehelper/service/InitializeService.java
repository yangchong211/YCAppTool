package com.ns.yc.lifehelper.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;

/**
 * <pre>
 *     @author      杨充
 *     blog         https://www.jianshu.com/p/53017c3fc75d
 *     time         2015/08/22
 *     desc         子线程初始化工作
 *     revise
 *     GitHub       https://github.com/yangchong211
 * </pre>
 */
@SuppressLint("Registered")
public class InitializeService extends IntentService {

    private static final String ACTION_INIT = "initApplication";

    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT);
        context.startService(intent);
    }

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
        initBugly();
        initQQX5();
        initUtils();
    }


    /**
     * 初始化腾讯bug管理平台
     */
    private void initBugly() {
        /* SDK初始化
        * 参数1：上下文对象
        * 参数2：APPID，平台注册时得到,注意替换成你的appId
        * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
        * 注意：如果您之前使用过Bugly SDK，请将以下这句注释掉。
        */
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setAppVersion(AppUtils.getAppVersionName());
        strategy.setAppPackageName(AppUtils.getAppPackageName());
        //会在启动20s后联网同步数据
        strategy.setAppReportDelay(20000);
        CrashReport.initCrashReport(getApplicationContext(), "a3f5f3820f", false ,strategy);
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


    /**
     * 初始化utils工具类
     */
    private void initUtils() {
        LogUtils.Config config = LogUtils.getConfig();
        //边框开关，设置打开
        config.setBorderSwitch(true);
        //logcat 是否打印，设置打印
        config.setConsoleSwitch(true);
        //设置打印日志总开关，线上时关闭
        config.setLogSwitch(true);
    }

}
