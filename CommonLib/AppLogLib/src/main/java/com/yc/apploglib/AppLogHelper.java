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
    public static void v(String tag, Object message) {
        AppLogFactory.S_LOG_DISPATCHER.v(tag, message.toString());
    }

    public static void v(String format, Object... args) {
        v(AppLogFactory.getAppLogConfig().getTag(), format, args);
    }

    public static void v(String tag, String format, Object... args) {
        AppLogFactory.S_LOG_DISPATCHER.v(tag, format, args);
    }

    public static void v(String tag, Throwable tr, String format, Object... args) {
        AppLogFactory.S_LOG_DISPATCHER.v(tag, format, tr, args);
    }

    /**
     * Debug
     */
    public static void d(String tag, Object message) {
        AppLogFactory.S_LOG_DISPATCHER.d(tag, message.toString());
    }

    public static void d(String format, Object... args) {
        d(AppLogFactory.getAppLogConfig().getTag(), format, args);
    }

    public static void d(String tag, String format, Object... args) {
        AppLogFactory.S_LOG_DISPATCHER.d(tag, format, args);
    }

    public static void d(String tag, Throwable tr, String format, Object... args) {
        AppLogFactory.S_LOG_DISPATCHER.d(tag, format, tr, args);
    }

    /**
     * INFO.
     */
    public static void i(String tag, Object message) {
        AppLogFactory.S_LOG_DISPATCHER.i(tag, message.toString());
    }

    public static void i(String format, Object... args) {
        i(AppLogFactory.getAppLogConfig().getTag(), format, args);
    }

    public static void i(String tag, String format, Object... args) {
        AppLogFactory.S_LOG_DISPATCHER.i(tag, format, args);
    }

    public static void i(String tag, Throwable tr, String format, Object... args) {
        AppLogFactory.S_LOG_DISPATCHER.i(tag, format, tr, args);
    }

    /**
     * WARN.
     */
    public static void w(String tag, Object message) {
        AppLogFactory.S_LOG_DISPATCHER.w(tag, message.toString());
    }

    public static void w(String format, Object... args) {
        w(AppLogFactory.getAppLogConfig().getTag(), format, args);
    }

    public static void w(String tag, String format, Object... args) {
        AppLogFactory.S_LOG_DISPATCHER.w(tag, format, args);
    }

    public static void w(String tag, Throwable tr, String format, Object... args) {
        AppLogFactory.S_LOG_DISPATCHER.w(tag, format, tr, args);
    }

    public static void w(String tag, Throwable tr) {
        AppLogFactory.S_LOG_DISPATCHER.w(tag, tr);
    }

    /**
     * ERROR.
     */
    public static void e(String tag, Object message) {
        AppLogFactory.S_LOG_DISPATCHER.e(tag, message.toString());
    }

    public static void e(String format, Object... args) {
        e(AppLogFactory.getAppLogConfig().getTag(), format, args);
    }

    public static void e(String tag, String format, Object... args) {
        AppLogFactory.S_LOG_DISPATCHER.e(tag, format, args);
    }

    public static void e(String tag, Throwable tr, String format, Object... args) {
        AppLogFactory.S_LOG_DISPATCHER.e(tag, format, tr, args);
    }

    public static void e(String tag, Throwable tr) {
        AppLogFactory.S_LOG_DISPATCHER.e(tag, tr);
    }

    /**
     * ASSERT
     */
    public static void wtf(String tag, Object message) {
        AppLogFactory.S_LOG_DISPATCHER.wtf(tag, message.toString());
    }

    public static void wtf(String tag, String format, Object... args) {
        AppLogFactory.S_LOG_DISPATCHER.wtf(tag, format, args);
    }

    public static void wtf(String tag, Throwable tr, String format, Object... args) {
        AppLogFactory.S_LOG_DISPATCHER.wtf(tag, format, tr, args);
    }

    public static void wtf(String tag, Throwable tr) {
        AppLogFactory.S_LOG_DISPATCHER.wtf(tag, tr);
    }
}

