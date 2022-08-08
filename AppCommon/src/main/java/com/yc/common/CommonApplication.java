package com.yc.common;

import android.app.Application;
import android.util.Log;

import com.yc.apploglib.config.AppLogConfig;
import com.yc.apploglib.config.AppLogFactory;
import com.yc.store.config.CacheConfig;
import com.yc.store.config.CacheInitHelper;
import com.yc.toolutils.file.AppFileUtils;

public class CommonApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initAppCache();
        initAppLog();
    }

    private void initAppLog() {
        String ycLogPath = AppFileUtils.getCacheFilePath(this, "ycLog");
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
        CacheInitHelper.INSTANCE.init(this,cacheConfig);
        //最简单的初始化
        //CacheInitHelper.INSTANCE.init(CacheConfig.Companion.newBuilder().build());
    }

}
