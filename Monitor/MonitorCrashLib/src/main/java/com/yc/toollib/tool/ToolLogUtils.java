package com.yc.toollib.tool;


import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : Log 工具
 *     revise:
 * </pre>
 */
public final class ToolLogUtils {

    private static final int VERBOSE = 1;
    private static final int DEBUG = 2;
    private static final int INFO = 3;
    private static final int WARN = 4;
    private static final int ERROR = 5;
    public static int LEVEL = 1;
    public static String LOG_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static boolean FILEABLE = getFILEABLE();

    private static boolean $(String msg) {
        return msg == null;
    }

    /**
     * 打印verbose级别信息
     *
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if ($(msg)) {
            return;
        }
        if (LEVEL <= VERBOSE) {
            android.util.Log.v(tag, msg);
        } else if (FILEABLE) {
            logFile("V", tag, msg);
        }
    }

    /**
     * 打印verbose级别信息，tag默认为文件名
     *
     * @param msg
     */
    public static void v(String msg) {
        if ($(msg)) {
            return;
        }
        if (LEVEL <= VERBOSE) {
            android.util.Log.v(FILE(), msg);
        } else if (FILEABLE) {
            logFile("V", FILE(), msg);
        }
    }

    /**
     * 打印debug级别信息
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if ($(msg)) {
            return;
        }
        if (LEVEL <= DEBUG) {
            android.util.Log.d(tag, msg);
        } else if (FILEABLE) {
            logFile("D", tag, msg);
        }
    }

    /**
     * 打印debug级别信息，tag默认为文件名
     *
     * @param msg
     */
    public static void d(String msg) {
        if ($(msg)) {
            return;
        }
        if (LEVEL <= DEBUG) {
            android.util.Log.d(FILE(), msg);
        } else if (FILEABLE) {
            logFile("D", FILE(), msg);
        }
    }

    /**
     * 打印info级别信息
     *
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if ($(msg)) {
            return;
        }
        if (LEVEL <= INFO) {
            android.util.Log.i(tag, msg);
        } else if (FILEABLE) {
            logFile("I", tag, msg);
        }
    }

    /**
     * 打印info级别信息，tag默认为文件名
     *
     * @param msg
     */
    public static void i(String msg) {
        if ($(msg)) {
            return;
        }
        if (LEVEL <= INFO) {
            android.util.Log.i(FILE(), msg);
        } else if (FILEABLE) {
            logFile("I", FILE(), msg);
        }
    }

    /**
     * 打印warn级别信息
     *
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if ($(msg)) {
            return;
        }
        if (LEVEL <= WARN) {
            android.util.Log.w(tag, msg);
        } else if (FILEABLE) {
            logFile("W", tag, msg);
        }
    }

    /**
     * 打印warn级别信息，tag默认为文件名
     *
     * @param msg
     */
    public static void w(String msg) {
        if ($(msg)) {
            return;
        }
        if (LEVEL <= WARN) {
            android.util.Log.w(FILE(), msg);
        } else if (FILEABLE) {
            logFile("W", FILE(), msg);
        }
    }

    /**
     * 打印error级别信息
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if ($(msg)) {
            return;
        }
        if (LEVEL <= ERROR) {
            android.util.Log.e(tag, msg);
        } else if (FILEABLE) {
            logFile("E", tag, msg);
        }
    }

    /**
     * 打印error级别信息，tag默认为文件名
     *
     * @param msg
     */
    public static void e(String msg) {
        if ($(msg)) {
            return;
        }
        if (LEVEL <= ERROR) {
            android.util.Log.e(FILE(), msg);
        } else if (FILEABLE) {
            logFile("E", FILE(), msg);
        }
    }

    /**
     * 判断是否输出到文件
     *
     * @return
     */
    private static boolean getFILEABLE() {
        File file = new File(LOG_FOLDER + "/_FILEABLE");
        if (file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 输出到文件
     *
     * @param type
     * @param tag
     * @param value
     */
    private static void logFile(String type, String tag, String value) {
        String now = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"))
                .format(new Date());
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(
                    LOG_FOLDER + "/log.txt", true)));
            out.println(String.format("[%s][%s][%s]%s", now, type, tag, value));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 获取当前文件名
     *
     * @return
     */
    private static String FILE() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        return traceElement.getFileName();
    }

    /**
     * 获取当前文件名、方法名、行数
     * 暂时没用到
     *
     * @return
     */
    private static String getFileLineMethod() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        return "[" + traceElement.getFileName() + " | " + traceElement.getLineNumber() +
                " | " + traceElement.getMethodName() + "]";
    }

    /**
     * 获取当前方法名和当前行数
     * 暂时没用到
     *
     * @return
     */
    private static String getLineMethod() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        return "[" + traceElement.getLineNumber() + " | " + traceElement.getMethodName() + "]";
    }

    /**
     * 获取当前方法名
     * 暂时没用到
     *
     * @return
     */
    private static String FUNC() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return traceElement.getMethodName();
    }

    /**
     * 获取当前行数
     * 暂时没用到
     *
     * @return
     */
    private static int LINE() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return traceElement.getLineNumber();
    }

    /**
     * 获取时间
     * 暂时没用到
     *
     * @return
     */
    private static String TIME() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(now);
    }

    public static String getLog(String className, String log) {
        return "{Thread_:" + Thread.currentThread().getName() + "}" + "[" + className + "_:]" + log;
    }

}
