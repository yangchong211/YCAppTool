package com.yc.apprestartlib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yc.activitymanager.activity.ActivityManager;
import com.yc.toolutils.system.AppLogUtils;

import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     email : yangchong211@163.com
 *     time  : 2018/11/9
 *     desc  : 重启APP接口，使用launcher方式重启实现
 *     revise:
 * </pre>
 */
public class LauncherRestartImpl implements IRestartProduct {

    public LauncherRestartImpl(){

    }

    @Override
    public void restartApp(Context context) {
        String packageName = context.getPackageName();
        Activity activity = ActivityManager.getInstance().peek();
        Class<? extends Activity> clazz = guessRestartActivityClass(activity);
        AppLogUtils.w("IRestartApp:", "restart app launcher " + packageName + " " + clazz);
        if (clazz != null) {
            Intent intent = new Intent(activity, clazz);
            restartApplicationWithIntent(context, intent, false);
        }
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
                String className = intent.getComponent().getClassName();
                AppLogUtils.w("IRestartApp:", "restart app launcher , get launcher " + className);
                return (Class<? extends Activity>) Class.forName(className);
            } catch (ClassNotFoundException e) {
                AppLogUtils.e(e.getLocalizedMessage());
            }
        }
        return null;
    }


    private static void restartApplicationWithIntent(Context context, Intent intent, final boolean isKillProcess) {
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        context.startActivity(intent);
        if (!isKillProcess) {
            return;
        }
        ActivityManager.getInstance().killCurrentProcess(false);
    }


}
