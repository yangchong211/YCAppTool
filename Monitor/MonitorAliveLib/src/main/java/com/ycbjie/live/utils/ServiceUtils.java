package com.ycbjie.live.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/19
 *     desc  : 工具类
 *     revise:
 * </pre>
 */
public final class ServiceUtils {

    private ServiceUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断某个service服务是否正在运行
     * @param context                           上下文
     * @param className                         类型
     * @return
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> servicesList = activityManager
                .getRunningServices(Integer.MAX_VALUE);
        if (servicesList != null) {
            for (ActivityManager.RunningServiceInfo si : servicesList) {
                if (className.equals(si.service.getClassName())) {
                    isRunning = true;
                }
            }
        }
        return isRunning;
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
     * 判断是否是主进程
     * @param application                       全局上下文
     * @return                                  布尔值
     */
    public static boolean isMainProcess(@NonNull Application application) {
        //获取进程pid
        int pid = android.os.Process.myPid();
        AliveLogUtils.d("当前app的进程pid---"+pid);
        String processName = "";
        ActivityManager manager = (ActivityManager) application
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos =
                manager.getRunningAppProcesses();
        if (runningAppProcessInfos != null) {
            for (ActivityManager.RunningAppProcessInfo appProcess : manager.getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    processName = appProcess.processName;
                    break;
                }
            }
            return processName.equals(application.getPackageName());
        }
        return false;
    }
}
