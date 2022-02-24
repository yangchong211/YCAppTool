package com.yc.lifehelper;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.yc.appstatuslib.AppStatusManager;
import com.yc.appstatuslib.info.BatteryInfo;
import com.yc.appstatuslib.info.ThreadInfo;
import com.yc.appstatuslib.listener.BaseStatusListener;
import com.yc.localelib.listener.OnLocaleChangedListener;
import com.yc.localelib.service.LocaleService;
import com.yc.longevitylib.LongevityMonitor;
import com.yc.longevitylib.LongevityMonitorConfig;
import com.yc.toollib.tool.ToolFileUtils;
import com.yc.toollib.tool.ToolLogUtils;
import com.yc.library.base.app.LibApplication;

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
        Log.d("Application", "onCreate");
        super.onCreate();
        LongevityMonitor();
        initAppStatusListener();
        initLang();
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
                    public void log(String var1) {
                        ToolLogUtils.i("Longevity--"+var1);
                    }
                })
                .build());
    }


    private void initAppStatusListener() {
        String cachePath = ToolFileUtils.getCrashPicPath(this);
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
                    ToolLogUtils.i("app status Wifi 打开");
                } else {
                    ToolLogUtils.i("app status Wifi 关闭");
                }
            }

            @Override
            public void gpsStatusChange(boolean isGpsOn) {
                super.gpsStatusChange(isGpsOn);
                if (isGpsOn){
                    ToolLogUtils.i("app status Gps 打开");
                } else {
                    ToolLogUtils.i("app status Gps 关闭");
                }
            }

            @Override
            public void networkStatusChange(boolean isConnect) {
                super.networkStatusChange(isConnect);
                if (isConnect){
                    ToolLogUtils.i("app status Network 打开");
                } else {
                    ToolLogUtils.i("app status Network 关闭");
                }
            }

            @Override
            public void screenStatusChange(boolean isScreenOn) {
                super.screenStatusChange(isScreenOn);
                if (isScreenOn){
                    ToolLogUtils.i("app status Screen 打开");
                } else {
                    ToolLogUtils.i("app status Screen 关闭");
                }
            }

            @Override
            public void screenUserPresent() {
                super.screenUserPresent();
                ToolLogUtils.i("app status Screen 使用了");
            }

            @Override
            public void appOnFrontOrBackChange(boolean isBack) {
                super.appOnFrontOrBackChange(isBack);
                if (isBack){
                    ToolLogUtils.i("app status app 推到后台");
                } else {
                    ToolLogUtils.i("app status app 回到前台");
                }
            }

            @Override
            public void bluetoothStatusChange(boolean isBluetoothOn) {
                super.bluetoothStatusChange(isBluetoothOn);
                if (isBluetoothOn){
                    ToolLogUtils.i("app status 蓝牙 打开");
                } else {
                    ToolLogUtils.i("app status 蓝牙 关闭");
                }
            }

            @Override
            public void batteryStatusChange(BatteryInfo batteryInfo) {
                super.batteryStatusChange(batteryInfo);
                ToolLogUtils.i("app status 电量 " + batteryInfo.toStringInfo());
            }

            @Override
            public void appThreadStatusChange(ThreadInfo threadInfo) {
                super.appThreadStatusChange(threadInfo);
                ToolLogUtils.i("app status 所有线程数量 " + threadInfo.getThreadCount());
                ToolLogUtils.i("app status run线程数量 " + threadInfo.getRunningThreadCount().size());
                ToolLogUtils.i("app status wait线程数量 " + threadInfo.getWaitingThreadCount().size());
                ToolLogUtils.i("app status block线程数量 " + threadInfo.getBlockThreadCount().size());
                ToolLogUtils.i("app status timewait线程数量 " + threadInfo.getTimeWaitingThreadCount().size());
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
                Log.d("MultiLanguages", "监听到系统切换了语种，旧语种：" + oldLocale + "，新语种：" + newLocale + "，是否跟随系统：" + LocaleService.getInstance().isSystemLanguage());
            }
        });
    }

}


