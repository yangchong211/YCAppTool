

package com.yc.threaddebug;

/**
 * @author jacks
 * @since 2019-07-29 12:03
 */
public class ThreadUtils {

    private static ThreadGroup rootGroup = null;

    /**
     * @return threads but its count not equal to real thread count, you need filter null manually.
     */
    public static Thread[] getAllThreads() {
        ThreadGroup rootGroup = ThreadUtils.rootGroup;
        if (rootGroup == null) {
            rootGroup = Thread.currentThread().getThreadGroup();
            ThreadGroup parentGroup;
            while ((parentGroup = rootGroup.getParent()) != null) {
                rootGroup = parentGroup;
            }
            ThreadUtils.rootGroup = rootGroup;
        }

        Thread[] threads = new Thread[rootGroup.activeCount()];
        while (rootGroup.enumerate(threads, true) == threads.length) {
            // thread array not big enough for enumerate try more
            threads = new Thread[threads.length + threads.length / 2];
        }

        return threads;
    }
}
