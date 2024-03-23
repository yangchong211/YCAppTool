package com.yc.toolutils.component;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import com.yc.toolutils.context.AppToolUtils;

import java.util.List;


/**
 * @author yangchong
 * GitHub : <a href="https://github.com/yangchong211">...</a>
 * email  : yangchong211@163.com
 * time  : 2016/09/23
 * desc  : Activity 相关工具类
 * revise:
 */
public final class AppActivityUtils {

    private AppActivityUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断某Activity是否挂掉
     *
     * @param activity activity
     * @return
     */
    public static boolean isActivityLiving(@Nullable Activity activity) {
        if (activity == null) {
            return false;
        }
        if (activity.isFinishing()) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            return false;
        }
        return true;
    }


    /**
     * 通过上下文获取到activity，使用到了递归
     *
     * @param context 上下文
     * @return 对象的活动对象，如果它不是活动对象，则为空。
     */
    public static Activity scanForActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }


    /**
     * Return whether the activity is alive.
     *
     * @param context The context.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isActivityAlive(final Context context) {
        return isActivityLiving(getActivityByContext(context));
    }

    /**
     * Return the activity by context.
     *
     * @param context The context.
     * @return the activity by context.
     */
    public static Activity getActivityByContext(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }


    /**
     * 通过context上下文找到activity
     *
     * @param context 上下文
     * @return activity
     */
    private static AppCompatActivity getAppCompActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        } else if (context instanceof ContextThemeWrapper) {
            return getAppCompActivity(((ContextThemeWrapper) context).getBaseContext());
        }
        return null;
    }

    /**
     * 获取启动项 Activity
     *
     * @return 启动项 Activity
     */
    public static String getLauncherActivity() {
        return getLauncherActivity(AppToolUtils.getApp().getPackageName());
    }

    /**
     * 获取启动项 Activity
     *
     * @param packageName 包名
     * @return 启动项 Activity
     */
    public static String getLauncherActivity(@NonNull final String packageName) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PackageManager pm = AppToolUtils.getApp().getPackageManager();
        List<ResolveInfo> info = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo aInfo : info) {
            if (aInfo.activityInfo.packageName.equals(packageName)) {
                return aInfo.activityInfo.name;
            }
        }
        return "no " + packageName;
    }


    /**
     * 是否为竖屏
     */
    public static boolean isPortrait(Activity activity) {
        return activity.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT;
    }

}
