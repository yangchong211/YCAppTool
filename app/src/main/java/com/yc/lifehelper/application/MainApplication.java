package com.yc.lifehelper.application;

import android.content.Context;
import android.util.Log;

import com.yc.appstart.AppStartTask;
import com.yc.appstart.AppTaskDispatcher;
import com.yc.lifehelper.MainActivity;
import com.yc.lifehelper.listener.MainActivityListener;
import com.yc.applicationlib.activity.ActivityManager;
import com.yc.appstatuslib.AppStatusManager;
import com.yc.appstatuslib.backgroud.AppStateMonitor;
import com.yc.appstatuslib.info.BatteryInfo;
import com.yc.appstatuslib.info.ThreadInfo;
import com.yc.appstatuslib.listener.BaseStatusListener;
import com.yc.library.utils.AppLogHelper;
import com.yc.localelib.listener.OnLocaleChangedListener;
import com.yc.localelib.service.LocaleService;
import com.yc.longevitylib.LongevityMonitor;
import com.yc.longevitylib.LongevityMonitorConfig;
import com.yc.library.base.app.LibApplication;
import com.yc.netlib.utils.NetworkTool;
import com.yc.toollib.crash.CrashHandler;
import com.yc.toollib.crash.CrashListener;
import com.yc.toolutils.AppToolUtils;
import com.yc.toolutils.file.AppFileUtils;
import com.yc.toolutils.logger.AppLogUtils;

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

    private static Context context;

    public static Context getContext() {
        return context;
    }

    /**
     * 程序启动的时候执行
     */
    @Override
    public void onCreate() {
        Log.d("Application : ", "onCreate");
        super.onCreate();
        context = this;
        //执行task任务
        AppTaskDispatcher.create()
                .setShowLog(true)
                .addAppStartTask(new AppCoreTask())
                .addAppStartTask(new AppMonitorTask())
                .addAppStartTask(new AppDelayTask())
                .addAppStartTask(new AppThreadTask())
                .addAppStartTask(new AppLazyTask())
                .start()
                .await();
    }

    @Override
    protected void attachBaseContext(Context base) {
        // 绑定语种
        super.attachBaseContext(LocaleService.getInstance().attachBaseContext(base));
    }


}


