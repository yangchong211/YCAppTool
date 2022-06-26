package com.yc.logging.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import androidx.annotation.RestrictTo;
import android.text.TextUtils;

import com.yc.toolutils.file.AppFileUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class LoggerUtils {

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private static final CachingDateFormatter CACHING_DATE_FORMATTER = new CachingDateFormatter(DATE_PATTERN);

    private static final Pattern DEFAULT_LOG_FILE_NAME_REGEX_PATTERN =
            Pattern.compile("^(logback|binary)-(\\w+)-(\\d{4}-\\d{2}-\\d{2})-(\\d{1,3})\\.log");

    /**
     * ThreadLocal是线程内部的数据存储类，通过它可以在指定线程中存储数据，其他线程则无法获取到
     */
    private static final ThreadLocal<DateFormat> sDateFormatThreadLocal = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        }
    };

    private LoggerUtils() {

    }

    public static String getNetworkType(Context application) {
        ConnectivityManager connMgr =
                (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr == null) {
            return "NONE";
        }
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            return "NONE";
        }
        return networkInfo.getTypeName().toUpperCase();
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static File[] collectLogFiles(File logDir) {
        return logDir.listFiles();
    }

    public static File[] collectLogFiles(File logDir, String buffers, Date date) {
        return collectLogFiles(logDir, null, buffers, formatForFileName(date));
    }

    public static File[] collectLogFiles(File logDir, String buffers, String date) {
        return collectLogFiles(logDir, null, buffers, date);
    }

    public static File[] collectLogFiles(File logDir, String logType, String buffers, final String date) {
        final String[] bufferArr =
                TextUtils.isEmpty(buffers) ? new String[]{} : buffers.trim().split(",");

        final Pattern fileNamePattern =
                logType == null ? DEFAULT_LOG_FILE_NAME_REGEX_PATTERN : getLogFileRegex(logType);

        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                File file = new File(dir, name);
                if (file.isDirectory()) {
                    return false;
                }
                if (file.length() == 0) {
                    return false;
                }

                Matcher m = fileNamePattern.matcher(name);
                if (!m.matches()) {
                    return false;
                }
                if (bufferArr.length == 0) {
                    return TextUtils.equals(m.group(3), date);
                } else {
                    for (String buffer : bufferArr) {
                        if (TextUtils.equals(m.group(2), buffer) && TextUtils.equals(m.group(3), date)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
        File[] files = logDir.listFiles(filter);
        return files == null ? new File[0] : files;
    }


    public static Pattern getLogFileRegex(String logType) {
        return Pattern.compile("^(" + logType + ")-(\\w+)-(\\d{4}-\\d{2}-\\d{2})-(\\d{1,3})\\.log");
    }

    public static List<File> collectLogFilesWithRange(File logDir, String buffers, String startTime, String endTime) {
        List<File> catchFiles = new ArrayList<>();
        try {
            SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());
            Date startDate = format.parse(startTime);
            Date endDate = format.parse(endTime);
            Calendar calendar = Calendar.getInstance();
            for (Date date = startDate; date.compareTo(endDate) <= 0; ) {
                File[] matchingFiles = collectLogFiles(logDir, null, buffers, format.format(date));
                if (matchingFiles != null && matchingFiles.length > 0) {
                    catchFiles.addAll(Arrays.asList(matchingFiles));
                }
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                date = calendar.getTime();
            }
        } catch (ParseException ignore) {
        }
        return catchFiles;
    }

    public static List<File> collectExtraLogFiles(File dir) {
        List<File> catchFiles = new ArrayList<>();
        if (dir != null && dir.exists() && dir.isDirectory()) {
            catchFiles.addAll(AppFileUtils.collectAllFiles(dir));
        }
        return catchFiles;
    }

    public static String formatForFileName(final Date date) {
        return CACHING_DATE_FORMATTER.format(date.getTime());
    }

    public static String formatForLogHead(final Date date) {
        DateFormat dateFormat = sDateFormatThreadLocal.get();
        if (dateFormat == null){
            return "";
        }
        return dateFormat.format(date.getTime());
    }

}
