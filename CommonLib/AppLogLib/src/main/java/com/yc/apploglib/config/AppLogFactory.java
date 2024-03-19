package com.yc.apploglib.config;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.yc.toolutils.context.AppToolUtils;
import com.yc.toolutils.file.AppFileUtils;
import com.yc.apploglib.log.DefaultLoggerImpl;
import com.yc.apploglib.log.InterLogger;
import com.yc.apploglib.printer.AbsPrinter;
import com.yc.apploglib.printer.FilePrinterImpl;
import com.yc.apploglib.printer.LogcatPrinterImpl;

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

    public final static LogDispatcher S_LOG_DISPATCHER = new LogDispatcher();
    private static AppLogConfig appLogConfig;

    public static void init(@NonNull AppLogConfig config) {
        appLogConfig = config;
        if (config.isEnableDbgLog()) {
            S_LOG_DISPATCHER.addPrinter(new LogcatPrinterImpl());
        }
        if (config.isWriteFile()){
            File file;
            if (TextUtils.isEmpty(config.getFilePath())){
                String log = AppFileUtils.getExternalFilePath(AppToolUtils.getApp(), "ycLog");
                file = new File(log);
            } else {
                file = new File(config.getFilePath());
            }
            Log.d("AppLog" , "file path" + file.getPath());
            S_LOG_DISPATCHER.addPrinter(new FilePrinterImpl(file));
        }
        S_LOG_DISPATCHER.setMinLogLevel(config.getMinLogLevel());
    }

    public static InterLogger getLogger(String tag) {
        return new DefaultLoggerImpl(tag, S_LOG_DISPATCHER);
    }

    public static boolean addPrinter(@NonNull AbsPrinter printer) {
        return S_LOG_DISPATCHER.addPrinter(printer);
    }

    public static void removePrinter(@NonNull AbsPrinter printer) {
        S_LOG_DISPATCHER.removePrinter(printer);
    }

    public static boolean hasPrinter(@NonNull String printerName) {
        return S_LOG_DISPATCHER.hasPrinter(printerName);
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
        if (!S_LOG_DISPATCHER.hasPrinter(LogcatPrinterImpl.PRINTER_NAME)) {
            S_LOG_DISPATCHER.addPrinter(new LogcatPrinterImpl());
            S_LOG_DISPATCHER.setMinLogLevel(logLevel);
        }
    }
}
