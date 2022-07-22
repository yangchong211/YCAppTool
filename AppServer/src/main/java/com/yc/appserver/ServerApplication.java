package com.yc.appserver;

import android.app.Application;

import com.yc.logclient.LogUtils;
import com.yc.logclient.client.LogClient;
import com.yc.toolutils.file.AppFileUtils;

public class ServerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initLogUtils();
    }

    private void initLogUtils() {
        //通过跨进程写日志
        String aLogPath = AppFileUtils.getCacheFilePath(this, "ALog");
        LogUtils.initLogEngine(this);
        LogUtils.setLogSaveFolder(aLogPath);
        LogUtils.setShowLog(true);
        LogUtils.enableTooLargeAutoDevide(true);
        LogUtils.setAutoDivideRatio(0.5f);
        LogUtils.setBaseStackIndex(LogClient.mBaseIndex);
        LogUtils.setLogPre("ALog");
    }
}
