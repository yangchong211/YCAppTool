package com.yc.toolutils.process;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Application;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;

import com.yc.toolutils.context.AppToolUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static android.Manifest.permission.KILL_BACKGROUND_PROCESSES;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2018/5/6
 *     desc  : 进程工具类
 *     revise:
 * </pre>
 */
public final class AppProcessUtils {

    private static String currentProcessName;

    /**
     * 获取当前进程名
     * 我们优先通过 Application.getProcessName() 方法获取进程名。
     * 如果获取失败，我们再反射ActivityThread.currentProcessName()获取进程名
     * 如果失败，我们才通过常规方法ActivityManager来获取进程名
     * @return                      当前进程名
     */
    @Nullable
    public static String getCurrentProcessName(@NonNull Context context) {
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }
        //1)通过Application的API获取当前进程名
        currentProcessName = getCurrentProcessNameByApplication();
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }
        //2)通过反射ActivityThread获取当前进程名
        currentProcessName = getCurrentProcessNameByActivityThread();
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }
        //3)通过ActivityManager获取当前进程名
        currentProcessName = getCurrentProcessNameByActivityManager(context);
        return currentProcessName;
    }

    /**
     * 通过Application新的API获取进程名，无需反射，无需IPC，效率最高。
     * 这不就是我们想要的API吗！但是这个方法只有在android9【也就是aip28】之后的系统才能调用。
     */
    public static String getCurrentProcessNameByApplication() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            //Application.getProcessName()方法直接返回当前进程名。
            //这个方法只有在android9【也就是aip28】之后的系统才能调用
            return Application.getProcessName();
        }
        return null;
    }

    /**
     * 通过反射ActivityThread获取进程名，避免了ipc
     */
    public static String getCurrentProcessNameByActivityThread() {
        String processName = null;
        try {
            //ActivityThread.currentProcessName()方法居然是public static的
            //ActivityThread类是hide的，app无法直接调用。
            //继续翻源码，看看这个方法是什么时候新增的。发现这个方法在android4.3.1上就已经有了这个方法了。
            //在android4.0.4上没有找到currentProcessName()方法。
            //那么意味着，我们是不是可以反射调用 ActivityThread.currentProcessName()
            ClassLoader classLoader = Application.class.getClassLoader();
            @SuppressLint("PrivateApi")
            final Method declaredMethod = Class.forName("android.app.ActivityThread",
                    false, classLoader)
                    .getDeclaredMethod("currentProcessName", (Class<?>[]) new Class[0]);
            declaredMethod.setAccessible(true);
            final Object invoke = declaredMethod.invoke(null, new Object[0]);
            if (invoke instanceof String) {
                processName = (String) invoke;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return processName;
    }

    /**
     * 通过ActivityManager 获取进程名，需要IPC通信
     * 1。ActivityManager.getRunningAppProcesses() 方法需要跨进程通信，效率不高。
     *    需要 和 系统进程的 ActivityManagerService 通信。必然会导致该方法调用耗时。
     * 2。拿到RunningAppProcessInfo的列表之后，还需要遍历一遍找到与当前进程的信息。
     * 3。ActivityManager.getRunningAppProcesses()有可能调用失败，返回null，也可能 AIDL 调用失败。
     *    当然ActivityManager.getRunningAppProcesses()调用失败是极低的概率。
     *    当你的APP用户量达到一定的数量级别时，一定会有用户遇到ActivityManager.getRunningAppProcesses()调用失败的情况。
     */
    public static String getCurrentProcessNameByActivityManager(@NonNull Context context) {
        if (context == null) {
            return null;
        }
        //指的是Process的id。每个进程都有一个独立的id，可以通过pid来区分不同的进程。
        int pid = android.os.Process.myPid( );
        ActivityManager am = (ActivityManager) context.getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            //获取当前正在运行的进程
            List<ActivityManager.RunningAppProcessInfo> runningAppList = am.getRunningAppProcesses();
            if (runningAppList != null) {
                for (ActivityManager.RunningAppProcessInfo processInfo : runningAppList) {
                    //相应的RunningServiceInfo的pid
                    if (processInfo.pid == pid) {
                        return processInfo.processName;
                    }
                }
            }
        }
        return null;
    }


    /**
     * 获取前台线程包名
     * <p>当不是查看当前 App，且 SDK 大于 21 时，
     * 需添加权限
     * {@code <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS" />}</p>
     *
     * @return 前台应用包名
     */
    public static String getForegroundProcessName() {
        ActivityManager am = (ActivityManager) AppToolUtils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        //noinspection ConstantConditions
        List<ActivityManager.RunningAppProcessInfo> pInfo = am.getRunningAppProcesses();
        if (pInfo != null && pInfo.size() > 0) {
            for (ActivityManager.RunningAppProcessInfo aInfo : pInfo) {
                if (aInfo.importance
                        == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return aInfo.processName;
                }
            }
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            PackageManager pm = AppToolUtils.getApp().getPackageManager();
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            List<ResolveInfo> list =
                    pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            Log.i("ProcessUtils", list.toString());
            if (list.size() <= 0) {
                Log.i("ProcessUtils",
                        "getForegroundProcessName: noun of access to usage information.");
                return "";
            }
            try {// Access to usage information.
                ApplicationInfo info =
                        pm.getApplicationInfo(AppToolUtils.getApp().getPackageName(), 0);
                AppOpsManager aom =
                        (AppOpsManager) AppToolUtils.getApp().getSystemService(Context.APP_OPS_SERVICE);
                if (aom.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        info.uid,
                        info.packageName) != AppOpsManager.MODE_ALLOWED) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    AppToolUtils.getApp().startActivity(intent);
                }
                if (aom.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        info.uid,
                        info.packageName) != AppOpsManager.MODE_ALLOWED) {
                    Log.i("ProcessUtils",
                            "getForegroundProcessName: refuse to device usage stats.");
                    return "";
                }
                UsageStatsManager usageStatsManager = (UsageStatsManager) AppToolUtils.getApp()
                        .getSystemService(Context.USAGE_STATS_SERVICE);
                List<UsageStats> usageStatsList = null;
                if (usageStatsManager != null) {
                    long endTime = System.currentTimeMillis();
                    long beginTime = endTime - 86400000 * 7;
                    usageStatsList = usageStatsManager
                            .queryUsageStats(UsageStatsManager.INTERVAL_BEST,
                                    beginTime, endTime);
                }
                if (usageStatsList == null || usageStatsList.isEmpty()) {
                    return "";
                }
                UsageStats recentStats = null;
                for (UsageStats usageStats : usageStatsList) {
                    if (recentStats == null
                            || usageStats.getLastTimeUsed() > recentStats.getLastTimeUsed()) {
                        recentStats = usageStats;
                    }
                }
                return recentStats == null ? null : recentStats.getPackageName();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 获取后台服务进程
     * <p>需添加权限
     * {@code <uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" />}</p>
     *
     * @return 后台服务进程
     */
    @RequiresPermission(KILL_BACKGROUND_PROCESSES)
    public static Set<String> getAllBackgroundProcesses(Context context) {
        ActivityManager am =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        Set<String> set = new HashSet<>();
        if (info != null) {
            for (ActivityManager.RunningAppProcessInfo aInfo : info) {
                Collections.addAll(set, aInfo.pkgList);
            }
        }
        return set;
    }

    /**
     * 杀死所有的后台服务进程
     * <p>需添加权限
     * {@code <uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" />}</p>
     *
     * @return 被暂时杀死的服务集合
     */
    @RequiresPermission(KILL_BACKGROUND_PROCESSES)
    public static Set<String> killAllBackgroundProcesses(Context context) {
        ActivityManager am =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        Set<String> set = new HashSet<>();
        if (info == null) {
            return set;
        }
        for (ActivityManager.RunningAppProcessInfo aInfo : info) {
            for (String pkg : aInfo.pkgList) {
                am.killBackgroundProcesses(pkg);
                set.add(pkg);
            }
        }
        info = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo aInfo : info) {
            for (String pkg : aInfo.pkgList) {
                set.remove(pkg);
            }
        }
        return set;
    }

    /**
     * 杀死后台服务进程
     * <p>需添加权限
     * {@code <uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" />}</p>
     *
     * @param packageName The name of the package.
     * @return {@code true}: 杀死成功<br>{@code false}: 杀死失败
     */
    @SuppressLint("MissingPermission")
    public static boolean killBackgroundProcesses(Context context,@NonNull final String packageName) {
        ActivityManager am =
                (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) {
            return false;
        }
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        if (info == null || info.size() == 0) {
            return true;
        }
        for (ActivityManager.RunningAppProcessInfo aInfo : info) {
            if (Arrays.asList(aInfo.pkgList).contains(packageName)) {
                am.killBackgroundProcesses(packageName);
            }
        }
        info = am.getRunningAppProcesses();
        if (info == null || info.size() == 0) {
            return true;
        }
        for (ActivityManager.RunningAppProcessInfo aInfo : info) {
            if (Arrays.asList(aInfo.pkgList).contains(packageName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否在主线程
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isMainProcess(Context context) {
        if (context == null){
            return false;
        }
        //获取当前进程名，并与主进程对比，来判断是否为主进程
        String packageName = context.getPackageName();
        String currentProcessName = getCurrentProcessName(context);
        return packageName.equals(currentProcessName);
    }


    /**
     * 判断app是否正在运行
     * @param context                       上下文
     * @param packageName                   应用的包名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isAppRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list ;
        if (am != null) {
            list = am.getRunningTasks(100);
            if (list.size() <= 0) {
                return false;
            }
            for (ActivityManager.RunningTaskInfo info : list) {
                if (info.baseActivity.getPackageName().equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 判断某个进程是否在运行中
     * @param context                           上下文
     * @param processName                       进程名称
     * @return
     */
    public static boolean isRunningTaskExist(Context context, String processName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processList = am.getRunningAppProcesses();
        if (processList != null) {
            for (ActivityManager.RunningAppProcessInfo info : processList) {
                if (info.processName.equals(processName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否运行在后台
     * 参考链接：https://www.cnblogs.com/zhujiabin/p/9336663.html
     * 方案一：利用ActivityManager的RunningAppProcessInfo类
     * 方案二：监听Home键点击
     * 方案三：利用ActivityLifecycleCallbacks监听所有activity的生命周期
     *
     * 建议使用：https://github.com/yangchong211/YCEfficient
     *
     * @param context                           上下文
     * @return                                  返回true表示在后台
     */
    @Deprecated
    public static boolean isRunningInForeground(Context context) {
        if (Build.VERSION.SDK_INT >= 21){
            return LollipopRunningProcessCompat.isRunningInForeground(context);
        }
        return RunningProcessCompat.isRunningInForeground(context);
    }


    private static final class LollipopRunningProcessCompat extends RunningProcessCompat {
        private LollipopRunningProcessCompat() {

        }

        @Deprecated
        public static boolean isRunningInForeground(Context context) {
            try {
                Class clazz = ActivityManager.RunningAppProcessInfo.class;
                Field processStateField = clazz.getDeclaredField("processState");
                ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
                //其中RunningAppProcessInfo类则封装了正在运行着的进程信息，当然也包含了正在运行的app的包名
                //可以activitymanager.getRunningAppProcesses()获取当前运行的app列表
                List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
                if (null == processInfos || processInfos.isEmpty()) {
                    return false;
                }
                String packageName = context.getPackageName();
                //对比自身的包名，来判断本身app是否处于前台运行。
                Iterator<ActivityManager.RunningAppProcessInfo> var5 = processInfos.iterator();
                while(var5.hasNext()) {
                    ActivityManager.RunningAppProcessInfo rapi = var5.next();
                    if (rapi.importance == 100 && rapi.importanceReasonCode == 0) {
                        try {
                            int processState = processStateField.getInt(rapi);
                            if (processState == 2 && rapi.pkgList != null && rapi.pkgList.length > 0) {
                                return rapi.pkgList[0].equals(packageName);
                            }
                        } catch (Exception var8) {
                        }
                    }
                }
            } catch (Exception var9) {
            }
            return false;
        }
    }


    private static class RunningProcessCompat {

        private RunningProcessCompat() {

        }

        @Deprecated
        public static boolean isRunningInForeground(Context context) {
            try {
                ActivityManager am = (ActivityManager)context.getSystemService(
                        Context.ACTIVITY_SERVICE);
                String packageName = context.getPackageName();
                List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
                return null != tasks && !tasks.isEmpty() &&
                        tasks.get(0).topActivity.getPackageName().equals(packageName);
            } catch (Exception var3) {
                return false;
            }
        }
    }

    /**
     * 判断app是否处于前台
     * 建议使用：https://github.com/yangchong211/YCEfficient
     *
     * @return  true表示前台
     */
    @Deprecated
    public static boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) AppToolUtils.getApp()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = AppToolUtils.getApp().getPackageName();
        //获取Android设备中所有正在运行的App
        List<ActivityManager.RunningAppProcessInfo> appProcesses =
                activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance ==
                    ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

}
