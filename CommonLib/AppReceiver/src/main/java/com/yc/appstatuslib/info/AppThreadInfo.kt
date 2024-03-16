package com.yc.appstatuslib.info

/**
 * @author: yangchong
 * email  : yangchong211@163.com
 * time   : 2017/5/18
 * desc   : 线程信息
 * revise :
 */
class AppThreadInfo {

    var threadCount = 0
    var runningThreadCount: List<Thread>? = null
    var blockThreadCount: List<Thread>? = null
    var timeWaitingThreadCount: List<Thread>? = null
    var waitingThreadCount: List<Thread>? = null

    override fun toString(): String {
        return "ThreadInfo{" +
                "threadCount=" + threadCount +
                ", runningThreadCount=" + runningThreadCount +
                ", blockThreadCount=" + blockThreadCount +
                ", timeWaitingThreadCount=" + timeWaitingThreadCount +
                ", waitingThreadCount=" + waitingThreadCount +
                '}'
    }
}