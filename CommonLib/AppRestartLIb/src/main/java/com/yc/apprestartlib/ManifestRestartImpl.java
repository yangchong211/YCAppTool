package com.yc.apprestartlib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import androidx.annotation.NonNull;

import com.yc.activitymanager.activity.ActivityManager;

import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     email : yangchong211@163.com
 *     time  : 2018/11/9
 *     desc  : 重启APP接口，使用清单文件方式重启实现
 *     revise:
 * </pre>
 */
public class ManifestRestartImpl implements IRestartProduct {

    public ManifestRestartImpl(){

    }

    @Override
    public void restartApp(Context context) {
        relaunchApp(context,true);
    }

    public static void relaunchApp(Context context, final boolean isKillProcess) {
        String packageName = context.getApplicationContext().getPackageName();
        Intent intent = getLaunchAppIntent(context,packageName, true);
        if (intent == null) {
            Log.e("AppUtils", "Didn't exist launcher activity.");
            return;
        }
        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );
        context.startActivity(intent);
        if (!isKillProcess) {
            return;
        }
        ActivityManager.getInstance().killCurrentProcess(false);
    }


    private static Intent getLaunchAppIntent(Context context, final String packageName) {
        return getLaunchAppIntent(context,packageName, false);
    }


    private static Intent getLaunchAppIntent(Context context, final String packageName, final boolean isNewTask) {
        String launcherActivity = getLauncherActivity(context,packageName);
        if (!launcherActivity.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(packageName, launcherActivity);
            intent.setComponent(cn);
            return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
        }
        return null;
    }

    private static String getLauncherActivity(Context context, @NonNull final String pkg) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setPackage(pkg);
        PackageManager pm = context.getApplicationContext().getPackageManager();
        List<ResolveInfo> info = pm.queryIntentActivities(intent, 0);
        int size = info.size();
        if (size == 0) {
            return "";
        }
        for (int i = 0; i < size; i++) {
            ResolveInfo ri = info.get(i);
            if (ri.activityInfo.processName.equals(pkg)) {
                return ri.activityInfo.name;
            }
        }
        return info.get(0).activityInfo.name;
    }

}
