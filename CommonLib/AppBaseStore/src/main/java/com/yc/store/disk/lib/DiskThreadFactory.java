package com.yc.store.disk.lib;

import java.util.concurrent.ThreadFactory;

public class DiskThreadFactory implements ThreadFactory {

    @Override
    public synchronized Thread newThread(Runnable runnable) {
        //设置线程优先级
        Thread result = new Thread(runnable, "disk-lru-cache-thread");
        result.setPriority(Thread.MIN_PRIORITY);
        return result;
    }
}
