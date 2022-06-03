package com.yc.apploglib.config;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.yc.apploglib.printer.AbsPrinter;

import java.util.concurrent.CopyOnWriteArrayList;

import static android.util.Log.ASSERT;
import static android.util.Log.DEBUG;
import static android.util.Log.ERROR;
import static android.util.Log.INFO;
import static android.util.Log.VERBOSE;
import static android.util.Log.WARN;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     email : yangchong211@163.com
 *     time  : 2018/11/9
 *     desc  : log日志分发
 *     revise:
 * </pre>
 */
public final class LogDispatcher {

    public static final int OFF = Integer.MAX_VALUE;
    private volatile int sMinLogLevel = OFF;
    private final CopyOnWriteArrayList<AbsPrinter> sPrinters = new CopyOnWriteArrayList<>();
    private volatile boolean hasPrinters = false;

    void setMinLogLevel(int minLogLevel) {
        sMinLogLevel = minLogLevel;
    }

    private void updateHasPrinter() {
        hasPrinters = !sPrinters.isEmpty();
    }

    @VisibleForTesting
    public void reset() {
        sMinLogLevel = OFF;
        sPrinters.clear();
        updateHasPrinter();
    }

    public boolean addPrinter(@NonNull AbsPrinter printer) {
        try {
            return sPrinters.addIfAbsent(printer);
        } finally {
            updateHasPrinter();
        }
    }

    // for test.
    boolean removePrinter(@NonNull AbsPrinter printer) {
        try {
            return sPrinters.remove(printer);
        } finally {
            updateHasPrinter();
        }
    }

    boolean hasPrinter(@NonNull String name) {
        for (AbsPrinter printer : sPrinters) {
            if (printer.name().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 这个是普通的日志
     *
     * @param level
     * @param tag
     * @param msg
     * @param tr
     */
    private void println(int level, String tag, String msg, Throwable tr) {
        for (AbsPrinter printer : sPrinters) {
            printer.println(level, tag, msg, tr);
        }
    }

    /**
     * 这个是带有format格式化的日志
     *
     * @param level
     * @param tag
     * @param format
     * @param tr
     * @param args
     */
    private void println(int level, String tag, String format, Throwable tr, Object... args) {
        final String msg = genMsg(format, args);
        for (AbsPrinter printer : sPrinters) {
            printer.println(level, tag, msg, tr);
        }
    }

    private String genMsg(String format, Object... args) {
        String message;
        if (args != null && args.length > 0) {
            message = String.format(format, args);
        } else {
            message = format;
        }
        return message;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isLoggable(int level) {
        return level >= sMinLogLevel && hasPrinters;
    }

    /**
     * verbose. lowest level.
     */
    public void v(String tag, String message) {
        if (!isLoggable(VERBOSE)) {
            return;
        }
        println(VERBOSE, tag, message, null);
    }

    public void v(String tag, String format, Object... args) {
        if (!isLoggable(VERBOSE)) {
            return;
        }

        println(VERBOSE, tag, format, null, args);
    }

    public void v(String tag, String format, Throwable tr, Object... args) {
        if (!isLoggable(VERBOSE)) {
            return;
        }

        println(VERBOSE, tag, format, tr, args);
    }


    /**
     * DEBUG.
     * 发布版手动创建日志目录，不会记录到文件
     */
    public void d(String tag, String message) {
        if (!isLoggable(DEBUG)) {
            return;
        }
        println(DEBUG, tag, message, null);
    }

    public void d(String tag, String format, Object... args) {
        if (!isLoggable(DEBUG)) {
            return;
        }
        println(DEBUG, tag, format, null, args);
    }

    public void d(String tag, String format, Throwable tr, Object... args) {
        if (!isLoggable(DEBUG)) {
            return;
        }
        println(DEBUG, tag, format, tr, args);
    }

    /**
     * INFO.
     */
    public void i(String tag, String message) {
        if (!isLoggable(INFO)) {
            return;
        }
        println(INFO, tag, message, null);
    }

    public void i(String tag, String format, Object... args) {
        if (!isLoggable(INFO)) {
            return;
        }
        println(INFO, tag, format, null, args);
    }

    public void i(String tag, String format, Throwable tr, Object... args) {
        if (!isLoggable(INFO)) {
            return;
        }
        println(INFO, tag, format, tr, args);
    }

    /**
     * WARN.
     */
    public void w(String tag, String message) {
        if (!isLoggable(WARN)) {
            return;
        }
        println(WARN, tag, message, null);
    }

    public void w(String tag, String format, Object... args) {
        if (!isLoggable(WARN)) {
            return;
        }
        println(WARN, tag, format, null, args);
    }

    public void w(String tag, String format, Throwable tr, Object... args) {
        if (!isLoggable(WARN)) {
            return;
        }
        println(WARN, tag, format, tr, args);
    }

    public void w(String tag, Throwable tr) {
        if (!isLoggable(WARN)) {
            return;
        }
        println(WARN, tag, "warn:", tr);
    }

    /**
     * ERROR.
     */
    public void e(String tag, String message) {
        if (!isLoggable(ERROR)) {
            return;
        }
        println(ERROR, tag, message, null);
    }

    public void e(String tag, String format, Object... args) {
        if (!isLoggable(ERROR)) {
            return;
        }
        println(ERROR, tag, format, null, args);
    }

    public void e(String tag, String format, Throwable tr, Object... args) {
        if (!isLoggable(ERROR)) {
            return;
        }
        println(ERROR, tag, format, tr, args);
    }

    public void e(String tag, Throwable e) {
        if (!isLoggable(ERROR)) {
            return;
        }
        println(ERROR, tag, "error:", e);
    }

    /**
     * ASSERT
     */
    public void wtf(String tag, String message) {
        if (!isLoggable(ASSERT)) {
            return;
        }
        println(ASSERT, tag, message, null);
    }

    public void wtf(String tag, String format, Object... args) {
        if (!isLoggable(ASSERT)) {
            return;
        }
        println(ASSERT, tag, format, null, args);
    }

    public void wtf(String tag, String format, Throwable tr, Object... args) {
        if (!isLoggable(ASSERT)) {
            return;
        }
        println(ASSERT, tag, format, tr, args);
    }

    public void wtf(String tag, Throwable tr) {
        if (!isLoggable(ASSERT)) {
            return;
        }
        println(ASSERT, tag, "wtf:", tr);
    }
}

