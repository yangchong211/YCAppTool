package com.zuoyebang.iot.mod.tcp.thread

import com.zuoyebang.iot.mod.tcp.TcpConfig
import com.zuoyebang.iot.mod.tcp.TcpLog
import com.zuoyebang.iot.mod.tcp.data.TcpDataBean
import com.zuoyebang.iot.mod.tcp.inter.MessageDispatchCallBack
import java.util.concurrent.LinkedBlockingQueue

class TcpDispatcherThread(
    private val readQueue: LinkedBlockingQueue<TcpDataBean>,
    private val dispatcher: MessageDispatchCallBack) : Thread("ReceiveDispatcher") {

    private var mQuit = false
    override fun run() {
        TcpLog.d(
            TAG,
            "${TcpConfig.THREAD_RUN},mQuit:${mQuit},${this}-${System.identityHashCode(this)}"
        )
        while (!mQuit) {
            var data: TcpDataBean? = null
            if (isInterrupted { data = readQueue.take() }) {
                if (mQuit) {
                    return
                }
                continue
            }

            data?.let {
                dispatcher.invoke(it)
            }
        }

        TcpLog.d(
            TAG,
            "${TcpConfig.THREAD_OVER},mQuit:${mQuit},${this}-${System.identityHashCode(this)}"
        )
    }

    /**
     * 退出线程
     */
    fun quit() {
        mQuit = true
        interrupt()
    }

    companion object {
        val TAG: String = TcpDispatcherThread::class.java.simpleName
    }
}



