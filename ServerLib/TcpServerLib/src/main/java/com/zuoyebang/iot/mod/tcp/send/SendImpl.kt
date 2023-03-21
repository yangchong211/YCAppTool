package com.zuoyebang.iot.mod.tcp.send

import com.zuoyebang.iot.mod.tcp.TcpLog
import com.zuoyebang.iot.mod.tcp.data.TcpDataBean
import com.zuoyebang.iot.mod.tcp.inter.DataState
import com.zuoyebang.iot.mod.tcp.inter.ISend
import java.util.concurrent.LinkedBlockingQueue

internal class SendImpl(private val sendQueue: LinkedBlockingQueue<TcpDataBean>) : ISend {
    override fun sendTcpData(data: TcpDataBean) {
        offer(data)
    }

    override fun sendIfNotExists(data: TcpDataBean) {
        if (!contains(data)) {
            offer(data)
        }
    }

    operator fun contains(data: TcpDataBean?): Boolean {
        return sendQueue.contains(data)
    }

    private fun offer(data: TcpDataBean) {
        //取消的消息,直接丢弃
        if (data.state === DataState.Canceled) {
            TcpLog.d(TAG, "dataWrapper state = Canceled, do not put it into priorityBlockingQueue")
            return
        }
        data.state = DataState.WaitSend
        sendQueue.offer(data)
    }

    override fun sendByPriority(data: TcpDataBean) {
        data.state = DataState.WaitSend
        val originalData = sendQueue.toTypedArray()
        sendQueue.clear()
        sendQueue.add(data)
        sendQueue.addAll(originalData)
    }

    companion object {
        private val TAG = SendImpl::class.java.simpleName
    }

}