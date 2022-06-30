package com.yc.logging;

import android.app.Application;
import android.content.Context;

import com.yc.logging.config.LoggerConfig;
import com.yc.logging.config.LoggerContext;
import com.yc.logging.logger.Logger;
import com.yc.logging.logger.InternalLogger;
import com.yc.toolutils.ObjectsUtils;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public abstract class LoggerFactory {

    private static LoggerConfig sLoggerConfig;
    @SuppressWarnings("WeakerAccess")
    private static final Map<String, WeakReference<Logger>> sCachedLoggerMap = new ConcurrentHashMap<>();
    private static boolean sInitial;

    public synchronized static void init(Context context, LoggerConfig config) {
        if (sInitial) {
            return;
        }
        sInitial = true;

        ObjectsUtils.requireNonNull(context);
        ObjectsUtils.requireNonNull(config);
        sLoggerConfig = config;
        Context appContext = context instanceof Application ? context : context.getApplicationContext();
        LoggerContext.getDefault().init(appContext);
        LoggerContext.getDefault().update(config);
    }

    public synchronized static void init2(Context context, LoggerConfig config) {
        init(context, config);
    }

    public static boolean isInitial() {
        return sInitial;
    }

    public static LoggerConfig getConfig() {
        return sLoggerConfig;
    }

    public static Logger getLogger(final Class<?> clazz) {
        ObjectsUtils.requireNonNull(clazz);
        return getLogger(clazz.getName(), "main");
    }

    public static Logger getLogger(final String name) {
        ObjectsUtils.requireNonNull(name);
        return getLogger(name, "main");
    }

    public static Logger getLogger(final Class<?> clazz, String bufferId) {
        ObjectsUtils.requireNonNull(clazz);
        ObjectsUtils.requireNonNull(bufferId);
        return internalGetLogger(clazz.getName(), bufferId);
    }

    public static Logger getLogger(final String name, String bufferId) {
        ObjectsUtils.requireNonNull(name);
        ObjectsUtils.requireNonNull(bufferId);
        return internalGetLogger(name, bufferId);
    }

    private static Logger internalGetLogger(final String name, String bufferId) {
        Logger logger;
        String key = name + "-" + bufferId;
        WeakReference<Logger> cachedRef = sCachedLoggerMap.get(key);
        if (cachedRef != null) {
            logger = cachedRef.get();
            if (logger != null) {
                return logger;
            }
        }

        Set<Map.Entry<String, WeakReference<Logger>>> entries = sCachedLoggerMap.entrySet();
        for (Map.Entry<String, WeakReference<Logger>> en : entries) {
            WeakReference<Logger> value = en.getValue();
            if (value == null || value.get() == null) {
                sCachedLoggerMap.remove(en.getKey());
            }
        }

        logger = new InternalLogger(name, bufferId, sLoggerConfig);
        WeakReference<Logger> ref = new WeakReference<>(logger);

        sCachedLoggerMap.put(key, ref);
        return logger;
    }

    private LoggerFactory() {
    }
}
