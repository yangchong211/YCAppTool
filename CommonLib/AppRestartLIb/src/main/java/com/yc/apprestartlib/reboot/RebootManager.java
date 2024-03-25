package com.yc.apprestartlib.reboot;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import com.yc.toolutils.system.AppLogUtils;
import com.yc.toolutils.sp.AppSpUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 定时重启的类
 */
public final class RebootManager {

    private static final String TAG = RebootManager.class.getSimpleName();
    //SP的key
    private final static String LAST_REBOOT_DATE = "last_reboot_date";

    private static volatile RebootManager instance;
    private OnLimitCallback onLimitCallback;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final SimpleDateFormat df = new SimpleDateFormat("HH", Locale.getDefault());

    public static RebootManager getInstance() {
        if (instance == null) {
            synchronized (RebootManager.class) {
                if (instance == null) {
                    instance = new RebootManager();
                }
            }
        }
        return instance;
    }

    private RebootManager() {

    }


    /**
     * 检测是否要重启设备
     */
    public void checkReboot(Context context) {
        AppLogUtils.v(TAG, "checkReboot");
        //如果到了重启时间，但是今天还没重启过，就重启
        if (isRebootTime() && !isTodayRebooted()) {
            if (onLimitCallback != null && onLimitCallback.isLimitReboot()) {
                AppLogUtils.v(TAG, "外界条件不允许自动重启");
                return;
            }
            //保存今日重启记录
            AppSpUtils.getInstance().put(LAST_REBOOT_DATE, getTodayTime());
            //发送一条重启请求，保存日志
            AppLogUtils.v(TAG, "20秒后开始重启");
            //20秒之后再执行，防止无限重启，还能救火...
            reboot(context);
        } else {
            AppLogUtils.d(TAG + "已经重启过：" + isRebootTime() + " , " + isTodayRebooted());
        }
    }

    /**
     * 开始重启
     */
    public void reboot(Context context) {
        if (context == null) {
            AppLogUtils.d(TAG + "上下文异常");
            return;
        }
        try {
            AppLogUtils.d(TAG + "开始重启");
            ProcessReboot.triggerRebirth(context);
        } catch (Exception e) {
            AppLogUtils.d(TAG + "开始重启异常: " + e);
        }
    }

    /**
     * 通常会在Application的onCreate方法中做一系列初始化的操作。
     * 需要在onCreate方法中判断，如果当前进程是Phoenix进程，则直接return，跳过初始化的操作。
     * 检查当前进程是否为临时reboot进程。这可用于避免初始化未使用的资源，或防止运行未准备好多进程的代码。
     *
     * @return 如果当前进程是临时的reboot进程，则为true
     */
    public boolean isRebootProcess(Context context) {
        int currentPid = Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = manager.getRunningAppProcesses();
        if (runningProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.pid == currentPid && processInfo.processName.endsWith(":reboot")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 今天是否重启过
     *
     * @return 是否重启过
     */
    private boolean isTodayRebooted() {
        String lastRebootDate = AppSpUtils.getInstance().getString(LAST_REBOOT_DATE, "");
        AppLogUtils.v(TAG, "lastRebootDate:" + lastRebootDate);
        return getTodayTime().equals(lastRebootDate);
    }

    /**
     * 是否为检测重启的时间，目前每天凌晨两点到四点重启一次
     *
     * @return true表示是这个时间
     */
    private boolean isRebootTime() {
        Date date = new Date();
        String str = df.format(date);
        int a = Integer.parseInt(str);
        return (a >= 2) && (a <= 4);
    }

    /**
     * 读取当前时间,年月日
     *
     * @return 当前时间
     */
    private String getTodayTime() {
        return dateFormat.format(new Date());
    }

    public interface OnLimitCallback {
        /**
         * 是否限制重启
         *
         * @return true表示限制重启
         */
        boolean isLimitReboot();
    }

}
