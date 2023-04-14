package com.yc.logclient;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.yc.logclient.client.LogClient;
import com.yc.logclient.client.LogDecrator;
import com.yc.logclient.constant.LogConstant;

import static com.yc.logclient.constant.LogConstant.Log_Level_debug;
import static com.yc.logclient.constant.LogConstant.Log_Level_error;
import static com.yc.logclient.constant.LogConstant.Log_Level_info;
import static com.yc.logclient.constant.LogConstant.Log_Level_verbose;
import static com.yc.logclient.constant.LogConstant.Log_Level_warn;


public final class LogUtils {

    /**
     * Log信息级别>=logLevel的日志信息打印出来
     */
    public static int logLevel = Log_Level_verbose;
    public static LogClient logClient = new LogClient();
    private static long count = 0;

    /**
     * 是否在logcat 显示调试日志
     */
    private static boolean isShowLog = true;
    /**
     * 打印日志时的堆栈层级
     *
     * @return
     */
    private static int getStackIndex() {
        return LogClient.mBaseIndex + 1;
    }


    /**
     * 初始化 日志引擎
     *
     * @param context
     */
    public static void initLogEngine(Context context) {
        logClient.initLogEngine(context);
    }

    /**
     * flush 清空数据,写文件。
     */
    public static void flush() {
        logClient.flushLog();
    }

    /**
     * 详细信息
     */
    public static void v(String msg) {
        doLog(LogConstant.Log_Type_App, Log_Level_verbose, isShowLog, "", msg, null, getStackIndex());
    }

    public static void v(String tag, String msg) {
        doLog(LogConstant.Log_Type_App, Log_Level_verbose, isShowLog, tag, msg, null, getStackIndex());
    }

    public static void v(String tag, String msg, Throwable tr) {
        doLog(LogConstant.Log_Type_App, Log_Level_verbose, isShowLog, tag, msg, tr, getStackIndex());
    }

    public static void v(String msg, int stackIndex) {
        doLog(LogConstant.Log_Type_App, Log_Level_verbose, isShowLog, "", msg, null, getStackIndex());
    }

    public static void v(String tag, String msg, int stackIndex) {
        doLog(LogConstant.Log_Type_App, Log_Level_verbose, isShowLog, tag, msg, null, stackIndex);
    }


    /**
     * 调试日志
     */
    public static void d() {
        doLog(LogConstant.Log_Type_App, Log_Level_debug, isShowLog, "", "", null, getStackIndex());
    }

    public static void d(String info) {
        doLog(LogConstant.Log_Type_App, Log_Level_debug, isShowLog, "", info, null, getStackIndex());
    }

    public static void d(String tag, String info) {
        doLog(LogConstant.Log_Type_App, Log_Level_debug, isShowLog, tag, info, null, getStackIndex());
    }

    public static void d(String tag, String info, Throwable tr) {
        doLog(LogConstant.Log_Type_App, Log_Level_debug, isShowLog, tag, info, tr, getStackIndex());
    }


    public static void d(String info, int stackIndex) {
        doLog(LogConstant.Log_Type_App, Log_Level_debug, isShowLog, "", info, null, stackIndex);
    }

    public static void d(String tag, String info, int stackIndex) {
        doLog(LogConstant.Log_Type_App, Log_Level_debug, isShowLog, tag, info, null, stackIndex);
    }

    public static void d(String tag, String info, Throwable tr, int stackIndex) {
        doLog(LogConstant.Log_Type_App, Log_Level_debug, isShowLog, tag, info, tr, stackIndex);
    }


    /**
     * 信息日志
     */
    public static void i(String info) {
        doLog(LogConstant.Log_Type_App, Log_Level_info, isShowLog, "", info, null, getStackIndex());
    }

    public static void i(String tag, String info) {
        doLog(LogConstant.Log_Type_App, Log_Level_info, isShowLog, tag, info, null, getStackIndex());
    }

    public static void i(String tag, String info, Throwable tr) {
        doLog(LogConstant.Log_Type_App, Log_Level_info, isShowLog, tag, info, tr, getStackIndex());
    }


    public static void i(String info, int stackIndex) {
        doLog(LogConstant.Log_Type_App, Log_Level_info, isShowLog, "", info, null, stackIndex);
    }

    public static void i(String tag, String info, int stackIndex) {
        doLog(LogConstant.Log_Type_App, Log_Level_info, isShowLog, tag, info, null, stackIndex);
    }

    public static void i(String tag, String info, Throwable tr, int stackIndex) {
        doLog(LogConstant.Log_Type_App, Log_Level_info, isShowLog, tag, info, tr, stackIndex);
    }


    /**
     * 警告日志
     */
    public static void w(String info) {
        doLog(LogConstant.Log_Type_App, Log_Level_warn, isShowLog, "", info, null, getStackIndex());
    }

    public static void w(String tag, String info) {
        doLog(LogConstant.Log_Type_App, Log_Level_warn, isShowLog, tag, info, null, getStackIndex());
    }

    public static void w(String tag, String info, Throwable tr) {
        doLog(LogConstant.Log_Type_App, Log_Level_warn, isShowLog, tag, info, tr, getStackIndex());
    }

    public static void w(String info, int stackIndex) {
        doLog(LogConstant.Log_Type_App, Log_Level_warn, isShowLog, "", info, null, stackIndex);
    }

    public static void w(String tag, String info, int stackIndex) {
        doLog(LogConstant.Log_Type_App, Log_Level_warn, isShowLog, tag, info, null, stackIndex);
    }

    public static void w(String tag, String info, Throwable tr, int stackIndex) {
        doLog(LogConstant.Log_Type_App, Log_Level_warn, isShowLog, tag, info, tr, stackIndex);
    }

    /**
     * 错误日志
     */
    public static void e(String info) {
        doLog(LogConstant.Log_Type_App, Log_Level_error, isShowLog, "", info, null, getStackIndex());
    }

    public static void e(String tag, String info) {
        doLog(LogConstant.Log_Type_App, Log_Level_error, isShowLog, tag, info, null, getStackIndex());
    }

    public static void e(Throwable tr) {
        doLog(LogConstant.Log_Type_App, Log_Level_error, isShowLog, "", "", tr, getStackIndex());
    }

    public static void e(String info, Throwable tr) {
        doLog(LogConstant.Log_Type_App, Log_Level_error, isShowLog, "", info, tr, getStackIndex());
    }

    public static void e(String tag, String info, Throwable tr) {
        doLog(LogConstant.Log_Type_App, Log_Level_error, isShowLog, tag, info, tr, getStackIndex());
    }


    public static void e(String info, int stackIndex) {
        doLog(LogConstant.Log_Type_App, Log_Level_error, isShowLog, "", info, null, stackIndex);
    }

    public static void e(String tag, String info, int stackIndex) {
        doLog(LogConstant.Log_Type_App, Log_Level_error, isShowLog, tag, info, null, stackIndex);
    }

    public static void e(Throwable tr, int stackIndex) {
        doLog(LogConstant.Log_Type_App, Log_Level_error, isShowLog, "", "", tr, stackIndex);
    }

    public static void e(String info, Throwable tr, int stackIndex) {
        doLog(LogConstant.Log_Type_App, Log_Level_error, isShowLog, "", info, tr, stackIndex);
    }

    public static void e(String tag, String info, Throwable tr, int stackIndex) {
        doLog(LogConstant.Log_Type_App, Log_Level_error, isShowLog, tag, info, tr, stackIndex);
    }

    /**
     * crash --- 崩溃日志
     */
    public static void crash(String logInfo, Throwable ex) {
        doLog(LogConstant.Log_Type_Crash, LogConstant.Log_Level_crash, isShowLog, "", logInfo, ex, getStackIndex());
    }

    public static void crash(String logInfo, Throwable ex, int stackIndex) {
        doLog(LogConstant.Log_Type_Crash, LogConstant.Log_Level_crash, isShowLog, "", logInfo, ex, stackIndex);
    }

    /**
     * anr ---- anr不响应日志
     */

    public static void anr(String tag, String info) {
        doLog(LogConstant.Log_Level_anr, LogConstant.Log_Level_anr, isShowLog, tag, info, null, getStackIndex());
    }

    public static void anr(String tag, String info, int stackIndex) {
        doLog(LogConstant.Log_Level_anr, LogConstant.Log_Level_anr, isShowLog, tag, info, null, stackIndex);
    }

    /**
     * dot ---  埋点日志
     *
     * @param info
     */
    public static void statistics(String info) {
        doLog(LogConstant.Log_Type_App, Log_Level_info, isShowLog, "", "statistics_" + info, null, getStackIndex());
        doLog(LogConstant.Log_Type_Statistics, LogConstant.Log_Level_statistics, isShowLog, "", info, null, getStackIndex());
    }

    public static void statistics(String info, int stackIndex) {
        doLog(LogConstant.Log_Type_App, Log_Level_info, isShowLog, "", "statistics_" + info, null, stackIndex);
        doLog(LogConstant.Log_Type_Statistics, LogConstant.Log_Level_statistics, isShowLog, "", info, null, stackIndex);
    }

    /**
     * 强制不打印信息到控制台
     *
     * @param tag
     * @param info
     */
    public static void d_no_print(String tag, String info) {
        doLog(LogConstant.Log_Type_App, Log_Level_debug, false, tag, info, null, getStackIndex());
    }

    public static void d_no_print(String tag, String info, int statckIndex) {
        doLog(LogConstant.Log_Type_App, Log_Level_debug, false, tag, info, null, statckIndex);
    }

    public static void setLogSaveFolder(String logFolder) {
        LogManager.setLogSaveFolder(logFolder);
    }

    public static void setLogTag(String tag) {
        LogDecrator.LOG_TAG = tag;
    }


    /**
     * 打印log时 的堆栈层级,用于获取调用堆栈和行数。点击日志，跳转到源文件位置。
     * @param logIndex
     */
    public static void setBaseStackIndex(int logIndex) {
        LogClient.mBaseIndex = logIndex;
    }

    public static void probeStackLevel() {
        logClient.probeStackLevel();
    }

    /**
     * 大日志内容自动截断
     * 快进程传递数据 大小超过900K,会报错 需要拆分。
     * @param enable
     */
    public static void enableTooLargeAutoDevide(boolean enable) {
        logClient.enableTooLargeAutoDevide(enable);
    }

    /**
     * 大文件拆分 比例
     * @param ratio
     */
    public static void setAutoDivideRatio(float ratio) {
        logClient.setAutoDivideRatio(ratio);
    }

    public static void collectLogcat(String savepath, long delaytime, boolean clearlogcat) {
        logClient.collectLogcat(savepath, delaytime, clearlogcat);
    }

    public static void saveLogWithPath(String log, String path) {
        logClient.saveLogWithPath(log, path);
    }

    /**
     * log输出前缀
     * @param pre
     */
    public static void setLogPre(String pre) {
        if (TextUtils.isEmpty(pre)) {
            LogDecrator.LOG_PRE = "";
        } else {
            LogDecrator.LOG_PRE = pre;
        }
    }

    /**
     * 区分app、crash、anr、statistics 将日志信息 传递给Tmlog
     *
     * @param type
     * @param level
     * @param showLog
     * @param tagStr
     * @param obj
     * @param ex
     * @param index
     */
    private static void doLog(int type, int level, boolean showLog, String tagStr, Object obj, Throwable ex, int index) {
        if (level < logLevel) {
            return;
        }
        //获取tag
        String tag = LogDecrator.decorateTag(tagStr);
        //组装日志信息
        String logStr = LogDecrator.decorate4JumpSource(type, tagStr, obj, ex, index);
        //打印出日志
        tryLogcat(showLog, type, level, tag, logStr);
        //往文件中写日志
        if (type == LogConstant.Log_Type_Crash) {
            logClient.logu(LogConstant.Log_Type_App, level, tag + logStr);
            logClient.crash(type, level, logStr, ex);
        } else if (type == LogConstant.Log_Type_Anr) { //ANR 的信息打印
            logClient.logu(LogConstant.Log_Type_App, level, tag + logStr);
            logClient.anr(type, level, logStr);
        } else if (type == LogConstant.Log_Type_Statistics) {
            logClient.logu(type, type, "[" + count + "] " + tag + logStr);
            count++;
        } else {
            logClient.logu(type, level, tag + logStr);
        }
    }

    /**
     * 打印日志到logcat
     *
     * @param showLog
     * @param type
     * @param level
     * @param tag
     * @param msg
     */
    private static void tryLogcat(boolean showLog, int type, int level, String tag, String msg) {
        if (showLog) {
            switch (level) {
                case Log_Level_info:
                    Log.i(tag, msg);
                    break;
                case Log_Level_verbose:
                    Log.v(tag, msg);
                    break;
                case Log_Level_debug:
                    Log.d(tag, msg);
                    break;
                case Log_Level_warn:
                    Log.w(tag, msg);
                    break;
                case Log_Level_error:
                    Log.e(tag, msg);
                    break;
            }
        }
    }

    public static void setShowLog(boolean show) {
        isShowLog = show;
    }

    public static boolean isShowLog() {
        return isShowLog;
    }
}

