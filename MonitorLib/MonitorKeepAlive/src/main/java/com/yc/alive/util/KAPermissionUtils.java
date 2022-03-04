package com.yc.alive.util;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;

import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.core.app.NotificationManagerCompat;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;
import java.lang.reflect.Method;
import java.util.List;

import static android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_GENERIC;
import static android.content.Context.ACCESSIBILITY_SERVICE;


/**
 * 权限开关判断
 */
@RestrictTo(LIBRARY)
public class KAPermissionUtils {

    /**
     * 辅助功能
     */
    public static boolean isAccessibilityServiceEnabled(Context context, Class serviceClass) {
        if (context == null || serviceClass == null) {
            return false;
        }
        String serviceName = serviceClass.getName();
        String packageName = context.getPackageName();
        if (KAStringUtils.isEmpty(serviceName) || KAStringUtils.isEmpty(packageName)) {
            return false;
        }
        AccessibilityManager accessibilityManager =
            (AccessibilityManager) context.getSystemService(ACCESSIBILITY_SERVICE);
        if (accessibilityManager == null || !accessibilityManager.isEnabled()) {
            return false;
        }
        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(FEEDBACK_GENERIC);
        if (list == null) {
            return false;
        }
        for (AccessibilityServiceInfo info : list) {
            if (info != null && info.getResolveInfo() != null) {
                ServiceInfo serviceInfo = info.getResolveInfo().serviceInfo;
                if (serviceInfo != null && packageName.equals(serviceInfo.packageName)
                    && serviceName.equals(serviceInfo.name)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 悬浮窗
     */
    public static boolean isFloatWindowEnabled(Context context) {
        if (context == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return checkOps(context);
        } else {
            // 默认都有
            return true;
        }
    }

    /**
     * 通知
     */
    public static boolean isNotificationEnabled(Context context) {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }

    /**
     * wifi 休眠
     */
    public static boolean isWifiNeverSleepEnabled(Context context) {
        try {
            int value = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                value = Settings.System.getInt(context.getContentResolver(), Settings.Global.WIFI_SLEEP_POLICY);
            }
            if (value == Settings.Global.WIFI_SLEEP_POLICY_NEVER) {
                return true;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static boolean checkOps(Context context) {
        try {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = 24;
            arrayOfObject1[1] = Binder.getCallingUid();
            arrayOfObject1[2] = context.getPackageName();
            int m = (Integer) method.invoke(object, arrayOfObject1);
            // 4.4至6.0之间的非国产手机，例如samsung，sony一般都可以直接添加悬浮窗
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
