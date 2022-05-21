package com.sankuai.erp.component.appinit.api;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import com.sankuai.erp.component.appinit.common.AppInitCommonUtils;
import com.sankuai.erp.component.appinit.common.AppInitItem;
import com.sankuai.erp.component.appinit.common.ChildInitTable;

import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2019/5/11
 *     desc   : 每个模块生成一张字表
 *     revise :
 *     GitHub : https://github.com/yangchong211/YCAppTool
 * </pre>
 */
public final class AppInitApiUtils {

    private AppInitApiUtils() {
    }

    /**
     * 包名判断是否为主进程
     */
    public static boolean isMainProcess() {
        return TextUtils.equals(AppInitManager.get().getApplication().getPackageName(), getProcessName());
    }

    /**
     * 获取进程全名
     */
    static String getProcessName() {
        ActivityManager am = (ActivityManager) AppInitManager.get().getApplication().getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) {
            return "";
        }
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return "";
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return "";
    }

    public static String getInitOrderAndTimeLog(List<ChildInitTable> childInitTableList, List<AppInitItem> appInitItemList) {
        StringBuilder logSb = new StringBuilder("初始化顺序为：\n");
        AppInitItem pre = null;
        for (AppInitItem appInitItem : appInitItemList) {
            if (pre == null || !AppInitCommonUtils.equals(pre.moduleCoordinate, appInitItem.moduleCoordinate)) {
                logSb.append(getModuleTimeInfo(childInitTableList, appInitItem.moduleCoordinate)).append("\n");
            }
            pre = appInitItem;
            logSb.append(appInitItem.timeInfo()).append("\n");
        }
        int time = 0;
        for (ChildInitTable childInitTable : childInitTableList) {
            time += childInitTable.time;
        }
        return logSb.append("\n").append(String.format("所有模块的初始化耗时：%sms", time)).toString();
    }

    private static String getModuleTimeInfo(List<ChildInitTable> childInitTableList, String moduleCoordinate) {
        for (ChildInitTable childInitTable : childInitTableList) {
            if (AppInitCommonUtils.equals(childInitTable.coordinate, moduleCoordinate)) {
                return childInitTable.getTimeInfo();
            }
        }
        return "";
    }
}
