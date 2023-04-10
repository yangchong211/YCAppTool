package com.yc.tcp.thread

import com.yc.logclient.LogUtils
import com.yc.tcp.TcpConfig
import com.yc.tcp.data.TcpDataBean
import com.yc.tcp.inter.MessageDispatchCallBack
import java.util.concurrent.LinkedBlockingQueue

class TcpDispatcherThread(
    private val readQueue: LinkedBlockingQueue<TcpDataBean>,
    private val dispatcher: MessageDispatchCallBack) : Thread("ReceiveDispatcher") {

    private var mQuit = false

    override fun run() {
        LogUtils.d(
            TAG,
            "${TcpConfig.THREAD_RUN},mQuit:${mQuit},${this}-${System.identityHashCode(this)}"
        )
        while (!mQuit) {
            var data: TcpDataBean? = null
            //调用take方法从队列中取出数据data
            if (isInterrupted { data = readQueue.take() }) {
                if (mQuit) {
                    //如果推出线程，则终止操作
                    return
                }
                //否则跳过异常数据继续
                continue
            }
            data?.let {
                dispatcher.invoke(it)
            }
        }
        LogUtils.d(
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



