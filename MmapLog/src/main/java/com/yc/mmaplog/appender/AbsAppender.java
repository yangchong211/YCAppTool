package com.yc.mmaplog.appender;

import com.yc.mmaplog.interceptor.LevelInterceptor;

import java.util.ArrayList;
import java.util.List;

import com.yc.mmaplog.LogPoolData;
import com.yc.mmaplog.inter.InterAppender;
import com.yc.mmaplog.inter.Interceptor;


public abstract class AbsAppender implements InterAppender {

    public static final int MAX_LENGTH_OF_SINGLE_MESSAGE = 4063;

    public int maxSingleLength = MAX_LENGTH_OF_SINGLE_MESSAGE;

    private final List<Interceptor> interceptors = new ArrayList<>();

    private final LevelInterceptor levelInterceptor = new LevelInterceptor();

    public AbsAppender() {
        //添加拦截器
        addInterceptor(levelInterceptor);
    }

    public void setLevel(int level) {
        levelInterceptor.setLevel(level);
    }

    public void addInterceptor(List<Interceptor> interceptors) {
        if (interceptors != null && !interceptors.isEmpty()) {
            this.interceptors.addAll(interceptors);
        }
    }

    public void addInterceptor(Interceptor interceptor) {
        if (interceptor != null) {
            interceptors.add(interceptor);
        }
    }

    public void setMaxSingleLength(int maxSingleLength) {
        this.maxSingleLength = maxSingleLength;
    }

    @Override
    public void append(int logLevel, String tag, String msg) {
        LogPoolData logData = LogPoolData.obtain(logLevel, tag, msg);
        boolean intercepted = false;
        for (Interceptor interceptor : interceptors) {
            if (!interceptor.intercept(logData)) {
                intercepted = true;
            }
        }
        if (!intercepted) {
            appendInner(logData.logLevel, logData.tag, logData.msg);
        }
        logData.recycle();
    }

    private void appendInner(int logLevel, String tag, String msg) {
        if (msg.length() <= maxSingleLength) {
            doAppend(logLevel, tag, msg);
            return;
        }
        int msgLength = msg.length();
        int start = 0;
        int end = start + maxSingleLength;
        while (start < msgLength) {
            doAppend(logLevel, tag, msg.substring(start, end));
            start = end;
            end = Math.min(start + maxSingleLength, msgLength);
        }
    }

    protected abstract void doAppend(int logLevel, String tag, String msg);

    @Override
    public void flush() {

    }

    @Override
    public void release() {

    }
}
