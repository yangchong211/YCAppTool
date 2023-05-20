package com.yc.appmonitor;

import android.app.Application;

import com.apm.yc.ApmMonitorHelper;
import com.apm.yc.MonitorListener;
import com.apm.yc.bean.FlowBean;
import com.apm.yc.bean.Memory;
import com.apm.yc.bean.SDCard;
import com.apm.yc.bean.Version;
import com.yc.cpu.CpuRateBean;
import com.yc.monitortimelib.OnMonitorListener;
import com.yc.monitortimelib.TimeMonitorHelper;
import com.yc.netlib.utils.NetworkTool;
import com.yc.toollib.crash.CrashHandler;
import com.yc.toollib.crash.CrashListener;
import com.yc.toolutils.AppLogUtils;

import java.util.HashMap;

public class MonitorApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initCrash();
        // 初始化打点计时器
        // 第一个参数是是否开启打点，如果是true则打点，如果是false则表示不会记录打点
        // 第二个参数是自定义打印接口适配器，主要是自定义打印日志
        TimeMonitorHelper.init(true,null);
        // 设置自定义监听日志listener，所有的打点都会回调该方法
        TimeMonitorHelper.setOnMonitorListener(new OnMonitorListener() {
            @Override
            public void onMonitorResult(String processName, String result) {
                com.yc.toolutils.AppLogUtils.d("TimeMonitor processName: " + processName + " , result: " + result);
            }
        });
        initNetWork();
//        initLeakCanary();
        initApm();
    }

    private void initCrash() {
        CrashHandler.getInstance()
                .setWriteLog(true)
                .init(this, new CrashListener() {
                    @Override
                    public void recordException(Throwable ex, int crashCount) {
                        com.yc.toolutils.AppLogUtils.e("record exception : " + ex.getMessage());
                        //自定义上传crash，支持开发者上传自己捕获的crash数据
                        //StatService.recordException(getApplication(), ex);
                    }
                });
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
        ApmMonitorHelper.getInstance().init(this, new MonitorListener() {
            @Override
            public void onSystem(long cosTime, CpuRateBean cpuRate, Memory memory, SDCard sdCard,
                                 Version version,
                                 FlowBean flow, FlowBean flowApp) {
                AppLogUtils.v("ApmMonitorHelper", "系统上报-耗费时间：" + cosTime);
                AppLogUtils.v("ApmMonitorHelper", "系统上报-CPU：" + cpuRate.toString());
                AppLogUtils.v("ApmMonitorHelper", "系统上报-内存：" + memory.toString());
                AppLogUtils.v("ApmMonitorHelper", "系统上报-存储：" + sdCard.toString());
                AppLogUtils.v("ApmMonitorHelper", "系统上报-版本：" + version.toString());
                AppLogUtils.v("ApmMonitorHelper", "系统上报-流量：" + flowApp.toString());
            }

            @Override
            public void onBlock(long cos, String stackMsg) {
                AppLogUtils.v("ApmMonitorHelper", "检测卡顿-耗时：" + cos + " 卡顿位置:" + stackMsg);
            }

            @Override
            public void onLeaks(long cosTime, HashMap<String, Integer> hashMap) {
                AppLogUtils.v("ApmMonitorHelper", "检测内存泄漏,耗费时间：" + cosTime);
                AppLogUtils.v("ApmMonitorHelper", "检测内存泄漏：" + hashMap.toString());
            }
        });
        ApmMonitorHelper.getInstance().getSystem().setDuration(60 * 1000);
        ApmMonitorHelper.getInstance().getLeaks().setOpen(true);
        ApmMonitorHelper.getInstance().getBlock().setMonitorTime(2000);
        ApmMonitorHelper.getInstance().begin();
    }

}
