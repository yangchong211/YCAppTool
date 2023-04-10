package com.yc.tcp.receive

import com.yc.tcp.data.TcpDataBean
import com.yc.tcp.inter.IReceive

import java.util.concurrent.LinkedBlockingQueue


class ReceiveProxy(queue: LinkedBlockingQueue<TcpDataBean>) : IReceive {

    private val impl: IReceive
    override fun receive(data: TcpDataBean) {
        impl.receive(data)
    }

    init {
        impl = ReceiveImpl(queue)
    }
}