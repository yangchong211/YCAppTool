package com.yc.appserver;

import android.app.Application;

import com.yc.appfilelib.AppFileUtils;
import com.yc.logclient.LogUtils;
import com.yc.logclient.client.LogClient;

public class ServerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initLogUtils();
    }

    private void initLogUtils() {
        //通过跨进程写日志
        String aLogPath = AppFileUtils.getExternalFilePath(this, "YcLog");
        LogUtils.initLogEngine(this);
        LogUtils.setLogSaveFolder(aLogPath);
        LogUtils.setShowLog(true);
        LogUtils.enableTooLargeAutoDevide(true);
        LogUtils.setAutoDivideRatio(0.5f);
        LogUtils.setBaseStackIndex(LogClient.mBaseIndex);
        LogUtils.setLogPre("YcLog");
    }
}
