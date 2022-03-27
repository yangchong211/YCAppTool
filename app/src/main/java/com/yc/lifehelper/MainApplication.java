package com.yc.lifehelper;

import android.content.Context;
import android.util.Log;

import com.yc.appstart.AppStartTask;
import com.yc.appstart.AppTaskDispatcher;
import com.yc.lifehelper.listener.MainActivityListener;
import com.yc.memoryleakupload.DMemoryLeak;
import com.yc.applicationlib.activity.ActivityManager;
import com.yc.appstatuslib.AppStatusManager;
import com.yc.appstatuslib.backgroud.AppStateMonitor;
import com.yc.appstatuslib.info.BatteryInfo;
import com.yc.appstatuslib.info.ThreadInfo;
import com.yc.appstatuslib.listener.BaseStatusListener;
import com.yc.library.utils.AppLogHelper;
import com.yc.localelib.listener.OnLocaleChangedListener;
import com.yc.localelib.service.LocaleService;
import com.yc.longevitylib.LongevityMonitor;
import com.yc.longevitylib.LongevityMonitorConfig;
import com.yc.library.base.app.LibApplication;
import com.yc.netlib.utils.NetworkTool;
import com.yc.toollib.crash.CrashHandler;
import com.yc.toollib.crash.CrashListener;
import com.yc.toolutils.AppToolUtils;
import com.yc.toolutils.file.AppFileUtils;
import com.yc.toolutils.logger.AppLogUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;


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
public class MainApplication extends LibApplication {

    /**
     * 程序启动的时候执行
     */
    @Override
    public void onCreate() {
        Log.d("Application : ", "onCreate");
        super.onCreate();
        //工具库
        AppToolUtils.init(this);
        //日志库
        AppLogHelper.config(this);
        //保活
        LongevityMonitor();
        //各种状态监听回调
        initAppStatusListener();
        //初始化国际化语言
        initLang();
        //网络拦截库
        initNetWork();
        //崩溃库
        initCrash();
        //activity栈自动管理
        initActivityManager();
        //保活方案
        //KeepAliveHelper.init(this);
        //OOM上报日志操作库
        DMemoryLeak.installLeakCanary(this,true);
        //app 进入后台一定时间后执行退出或者重启操作，有助于释放内存，减少用户电量消耗
        //AppAutoCloser.getInstance().init(this);
        //执行task任务
        AppTaskDispatcher.create().addAppStartTask(new AppStartTask() {
            @Override
            public void run() {
                AppLogUtils.i("AppTaskDispatcher is run");
            }

            @Override
            public boolean isRunOnMainThread() {
                return false;
            }
        }).start();
    }

    private void initActivityManager() {
        //初始化activity栈管理
        ActivityManager.getInstance().init(this);
        ActivityManager.getInstance().registerActivityLifecycleListener(MainActivity.class,
                new MainActivityListener());
    }

    @Override
    protected void attachBaseContext(Context base) {
        // 绑定语种
        super.attachBaseContext(LocaleService.getInstance().attachBaseContext(base));
    }

    private void LongevityMonitor() {
        LongevityMonitor.init(new LongevityMonitorConfig.Builder(this)
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


    private void initAppStatusListener() {
        String cachePath = AppFileUtils.getSrcFilePath(this,"cache");
        String path = cachePath + File.separator + "status";
        File file = new File(path);
        AppStatusManager manager = new AppStatusManager.Builder()
                .context(this)
                .interval(5)
                .file(file)
                .threadSwitchOn(false)
                .builder();
        manager.registerAppStatusListener(new BaseStatusListener() {
            @Override
            public void wifiStatusChange(boolean isWifiOn) {
                super.wifiStatusChange(isWifiOn);
                if (isWifiOn){
                    AppLogUtils.i("app status Wifi 打开");
                } else {
                    AppLogUtils.i("app status Wifi 关闭");
                }
            }

            @Override
            public void gpsStatusChange(boolean isGpsOn) {
                super.gpsStatusChange(isGpsOn);
                if (isGpsOn){
                    AppLogUtils.i("app status Gps 打开");
                } else {
                    AppLogUtils.i("app status Gps 关闭");
                }
            }

            @Override
            public void networkStatusChange(boolean isConnect) {
                super.networkStatusChange(isConnect);
                if (isConnect){
                    AppLogUtils.i("app status Network 打开");
                } else {
                    AppLogUtils.i("app status Network 关闭");
                }
            }

            @Override
            public void screenStatusChange(boolean isScreenOn) {
                super.screenStatusChange(isScreenOn);
                if (isScreenOn){
                    AppLogUtils.i("app status Screen 打开");
                } else {
                    AppLogUtils.i("app status Screen 关闭");
                }
            }

            @Override
            public void screenUserPresent() {
                super.screenUserPresent();
                AppLogUtils.i("app status Screen 使用了");
            }

            @Override
            public void appOnFrontOrBackChange(boolean isBack) {
                super.appOnFrontOrBackChange(isBack);
                if (isBack){
                    AppLogUtils.i("app status app 推到后台");
                } else {
                    AppLogUtils.i("app status app 回到前台");
                }
            }

            @Override
            public void bluetoothStatusChange(boolean isBluetoothOn) {
                super.bluetoothStatusChange(isBluetoothOn);
                if (isBluetoothOn){
                    AppLogUtils.i("app status 蓝牙 打开");
                } else {
                    AppLogUtils.i("app status 蓝牙 关闭");
                }
            }

            @Override
            public void batteryStatusChange(BatteryInfo batteryInfo) {
                super.batteryStatusChange(batteryInfo);
                AppLogUtils.i("app status 电量 " + batteryInfo.toStringInfo());
            }

            @Override
            public void appThreadStatusChange(ThreadInfo threadInfo) {
                super.appThreadStatusChange(threadInfo);
                AppLogUtils.i("app status 所有线程数量 " + threadInfo.getThreadCount());
                AppLogUtils.i("app status run线程数量 " + threadInfo.getRunningThreadCount().size());
                AppLogUtils.i("app status wait线程数量 " + threadInfo.getWaitingThreadCount().size());
                AppLogUtils.i("app status block线程数量 " + threadInfo.getBlockThreadCount().size());
                AppLogUtils.i("app status timewait线程数量 " + threadInfo.getTimeWaitingThreadCount().size());
            }
        });

        AppStateMonitor.getInstance().registerAppStateListener(new AppStateMonitor.AppStateListener() {
            @Override
            public void onInForeground() {
                AppLogUtils.i("app status 在后台");
            }

            @Override
            public void onInBackground() {
                AppLogUtils.i("app status 在前台");
            }
        });
    }

    private void initLang(){
        // 初始化多语种框架
        LocaleService.getInstance().init(this);
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


    private void initNetWork() {
        NetworkTool.getInstance().init(this);
    }

    private void initCrash() {
        //ThreadHandler.getInstance().init(this);
        CrashHandler.getInstance().init(this, new CrashListener() {
            /**
             * 重启app
             */
            @Override
            public void againStartApp() {
                System.out.println("崩溃重启----------againStartApp------");
                //CrashToolUtils.reStartApp1(MainApplication.this,2000);
                //CrashToolUtils.reStartApp2(App.this,2000, MainActivity.class);
                //CrashToolUtils.reStartApp3(App.this);
            }

            /**
             * 自定义上传crash，支持开发者上传自己捕获的crash数据
             * @param ex                        ex
             */
            @Override
            public void recordException(Throwable ex) {
                AppLogUtils.e("record exception : " + ex.getMessage());
                //自定义上传crash，支持开发者上传自己捕获的crash数据
                //StatService.recordException(getApplication(), ex);
            }
        });
    }

}


