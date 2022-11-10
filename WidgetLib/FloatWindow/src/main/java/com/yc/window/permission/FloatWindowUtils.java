package com.yc.window.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/08/18
 *     desc  : 悬浮工具类
 *     revise:
 * </pre>
 */
public final class FloatWindowUtils {

    /**
     * 悬浮窗权限（特殊权限）
     *
     * 在 Android 10 及之前的版本能跳转到应用悬浮窗设置页面，而在 Android 11 及之后的版本只能跳转到系统设置悬浮窗管理列表了
     * 官方解释：https://developer.android.google.cn/reference/android/provider/Settings#ACTION_MANAGE_OVERLAY_PERMISSION
     */
    public static final String SYSTEM_ALERT_WINDOW = "android.permission.SYSTEM_ALERT_WINDOW";

    /**
     * 当API Level>=23的时候就要动态的申请权限了，判断是否能够绘制悬浮窗
     * @param context           上下文
     * @return                  返回为true就表明已同意权限，否则就表示没有全局绘制的权限。
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean hasPermission(Context context) {
        return Settings.canDrawOverlays(context);
    }

    /**
     * 跳转设置中心
     * @param activity          上下文
     */
    public static void goToSetting(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = activity.getPackageName();
            Uri uri = Uri.parse("package:" + packageName);
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri);
            activity.startActivityForResult(intent, 1);
        }
    }

}
