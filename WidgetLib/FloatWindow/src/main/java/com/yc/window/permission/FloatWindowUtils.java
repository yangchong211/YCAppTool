package com.yc.window.permission;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;


public final class FloatWindowUtils {

    /**
     * 悬浮窗权限（特殊权限）
     *
     * 在 Android 10 及之前的版本能跳转到应用悬浮窗设置页面，而在 Android 11 及之后的版本只能跳转到系统设置悬浮窗管理列表了
     * 官方解释：https://developer.android.google.cn/reference/android/provider/Settings#ACTION_MANAGE_OVERLAY_PERMISSION
     */
    public static final String SYSTEM_ALERT_WINDOW = "android.permission.SYSTEM_ALERT_WINDOW";


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean hasPermission(Context context) {
        return Settings.canDrawOverlays(context);
    }

}
