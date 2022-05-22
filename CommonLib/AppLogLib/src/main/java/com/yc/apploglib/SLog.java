package com.yc.apploglib;

public class SLog {
    public static void v(String tag, String format, Object... args) {
        LoggerFactory.sLogDispatcher.v(tag, format, args);
    }

    public static void v(String tag, Throwable tr, String format, Object... args) {
        LoggerFactory.sLogDispatcher.v(tag, format, tr, args);
    }

    /**
     * Debug
     */
    public static void d(String tag, String format, Object... args) {
        LoggerFactory.sLogDispatcher.d(tag, format, args);
    }

    public static void d(String tag, Throwable tr, String format, Object... args) {
        LoggerFactory.sLogDispatcher.d(tag, format, tr, args);
    }

    /**
     * INFO.
     */
    public static void i(String tag, String format, Object... args) {
        LoggerFactory.sLogDispatcher.i(tag, format, args);
    }

    public static void i(String tag, Throwable tr, String format, Object... args) {
        LoggerFactory.sLogDispatcher.i(tag, format, tr, args);
    }

    /**
     * WARN.
     */
    public static void w(String tag, String format, Object... args) {
        LoggerFactory.sLogDispatcher.w(tag, format, args);
    }

    public static void w(String tag, Throwable tr, String format, Object... args) {
        LoggerFactory.sLogDispatcher.w(tag, format, tr, args);
    }

    public static void w(String tag, Throwable tr) {
        LoggerFactory.sLogDispatcher.w(tag, tr);
    }

    /**
     * ERROR.
     */
    public static void e(String tag, String format, Object... args) {
        LoggerFactory.sLogDispatcher.e(tag, format, args);
    }

    public static void e(String tag, Throwable tr, String format, Object... args) {
        LoggerFactory.sLogDispatcher.e(tag, format, tr, args);
    }

    public static void e(String tag, Throwable tr) {
        LoggerFactory.sLogDispatcher.e(tag, tr);
    }

    /**
     * ASSERT
     */
    public static void wtf(String tag, String format, Object... args) {
        LoggerFactory.sLogDispatcher.wtf(tag, format, args);
    }

    public static void wtf(String tag, Throwable tr, String format, Object... args) {
        LoggerFactory.sLogDispatcher.wtf(tag, format, tr, args);
    }

    public static void wtf(String tag, Throwable tr) {
        LoggerFactory.sLogDispatcher.wtf(tag, tr);
    }
}

