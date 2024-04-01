package com.yc.appmonitor;

import android.app.Application;


import com.yc.monitortimelib.monitor.MonitorListener;
import com.yc.monitortimelib.monitor.MonitorTimeHelper;
import com.yc.netlib.utils.NetworkTool;
import com.yc.crash.lib.CrashHandler;
import com.yc.crash.lib.CrashListener;
import com.yc.toolutils.system.AppLogUtils;

import java.io.FileInputStream;
import java.io.IOException;


public class MonitorApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initCrash();
        // 初始化打点计时器
        // 第一个参数是是否开启打点，如果是true则打点，如果是false则表示不会记录打点
        // 第二个参数是自定义打印接口适配器，主要是自定义打印日志
        MonitorTimeHelper.init(true,null);
        // 设置自定义监听日志listener，所有的打点都会回调该方法
        MonitorTimeHelper.setOnMonitorListener(new MonitorListener() {
            @Override
            public void onMonitorResult(String processName, String result) {
                AppLogUtils.d("TimeMonitor processName: " + processName + " , result: " + result);
            }
        });
        initNetWork();
//        initLeakCanary();
        initApm();
    }

    private void initCrash() {
//        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
//                AppLogUtils.e("CrashHandler : the last uncaught " + e);
//            }
//        });
        CrashHandler.getInstance()
                .setWriteLog(true)
                .init(this, new CrashListener() {
                    @Override
                    public void recordException(Throwable ex, int crashCount) {
                        AppLogUtils.e("CrashHandler : record exception  " + ex);
                        //自定义上传crash，支持开发者上传自己捕获的crash数据
                        //StatService.recordException(getApplication(), ex);
                    }
                });
//        boolean init = NativeCrashDumper.getInstance().init(getFilesDir().getAbsolutePath(), new NativeCrashListener() {
//            @Override
//            public void onSignalReceived(int signal, final String logPath) {
//                final String content = readContentFromFile(logPath);
//                AppLogUtils.e("onSignalReceived  " + content);
//            }
//        }, NativeHandleMode.RAISE_ERROR);
    }

    private String readContentFromFile(String path) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            return new String(data);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initNetWork() {
        NetworkTool.getInstance().init(this);
    }

//    private RefWatcher mRefWatcher;
//    private void initLeakCanary() {
//        mRefWatcher = LeakCanary.install(this);
//    }
//
//    public static RefWatcher getRefWatcher(Context context) {
//        MonitorApplication application = (MonitorApplication) context.getApplicationContext();
//        return application.mRefWatcher;
//    }


    private void initApm() {

    }

}
