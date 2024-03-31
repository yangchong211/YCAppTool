package com.yc.mmaplog;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

import com.yc.mmaplog.logger.AndroidLogger;
import com.yc.mmaplog.inter.InterLogger;

import static com.yc.mmaplog.LogLevel.*;

import android.util.Log;


/**
 * @author yangchong
 * GitHub : <a href="https://github.com/yangchong211">...</a>
 * email  : yangchong211@163.com
 * time  : 2017/09/23
 * desc  : 日志工具类
 * revise:
 */
public final class LogManager {

    private static InterLogger sLoggerDelegate = new AndroidLogger();

    public static void setLogger(InterLogger logger) {
        sLoggerDelegate = logger;
    }

    public static InterLogger getLogger() {
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
            //携带方法和的类的源文件行号
            String message = decorate4JumpSource(msg,5);
            sLoggerDelegate.println(priority, tag, message);
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

    /**
     * 重新组装 日志字符串，携带方法和的类的源文件行号，支持点击跳转到源文件。
     * 通过该组装信息，可以快速知道打印日志的位置和代码函数方法
     * 比如组装的字符串：
     * [(PalmServiceApi.java:281)#SetMixedModel] status poll looper endRead
     * [(TokenManager.java:173)#OnReqSuccess] getToken-onResponse , sync palm
     *
     * @param obj   打印内容
     * @param index 索引index
     * @return 组装后的日志字符串
     */
    private static String decorate4JumpSource(Object obj, int index) {
        StringBuilder stringBuilder = new StringBuilder();
        String msg;
        try {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            //这个地方为何要设置index为5，通过下面的 printThreadLog 方法打印可以看到，打印此处日志的关键在第5行。
            index = Math.min(index, stackTrace.length - 1);
            //通过索引查询数组的效率是O(1)
            String className = stackTrace[index].getFileName();
            String methodName = stackTrace[index].getMethodName();
            int lineNumber = stackTrace[index].getLineNumber();
            methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
            stringBuilder.append("[(").append(className).append(":").append(lineNumber).append(")#").append(methodName).append("] ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (obj == null) {
            msg = "Log with null Object";
        } else {
            msg = obj.toString();
        }
        stringBuilder.append(msg);
        String logStr = stringBuilder.toString();
        //Log.d("PalmSdk |: ", "decorate4JumpSource " + logStr);
        return logStr;
    }


    /**
     * 打印线程堆栈日志。打印日志如下所示：
     * 那么看一下，printThreadLog方法是在哪里调用的，通过日志第四行可以看到关键日志。
     * <p>
     * dalvik.system.VMStack.getThreadStackTrace(Native Method)
     * java.lang.Thread.getStackTrace(Thread.java:1538)
     * com.tencent.wx.palmsdk.log.FileLogManager.printThreadLog(FileLogManager.java:105)
     * com.tencent.wx.palmsdk.log.FileLogManager.<init>(FileLogManager.java:37)
     * com.tencent.wx.palmsdk.log.FileLogManager.getInstance(FileLogManager.java:28)
     * com.tencent.wx.palmsdk.yt.YTPalm.auth(YTPalm.java:85)
     * com.tencent.wx.palmsdk.yt.YTSdkService.auth(YTSdkService.java:66)
     * com.tencent.wx.palmsdk.api.PalmSdkApiImpl.initPalmSdk(PalmSdkApiImpl.java:134)
     * com.tencent.wx.palmsdk.PalmServiceApi.initPalmSdk(PalmServiceApi.java:86)
     * com.tencent.wx.ssdademo.SplashActivity.onCreate(SplashActivity.java:31)
     * android.app.Activity.performCreate(Activity.java:7207)
     * android.app.Activity.performCreate(Activity.java:7198)
     * android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1273)
     * android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2924)
     * android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:3079)
     * android.app.servertransaction.LaunchActivityItem.execute(LaunchActivityItem.java:78)
     * android.app.servertransaction.TransactionExecutor.executeCallbacks(TransactionExecutor.java:108)
     * android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:68)
     * android.app.ActivityThread$H.handleMessage(ActivityThread.java:1836)
     * android.os.Handler.dispatchMessage(Handler.java:106)
     * android.os.Looper.loop(Looper.java:193)
     * android.app.ActivityThread.main(ActivityThread.java:6702)
     * java.lang.reflect.Method.invoke(Native Method)
     * com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:493)
     * com.android.internal.os.ZygoteInit.main(ZygoteInit.java:911)
     */
    private void printThreadLog() {
        Thread thread = Thread.currentThread();
        //堆栈跟踪元素，它由 Throwable.getStackTrace() 返回。每个元素表示单独的一个【堆栈帧】。
        //所有的堆栈帧（堆栈顶部的那个堆栈帧除外）都表示一个【方法调用】。堆栈顶部的帧表示【生成堆栈跟踪的执行点】。
        StackTraceElement[] stackTraces = thread.getStackTrace();
        String print = printToString1(stackTraces);
        Log.i("printThreadLog", print);
    }

    private String printToString1(StackTraceElement[] stackTraces) {
        StringBuilder result = new StringBuilder();
        for (StackTraceElement stackTrace : stackTraces) {
            //获取class的名称，该类包含由该堆栈跟踪元素所表示的执行点
            String clazzName = stackTrace.getClassName();
            //返回源文件名，该文件包含由该堆栈跟踪元素所表示的执行点。
            String fileName = stackTrace.getFileName();
            //返回源行的行号，该行包含由该堆栈该跟踪元素所表示的执行点。
            int lineNumber = stackTrace.getLineNumber();
            //返回方法名，此方法包含由该堆栈跟踪元素所表示的执行点。
            String methodName = stackTrace.getMethodName();
            //如果包含由该堆栈跟踪元素所表示的执行点的方法是一个本机方法，则返回 true。
            boolean nativeMethod = stackTrace.isNativeMethod();
            result.append(clazzName).append(".").append(methodName);
            if (nativeMethod) {
                //这种是 native 方法
                result.append("(Native Method)");
            } else if (fileName != null) {
                //有方法名称
                if (lineNumber >= 0) {
                    result.append("(").append(fileName).append(":").append(lineNumber).append(")");
                } else {
                    result.append("(").append(fileName).append(")");
                }
            } else {
                //没有方法名称
                if (lineNumber >= 0) {
                    result.append("(Unknown Source:").append(lineNumber).append(")");
                } else {
                    result.append("(Unknown Source)");
                }
            }
            result.append("\n");
        }
        return result.toString();
    }


}
