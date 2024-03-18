package com.yc.ycthreadpoollib.easy;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2019/5/11
 *     desc   : ThreadFactory
 *     revise :
 *     GitHub : https://github.com/yangchong211/YCThreadPool
 * </pre>
 */
public class MyThreadFactory implements ThreadFactory {

    private final AtomicLong mCount = new AtomicLong(0);
    private final String threadName;

    public MyThreadFactory() {
        this("EasyExecutor");
    }

    public MyThreadFactory(String threadName) {
        if (threadName == null || threadName.length() == 0) {
            this.threadName = "MyThreadFactory";
        } else {
            this.threadName = threadName;
        }
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, threadName + mCount.getAndIncrement());
    }
}
