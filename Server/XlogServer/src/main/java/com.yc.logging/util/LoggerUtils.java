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
import com.yc.logging.annotation.KeepSource;

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
@KeepSource
public class LoggerUtils {

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private static final CachingDateFormatter CACHING_DATE_FORMATTER = new CachingDateFormatter(DATE_PATTERN);

    private static final Pattern DEFAULT_LOG_FILE_NAME_REGEX_PATTERN =
            Pattern.compile("^(logback|binary)-(\\w+)-(\\d{4}-\\d{2}-\\d{2})-(\\d{1,3})\\.log");


    private static ThreadLocal<DateFormat> sDateFormatThreadLocal = new ThreadLocal<DateFormat>() {
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
                if (file.isDirectory()) return false;
                if (file.length() == 0) return false;

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
            catchFiles.addAll(FileUtils.collectAllFiles(dir));
        }
        return catchFiles;
    }

    public static String formatForFileName(final Date date) {
        return CACHING_DATE_FORMATTER.format(date.getTime());
    }

    public static String formatForLogHead(final Date date) {
        return sDateFormatThreadLocal.get().format(date.getTime());
    }

    public static String formatFileSize(long size) {
        DecimalFormat df = new DecimalFormat("#.00");
        String sizeString;
        String wrongSize = "0B";
        if (size == 0) {
            return wrongSize;
        }
        if (size < 1024) {
            sizeString = df.format((double) size) + "B";
        } else if (size < 1048576) {
            sizeString = df.format((double) size / 1024) + "KB";
        } else if (size < 1073741824) {
            sizeString = df.format((double) size / 1048576) + "MB";
        } else {
            sizeString = df.format((double) size / 1073741824) + "GB";
        }
        return sizeString;
    }

    public static String currentProcessName(Context context) {
        String processName = null;
        if (Build.VERSION.SDK_INT >= 28) {
            processName = Application.getProcessName();
        } else {
            // Using the same technique as Application.getProcessName() for older devices
            // Using reflection since ActivityThread is an internal API
            try {
                @SuppressLint("PrivateApi")
                Class<?> activityThread = Class.forName("android.app.ActivityThread");

                // Before API 18, the method was incorrectly named "currentPackageName", but it still returned the process name
                // See https://github.com/aosp-mirror/platform_frameworks_base/commit/b57a50bd16ce25db441da5c1b63d48721bb90687
                String methodName =
                        Build.VERSION.SDK_INT >= 18 ? "currentProcessName" : "currentPackageName";

                Method getProcessName = activityThread.getDeclaredMethod(methodName);
                processName = (String) getProcessName.invoke(null);
            } catch (Exception ignore) {
            }
        }

        if (!TextUtils.isEmpty(processName)) {
            return processName;
        }

        try {
            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                    .getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        } catch (Exception ignore) {
        }
        return null;
    }

    public static String dumpBundleArray(Bundle[] bundleArray) {
        if (bundleArray == null) {
            return "null";
        }
        StringBuilder content = new StringBuilder("[");
        for (int i = 0; i < bundleArray.length; i++) {
            content.append(dumpBundle(bundleArray[i]));
            if (i < bundleArray.length - 1) {
                content.append(", ");
            }
        }
        content.append("]");
        return content.toString();
    }

    public static String dumpBundle(Bundle bundle) {
        if (bundle == null) {
            return "null";
        }
        StringBuilder content = new StringBuilder("{");
        Iterator<String> it = bundle.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            content.append(key).append("=");
            Object obj = bundle.get(key);
            String val;
            if (obj instanceof Bundle) {
                if (obj == bundle) {
                    val = "{this}";
                } else {
                    val = dumpBundle((Bundle) obj);
                }
            }//
            else if (obj instanceof Bundle[]) {
                val = dumpBundleArray((Bundle[]) obj);
            }//
            else if (obj instanceof Object[]) {
                val = Arrays.toString((Object[]) obj);
            }//
            else {
                val = String.valueOf(obj);
            }
            content.append(val);
            if (it.hasNext()) {
                content.append(", ");
            }
        }
        content.append("}");
        return content.toString();
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }
}
