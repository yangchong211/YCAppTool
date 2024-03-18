package com.yc.http.synthesis;

import com.yc.http.synthesis.request.HttpRequest;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2020/05/10
 *    desc   : 日志打印类
 */
public final class EasyLog {

    /** 创建线程池来打印日志，解决出现大日志阻塞线程的情况 */
    @SuppressWarnings("AlibabaThreadShouldSetName")
    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
            Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());

    /**
     * 打印分割线
     */
    public static void printLine(HttpRequest<?> httpRequest) {
        if (!EasyConfig.getInstance().isLogEnabled()) {
            return;
        }
        EXECUTOR.execute(() -> EasyConfig.getInstance().getLogStrategy().printLine(getLogTag(httpRequest)));
    }

    /**
     * 打印日志
     */
    public static void printLog(HttpRequest<?> httpRequest, String log) {
        if (!EasyConfig.getInstance().isLogEnabled()) {
            return;
        }
        EXECUTOR.execute(() -> EasyConfig.getInstance().getLogStrategy().printLog(getLogTag(httpRequest), log));
    }

    /**
     * 打印 Json
     */
    public static void printJson(HttpRequest<?> httpRequest, String json) {
        if (!EasyConfig.getInstance().isLogEnabled()) {
            return;
        }
        EXECUTOR.execute(() -> EasyConfig.getInstance().getLogStrategy().printJson(getLogTag(httpRequest), json));
    }

    /**
     * 打印键值对
     */
    public static void printKeyValue(HttpRequest<?> httpRequest, String key, String value) {
        if (!EasyConfig.getInstance().isLogEnabled()) {
            return;
        }
        EXECUTOR.execute(() -> EasyConfig.getInstance().getLogStrategy().printKeyValue(getLogTag(httpRequest), key, value));
    }

    /**
     * 打印异常
     */
    public static void printThrowable(HttpRequest<?> httpRequest, Throwable throwable) {
        if (!EasyConfig.getInstance().isLogEnabled()) {
            return;
        }
        EXECUTOR.execute(() -> EasyConfig.getInstance().getLogStrategy().printThrowable(getLogTag(httpRequest), throwable));
    }

    /**
     * 打印堆栈
     */
    public static void printStackTrace(HttpRequest<?> httpRequest, StackTraceElement[] stackTrace) {
        if (!EasyConfig.getInstance().isLogEnabled()) {
            return;
        }
        EXECUTOR.execute(() -> EasyConfig.getInstance().getLogStrategy().printStackTrace(getLogTag(httpRequest), stackTrace));
    }

    private static String getLogTag(HttpRequest<?> httpRequest) {
        String logTag = EasyConfig.getInstance().getLogTag();
        if (httpRequest == null) {
            return logTag;
        }
        return logTag + " " + httpRequest.getRequestApi().getClass().getSimpleName();
    }
}