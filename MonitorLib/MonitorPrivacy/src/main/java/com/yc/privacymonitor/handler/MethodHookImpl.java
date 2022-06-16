package com.yc.privacymonitor.handler;

import android.util.Log;

import com.yc.privacymonitor.helper.PrivacyHelper;
import com.yc.toolutils.StackTraceUtils;

import de.robv.android.xposed.XC_MethodHook;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCAndroidTool
 *     email : yangchong211@163.com
 *     time  : 2021/05/09
 *     desc  : 在hook方法之前调用
 *     revise:
 * </pre>
 */
public class MethodHookImpl extends XC_MethodHook {

    @Override
    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
        Log.d(PrivacyHelper.TAG, "beforeHookedMethod");
        //举个例子
        //android.telephony.TelephonyManager#getTelephonyProperty
        String className = param.method.getDeclaringClass().getName();
        //android.telephony.TelephonyManager
        String methodName = param.method.getName();
        //getTelephonyProperty
        Log.i(PrivacyHelper.TAG, "检测到风险函数被调用: " +className +"#"+ methodName);
        String methodStack = StackTraceUtils.getMethodStack();
        Log.d(PrivacyHelper.TAG, methodStack);
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        Log.d(PrivacyHelper.TAG, "afterHookedMethod");
    }
}

