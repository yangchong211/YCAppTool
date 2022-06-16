package com.yc.privacymonitor.test;

import android.telephony.TelephonyManager;
import android.util.Log;

import com.yc.privacymonitor.bean.ClassMethodBean;
import com.yc.privacymonitor.helper.PrivacyHelper;
import com.yc.toolutils.StackTraceUtils;

import java.lang.reflect.Method;

import de.robv.android.xposed.DexposedBridge;
import de.robv.android.xposed.XC_MethodHook;

public class TestPrivacyMonitor {

    public static void hookGetDeviceId() {
        try {
            Class<?> targetClass = TelephonyManager.class;
            String targetMethod = "getDeviceId";
            Object[] paramsWithDefaultHandler = {int.class};
            //核心方法
            DexposedBridge.findAndHookMethod(targetClass, targetMethod, paramsWithDefaultHandler);
        } catch (NoSuchMethodError error) {
            PrivacyHelper.getLogger().log("NoSuchMethodError->" + error.getMessage());
        }
    }

    public static void hookGetDeviceId2() {
        try {
            Class<?> targetClass = TelephonyManager.class;
            String targetMethod = "getDeviceId";
            //核心方法
            DexposedBridge.findAndHookMethod(targetClass, targetMethod, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    //举个例子
                    //android.telephony.TelephonyManager#getTelephonyProperty
                    String className = param.method.getDeclaringClass().getName();
                    //android.telephony.TelephonyManager
                    String methodName = param.method.getName();
                    Log.i(PrivacyHelper.TAG, "检测到风险函数被调用: " + className + "#" + methodName);
                    Log.i(PrivacyHelper.TAG, "检测到风险函数被调用: " + className + "#" + methodName);
                    String methodStack = StackTraceUtils.getMethodStack();
                    Log.d(PrivacyHelper.TAG, methodStack);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Log.d(PrivacyHelper.TAG, "afterHookedMethod getDeviceId");
                }
            });
        } catch (NoSuchMethodError error) {
            PrivacyHelper.getLogger().log("NoSuchMethodError->" + error.getMessage());
        }
    }

    public static void hookPackageManager() {
        try {
            ClassMethodBean pkgManagerHook = new ClassMethodBean(
                    "android.app.ApplicationPackageManager");
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

}
