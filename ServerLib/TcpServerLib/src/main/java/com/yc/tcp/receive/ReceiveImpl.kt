package com.yc.tcp.receive

import com.yc.logclient.LogUtils
import com.yc.tcp.data.TcpDataBean
import com.yc.tcp.inter.IReceive
import java.util.concurrent.LinkedBlockingQueue


internal class ReceiveImpl(private val mBlockingQueue: LinkedBlockingQueue<TcpDataBean>) : IReceive {

    override fun receive(data: TcpDataBean) {
        mBlockingQueue.add(data)
        LogUtils.d(TAG, "receive:${data},queue.size:${mBlockingQueue.size}")
    }

    companion object {
        private val TAG = ReceiveImpl::class.java.simpleName
    }

}