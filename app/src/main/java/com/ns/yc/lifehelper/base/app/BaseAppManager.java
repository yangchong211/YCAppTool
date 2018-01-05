package com.ns.yc.lifehelper.base.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/1/18
 * 描    述：Activity容器
 * 修订历史：
 * 备注：
 *      建议：在Application中用registerActivityLifecycleCallbacks进行Activity生命周期的栈管理
 * ================================================
 */
public class BaseAppManager implements Application.ActivityLifecycleCallbacks {

    private static final BaseAppManager sInstance = new BaseAppManager();
    //private Stack<Activity> activityStack = new Stack<>();
    private Set<Activity> activityStack;

    public static BaseAppManager getInstance() {
        return sInstance;
    }

    private BaseAppManager() {}

    /**
     * 必须在 Application 的 onCreate 方法中调用
     */
    public void init(Application application) {
        application.registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.e("Activity生命周期","onActivityCreated");
        //AppManager.getAppManager().addActivity(activity);
        //addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.e("Activity生命周期","onActivityStarted");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.e("Activity生命周期","onActivityResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.e("Activity生命周期","onActivityPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.e("Activity生命周期","onActivityStopped");
        //AppManager.getAppManager().removeActivity(activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.e("Activity生命周期","onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.e("Activity生命周期","onActivityDestroyed");
        //AppManager.getAppManager().finishActivity(activity);
        //removeActivity(activity);
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new HashSet<>();
        }
        activityStack.add(activity);
    }


    /**
     * 移除指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activityStack != null) {
            activityStack.remove(activity);
        }
    }


    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            if (activityStack != null) {
                //noinspection SynchronizeOnNonFinalField
                synchronized (activityStack) {
                    for (Activity act : activityStack) {
                        act.finish();
                    }
                }
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            //
        } finally {
            //
        }
    }

}
