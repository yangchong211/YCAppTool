package com.yc.apploglib;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import java.util.concurrent.CopyOnWriteArrayList;

import static android.util.Log.ASSERT;
import static android.util.Log.DEBUG;
import static android.util.Log.ERROR;
import static android.util.Log.INFO;
import static android.util.Log.VERBOSE;
import static android.util.Log.WARN;

class LogDispatcher {
    static final int OFF = Integer.MAX_VALUE;

    private volatile int sMinLogLevel = OFF;
    private final CopyOnWriteArrayList<Printer> sPrinters = new CopyOnWriteArrayList<>();
    private volatile boolean hasPrinters = false;

    void setMinLogLevel(int minLogLevel) {
        sMinLogLevel = minLogLevel;
    }

    private void updateHasPrinter() {
        hasPrinters = !sPrinters.isEmpty();
    }

    @VisibleForTesting
    void reset() {
        sMinLogLevel = OFF;
        sPrinters.clear();
        updateHasPrinter();
    }

    boolean addPrinter(@NonNull Printer printer) {
        try {
            return sPrinters.addIfAbsent(printer);
        } finally {
            updateHasPrinter();
        }
    }

    // for test.
    boolean removePrinter(@NonNull Printer printer) {
        try {
            return sPrinters.remove(printer);
        } finally {
            updateHasPrinter();
        }
    }

    boolean hasPrinter(@NonNull String name) {
        for (Printer printer : sPrinters) {
            if (printer.name().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private void println(
            int level, String tag, String format, Throwable tr, Object... args) {
        final String msg = genMsg(format, args);

        for (Printer printer : sPrinters) {
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

