package com.yc.logging.config;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import androidx.annotation.RestrictTo;
import android.text.TextUtils;
import com.yc.logging.annotation.KeepSource;
import com.yc.logging.util.Debug;

import java.io.File;


@RestrictTo(RestrictTo.Scope.LIBRARY)
@KeepSource
public class LoggerContext {

    private String mPackageName;
    private File mLogPathDir;
    private File mDefaultLogPathDir;
    private File mLogCacheDir;
    private File mAppRootDir;
    private boolean mInitial;
    private boolean mAppDebuggable;

    private static LoggerContext sDefault;

    private LoggerContext() {
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    public synchronized void init(Context context) {
        if (mInitial) return;
        mInitial = true;
        mPackageName = context.getPackageName();
        File filesDir = context.getFilesDir();
        mAppRootDir = filesDir.getParentFile();
        mDefaultLogPathDir = new File(filesDir, "logging");
        if (!mDefaultLogPathDir.exists()) {
            mDefaultLogPathDir.mkdirs();
        }
        mLogCacheDir = new File(filesDir, "logging-cache");
        if (!mLogCacheDir.exists()) {
            mLogCacheDir.mkdirs();
        }
        mAppDebuggable = (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    public synchronized void update(LoggerConfig config) {
        File logDir = config.getLogDir();
        if (logDir != null) {
            mLogPathDir = logDir;
        }
    }

    public static LoggerContext getDefault() {
        if (sDefault == null) {
            sDefault = new LoggerContext();
        }
        return sDefault;
    }

    public boolean isInitial() {
        return mInitial;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public File getSecondaryLogPathDir() {
        if (mLogPathDir == null) {
            return null;
        }
        if (TextUtils.equals(mLogPathDir.getPath(), mDefaultLogPathDir.getPath())) {
            return null;
        }
        return mDefaultLogPathDir;
    }

    public synchronized File getMainLogPathDir() {
        if (mLogPathDir == null) {
            return mDefaultLogPathDir;
        }
        try {
            if (!mLogPathDir.exists()) {
                boolean success = mLogPathDir.mkdirs();
                if (!success) {
                    return mDefaultLogPathDir;
                }
            }

            if (!mLogPathDir.exists()) {
                return mDefaultLogPathDir;
            }

            if (!mLogPathDir.canWrite() || !mLogPathDir.canRead()) {
                return mDefaultLogPathDir;
            }
        } catch (Exception e) {
            Debug.logOrThrow("check log dir " + mLogPathDir + "failed", e);
        }

        return mLogPathDir;
    }

    public File getLogCacheDir() {
        return mLogCacheDir;
    }

    public File getAppRootDir() {
        return mAppRootDir;
    }

    public boolean isAppDebuggable() {
        return mAppDebuggable;
    }
}
