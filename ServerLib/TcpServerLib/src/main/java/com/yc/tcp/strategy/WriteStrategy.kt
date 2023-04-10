package com.yc.tcp.strategy



class WriteStrategy {

    private val lock = Object()

    /**
     * tcp 未连接 则阻塞线程，直到tcp成功建立连接
     */
    fun checkTcpNoConnect(connect: Boolean) {
        if (!connect) {
            block()
        }
    }

    /**
     * 阻塞线程一定时间
     * delay = 0 无限阻塞
     * delay != 0 则阻塞delay时间
     */
    fun block(delay: Long = 0) {
        synchronized(lock) {
            if (delay != 0L) {
                lock.wait(delay)
            } else {
                lock.wait()
            }
        }
    }

    /**
     * 释放阻塞线程
     */
    fun freeBlock() {
        synchronized(lock) {
            lock.notifyAll()
        }
    }

}