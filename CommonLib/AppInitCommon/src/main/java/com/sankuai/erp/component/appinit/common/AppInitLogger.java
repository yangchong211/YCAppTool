package com.sankuai.erp.component.appinit.common;


public final class AppInitLogger {
    public static ILogger sLogger;

    private AppInitLogger() {
    }

    private static boolean isNotDebug() {
        if (sLogger == null) {
            return true;
        }
        return !sLogger.isDebug();
    }

    public static void demo(String msg) {
        if (isNotDebug()) {
            return;
        }
        sLogger.demo(msg);
    }

    public static void d(String msg) {
        if (isNotDebug()) {
            return;
        }
        sLogger.d(msg);
    }

    public static void e(String msg) {
        if (isNotDebug()) {
            return;
        }
        sLogger.e(msg);
    }
}
