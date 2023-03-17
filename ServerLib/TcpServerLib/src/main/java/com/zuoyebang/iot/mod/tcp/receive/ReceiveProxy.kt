package com.zuoyebang.iot.mod.tcp.receive

import com.zuoyebang.iot.mod.tcp.data.TcpDataBean
import com.zuoyebang.iot.mod.tcp.inter.IReceive

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