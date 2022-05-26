package com.yc.apploglib.log;


/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     email : yangchong211@163.com
 *     time  : 2018/11/9
 *     desc  : log日志接口
 *     revise:
 * </pre>
 */
public interface InterLogger {

    String tagName();

    void v(String format, Object... args);

    void v(Throwable tr, String format, Object... args);

    void d(String format, Object... args);

    void d(Throwable tr, String format, Object... args);

    void i(String format, Object... args);

    void i(Throwable tr, String format, Object... args);

    void w(String format, Object... args);

    void w(Throwable tr, String format, Object... args);

    void w(Throwable tr);

    void e(String format, Object... args);

    void e(Throwable tr, String format, Object... args);

    void e(Throwable tr);

    void wtf(String format, Object... args);

    void wtf(Throwable tr);

    void wtf(Throwable tr, String format, Object... args);
}

