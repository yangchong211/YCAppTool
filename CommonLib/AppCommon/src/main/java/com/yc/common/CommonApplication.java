package com.yc.common;

import android.os.Build;
import android.util.Log;

import com.yc.appcommoninter.inter.IMonitorToggle;
import com.yc.toolutils.context.AppToolUtils;
import com.yc.toolutils.file.AppFileUtils;

import com.yc.activitymanager.application.AppInitHelper;
import com.yc.activitymanager.application.LibBaseApplication;
import com.yc.apploglib.config.AppLogConfig;
import com.yc.apploglib.config.AppLogFactory;
import com.yc.common.impl.PermissionDialogImpl;
import com.yc.common.init.AppLifecycleInit;
import com.yc.appcommoninter.report.PermissionReporter;
import com.yc.store.config.CacheConfig;
import com.yc.store.config.CacheInitHelper;

public class CommonApplication extends LibBaseApplication {

    @Override
    public void onCreate() {
        AppInitHelper.getInstance().setAppInit(new AppLifecycleInit(this));
        super.onCreate();
        initAppCache();
        initAppLog();
        AppToolUtils.init(this);
        PermissionReporter.setPermissionDialog(new PermissionDialogImpl());
    }

    @Override
    protected void registerApplicationHelper() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            registerTargetApplicationHelper("com.yc.common.init.AppLifecycle");
            registerTargetApplicationHelper("com.yc.common.init.AppLifecycle2");
        }
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
                .monitorToggle(new IMonitorToggle() {
                    @Override
                    public boolean isOpen() {
                        //todo 是否降级，如果降级，则不使用该功能。
                        return false;
                    }
                })
                //创建
                .build();
        CacheInitHelper.INSTANCE.init(this,cacheConfig);
        //最简单的初始化
        //CacheInitHelper.INSTANCE.init(CacheConfig.Companion.newBuilder().build());
    }

}
