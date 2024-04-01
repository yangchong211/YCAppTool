package com.yc.lifehelper.app;

import android.os.Looper;
import android.util.Log;

import com.yc.apploglib.config.AppLogConfig;
import com.yc.apploglib.config.AppLogFactory;
import com.yc.monitortimelib.monitor.MonitorTimeHelper;
import com.yc.parallel.AbsParallelTask;
import com.yc.appstatuslib.AppStatusManager;
import com.yc.appstatuslib.info.AppBatteryInfo;
import com.yc.appstatuslib.info.AppThreadInfo;
import com.yc.appstatuslib.listener.BaseStatusListener;
import com.yc.logclient.LogUtils;
import com.yc.logclient.client.LogClient;
import com.yc.toolutils.system.AppLogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AppDelayTask extends AbsParallelTask {

    @Override
    public void run() {
        MonitorTimeHelper.start("AppDelayTask");
        long start = System.currentTimeMillis();
        //各种状态监听回调
        initAppStatusListener();
        //OOM上报日志操作库
        //DMemoryLeak.installLeakCanary(MainApplication.getInstance(),true);
        try {
            Thread.sleep(100);
        }catch (Exception e){
            e.printStackTrace();
        }
        initAppLog();
        long end = System.currentTimeMillis();
        boolean isMainThread = (Looper.myLooper() == Looper.getMainLooper());
        AppLogUtils.i("app init 3 task delay total time : " + (end-start)
                + " ; 线程是否是主线程" + isMainThread);
        MonitorTimeHelper.end("AppDelayTask");
    }

    private void initAppLog() {

        String ycLogPath = AppFileUtils.getCacheFilePath(MainApplication.getInstance(), "ycLog");
        AppLogConfig config = new AppLogConfig.Builder()
                //设置日志tag总的标签
                .setLogTag("yc")
                //是否将log日志写到文件
                .isWriteFile(true)
                //是否是debug
                .enableDbgLog(true)
                //设置日志最小级别
                .minLogLevel(Log.VERBOSE)
                //设置输出日志到file文件的路径。前提是将log日志写入到文件设置成true
                .setFilePath(ycLogPath)
                .build();
        //配置
        AppLogFactory.init(config);


        //通过跨进程写日志
        String aLogPath = AppFileUtils.getCacheFilePath(MainApplication.getInstance(), "ALog");
        LogUtils.initLogEngine(MainApplication.getInstance());
        LogUtils.setLogSaveFolder(aLogPath);
        LogUtils.setShowLog(true);
        LogUtils.enableTooLargeAutoDevide(true);
        LogUtils.setAutoDivideRatio(0.5f);
        LogUtils.setBaseStackIndex(LogClient.mBaseIndex);
        LogUtils.setLogPre("ALog");
    }

    @Override
    public boolean isRunOnMainThread() {
        return true;
    }

    @Override
    public List<Class<? extends AbsParallelTask>> getDependsTaskList() {
        List<Class<? extends AbsParallelTask>> dependsTaskList = new ArrayList<>();
        dependsTaskList.add(AppMonitorTask.class);
        return dependsTaskList;
    }

    private void initAppStatusListener() {
        String cachePath = AppFileUtils.getCacheFilePath(MainApplication.getInstance(),"cache");
        String path = cachePath + File.separator + "status";
        File file = new File(path);
        AppStatusManager manager = new AppStatusManager.Builder()
                .context(MainApplication.getInstance())
                .file(file)
                .context(MainApplication.getInstance())
                .threadSwitchOn(false)
                .builder();
//        manager.dispatcherUserPresent$AppStatusLib_debug();
//        manager.dispatcherNetworkState$AppStatusLib_debug(false);
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
            public void bluetoothStatusChange(boolean isBluetoothOn) {
                super.bluetoothStatusChange(isBluetoothOn);
                if (isBluetoothOn){
                    AppLogUtils.i("app status 蓝牙 打开");
                } else {
                    AppLogUtils.i("app status 蓝牙 关闭");
                }
            }

            @Override
            public void batteryStatusChange(AppBatteryInfo batteryInfo) {
                super.batteryStatusChange(batteryInfo);
                AppLogUtils.i("app status 电量 " + batteryInfo.toStringInfo());
            }

            @Override
            public void appThreadStatusChange(AppThreadInfo threadInfo) {
                super.appThreadStatusChange(threadInfo);
                AppLogUtils.i("app status 所有线程数量 " + threadInfo.getThreadCount());
                AppLogUtils.i("app status run线程数量 " + threadInfo.getRunningThreadCount().size());
                AppLogUtils.i("app status wait线程数量 " + threadInfo.getWaitingThreadCount().size());
                AppLogUtils.i("app status block线程数量 " + threadInfo.getBlockThreadCount().size());
                AppLogUtils.i("app status timewait线程数量 " + threadInfo.getTimeWaitingThreadCount().size());
            }
        });
    }

}
