package com.yc.alive.util;

import android.os.Build;
import androidx.annotation.RestrictTo;

import java.lang.reflect.Method;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;

/**
 * 获取系统属性
 * <p>
 * 通过反射调用系统的方法 {@link android.os.Build#MODEL}
 * <p>
 *  2018/5/18.
 */
@RestrictTo(LIBRARY)
public class KASystemPropertiesUtils {

    private static final String TAG = "KASystemPropertiesUtils";

    private static final String CLASS_NAME = "android.os.SystemProperties";
    public static final String UNKNOWN = "unknown";

    private static final String BUILD_OPPO_VERSION_NAME = "ro.build.version.opporom";

    private static final String BUILD_VIVO_VERSION_NAME = "ro.vivo.os.name";
    private static final String BUILD_VIVO_VERSION_CODE = "ro.vivo.os.version";

    private static final String BUILD_EMUI_VERSION_NAME = "ro.build.version.emui";
    private static final String BUILD_EMUI_VERSION_CODE = "ro.build.hw_emui_api_level";

    private static final String BUILD_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String BUILD_MIUI_VERSION_CODE = "ro.miui.ui.version.code";

    // TODO
    private static final String BUILD_MEIZU_VERSION_CODE = "ro.build.version.opporom";
    private static final String BUILD_MEIZU_VERSION_NAME = "ro.build.version.opporom";

    private static final String BUILD_SMARTISAN_VERSION_NAME = "ro.smartisan.version";

    private static final Class<?> sClass = getClass(CLASS_NAME);
    private static final Method sStringMethod = getStringMethod(sClass);
    private static final Method sIntMethod = getIntMethod(sClass);

    public static final String BUILD_MANUFACTURER = Build.MANUFACTURER;
    public static final String BUILD_MODEL = Build.MODEL;
    public static final int BUILD_SDK_INT = Build.VERSION.SDK_INT;
    public static final String BUILD_SDK_STR = Build.VERSION.RELEASE;

    public static final String ROM_OPPO_VERSION_NAME = get(BUILD_OPPO_VERSION_NAME, UNKNOWN);

    public static final String ROM_VIVO_VERSION_NAME = get(BUILD_VIVO_VERSION_NAME, UNKNOWN);
    public static final String ROM_VIVO_VERSION_CODE = get(BUILD_VIVO_VERSION_CODE, UNKNOWN);

    // EmotionUI_5.0.1
    public static final String ROM_EMUI_VERSION_NAME = get(BUILD_EMUI_VERSION_NAME, UNKNOWN);
    // 11
    public static final int ROM_EMUI_VERSION_CODE = getInt(BUILD_EMUI_VERSION_CODE, 0);

    // V9
    public static final String ROM_MIUI_VERSION_NAME = get(BUILD_MIUI_VERSION_NAME, UNKNOWN);
    // 7
    public static final int ROM_MIUI_VERSION_CODE = getInt(BUILD_MIUI_VERSION_CODE, 0);

    public static final String ROM_SMARTISAN_VERSION_NAME = get(BUILD_SMARTISAN_VERSION_NAME, UNKNOWN);

    private static String get(String key, String def) {
        if (sStringMethod == null) {
            return def;
        }
        try {
            return (String) sStringMethod.invoke(null, key, def);
        } catch (Exception e) {
            AliveLogUtils.d(TAG, "get Exception " + e.getMessage());
        }

        return def;
    }

    private static int getInt(String key, int def) {
        if (sIntMethod == null) {
            return def;
        }

        try {
            return (int) sIntMethod.invoke(null, key, def);
        } catch (Exception e) {
            AliveLogUtils.d(TAG, "getInt Exception " + e.getMessage());
        }
        return def;
    }

    private static Class<?> getClass(String name) {
        try {
            Class<?> cls = Class.forName(name);
            if (cls == null) {
                throw new ClassNotFoundException();
            }
            return cls;
        } catch (ClassNotFoundException e) {
            try {
                return ClassLoader.getSystemClassLoader().loadClass(name);
            } catch (ClassNotFoundException e1) {
                AliveLogUtils.d(TAG, "getClass ClassNotFoundException " + e1.getMessage());
                return null;
            }
        }
    }

    private static Method getStringMethod(Class<?> clz) {
        if (clz == null) {
            return null;
        }
        try {
            return clz.getMethod("get", String.class, String.class);
        } catch (Exception e) {
            AliveLogUtils.d(TAG, "getClass getStringMethod " + e.getMessage());
            return null;
        }
    }

    private static Method getIntMethod(Class<?> clz) {
        if (clz == null) {
            return null;
        }
        try {
            return clz.getMethod("getInt", String.class, int.class);
        } catch (Exception e) {
            AliveLogUtils.d(TAG, "getClass getIntMethod " + e.getMessage());
            return null;
        }
    }
}
