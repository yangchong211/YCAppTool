package com.yc.alive.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import com.yc.alive.constant.AliveIntentType;
import com.yc.alive.model.AliveOptionModel;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;

/**
 * App 相关工具类
 */
@RestrictTo(LIBRARY)
public class AliveAppUtils {

    private static final String PACKAGE = "package";

    /**
     * 获取应用名称
     */
    @Nullable
    public static String getAppName(Context context) {
        if (context == null) {
            return null;
        }
        try {
            return context.getPackageManager().getApplicationLabel(context.getApplicationInfo()).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 辅助功能设置页面
     */
    public static boolean openAccessibilitySettingPage(Context context) {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        return AliveIntentUtils.launch(context, intent, false);
    }

    /**
     * 打开页面
     */
    public static boolean open(Context context, AliveOptionModel option) {
        if (context == null || option == null) {
            return false;
        }
        if (option.intent == null) {
            return false;
        }
        if (option.intent.type == AliveIntentType.ACTION) {
            Intent i = new Intent();
            i.setAction(option.intent.action);
            if (option.intent.needPackage) {
                i.setData(Uri.fromParts(PACKAGE, context.getPackageName(), null));
            }
            return AliveIntentUtils.launch(context, i, false);
        } else {
            return AliveIntentUtils.launch(context, option.intent.packageName, option.intent.className, false);
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
