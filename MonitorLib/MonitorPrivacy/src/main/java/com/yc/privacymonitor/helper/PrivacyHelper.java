package com.yc.privacymonitor.helper;

import android.content.Context;

import com.yc.appcommoninter.ILogger;
import com.yc.privacymonitor.bean.ClassMethodBean;
import com.yc.privacymonitor.bean.MethodBean;
import com.yc.privacymonitor.config.PrivacyConfig;
import com.yc.privacymonitor.handler.MethodHookImpl;
import com.yc.privacymonitor.method.HookMethodList;

import java.lang.reflect.Method;

import de.robv.android.xposed.DexposedBridge;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCAndroidTool
 *     email : yangchong211@163.com
 *     time  : 2021/05/09
 *     desc  : 隐私合规检测帮助类
 *     revise:
 * </pre>
 */
public final class PrivacyHelper {

    public static final String TAG = "PrivacyHelper: ";
    public static final MethodHookImpl DEFAULT_METHOD_HANDLER = new MethodHookImpl();
    private static Context sContext;
    private static ILogger sLogger;
    
    public static void init(PrivacyConfig config) {
        sContext = config.getApplication();
        sLogger = config.getLogger();
    }

    public static ILogger getLogger(){
        return sLogger;
    }

    /**
     * 开启监控
     * 建议开启严格模式,普通模式只监测常见的问题
     */
    public static void start(HookMethodList methodList){
        hookMethod(methodList);
        printWarning();
    }

    private static void hookMethod(HookMethodList methodList) {
        for (MethodBean methodWrapper : methodList.getMethodList()) {
            registerMethod(methodWrapper);
        }
        for (ClassMethodBean classMethodGroup : methodList.getAbsMethodList()) {
            registerClass(classMethodGroup);
        }
    }

    private static void registerMethod(MethodBean methodWrapper){
        try{
            Class<?> targetClass = methodWrapper.getTargetClass();
            String targetMethod = methodWrapper.getTargetMethod();
            Object[] paramsWithDefaultHandler = methodWrapper.getParamsWithDefaultHandler();
            //核心方法
            DexposedBridge.findAndHookMethod(targetClass,targetMethod,paramsWithDefaultHandler);
        }catch (NoSuchMethodError error){
            sLogger.log( "NoSuchMethodError->"+error.getMessage());
        }
    }

    private static void registerClass(ClassMethodBean classMethodGroup) {
        try {
            Class<?> clazz = Class.forName(classMethodGroup.getTargetClassName());
            Method[] declareMethods = clazz.getDeclaredMethods();
            for (Method method : declareMethods) {
                if (classMethodGroup.getMethodGroup().contains(method.getName())){
                    DexposedBridge.hookMethod(method, DEFAULT_METHOD_HANDLER);
                }
            }
        } catch (Exception e) {
            sLogger.log("registerClass Error-> " + e.getMessage());
        }
    }

    private static void printWarning() {
        sLogger.log( "########################################");
        sLogger.log( "隐私合规检测库已经开启【注意在debug下使用即可】");
        sLogger.log( "########################################");
    }
}

