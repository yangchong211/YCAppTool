package com.zuoyebang.iot.mod.tcp.receive

import com.zuoyebang.iot.mod.tcp.data.TcpDataBean
import com.zuoyebang.iot.mod.tcp.inter.IReceive
import java.util.concurrent.LinkedBlockingQueue


internal class ReceiveImpl(private val mBlockingQueue: LinkedBlockingQueue<TcpDataBean>) : IReceive {

    override fun receive(data: TcpDataBean) {
        mBlockingQueue.add(data)
        //TcpLog.d(TAG, "receive:${data},queue.size:${mBlockingQueue.size}")
    }

    companion object {
        private val TAG = ReceiveImpl::class.java.simpleName
    }

}