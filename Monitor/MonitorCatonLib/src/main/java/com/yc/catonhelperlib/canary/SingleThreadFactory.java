
package com.yc.catonhelperlib.canary;

import java.util.concurrent.ThreadFactory;


final class SingleThreadFactory implements ThreadFactory {

    private final String threadName;

    SingleThreadFactory(String threadName) {
        this.threadName = "BlockCanary-" + threadName;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return new Thread(runnable, threadName);
    }
}
