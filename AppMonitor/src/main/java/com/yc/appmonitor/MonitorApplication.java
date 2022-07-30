package com.yc.appmonitor;

import android.app.Application;

import com.yc.toollib.crash.CrashHandler;
import com.yc.toollib.crash.CrashListener;
import com.yc.toolutils.AppLogUtils;

public class MonitorApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initCrash();
    }

    private void initCrash() {
        CrashHandler.getInstance()
                .setWriteLog(true)
                .init(this, new CrashListener() {
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
