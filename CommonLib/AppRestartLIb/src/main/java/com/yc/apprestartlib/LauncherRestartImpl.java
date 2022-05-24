package com.yc.apprestartlib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yc.baseclasslib.activity.ActivityManager;
import com.yc.toolutils.AppLogUtils;

import java.util.List;

public class LauncherRestartImpl implements IRestartApp{
    @Override
    public void reStartApp(Context context) {
        String packageName = context.getPackageName();
        Activity activity = ActivityManager.getInstance().peek();
        Class<? extends Activity> clazz = guessRestartActivityClass(activity);
        AppLogUtils.w("LauncherRestartImpl", "reStartApp--- 用来重启本APP--3-"+packageName + "--"+clazz);
        Intent intent = new Intent(activity, clazz);
        restartApplicationWithIntent(activity, intent);
    }

    @Nullable
    private static Class<? extends Activity> guessRestartActivityClass(@NonNull Context context) {
        Class<? extends Activity> resolvedActivityClass;
        resolvedActivityClass = getRestartActivityClassWithIntentFilter(context);
        if (resolvedActivityClass == null) {
            resolvedActivityClass = getLauncherActivity(context);
        }
        return resolvedActivityClass;
    }


    @SuppressWarnings("unchecked")
    @Nullable
    private static Class<? extends Activity> getRestartActivityClassWithIntentFilter(@NonNull Context context) {
        Intent searchedIntent = new Intent().setPackage(context.getPackageName());
        //检索可以为给定意图执行的所有活动
        List<ResolveInfo> resolveInfo = context.getPackageManager().queryIntentActivities(searchedIntent,
                PackageManager.GET_RESOLVED_FILTER);
        if (resolveInfo.size() > 0) {
            ResolveInfo info = resolveInfo.get(0);
            try {
                return (Class<? extends Activity>) Class.forName(info.activityInfo.name);
            } catch (ClassNotFoundException e) {
                AppLogUtils.e(e.getMessage());
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private static Class<? extends Activity> getLauncherActivity(@NonNull Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (intent != null && intent.getComponent() != null) {
            try {
                return (Class<? extends Activity>) Class.forName(intent.getComponent().getClassName());
            } catch (ClassNotFoundException e) {
                AppLogUtils.e(e.getLocalizedMessage());
            }
        }
        return null;
    }


    private static void restartApplicationWithIntent(@NonNull Activity activity, @NonNull Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        if (intent.getComponent() != null) {
            //如果类名已经设置，我们强制它模拟启动器启动。
            //如果我们不这样做，如果你从错误活动重启，然后按home，
            //然后从启动器启动活动，主活动将在backstack上出现两次。
            //这很可能不会有任何有害的影响，因为如果你设置了Intent组件，
            //if将始终启动，而不考虑此处指定的操作。
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
        }
        activity.startActivity(intent);
        activity.finish();
        ActivityManager.getInstance().killCurrentProcess(true);
    }


}
