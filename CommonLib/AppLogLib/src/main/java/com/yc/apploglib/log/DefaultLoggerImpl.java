package com.yc.apploglib.log;

import com.yc.apploglib.config.LogDispatcher;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     email : yangchong211@163.com
 *     time  : 2018/11/9
 *     desc  : log日志接口默认实现类
 *     revise:
 * </pre>
 */
public class DefaultLoggerImpl implements InterLogger {

    private final String mTag;
    private final LogDispatcher mLogDispatcher;

    public DefaultLoggerImpl(String mTag, LogDispatcher mLogDispatcher) {
        this.mTag = mTag;
        this.mLogDispatcher = mLogDispatcher;
    }

    @Override
    public String tagName() {
        return mTag;
    }

    @Override
    public void v(String format, Object... args) {
        mLogDispatcher.v(mTag, format, args);
    }

    @Override
    public void v(Throwable tr, String format, Object... args) {
        mLogDispatcher.v(mTag, format, tr, args);
    }

    @Override
    public void d(String format, Object... args) {
        mLogDispatcher.d(mTag, format, args);
    }

    @Override
    public void d(Throwable tr, String format, Object... args) {
        mLogDispatcher.d(mTag, format, tr, args);
    }

    @Override
    public void i(String format, Object... args) {
        mLogDispatcher.i(mTag, format, args);
    }

    @Override
    public void i(Throwable tr, String format, Object... args) {
        mLogDispatcher.i(mTag, format, tr, args);
    }

    @Override
    public void w(String format, Object... args) {
        mLogDispatcher.w(mTag, format, args);
    }

    @Override
    public void w(Throwable tr, String format, Object... args) {
        mLogDispatcher.w(mTag, format, tr, args);
    }

    @Override
    public void w(Throwable tr) {
        mLogDispatcher.w(mTag, tr);
    }

    @Override
    public void e(String format, Object... args) {
        mLogDispatcher.e(mTag, format, args);
    }

    @Override
    public void e(Throwable tr, String format, Object... args) {
        mLogDispatcher.e(mTag, format, tr, args);
    }

    @Override
    public void e(Throwable tr) {
        mLogDispatcher.e(mTag, tr);
    }

    @Override
    public void wtf(String format, Object... args) {
        mLogDispatcher.wtf(mTag, format, args);
    }

    @Override
    public void wtf(Throwable tr) {
        mLogDispatcher.wtf(mTag, tr);
    }

    @Override
    public void wtf(Throwable tr, String format, Object... args) {
        mLogDispatcher.wtf(mTag, format, tr, args);
    }
}

