package com.yc.apploglib.config;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.yc.appfilelib.AppFileUtils;
import com.yc.apploglib.log.DefaultLoggerImpl;
import com.yc.apploglib.log.InterLogger;
import com.yc.apploglib.printer.AbsPrinter;
import com.yc.apploglib.printer.FilePrinterImpl;
import com.yc.apploglib.printer.LogcatPrinterImpl;
import com.yc.toolutils.AppToolUtils;

import java.io.File;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     email : yangchong211@163.com
 *     time  : 2018/11/9
 *     desc  : log日志工厂累
 *     revise:
 * </pre>
 */
public final class AppLogFactory {

    public final static LogDispatcher sLogDispatcher = new LogDispatcher();
    private static AppLogConfig appLogConfig;

    public static void init(@NonNull AppLogConfig config) {
        appLogConfig = config;
        if (config.isEnableDbgLog()) {
            sLogDispatcher.addPrinter(new LogcatPrinterImpl());
        }
        if (config.isWriteFile()){
            File file;
            if (TextUtils.isEmpty(config.getFilePath())){
                String log = AppFileUtils.getExternalFilePath(AppToolUtils.getApp(), "ycLog");
                file = new File(log);
            } else {
                file = new File(config.getFilePath());
            }
            sLogDispatcher.addPrinter(new FilePrinterImpl(file));
        }
        sLogDispatcher.setMinLogLevel(config.getMinLogLevel());
    }

    public static InterLogger getLogger(String tag) {
        return new DefaultLoggerImpl(tag, sLogDispatcher);
    }

    public static boolean addPrinter(@NonNull AbsPrinter printer) {
        return sLogDispatcher.addPrinter(printer);
    }

    public static void removePrinter(@NonNull AbsPrinter printer) {
        sLogDispatcher.removePrinter(printer);
    }

    public static boolean hasPrinter(@NonNull String printerName) {
        return sLogDispatcher.hasPrinter(printerName);
    }

    public static void enableLogcatPrinter() {
        enableLogcatPrinter(Log.WARN);
    }

    public static AppLogConfig getAppLogConfig(){
        if (appLogConfig == null){
            throw new NullPointerException("please init log at first");
        }
        return appLogConfig;
    }

    public static void enableLogcatPrinter(int logLevel) {
        if (!sLogDispatcher.hasPrinter(LogcatPrinterImpl.PRINTER_NAME)) {
            sLogDispatcher.addPrinter(new LogcatPrinterImpl());
            sLogDispatcher.setMinLogLevel(logLevel);
        }
    }
}
