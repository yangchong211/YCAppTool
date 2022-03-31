package com.yc.lifehelper.application;

import android.os.Looper;
import android.util.Log;

import com.yc.applicationlib.activity.ActivityManager;
import com.yc.appstart.AppStartTask;
import com.yc.appstart.TaskExecutorManager;
import com.yc.library.utils.AppLogHelper;
import com.yc.lifehelper.MainActivity;
import com.yc.lifehelper.listener.MainActivityListener;
import com.yc.localelib.listener.OnLocaleChangedListener;
import com.yc.localelib.service.LocaleService;
import com.yc.longevitylib.LongevityMonitor;
import com.yc.longevitylib.LongevityMonitorConfig;
import com.yc.toolutils.AppToolUtils;
import com.yc.toolutils.logger.AppLogUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

public class AppCoreTask extends AppStartTask {
    @Override
    public void run() {
        long start = System.currentTimeMillis();
        //工具库
        AppToolUtils.init(MainApplication.getInstance());
        //日志库
        AppLogHelper.config(MainApplication.getInstance());
        //初始化国际化语言
        initLang();
        //保活
        LongevityMonitor();
        //activity栈自动管理
        initActivityManager();
        //保活方案
        //KeepAliveHelper.init(this);
        long end = System.currentTimeMillis();
        boolean isMainThread = (Looper.myLooper() == Looper.getMainLooper());
        AppLogUtils.i("app init 1 task core total time : " + (end-start)
                + " ; 线程是否是主线程" + isMainThread);
    }

    @Override
    public boolean isRunOnMainThread() {
        return true;
    }

    @Override
    public List<Class<? extends AppStartTask>> getDependsTaskList() {
        return null;
    }

    @Override
    public Executor runOnExecutor() {
        return TaskExecutorManager.getInstance().getCPUThreadPoolExecutor();
    }

    private void initLang(){
        // 初始化多语种框架
        LocaleService.getInstance().init(MainApplication.getInstance());
        // 设置语种变化监听器
        LocaleService.getInstance().addOnLocaleChangedListener(new OnLocaleChangedListener() {

            @Override
            public void onAppLocaleChange(Locale oldLocale, Locale newLocale) {
                Log.d("MultiLanguages", "监听到应用切换了语种，旧语种：" + oldLocale + "，新语种：" + newLocale);
            }

            @Override
            public void onSystemLocaleChange(Locale oldLocale, Locale newLocale) {
                Log.d("MultiLanguages", "监听到系统切换了语种，旧语种：" + oldLocale + "，新语种：" + newLocale + "，是否跟随系统：" + LocaleService.getInstance().isSystemLocale());
            }
        });
    }


    private void LongevityMonitor() {
        LongevityMonitor.init(new LongevityMonitorConfig.Builder(MainApplication.getInstance())
                // 业务埋点
                .setEventTrack(new LongevityMonitorConfig.ILongevityMonitorOmegaEventTrack() {
                    @Override
                    public void onEvent(HashMap<String, String> hashMap) {

                    }
                })
                // 保活监控 Apollo
                .setToggle(new LongevityMonitorConfig.ILongevityMonitorApolloToggle() {
                    @Override
                    public boolean isOpen() {
                        return true;
                    }
                })
                // 日志输出
                .setLogger(new LongevityMonitorConfig.ILongevityMonitorLogger() {
                    @Override
                    public void log(String log) {
                        AppLogUtils.i("Longevity--"+log);
                    }
                })
                .build());
    }


    private void initActivityManager() {
        //初始化activity栈管理
        ActivityManager.getInstance().init(MainApplication.getInstance());
        ActivityManager.getInstance().registerActivityLifecycleListener(MainActivity.class,
                new MainActivityListener());
    }

}
