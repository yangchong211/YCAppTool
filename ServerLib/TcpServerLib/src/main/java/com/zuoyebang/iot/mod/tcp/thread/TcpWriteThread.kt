package com.zuoyebang.iot.mod.tcp.thread

import com.zuoyebang.iot.mod.tcp.TcpConfig
import com.zuoyebang.iot.mod.tcp.TcpLog
import com.zuoyebang.iot.mod.tcp.inter.WriteCallBack
import com.zuoyebang.iot.mod.tcp.data.TcpDataBean
import com.zuoyebang.iot.mod.tcp.inter.DataState
import com.zuoyebang.iot.mod.tcp.inter.ITcpSocket
import com.zuoyebang.iot.mod.tcp.strategy.WriteStrategy
import java.util.concurrent.LinkedBlockingQueue

class TcpWriteThread(
    private val mSocket: ITcpSocket,
    private val mDataQueue: LinkedBlockingQueue<TcpDataBean>,
    private val mWriteCallback: WriteCallBack
) : Thread("WriteThread") {
    private var mQuit = false
    private var mWriteStrategy: WriteStrategy? = null

    override fun run() {

        TcpLog.e(
            TAG,
            "${TcpConfig.THREAD_RUN},mQuit：${mQuit},${this}-${System.identityHashCode(this)}"
        )

        while (!mQuit) {

            var data: TcpDataBean? = null


            //tcp 未连接,则阻塞线程
            if (isInterrupted { mWriteStrategy?.checkTcpNoConnect(mSocket.isTcpConnected()) }) {
                if (mQuit) {
                    return
                }
                continue
            }

            //取出一枚Tcp数据
            if (isInterrupted { data = mDataQueue.take() }) {
                if (mQuit) {
                    return
                }
                continue
            }


            if (data != null) {
                try {
                    if (data!!.state == DataState.Canceled) {
                        TcpLog.d(
                            TAG,
                            "----- data was canceled,just continue} ------${this}-${
                                System.identityHashCode(this)
                            }"
                        )
                        continue
                    }
                    data!!.state = DataState.Sending
                    mSocket.write(data!!.bytes)
                    data!!.state = DataState.Sent
                    //TcpLog.d(TAG, "Tcp Write成功:${data}")
                    mWriteCallback.writeSuccess(data!!)
                } catch (e: Exception) {
                    TcpLog.e(TAG, "Tcp Write异常:${data},${this}-${System.identityHashCode(this)}", e)
                    //写失败的数据放到队列尾部
                    data?.apply {
                        increaseRetry()
                        if (!shouldBeDrop()) {
                            TcpLog.e(
                                TAG,
                                "重新放入发送队列:${data},:${this@TcpWriteThread}-${
                                    System.identityHashCode(this@TcpWriteThread)
                                }"
                            )
                            insert2QueueHead(this)

                        } else {
                            TcpLog.e(
                                TAG,
                                "数据写失败,达到最大重试次数,丢弃:${data},${this@TcpWriteThread}-${
                                    System.identityHashCode(this@TcpWriteThread)
                                }"
                            )
                            //多次写失败,向上层回调写写失败状态
                            mWriteCallback.writeError(data!!, e)
                        }
                    }
                    //写异常后,阻塞一定时间 才能尝试再次写入
                    if (isInterrupted { mWriteStrategy?.block(2000) }) {
                        if (mQuit) {
                            return
                        }
                    }
                }
            }
        }

        TcpLog.e(TAG, TcpConfig.THREAD_OVER)
    }


    private fun insert2QueueHead(data: TcpDataBean) {
        val originalDataQue = mDataQueue.toTypedArray()
        mDataQueue.clear()
        mDataQueue.offer(data)
        mDataQueue.addAll(originalDataQue)
    }

    /**
     * 退出线程
     */
    fun quit() {
        mQuit = true
        interrupt()
    }

    /**
     * TCP 连接建立,释放阻塞点
     */
    fun notifyConnect() {
        mWriteStrategy?.freeBlock()
    }

    companion object {
        private val TAG = TcpWriteThread::class.java.simpleName
    }
}

/**
 * 检查是否发生InterruptedException 异常
 */
fun isInterrupted(block: () -> Unit): Boolean {
    var exceptionHappened = false
    try {
        block.invoke()
    } catch (e: InterruptedException) {
        e.printStackTrace()
        exceptionHappened = true
    }
    return exceptionHappened
}