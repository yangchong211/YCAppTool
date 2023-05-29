package com.yc.anrtoollib.watch;

import androidx.annotation.NonNull;

import java.io.Serializable;
/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2021/8/11
 *     desc   : anr数据trace
 *     revise :
 * </pre>
 */
public class ANRStackTrace implements Serializable {

    public final String name;
    public final StackTraceElement[] stackTrace;

    public class MyThread extends Throwable {

        public MyThread(ANRStackTrace.MyThread other) {
            super(name, other);
        }

        @Override
        @NonNull
        public Throwable fillInStackTrace() {
            setStackTrace(stackTrace);
            return this;
        }
    }

    public ANRStackTrace(String name, StackTraceElement[] stackTrace) {
        this.name = name;
        this.stackTrace = stackTrace;
    }
}
