package com.yc.appstatuslib.utils

import java.util.*

/**
 * <pre>
 * @author yangchong
 * email  : yangchong211@163.com
 * time   : 2017/5/18
 * desc   : 线程工具类
 * revise :
</pre> *
 */
class ThreadManagerUtils private constructor() {

    val threadCount: Int
        get() = Thread.getAllStackTraces().keys.size

    val runningThread: List<Thread>
        get() {
            val threadSet: Set<Thread> = Thread.getAllStackTraces().keys
            val threads = threadSet.toTypedArray()
            val runnableThread: MutableList<Thread> = ArrayList()
            val var5 = threads.size
            for (i in 0 until var5) {
                val thread = threads[i]
                if (thread.state == Thread.State.RUNNABLE) {
                    runnableThread.add(thread)
                }
            }
            return runnableThread
        }

    val blockThread: List<Thread>
        get() {
            val threadSet: Set<Thread> = Thread.getAllStackTraces().keys
            val threads = threadSet.toTypedArray()
            val runnableThread: MutableList<Thread> = ArrayList()
            val length = threads.size
            for (i in 0 until length) {
                val thread = threads[i]
                if (thread.state == Thread.State.BLOCKED) {
                    runnableThread.add(thread)
                }
            }
            return runnableThread
        }

    val timeWaitingThread: List<Thread>
        get() {
            val threadSet: Set<Thread> = Thread.getAllStackTraces().keys
            val threads = threadSet.toTypedArray()
            val runnableThread: MutableList<Thread> = ArrayList()
            for (i in threads.indices) {
                val thread = threads[i]
                if (thread.state == Thread.State.TIMED_WAITING) {
                    runnableThread.add(thread)
                }
            }
            return runnableThread
        }

    val waitingThread: List<Thread>
        get() {
            val threadSet: Set<Thread> = Thread.getAllStackTraces().keys
            val threads = threadSet.toTypedArray()
            val runnableThread: MutableList<Thread> = ArrayList()
            for (i in threads.indices) {
                val thread = threads[i]
                if (thread.state == Thread.State.WAITING) {
                    runnableThread.add(thread)
                }
            }
            return runnableThread
        }

    companion object {
        private var INSTANCE: ThreadManagerUtils? = null
        @JvmStatic
        val instance: ThreadManagerUtils?
            get() {
                if (INSTANCE == null) {
                    synchronized(ThreadManagerUtils::class.java) {
                        if (INSTANCE == null) {
                            INSTANCE = ThreadManagerUtils()
                        }
                    }
                }
                return INSTANCE
            }
    }
}