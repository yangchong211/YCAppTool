package com.yc.apprestartlib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

public class MainifestRestartImpl implements IRestartApp {

    @Override
    public void reStartApp(Context context) {
        relaunchApp(context,false);
    }

    /**
     * Relaunch the application.
     *
     * @param context
     * @param isKillProcess True to kill the process, false otherwise.
     */
    public static void relaunchApp(Context context, final boolean isKillProcess) {
        Intent intent = getLaunchAppIntent(context,context.getApplicationContext().getPackageName(), true);
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
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
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
