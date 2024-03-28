package com.yc.mmaplog.test;

import android.content.Context;

import com.yc.mmaplog.LogManager;
import com.yc.mmaplog.appender.AndroidAppender;
import com.yc.mmaplog.appender.FileAppender;
import com.yc.mmaplog.formatter.DateFileFormatter;
import com.yc.mmaplog.inter.Interceptor;
import com.yc.mmaplog.logger.AppenderLogger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.yc.mmaplog.LogLevel;


public class LogInit {

    public static final int BUFFER_SIZE = 1024 * 400; //400k

    public static void init(Context context) {
        int level = LogLevel.DEBUG;
        Interceptor wrapInterceptor = logData -> true;
        AndroidAppender androidAppender = new AndroidAppender.Builder()
                .setLevel(level)
                .addInterceptor(wrapInterceptor)
                .create();

        File log = getLogDir(context);
        String buffer_path = log.getAbsolutePath() + File.separator + ".logCache";
        String time = new SimpleDateFormat("yyyy_MM_dd", Locale.getDefault()).format(new Date());
        String log_path = log.getAbsolutePath() + File.separator + time + ".txt";
        FileAppender fileAppender = new FileAppender.Builder(context)
                .setLogFilePath(log_path)
                .setLevel(level)
                .addInterceptor(wrapInterceptor)
                .setBufferFilePath(buffer_path)
                .setFormatter(new DateFileFormatter())
                .setCompress(false)
                .setBufferSize(BUFFER_SIZE)
                .create();

        AppenderLogger logger = new AppenderLogger.Builder()
                .addAppender(androidAppender)
                .addAppender(fileAppender)
                .create();
        LogManager.setLogger(logger);
    }

    public static File getLogDir(Context context) {
        File log = context.getExternalFilesDir("logs");
        if (log == null) {
            log = new File(context.getFilesDir(), "logs");
        }
        if (!log.exists()) {
            log.mkdir();
        }
        return log;
    }
}
