package com.sankuai.erp.component.appinit.api;

import android.text.TextUtils;
import android.util.Log;

import com.sankuai.erp.component.appinit.common.ILogger;

/**
 * 作者:王浩
 * 创建时间:2018/11/2
 * 描述:AppInit 打印 Android 日志
 */
public class Logger implements ILogger {

    private static final String TAG = "AppInit";

    @Override
    public boolean isDebug() {
        return AppInitManager.get().isDebug();
    }

    @Override
    public boolean isIsMainProcess() {
        return AppInitApiUtils.isMainProcess();
    }

    @Override
    public void demo(String msg) {
        d("AppInitDemo", msg);
    }

    @Override
    public void d(String msg) {
        d(TAG, msg);
    }

    private void d(String tag, String msg) {
        if (AppInitManager.get().isDebug()) {
            Log.d(tag, String.format("[%s][%s]%s", getProcessName(), Thread.currentThread().getName(), msg));
        }
    }

    @Override
    public void e(String msg) {
        Log.e(TAG, String.format("[%s][%s]=>%s", getProcessName(), Thread.currentThread().getName(), msg));
    }

    private String getProcessName() {
        String processName;
        if (isIsMainProcess()) {
            processName = "主进程";
        } else {
            processName = AppInitApiUtils.getProcessName();
            if (!TextUtils.isEmpty(processName) && processName.contains(":")) {
                processName = processName.substring(processName.indexOf(":"));
            }
        }
        return processName;
    }
}
