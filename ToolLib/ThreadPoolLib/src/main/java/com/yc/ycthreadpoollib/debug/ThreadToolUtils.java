

package com.yc.ycthreadpoollib.debug;

public class ThreadToolUtils {

    private static ThreadGroup rootGroup = null;

    /**
     * @return threads but its count not equal to real thread count, you need filter null manually.
     */
    public static Thread[] getAllThreads() {
        ThreadGroup rootGroup = ThreadToolUtils.rootGroup;
        if (rootGroup == null) {
            rootGroup = Thread.currentThread().getThreadGroup();
            ThreadGroup parentGroup;
            while ((parentGroup = rootGroup.getParent()) != null) {
                rootGroup = parentGroup;
            }
            ThreadToolUtils.rootGroup = rootGroup;
        }

        Thread[] threads = new Thread[rootGroup.activeCount()];
        while (rootGroup.enumerate(threads, true) == threads.length) {
            // thread array not big enough for enumerate try more
            threads = new Thread[threads.length + threads.length / 2];
        }

        return threads;
    }
}
