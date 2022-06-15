package com.yc.privacymonitor.test;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.location.LocationManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class SherLockMonitor implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {

        if (lpparam == null) {
            return;
        }

        //hook获取设备信息方法
        XposedHelpers.findAndHookMethod(
                TelephonyManager.class.getName(),
                lpparam.classLoader,
                "getDeviceId",
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        XposedBridge.log("调用getDeviceId(int)获取了imei");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log(getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );

        //hook imsi获取方法
        XposedHelpers.findAndHookMethod(
                TelephonyManager.class.getName(),
                lpparam.classLoader,
                "getSubscriberId",
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        XposedBridge.log("调用getSubscriberId获取了imsi");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log(getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );
        //hook低版本系统获取mac地方方法
        XposedHelpers.findAndHookMethod(
                android.net.wifi.WifiInfo.class.getName(),
                lpparam.classLoader,
                "getMacAddress",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        XposedBridge.log("调用getMacAddress()获取了mac地址");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log(getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );
        //hook获取mac地址方法
        XposedHelpers.findAndHookMethod(
                java.net.NetworkInterface.class.getName(),
                lpparam.classLoader,
                "getHardwareAddress",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        XposedBridge.log("调用getHardwareAddress()获取了mac地址");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log(getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );
        //hook获取android_id
        XposedHelpers.findAndHookMethod(
                Settings.Secure.class.getName(),
                lpparam.classLoader,
                "getString",
                ContentResolver.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
//                        if(param != null && param.args != null){
//                            for(int i=0;i<param.args.length;i++){
//                                XposedBridge.log("调用getString，["+i+"]参数为:" + param.args[i]);
//                            }
//
//                        }

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
                            XposedBridge.log(getMethodStack());
                        }
                        super.afterHookedMethod(param);
                    }
                }
        );

        //hook获取sim卡序列号
        XposedHelpers.findAndHookMethod(
                TelephonyManager.class.getName(),
                lpparam.classLoader,
                "getSimSerialNumber",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
//                        if(param != null && param.args != null){
//                            for(int i=0;i<param.args.length;i++){
//                                XposedBridge.log("调用getString，["+i+"]参数为:" + param.args[i]);
//                            }
//
//                        }

                        XposedBridge.log("调用getSimSerialNumber，获取了sim卡序列号");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log(getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );

        //hook获取getSimOperatorName
        XposedHelpers.findAndHookMethod(
                TelephonyManager.class.getName(),
                lpparam.classLoader,
                "getSimOperatorName",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {

                        XposedBridge.log("调用getSimOperatorName，获取网络运营商，iccid");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log(getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );

        //hook获取getSimOperator
        XposedHelpers.findAndHookMethod(
                TelephonyManager.class.getName(),
                lpparam.classLoader,
                "getSimOperator",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {

                        XposedBridge.log("调用getSimOperator");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log(getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );

        //hook获取tasks
        XposedHelpers.findAndHookMethod(
                ActivityManager.class.getName(),
                lpparam.classLoader,
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
                        XposedBridge.log(getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );
        //hook获取tasks
        XposedHelpers.findAndHookMethod(
                ActivityManager.class.getName(),
                lpparam.classLoader,
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
                        XposedBridge.log(getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );
        //hook获取tasks
        XposedHelpers.findAndHookMethod(
                ActivityManager.class.getName(),
                lpparam.classLoader,
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
                        XposedBridge.log(getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );


        //hook定位方法
        XposedHelpers.findAndHookMethod(
                LocationManager.class.getName(),
                lpparam.classLoader,
                "getLastKnownLocation",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        XposedBridge.log("调用getLastKnownLocation获取了GPS地址");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log(getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );
        Log.w("kkkkk","name:" + LocationManager.class.getName());

//        //hook定位方法
//        XposedHelpers.findAndHookMethod(
//                Handler.class.getName(),
//                lpparam.classLoader,
//                "sendEmptyMessage",
//                int.class,
//                new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) {
//                        XposedBridge.log("qadqwewqeqwewqeq");
//                    }
//
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        XposedBridge.log(getMethodStack());
//                        super.afterHookedMethod(param);
//                    }
//                }
//        );
    }

    private String getMethodStack() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        StringBuilder stringBuilder = new StringBuilder();

        for (StackTraceElement temp : stackTraceElements) {
            stringBuilder.append(temp.toString() + "\n");
        }

        return stringBuilder.toString();

    }
}
