package me.pqpo.librarylog4a;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

import me.pqpo.librarylog4a.logger.AndroidLogger;
import me.pqpo.librarylog4a.logger.Logger;

import static me.pqpo.librarylog4a.Level.*;


/**
 * Created by pqpo on 2017/11/16.
 */
public class Log4a {

    private static Logger sLoggerDelegate = new AndroidLogger();

    public static void setLogger(Logger logger) {
        sLoggerDelegate = logger;
    }

    public static Logger getLogger() {
        return sLoggerDelegate;
    }

    public static void v(String tag, String msg) {
        println(VERBOSE, tag, msg);
    }

    public static void d(String tag, String msg) {
        println(DEBUG, tag, msg);
    }

    public static void i(String tag, String msg) {
        println(INFO, tag, msg);
    }

    public static void w(String tag, String msg) {
        println(WARN, tag, msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        println(WARN, tag, msg + "\n" + getStackTraceString(tr));
    }

    public static void w(String tag, Throwable tr) {
        println(WARN, tag, getStackTraceString(tr));
    }

    public static void e(String tag, String msg) {
        println(ERROR, tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        println(ERROR, tag, msg + "\n" + getStackTraceString(tr));
    }

    public static void e(String tag, Throwable tr) {
        println(ERROR, tag, getStackTraceString(tr));
    }

    public static void println(int priority, String tag, String msg) {
        if (sLoggerDelegate != null) {
            sLoggerDelegate.println(priority, tag, msg);
        }
    }

    public static void flush() {
        if (sLoggerDelegate != null) {
            sLoggerDelegate.flush();
        }
    }

    public static void release() {
        if (sLoggerDelegate != null) {
            sLoggerDelegate.release();
        }
        sLoggerDelegate = null;
    }

    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

}
