package com.yc.apploglib;

import android.util.Log;

import androidx.annotation.NonNull;

public class LoggerFactory {
    final static LogDispatcher sLogDispatcher = new LogDispatcher();

    public static void init(@NonNull LogConfig config) {
        if (config.isEnableDbgLog()) {
            sLogDispatcher.addPrinter(new LogcatPrinter());
        }

        sLogDispatcher.setMinLogLevel(config.getMinLogLevel());
    }

    public static Logger getLogger(String tag) {
        return new DefaultLogger(tag, sLogDispatcher);
    }

    public static boolean addPrinter(@NonNull Printer printer) {
        return sLogDispatcher.addPrinter(printer);
    }

    public static void removePrinter(@NonNull Printer printer) {
        sLogDispatcher.removePrinter(printer);
    }

    public static boolean hasPrinter(@NonNull String printerName) {
        return sLogDispatcher.hasPrinter(printerName);
    }

    public static void enableLogcatPrinter() {
        enableLogcatPrinter(Log.WARN);
    }

    public static void enableLogcatPrinter(int logLevel) {
        if (!sLogDispatcher.hasPrinter(LogcatPrinter.PRINTER_NAME)) {
            sLogDispatcher.addPrinter(new LogcatPrinter());
            sLogDispatcher.setMinLogLevel(logLevel);
        }
    }
}
