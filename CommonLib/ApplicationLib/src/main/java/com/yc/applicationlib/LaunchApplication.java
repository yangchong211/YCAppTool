package com.yc.applicationlib;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Process;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 应用app
 *     revise:
 * </pre>
 */
public class LaunchApplication extends Application {

    private static final String APPLICATION_START_CLASS =
            "com.yc.applicationlib.ApplicationListener.LaunchApplicationListener";
    private Object mInstance;
    private final HashMap<String, Method> mMethod = new HashMap<>();
    private static Application application;

    public static Application getInstance() {
        return application;
    }


    public boolean isMainProcess() {
        //判断是否是主进程
        return isMainProcess(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isMainProcess()) {
            invokeMethod(mInstance, "onConfigurationChanged", this, newConfig);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        if (isMainProcess()) {
            invokeMethod(mInstance, "onCreate", this);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (isMainProcess()) {
            invokeMethod(mInstance, "onLowMemory", this);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (isMainProcess()) {
            invokeMethod(mInstance, "onTerminate", this);
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (isMainProcess()) {
            invokeMethod(mInstance, "onTrimMemory", this, level);
        }
    }

    @Override
    protected void attachBaseContext(final Context base) {
        super.attachBaseContext(base);
        loadLaunchClass();
        if (isMainProcess()) {
            invokeMethod(mInstance, "attachBaseContext", base);
        }
    }

    private void loadLaunchClass() {
        try {
            Class launchClass = Class.forName(APPLICATION_START_CLASS, true, getClassLoader());
            mInstance = launchClass.newInstance();
            Method[] methods = launchClass.getDeclaredMethods();
            for (Method method : methods) {
                mMethod.put(method.getName(), method);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void invokeMethod(Object object, String methodName, Object... arg) {
        try {
            Method method = mMethod.get(methodName);
            if (method != null) {
                method.setAccessible(true);
                method.invoke(object, arg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean isMainProcess(Context context) {
        try {
            ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> raps = am.getRunningAppProcesses();
            if (null != raps && raps.size() > 0) {
                int pid = Process.myPid();
                String packageName = context.getPackageName();
                for (ActivityManager.RunningAppProcessInfo info : raps) {
                    if (packageName.equals(info.processName)) {
                        return pid == info.pid;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
