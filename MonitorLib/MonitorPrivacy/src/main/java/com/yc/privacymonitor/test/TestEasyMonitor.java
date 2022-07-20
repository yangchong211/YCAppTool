package com.yc.privacymonitor.test;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.location.LocationManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.yc.toolutils.StackTraceUtils;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class TestEasyMonitor implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) {
        if (param == null) {
            return;
        }
        //hook获取设备信息方法
        hookGetDeviceId(param);
        //hook imsi获取方法
        hookGetSubscriberId(param);
        //hook低版本系统获取mac地方方法
        hookGetMacAddress(param);
        //hook获取mac地址方法
        hookGetHardwareAddress(param);
        //hook获取android_id
        hookAndroidId(param);
        //hook获取sim卡序列号
        hookGetSimSerialNumber(param);
        //hook获取getSimOperatorName
        hookGetSimOperatorName(param);

        //hook获取getSimOperator
        XposedHelpers.findAndHookMethod(
                TelephonyManager.class.getName(),
                param.classLoader,
                "getSimOperator",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {

                        XposedBridge.log("调用getSimOperator");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log(StackTraceUtils.getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );

        //hook获取tasks
        XposedHelpers.findAndHookMethod(
                ActivityManager.class.getName(),
                param.classLoader,
                "getRunningTasks",
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
//                        if(param != null && param.args != null){
//                            for(int i=0;i<param.args.length;i++){
//                                XposedBridge.log("调用getString，["+i+"]参数为:" + param.args[i]);
//                            }
//
//                        }

                        XposedBridge.log("调用getRunningTasks，获取了tasks");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log(StackTraceUtils.getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );
        //hook获取tasks
        XposedHelpers.findAndHookMethod(
                ActivityManager.class.getName(),
                param.classLoader,
                "getAppTasks",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
//                        if(param != null && param.args != null){
//                            for(int i=0;i<param.args.length;i++){
//                                XposedBridge.log("调用getString，["+i+"]参数为:" + param.args[i]);
//                            }
//
//                        }

                        XposedBridge.log("调用getAppTasks，获取了tasks");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log(StackTraceUtils.getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );
        //hook获取tasks
        XposedHelpers.findAndHookMethod(
                ActivityManager.class.getName(),
                param.classLoader,
                "getRunningAppProcesses",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
//                        if(param != null && param.args != null){
//                            for(int i=0;i<param.args.length;i++){
//                                XposedBridge.log("调用getString，["+i+"]参数为:" + param.args[i]);
//                            }
//
//                        }

                        XposedBridge.log("调用getRunningAppProcesses，获取了tasks");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log(StackTraceUtils.getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );


        //hook定位方法
        XposedHelpers.findAndHookMethod(
                LocationManager.class.getName(),
                param.classLoader,
                "getLastKnownLocation",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        XposedBridge.log("调用getLastKnownLocation获取了GPS地址");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log(StackTraceUtils.getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );
    }

    private void hookGetSimOperatorName(XC_LoadPackage.LoadPackageParam param) {
        XposedHelpers.findAndHookMethod(
                TelephonyManager.class.getName(),
                param.classLoader,
                "getSimOperatorName",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        XposedBridge.log("调用getSimOperatorName，获取网络运营商，iccid");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log(StackTraceUtils.getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );
    }

    private void hookGetSimSerialNumber(XC_LoadPackage.LoadPackageParam param) {
        XposedHelpers.findAndHookMethod(
                TelephonyManager.class.getName(),
                param.classLoader,
                "getSimSerialNumber",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        XposedBridge.log("调用getSimSerialNumber，获取了sim卡序列号");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log(StackTraceUtils.getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );
    }

    private void hookAndroidId(XC_LoadPackage.LoadPackageParam param) {
        XposedHelpers.findAndHookMethod(
                Settings.Secure.class.getName(),
                param.classLoader,
                "getString",
                ContentResolver.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        /*if(param != null && param.args != null){
                            for(int i=0;i<param.args.length;i++){
                                XposedBridge.log("调用getString，["+i+"]参数为:" + param.args[i]);
                            }
                        }*/

                        if(param != null && param.args != null && param.args.length >= 2
                                && param.args[0] != null && param.args[1] != null
                                && param.args[0] instanceof ContentResolver
                                && param.args[1] instanceof String
                                && ((String)param.args[1]).equals("android_id")){
                            XposedBridge.log("调用getString，参数为android_id，获取了android_id");
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        if(param != null && param.args != null && param.args.length >= 2
                                && param.args[0] != null && param.args[1] != null
                                && param.args[0] instanceof ContentResolver
                                && param.args[1] instanceof String
                                && ((String)param.args[1]).equals("android_id")){
                            XposedBridge.log(StackTraceUtils.getMethodStack());
                        }
                        super.afterHookedMethod(param);
                    }
                }
        );
    }

    private void hookGetHardwareAddress(XC_LoadPackage.LoadPackageParam param) {
        XposedHelpers.findAndHookMethod(
                java.net.NetworkInterface.class.getName(),
                param.classLoader,
                "getHardwareAddress",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        XposedBridge.log("调用getHardwareAddress()获取了mac地址");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log(StackTraceUtils.getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );
    }

    private void hookGetMacAddress(XC_LoadPackage.LoadPackageParam param) {
        XposedHelpers.findAndHookMethod(
                android.net.wifi.WifiInfo.class.getName(),
                param.classLoader,
                "getMacAddress",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        XposedBridge.log("调用getMacAddress()获取了mac地址");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log(StackTraceUtils.getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );
    }

    private void hookGetSubscriberId(XC_LoadPackage.LoadPackageParam param) {
        XposedHelpers.findAndHookMethod(
                TelephonyManager.class.getName(),
                param.classLoader,
                "getSubscriberId",
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        XposedBridge.log("调用getSubscriberId获取了imsi");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log(StackTraceUtils.getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );
    }

    private void hookGetDeviceId(XC_LoadPackage.LoadPackageParam param) {
        XposedHelpers.findAndHookMethod(
                TelephonyManager.class.getName(),
                param.classLoader,
                "getDeviceId",
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        XposedBridge.log("调用getDeviceId(int)获取了imei");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log(StackTraceUtils.getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );
    }

}
