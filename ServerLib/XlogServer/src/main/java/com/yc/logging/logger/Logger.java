package com.yc.logging.logger;

import com.yc.logging.constant.HeaderType;
import com.yc.logging.annotation.KeepSource;

import java.util.Map;


@KeepSource
public interface Logger {

    /**
     * Returns the name of this logger
     *
     * @return the logger name
     */
    String getName();

    void setName(String str);

    /**
     * Test whether this logger is enabled for TRACE level
     *
     * @return true only if the TRACE level is enabled
     */
    boolean isTraceEnabled();

    /**
     * Test whether this logger is enabled for DEBUG level
     *
     * @return true only if the DEBUG level is enabled
     */
    boolean isDebugEnabled();

    /**
     * Test whether this logger is enabled for INFO level
     *
     * @return true only if the INFO level is enabled
     */
    boolean isInfoEnabled();

    /**
     * Test whether this logger is enabled for WARN level
     *
     * @return true only if the WARN level is enabled
     */
    boolean isWarnEnabled();

    /**
     * Test whether this logger is enabled for ERROR level
     *
     * @return true only if the ERROR level is enabled
     */
    boolean isErrorEnabled();

    /**
     * Returns the header type
     *
     * @return the header type
     */
    HeaderType getHeaderType();

    /**
     * Set the header type
     *
     * @param type The header type
     */
    void setHeaderType(final HeaderType type);


    /**
     * log with no extra info
     * @param msg msg
     */
    void println(String msg);

    /**
     * 写数据
     * @param bytes
     */
    void write(byte[] bytes);

    /**
     * Log a message at TRACE level
     *
     * @param msg The message accompanying the exception
     * @param t   The exception to log
     */
    void trace(final String msg, final Throwable t);

    /**
     * Log a message at TRACE level
     *
     * @param msg  The format string
     * @param args The arguments
     */
    void trace(final String msg, final Object... args);

    void traceEvent(final String event, final Map<?, ?> map);

    void traceEvent(final String event, final Object... args);

    /**
     * Log a message at DEBUG level
     *
     * @param msg The message accompanying the exception
     * @param t   The exception to log
     */
    void debug(final String msg, final Throwable t);

    /**
     * Log a message at DEBUG level
     *
     * @param msg  The format string
     * @param args The arguments
     */
    void debug(final String msg, final Object... args);

    void debugEvent(final String event, final Map<?, ?> map);

    void debugEvent(final String event, final Object... args);

    /**
     * Log a message at INFO level
     *
     * @param msg The message accompanying the exception
     * @param t   The exception to log
     */
    void info(final String msg, final Throwable t);

    /**
     * Log a message at INFO level
     *
     * @param msg  The format string
     * @param args The arguments
     */
    void info(final String msg, final Object... args);

    void infoEvent(final String event, final Map<?, ?> map);

    void infoEvent(final String event, final Object... args);

    /**
     * Log a message at WARN level
     *
     * @param msg The message accompanying the exception
     * @param t   The exception to log
     */
    void warn(final String msg, final Throwable t);

    /**
     * Log a message at WARN level
     *
     * @param msg  The format string
     * @param args The arguments
     */
    void warn(final String msg, final Object... args);

    void warnEvent(final String event, final Map<?, ?> map);

    void warnEvent(final String event, final Object... args);

    /**
     * Log a message at ERROR level
     *
     * @param msg The message accompanying the exception
     * @param t   The exception to log
     */
    void error(final String msg, final Throwable t);

    /**
     * Log a message at ERROR level
     *
     * @param msg  The format string
     * @param args The arguments
     */
    void error(final String msg, final Object... args);

    void errorEvent(final String event, final Map<?, ?> map);

    void errorEvent(final String event, final Object... args);

}
