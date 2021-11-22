package com.yc.logging;

import android.os.Process;
import android.support.annotation.RestrictTo;
import android.text.TextUtils;
import android.util.Log;

import com.yc.logging.util.Objects;
import com.yc.logging.util.StringUtils;

import java.util.Date;
import java.util.Map;


@RestrictTo(RestrictTo.Scope.LIBRARY)
class InternalLogger extends AbstractLogger {

    private final String mBuffer;

    //private int m FileLogLevel;
    //private int m LogcatLogLevel;
    private LoggerConfig mConfig;

    InternalLogger(final String name, String buffer, LoggerConfig config) {
        super(name);
        mConfig = config;
        this.mBuffer = buffer;
    }

    @Override
    public boolean isTraceEnabled() {
        return Level.TRACE.level >= mConfig.getFileLogLevel().level || Level.TRACE.level >= mConfig
                .getLogcatLogLevel().level;
    }

    @Override
    public boolean isDebugEnabled() {
        return Level.DEBUG.level >= mConfig.getFileLogLevel().level || Level.TRACE.level >= mConfig
                .getLogcatLogLevel().level;
    }

    @Override
    public boolean isInfoEnabled() {
        return Level.INFO.level >= mConfig.getFileLogLevel().level || Level.TRACE.level >= mConfig
                .getLogcatLogLevel().level;
    }

    @Override
    public boolean isWarnEnabled() {
        return Level.WARN.level >= mConfig.getFileLogLevel().level || Level.TRACE.level >= mConfig
                .getLogcatLogLevel().level;
    }

    @Override
    public boolean isErrorEnabled() {
        return Level.ERROR.level >= mConfig.getFileLogLevel().level || Level.TRACE.level >= mConfig
                .getLogcatLogLevel().level;
    }

    @Override
    public void write(byte[] bytes) {
        Objects.requireNonNull(bytes);
        if (bytes.length == 0) return;
        BinaryExecutor.getInstance(mBuffer).enqueue(new BinaryLog(bytes.clone()));
    }

    public void trace(String msg, Throwable t) {
        println(Level.TRACE, msg, t);
    }

    public void debug(String msg, Throwable t) {
        println(Level.DEBUG, msg, t);
    }

    public void info(String msg, Throwable t) {
        println(Level.INFO, msg, t);
    }

    public void warn(String msg, Throwable t) {
        println(Level.WARN, msg, t);
    }

    public void error(String msg, Throwable t) {
        println(Level.WARN, msg, t);
    }


    public void trace(String msg, Object... args) {
        printf(Level.TRACE, msg, args);
    }

    public void debug(String msg, Object... args) {
        printf(Level.DEBUG, msg, args);
    }

    public void info(String msg, Object... args) {
        printf(Level.INFO, msg, args);
    }

    public void warn(String msg, Object... args) {
        printf(Level.WARN, msg, args);
    }

    public void error(String msg, Object... args) {
        printf(Level.ERROR, msg, args);
    }

    public void traceEvent(String event, Map<?, ?> map) {
        println(Level.TRACE, event, mapCopy(map));
    }

    public void debugEvent(String event, Map<?, ?> map) {
        println(Level.DEBUG, event, mapCopy(map));
    }

    public void infoEvent(String event, Map<?, ?> map) {
        println(Level.INFO, event, mapCopy(map));
    }

    public void warnEvent(String event, Map<?, ?> map) {
        println(Level.WARN, event, mapCopy(map));
    }

    public void errorEvent(String event, Map<?, ?> map) {
        println(Level.ERROR, event, mapCopy(map));
    }

    public void traceEvent(String event, Object... args) {
        println(Level.TRACE, event, toMap(args));
    }

    public void debugEvent(String event, Object... args) {
        println(Level.DEBUG, event, toMap(args));
    }

    public void infoEvent(String event, Object... args) {
        println(Level.INFO, event, toMap(args));
    }

    public void warnEvent(String event, Object... args) {
        println(Level.WARN, event, toMap(args));
    }

    public void errorEvent(String event, Object... args) {
        println(Level.ERROR, event, toMap(args));
    }

    private void printf(Level level, String msg, Object... args) {
        if (!LoggerContext.getDefault().isInitial()) {
            return;
        }
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (level.level < mConfig.getFileLogLevel().level && level.level < mConfig.getLogcatLogLevel().level) {
            return;
        }
        LongLog longLog = new LongLog.Builder()
                .setLogLevel(level)
                .setArgs(args)
                .setMsg(msg)
                .setDate(new Date())
                .setTag(mName)
                .setTid(Process.myTid())
                .setTnm(Thread.currentThread().getName())
                .build();

        LogbackExecutor.getInstance(mBuffer).enqueue(longLog);
    }

    /**
     * An overloaded version of  println(final Level level, final String msg,Throwable throwable)
     *
     * @param level
     * @param msg
     */
    private void println(final Level level, final String msg) {
        println(level, msg, (Throwable) null);
    }

    /**
     * Println log message with formatForFileName: datetime [threadname] level tag - message
     *
     * @param level
     * @param msg
     */
    private void println(final Level level, String msg, Throwable throwable) {
        if (!LoggerContext.getDefault().isInitial()) {
            return;
        }
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (level.level < mConfig.getFileLogLevel().level && level.level < mConfig.getLogcatLogLevel().level) {
            return;
        }
        if (throwable != null) {
            msg += "\n" + Log.getStackTraceString(throwable);
        }
        LongLog longLog = new LongLog.Builder()
                .setLogLevel(level)
                .setDate(new Date())
                .setTag(mName)
                .setMsg(msg)
                .setTid(Process.myTid())
                .setTnm(StringUtils.ellipsize(Thread.currentThread().getName(), 20, 4))
                .build();
        LogbackExecutor.getInstance(mBuffer).enqueue(longLog);
    }

    @Override
    public void println(String msg) {
        if (TextUtils.isEmpty(msg)) return;
        LongLog log = new LongLog.Builder()
                .setTag("logging")
                .setLogLevel(Level.INFO)
                .setMsg(msg)
                .setFormat(false)
                .build();
        LogbackExecutor.getInstance(mBuffer).enqueue(log);
    }

    private void println(final Level level, final String event, final Map<?, ?> map) {
        if (level.level < mConfig.getFileLogLevel().level && level.level < mConfig.getLogcatLogLevel().level) {
            return;
        }
        LogbackExecutor.getInstance(mBuffer).enqueue(new FormatLog(this, level, event, map));
    }

}
