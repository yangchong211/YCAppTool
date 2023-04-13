package com.yc.memoryleakupload;


import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

import com.squareup.leakcanary.ActivityRefWatcher;
import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.DebugInfo;
import com.squareup.leakcanary.ExcludedRefs;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.leakcanary.RefWatcher.ReleaseListener;

/**
 * description:追踪内存泄漏
 */
public class DMemoryLeak {

    /**
     * 追踪内存泄漏
     *
     * @param application Application
     */
    public static RefWatcher installLeakCanary(Application application, boolean isDebug) {
        //LeakCanary.install(application);



        //替换成自己的，其作用等同于：LeakCanary.install(application)
        ExcludedRefs excludedRefs = AndroidExcludedRefs.createAppDefaults().build();
        RefWatcher refWatcher = LeakCanary.refWatcher(application)
                .listenerServiceClass(UploadLeakService.class)
            .excludedRefs(excludedRefs)
            .build();

        ReleaseRefHolder.getInstance().setDebug(isDebug);
        DebugInfo.getInstance().setDebug(isDebug);
        //release模式下隐藏leak Activity入口
        hideOrShowLeaksActivity(application);
        //设置release模式处理器
        if (!isDebug) {
            ReleaseRefHolder.getInstance().setRefWatcher(refWatcher);
            refWatcher.setReleaseListener(new ReleaseListener() {
                @Override
                public void onWatch(Object object) {
                    ReleaseRefHolder.getInstance().addRefObject(object);
                }
            });
        }

        if (isDebug) {
            LeakCanary.enableDisplayLeakActivity(application);
            //这个是注册观察 Activity 生命周期的操作
            ActivityRefWatcher.install(application, refWatcher);
        }
        return refWatcher;
    }

    /**
     * 手动触发检查内存泄漏
     */
    public static void findLeakInRelease() {
        ReleaseRefHolder.getInstance().findLeakInRelease();
    }

    /**
     * 隐藏入口
     */
    private static void hideOrShowLeaksActivity(Application application) {
        Context applicationContext = application.getApplicationContext();
        PackageManager packageManager = application.getPackageManager();
        ComponentName componentName = new ComponentName(applicationContext,
            "com.squareup.leakcanary.internal.DisplayLeakActivity");
        ComponentName rComponentName = new ComponentName(applicationContext,
            "com.squareup.leakcanary.internal.RequestStoragePermissionActivity");
        if (ReleaseRefHolder.getInstance().isDebug()) {
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
            packageManager.setComponentEnabledSetting(rComponentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        } else {
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
            packageManager.setComponentEnabledSetting(rComponentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        }
    }
}
