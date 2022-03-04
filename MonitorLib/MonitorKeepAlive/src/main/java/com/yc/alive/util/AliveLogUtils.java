package com.yc.alive.util;

import androidx.annotation.RestrictTo;

import com.yc.alive.KeepAliveHelper;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;


/**
 * Log 工具
 *
 */
@RestrictTo(LIBRARY)
public class AliveLogUtils {

    public static boolean ENABLE = false;

    public static KeepAliveHelper.Logger logger;

    public static void d(String tag, String message) {
        if (!ENABLE) {
            return;
        }
        if (logger != null) {
            logger.log("[KAAssistant] " + tag, message);
        }
    }
}
