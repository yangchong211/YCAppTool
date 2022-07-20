package com.yc.alive.util;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.RestrictTo;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;

/**
 * Intent 跳转工具类
 * <p>
 *  2018/5/17.
 */
@RestrictTo(LIBRARY)
public class AliveIntentUtils {

    private static final String TAG = "KAIntentUtils";

    /**
     * 打开页面 - 有错误提示
     *
     * @return 跳转是否成功
     */
    public static boolean launch(Context context, String packageName, String className) {
        return launch(context, packageName, className, true);
    }

    /**
     * 打开页面
     *
     * @param showErrorTip 显示错误提示
     *
     * @return 跳转是否成功
     */
    public static boolean launch(Context context, String packageName, String className, boolean showErrorTip) {
        if (context == null || packageName == null || className == null) {
            return false;
        }
        Intent intent = new Intent();
        ComponentName component = new ComponentName(packageName, className);
        intent.setComponent(component);
        return launch(context, intent, showErrorTip);
    }

    /**
     * 打开页面 - 有错误提示
     *
     * @return 跳转是否成功
     */
    public static boolean launch(Context context, Intent intent) {
        return launch(context, intent, true);
    }

    /**
     * 打开页面
     *
     * @param context      context
     * @param intent       intent
     * @param showErrorTip 显示错误提示
     *
     * @return 跳转是否成功
     */
    public static boolean launch(Context context, Intent intent, boolean showErrorTip) {
        if (context == null || intent == null) {
            return false;
        }
        try {
            if (context instanceof Application) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            AliveLogUtils.d(TAG, e.getMessage());
            if (showErrorTip) {
                Toast.makeText(context, " 打开失败", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }
}
