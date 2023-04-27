package com.yc.logclient.client;

import android.util.Log;
import com.yc.logclient.constant.LogConstant;


public class LogDecrator {

    //公共TAG前缀
    public static String LOG_PRE = "TR_";
    //公用TAG
    public static String LOG_TAG = "";

    public static String decorateTag(String tagStr) {
        String tag;
        tag = (tagStr == null || tagStr.isEmpty()) ? "" : tagStr;
        tag = LOG_PRE + LOG_TAG + tag;
        return tag;
    }

    public static String decorate4JumpSource(int type, String tagStr, Object obj, Throwable ex, int index) {
        String logStr = obj + "";
        if (type != LogConstant.Log_Type_Statistics) {
            logStr = decorate4JumpSource(obj, ex, index);
        }

        return logStr;
    }

    /**
     * 重新组装 日志字符串,携带方法和的类的源文件行号,支持点击跳转到源文件
     *
     * @param obj
     * @param ex
     * @param index
     * @return
     */
    public static String decorate4JumpSource(Object obj, Throwable ex, int index) {
        StringBuilder stringBuilder = new StringBuilder();
        String msg;
        try {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            index = Math.min(index, stackTrace.length - 1);
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
        if (msg != null) {
            stringBuilder.append(msg);
        }
        //如果有异常，则把异常拼接在后面
        if (ex != null) {
            stringBuilder.append("\n").append(Log.getStackTraceString(ex));
        }
        String logStr = stringBuilder.toString();
        return logStr;
    }

}
