package com.yc.privacymonitor.test;

import android.bluetooth.le.BluetoothLeScanner;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.yc.appcommoninter.ILogger;
import com.yc.privacymonitor.bean.ClassMethodBean;
import com.yc.privacymonitor.helper.PrivacyHelper;
import com.yc.toolutils.StackTraceUtils;

import java.lang.reflect.Method;

import de.robv.android.xposed.DexposedBridge;
import de.robv.android.xposed.XC_MethodHook;

public class TestPrivacyMonitor {


    /**
     * 监控java线程创建和销毁
     */
    private static void thread(){
        class ThreadMethodHook extends XC_MethodHook{
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Thread t = (Thread) param.thisObject;
                PrivacyHelper.getLogger().log("thread:" + t + ", started..");
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Thread t = (Thread) param.thisObject;
                PrivacyHelper.getLogger().log("thread:" + t + ", exit..");
            }
        }
        DexposedBridge.hookAllConstructors(Thread.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Thread thread = (Thread) param.thisObject;
                Class<?> clazz = thread.getClass();
                if (clazz != Thread.class) {
                    PrivacyHelper.getLogger().log( "found class extend Thread:" + clazz);
                    DexposedBridge.findAndHookMethod(clazz, "run", new ThreadMethodHook());
                }
                PrivacyHelper.getLogger().log("Thread: " + thread.getName() + " class:" + thread.getClass() +  " is created.");
            }
        });
        DexposedBridge.findAndHookMethod(Thread.class, "run", new ThreadMethodHook());
    }

    public static void findAndHookMethod() {
        try {
            Class<?> targetClass = TelephonyManager.class;
            String targetMethod = "getDeviceId";
            Object[] paramsWithDefaultHandler = {int.class};
            //核心方法
            DexposedBridge.findAndHookMethod(targetClass, targetMethod, paramsWithDefaultHandler);
            //核心方法
            DexposedBridge.findAndHookMethod(targetClass, targetMethod, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    ILogger logger = PrivacyHelper.getLogger();
                    //举个例子
                    //android.telephony.TelephonyManager#getTelephonyProperty
                    String className = param.method.getDeclaringClass().getName();
                    //android.telephony.TelephonyManager
                    String methodName = param.method.getName();
                    logger.log("检测到风险函数被调用: " + className + "#" + methodName);
                    logger.log("检测到风险函数被调用: " + className + "#" + methodName);
                    String methodStack = StackTraceUtils.getMethodStack();
                    logger.log(methodStack);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    ILogger logger = PrivacyHelper.getLogger();
                    logger.log("afterHookedMethod getDeviceId");
                }
            });
        } catch (NoSuchMethodError error) {
            PrivacyHelper.getLogger().log("NoSuchMethodError->" + error.getMessage());
        }
    }

    public static void hookMethod() {
        try {
            ClassMethodBean pkgManagerHook = new ClassMethodBean("android.app.ApplicationPackageManager");
            pkgManagerHook.addMethod("getInstalledPackagesAsUser");
            pkgManagerHook.addMethod("getInstalledApplicationsAsUser");
            Class<?> clazz = Class.forName(pkgManagerHook.getTargetClassName());
            Method[] declareMethods = clazz.getDeclaredMethods();
            for (Method method : declareMethods) {
                if (pkgManagerHook.getMethodGroup().contains(method.getName())) {
                    DexposedBridge.hookMethod(method, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                        }
                    });
                }
            }
        } catch (Exception e) {
            PrivacyHelper.getLogger().log("registerClass Error-> " + e.getMessage());
        }
    }

    private void startHook() {
        XC_MethodHook hook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Object hookObj = param.thisObject;
                String clsName = "unknownClass";
                if (hookObj != null) {
                    clsName = hookObj.getClass().getName();
                }
                String mdName = "unknownMethod";
                if (param.method != null) {
                    mdName = param.method.getName();
                }
                PrivacyHelper.getLogger().log( "beforeHookedMethod: " + clsName + "-" + mdName);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Object hookObj = param.thisObject;
                String clsName = "unknownClass";
                if (hookObj != null) {
                    clsName = hookObj.getClass().getName();
                }
                String mdName = "unknownMethod";
                if (param.method != null) {
                    mdName = param.method.getName();
                }
                PrivacyHelper.getLogger().log("afterHookedMethod: " + clsName + "-" + mdName);
            }
        };
        try {
            // hook系统方法
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                DexposedBridge.hookAllMethods(BluetoothLeScanner.class, "startScan", hook);
            }
            // hook系统方法
            DexposedBridge.hookAllMethods(Thread.class, "run", hook);
            // hook构造方法
            DexposedBridge.hookAllConstructors(Thread.class, hook);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
