package com.yc.http.synthesis.config;

import android.text.TextUtils;
import android.util.Log;

import com.yc.http.synthesis.EasyUtils;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2020/04/24
 *    desc   : 网络请求日志打印默认实现
 */
public final class LogStrategy implements ILogStrategy {

    @Override
    public void printLog(String tag, String log) {
        // 这里解释一下，为什么不用 Log.d，而用 Log.i，因为 Log.d 在魅族 16th 手机上面无法输出日志
        Log.i(tag, log != null ? log : "null");
    }

    @Override
    public void printJson(String tag, String json) {
        String text = EasyUtils.formatJson(json);
        if (TextUtils.isEmpty(text)) {
            return;
        }

        // 打印 Json 数据最好换一行再打印会好看一点
        text = " \n" + text;

        int segmentSize = 3 * 1024;
        long length = text.length();
        if (length <= segmentSize) {
            // 长度小于等于限制直接打印
            printLog(tag, text);
            return;
        }

        // 循环分段打印日志
        while (text.length() > segmentSize) {
            String logContent = text.substring(0, segmentSize);
            text = text.replace(logContent, "");
            printLog(tag, logContent);
        }

        // 打印剩余日志
        printLog(tag, text);
    }

    @Override
    public void printKeyValue(String tag, String key, String value) {
        printLog(tag, key + " = " + value);
    }

    @Override
    public void printThrowable(String tag, Throwable throwable) {
        Log.e(tag, throwable.getMessage(), throwable);
    }

    @Override
    public void printStackTrace(String tag, StackTraceElement[] stackTrace) {
        for (StackTraceElement element : stackTrace) {
            // 获取代码行数
            int lineNumber = element.getLineNumber();
            // 获取类的全路径
            String className = element.getClassName();
            if (lineNumber <= 0 || className.startsWith("com.hjq.http")) {
                continue;
            }

            printLog(tag, "RequestCode = (" + element.getFileName() + ":" + lineNumber + ") ");
            break;
        }
    }
}