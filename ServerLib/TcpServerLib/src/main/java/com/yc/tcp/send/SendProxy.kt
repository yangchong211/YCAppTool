package com.yc.tcp.send

import com.yc.tcp.data.TcpDataBean
import com.yc.tcp.inter.ISend
import java.util.concurrent.LinkedBlockingQueue


class SendProxy(priorityBlockingQueue: LinkedBlockingQueue<TcpDataBean>) : ISend {

    private var sendImpl: SendImpl = SendImpl(priorityBlockingQueue)

    override fun sendTcpData(data: TcpDataBean) {
        sendImpl.sendTcpData(data)
    }

    override fun sendIfNotExists(data: TcpDataBean) {
        sendImpl.sendIfNotExists(data)
    }

    override fun sendByPriority(data: TcpDataBean) {
        sendImpl.sendByPriority(data)
    }
}