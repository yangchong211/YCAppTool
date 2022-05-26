package com.yc.apploglib;

import com.yc.apploglib.config.AppLogFactory;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     email : yangchong211@163.com
 *     time  : 2018/11/9
 *     desc  : 日志帮助类
 *     revise:
 * </pre>
 */
public final class AppLogHelper {

    /**
     * Verbose
     */
    public static void v(String format, Object... args) {
        v(AppLogFactory.getAppLogConfig().getTag(), format, args);
    }

    public static void v(String tag, String format, Object... args) {
        AppLogFactory.sLogDispatcher.v(tag, format, args);
    }

    public static void v(String tag, Throwable tr, String format, Object... args) {
        AppLogFactory.sLogDispatcher.v(tag, format, tr, args);
    }

    /**
     * Debug
     */
    public static void d(String format, Object... args) {
        d(AppLogFactory.getAppLogConfig().getTag(), format, args);
    }

    public static void d(String tag, String format, Object... args) {
        AppLogFactory.sLogDispatcher.d(tag, format, args);
    }

    public static void d(String tag, Throwable tr, String format, Object... args) {
        AppLogFactory.sLogDispatcher.d(tag, format, tr, args);
    }

    /**
     * INFO.
     */
    public static void i(String format, Object... args) {
        i(AppLogFactory.getAppLogConfig().getTag(), format, args);
    }

    public static void i(String tag, String format, Object... args) {
        AppLogFactory.sLogDispatcher.i(tag, format, args);
    }

    public static void i(String tag, Throwable tr, String format, Object... args) {
        AppLogFactory.sLogDispatcher.i(tag, format, tr, args);
    }

    /**
     * WARN.
     */
    public static void w(String format, Object... args) {
        w(AppLogFactory.getAppLogConfig().getTag(), format, args);
    }

    public static void w(String tag, String format, Object... args) {
        AppLogFactory.sLogDispatcher.w(tag, format, args);
    }

    public static void w(String tag, Throwable tr, String format, Object... args) {
        AppLogFactory.sLogDispatcher.w(tag, format, tr, args);
    }

    public static void w(String tag, Throwable tr) {
        AppLogFactory.sLogDispatcher.w(tag, tr);
    }

    /**
     * ERROR.
     */
    public static void e(String format, Object... args) {
        e(AppLogFactory.getAppLogConfig().getTag(), format, args);
    }

    public static void e(String tag, String format, Object... args) {
        AppLogFactory.sLogDispatcher.e(tag, format, args);
    }

    public static void e(String tag, Throwable tr, String format, Object... args) {
        AppLogFactory.sLogDispatcher.e(tag, format, tr, args);
    }

    public static void e(String tag, Throwable tr) {
        AppLogFactory.sLogDispatcher.e(tag, tr);
    }

    /**
     * ASSERT
     */
    public static void wtf(String tag, String format, Object... args) {
        AppLogFactory.sLogDispatcher.wtf(tag, format, args);
    }

    public static void wtf(String tag, Throwable tr, String format, Object... args) {
        AppLogFactory.sLogDispatcher.wtf(tag, format, tr, args);
    }

    public static void wtf(String tag, Throwable tr) {
        AppLogFactory.sLogDispatcher.wtf(tag, tr);
    }
}

