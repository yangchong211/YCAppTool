package com.yc.privacymonitor;

import android.util.Log;

import com.yc.privacymonitor.handler.MethodHandler;
import com.yc.privacymonitor.method.ClassMethodGroup;
import com.yc.privacymonitor.method.HookMethodList;
import com.yc.privacymonitor.method.MethodWrapper;

import java.lang.reflect.Method;

import de.robv.android.xposed.DexposedBridge;

public class PrivacyMonitor {

    public static final String TAG = "PrivacyMonitor";
    public static final MethodHandler defaultMethodHandler = new MethodHandler();


    /**
     * 开启监控
     * 建议开启严格模式,普通模式只监测常见的问题
     */
    public static void start(HookMethodList methodList){
        hookMethod(methodList);
        printWarning();
    }

    private static void hookMethod(HookMethodList methodList) {
        for (MethodWrapper methodWrapper : methodList.getMethodList()) {
            registerMethod(methodWrapper);
        }
        for (ClassMethodGroup classMethodGroup : methodList.getAbsMethodList()) {
            registerClass(classMethodGroup);
        }

    }

    private static void registerMethod(MethodWrapper methodWrapper){
        try{
            DexposedBridge.findAndHookMethod(methodWrapper.getTargetClass(),methodWrapper.getTargetMethod(),methodWrapper.getParamsWithDefaultHandler());
        }catch (NoSuchMethodError error){
            Log.wtf(TAG, "registeredMethod: NoSuchMethodError->"+error.getMessage());
        }
    }
    private static void registerClass(ClassMethodGroup classMethodGroup) {
        try {
            Class<?> clazz = Class.forName(classMethodGroup.getTargetClassName());
            Method[] declareMethods = clazz.getDeclaredMethods();

            for (Method method : declareMethods) {
                if (classMethodGroup.getMethodGroup().contains(method.getName())){
                    DexposedBridge.hookMethod(method,defaultMethodHandler);
                }
            }
        } catch (Exception e) {
            Log.wtf(TAG,"registerClass Error-> " + e.getMessage());
        }
    }

    private static void printWarning() {
        Log.i(TAG, "########################################");
        Log.i(TAG, "#                                      #");
        Log.i(TAG, "#         *PrivacyMonitor 已启用*       #");
        Log.i(TAG, "#          请不要在正式环境使用!          #");
        Log.i(TAG, "#                                      #");
        Log.i(TAG, "########################################");
    }
}

