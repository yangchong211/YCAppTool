package com.yc.lifehelper.app;

import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.yc.anrtoollib.tool.ANRWatchDog;
import com.yc.anrtoollib.watch.ANRError;
import com.yc.anrtoollib.watch.ANRInterceptor;
import com.yc.anrtoollib.watch.ANRListener;
import com.yc.anrtoollib.watch.InterruptionListener;
import com.yc.appcommoninter.inter.IEventTrack;
import com.yc.appcommoninter.inter.ILogger;
import com.yc.appcommoninter.inter.IMonitorToggle;
import com.yc.appprocesslib.AppStateLifecycle;
import com.yc.appprocesslib.OnStateListener;
import com.yc.library.BuildConfig;
import com.yc.monitortimelib.monitor.MonitorTimeHelper;
import com.yc.parallel.AbsParallelTask;
import com.yc.activitymanager.activity.ActivityManager;
import com.yc.ycthreadpoollib.easy.DelegateTaskExecutor;
import com.yc.lifehelper.MainActivity;
import com.yc.lifehelper.listener.MainActivityListener;
import com.yc.localelib.locale.listener.OnLocaleChangedListener;
import com.yc.localelib.locale.service.LocaleService;
import com.yc.longalive.LongAliveMonitor;
import com.yc.longalive.LongAliveMonitorConfig;
import com.yc.store.config.CacheConfig;
import com.yc.store.config.CacheInitHelper;
import com.yc.toolutils.system.AppLogUtils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

public class AppCoreTask extends AbsParallelTask {
    @Override
    public void run() {
        long start = System.currentTimeMillis();
        //日志库
        MonitorTimeHelper.start("AppCoreTask");
        //初始化国际化语言
        initLang();
        //保活
        LongevityMonitor();
        //activity栈自动管理
        initActivityManager();
        //前后台
        initAppProcess();
        //初始化通用缓存方案
        initAppCache();
        //初始化ANR监听方案
        initAnrListener();
        long end = System.currentTimeMillis();
        boolean isMainThread = (Looper.myLooper() == Looper.getMainLooper());
        AppLogUtils.i("app init 1 task core total time : " + (end - start)
                + " ; 线程是否是主线程" + isMainThread);
        MonitorTimeHelper.end("AppCoreTask");
    }

    @Override
    public boolean isRunOnMainThread() {
        return true;
    }

    @Override
    public List<Class<? extends AbsParallelTask>> getDependsTaskList() {
        return null;
    }

    @Override
    public Executor runOnExecutor() {
        return DelegateTaskExecutor.getInstance().getCpuThreadExecutor();
    }

    private void initLang() {
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
        LongAliveMonitor.init(new LongAliveMonitorConfig.Builder(MainApplication.getInstance())
                // 业务埋点
                .setEventTrack(new IEventTrack() {
                    @Override
                    public void onEvent(String name, HashMap<String, String> map) {

                    }

                    @Override
                    public void onEvent(HashMap<String, String> hashMap) {

                    }
                })
                // 保活监控 Apollo
                .setToggle(new IMonitorToggle() {
                    @Override
                    public boolean isOpen() {
                        return true;
                    }
                })
                // 日志输出
                .setLogger(new ILogger() {
                    @Override
                    public void log(String log) {
                        AppLogUtils.i("Longevity--" + log);
                    }

                    @Override
                    public void error(String error) {
                        AppLogUtils.e("Longevity--" + error);
                    }
                })
                .build());
    }


    private void initActivityManager() {
        //初始化activity栈管理
        ActivityManager.getInstance().registerActivityLifecycleListener(MainActivity.class,
                new MainActivityListener());
    }

    private void initAppProcess() {
        /*AppStateMonitor.getInstance().registerStateListener(new StateListener() {
            @Override
            public void onInForeground() {
                AppLogUtils.i("app state in foreground");
                //ToastUtils.showRoundRectToast("前台");
            }

            @Override
            public void onInBackground() {
                AppLogUtils.i("app state in background");
                //ToastUtils.showRoundRectToast("后台");
            }
        });*/
        AppStateLifecycle.getInstance().init(MainApplication.getInstance());
        AppStateLifecycle.getInstance().registerStateListener(new OnStateListener() {
            @Override
            public void onInForeground() {
                AppLogUtils.i("app lifecycle state in foreground");
            }

            @Override
            public void onInBackground() {
                AppLogUtils.i("app lifecycle state in background");
            }
        });
        //判断是否在后台
        boolean inBackground = AppStateLifecycle.getInstance().isInBackground();
        //判断是否在前台
        boolean inForeground = AppStateLifecycle.getInstance().isInForeground();
    }

    private void initAppCache() {
        CacheConfig.Builder builder = CacheConfig.Companion.newBuilder();
        //设置是否是debug模式
        CacheConfig cacheConfig = builder.debuggable(BuildConfig.DEBUG)
                //设置外部存储根目录
                .extraLogDir(null)
                //设置lru缓存最大值
                .maxCacheSize(100)
                //内部存储根目录
                .logDir(null)
                //创建
                .build();
        CacheInitHelper.INSTANCE.init(MainApplication.getInstance(), cacheConfig);
        //最简单的初始化
        //CacheInitHelper.INSTANCE.init(CacheConfig.Companion.newBuilder().build());
    }

    private void initAnrListener() {
        new ANRWatchDog(5000).setANRListener(new ANRListener() {
            @Override
            public void onAppNotResponding(@NonNull ANRError error) {
                Throwable throwable = error.fillInStackTrace();
                AppLogUtils.d("ANRWatchDog", throwable);
            }
        }).start();

        new ANRWatchDog(5000).setANRInterceptor(new ANRInterceptor() {
                    @Override
                    public long intercept(long duration) {
                        return 20;
                    }
                })
                .setInterruptionListener(new InterruptionListener() {
                    @Override
                    public void onInterrupted(@NonNull InterruptedException exception) {
                        AppLogUtils.d("ANRWatchDog onInterrupted : ", exception);
                    }
                })
                .setLogThreadsWithoutStackTrace(true)
                .setIgnoreDebugger(false)
                .setANRListener(new ANRListener() {
                    @Override
                    public void onAppNotResponding(@NotNull ANRError error) {
                        Throwable throwable = error.fillInStackTrace();
                        AppLogUtils.d("ANRWatchDog", throwable);
                    }
                }).start();
    }

}
