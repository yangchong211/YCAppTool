package com.yc.tcp

import com.yc.logclient.LogUtils
import com.yc.tcp.data.TcpDataBean
import com.yc.tcp.inter.*
import com.yc.tcp.receive.ReceiveProxy
import com.yc.tcp.send.SendProxy
import com.yc.tcp.thread.TcpConnectThread
import com.yc.tcp.thread.TcpDispatcherThread
import com.yc.tcp.thread.TcpWriteThread
import kotlin.system.measureTimeMillis

/**
 *
 * 组成:
 * 1、待发送数据队列
 * 2、接收数据队列
 *
 * 3、TCP 状态:未知、未连接、连接中、连接出错
 *
 * 4、线程:
 * - 写数据线程: 从待发送队writeQueue中取出数据包,写入tcp outputStream
 * - 连接线程: 负责Tcp连接,以及从Tcp inputStream中 读取数据包，放入readQueue
 * - 消息分发线程: 从readQueue中都读取数据，进行消息分发
 *
 * 5、支持
 * TcpData 取消发送(未发出去之前)
 * TcpData 发送失败重试
 *
 *
 */
class TcpCore(private val socket: ITcpSocket) {

    private var send: ISend? = null
    private var receive: IReceive? = null
    private var mTcpConnectThread: TcpConnectThread? = null
    private var mTcpWriteThread: TcpWriteThread? = null
    private var mDispatcherThread: TcpDispatcherThread? = null
    private var mTcpListener: TcpListener? = null

    private val connectCallback: ConnectCallBack = object : ConnectCallBack {
        override fun connectSuccess() {
            //连接成功,需要发送登录和ping
            mTcpWriteThread?.notifyConnect()
            mTcpListener?.onConnectState(true)
        }

        override fun connectFail(exception: Exception) {
            LogUtils.d(TAG, "connectFail:${exception.localizedMessage}")
            mTcpListener?.onConnectState(false)
        }

    }
    private val writeCallback: WriteCallBack = object : WriteCallBack {
        override fun writeSuccess(data: TcpDataBean) {
            mTcpListener?.writeSuccess(data)
        }

        override fun writeError(data: TcpDataBean, e: Exception?) {
            //写失败后,进行重连操作
            mTcpConnectThread?.resetConnect(TcpError.ERROR_WRITE, e!!.message!!)
            mTcpListener?.writeFailure(data, e)
        }
    }
    private val readCallback: ReadCallBack = object : ReadCallBack {
        override fun readSuccess(tcpData: TcpDataBean) {
            //读成功,更新ping操作 - 去掉无效的ping操作
            mTcpListener?.readSuccess(tcpData)
        }

        override fun readError(e: Exception?) {
            mTcpListener?.readFailure(e)
        }
    }

    init {
        //send()方法 ,将一条帧数据 放到优先队列中.
        send = SendProxy(TcpContext.sendQueue)
        //收到一条消息,就保存到readQueue队列中.
        receive = ReceiveProxy(TcpContext.readQueue)
        LogUtils.d("TcpCore was init: ${Thread.currentThread().name}")
    }


    private fun checkThread() {
        //一个线程 : 负责连接tcp, 和读循环(死循环)，read数据,  读取的数据通过receive() 将byte[] 添加到接收队列
        if (mTcpConnectThread == null) {
            mTcpConnectThread = TcpConnectThread(
                socket,
                TcpContext,
                connectCallback,
                receive!!,
                readCallback
            )
        }

        //一个线程 :从sendQueue中取出一条数据,写入到socket中
        if (mTcpWriteThread == null) {
            mTcpWriteThread = TcpWriteThread(
                socket,
                TcpContext.sendQueue,
                writeCallback
            )
        }

        // 数据分发线程
        if (mDispatcherThread == null) {
            mDispatcherThread = TcpDispatcherThread(TcpContext.readQueue) {
                mTcpListener?.onDispatchMsg(it)
            }
        }
    }

    /**
     * 开启Tcp 线程
     */
    fun start() {
        LogUtils.d(TAG, "TcpCore start:" + Thread.currentThread().name)
        checkThread()
        mTcpConnectThread?.start()
        mTcpWriteThread?.start()
        mDispatcherThread?.start()
    }

    fun release() {
        LogUtils.d(TAG, "release():" + Thread.currentThread().name)
        mTcpConnectThread?.quit()
        mTcpWriteThread?.quit()
        mDispatcherThread?.quit()
        mTcpConnectThread = null
        mTcpWriteThread = null
    }

    fun setTcpListener(listener: TcpListener) {
        mTcpListener = listener
    }

    fun send(tcpData: TcpDataBean) {
        send?.sendTcpData(tcpData)
    }

    /**
     * 启动tcp连接 或直接断开tcp连接
     * 主要供外部控制调用
     * isEnable = false 断开tcp连接, isEnable = true 重新建立Tcp连接
     */
    fun setEnable(isEnable: Boolean) {
        val time = measureTimeMillis {
            mTcpConnectThread?.setEnable(isEnable)
        }
        LogUtils.d(TAG, "setEnable:$isEnable,spent:${time} ms")
    }


    /**
     * Tcp 是否连接
     */
    fun isTcpConnected(): Boolean = socket.isTcpConnected()

    /**
     * 服务端心跳超时,断开socket,自动进行二次重连
     */
    fun resetConnectPongTimeOut() {
        resetConnect(tcpError = TcpError.ERROR_PONG_TIMEOUT, errorMessage = "服务端Pong心跳超时")
    }

    private fun resetConnect(tcpError: TcpError, errorMessage: String?) {
        mTcpConnectThread?.resetConnect(tcpError, errorMessage)
    }


    fun findTcpDataInQueue(privateTag: String): TcpDataBean? {
        return TcpContext.sendQueue.find {
            it.tag == privateTag
        }
    }

    fun findTcpDataInQueByTypeTag(typeTag: String): TcpDataBean? {
        return TcpContext.sendQueue.find {
            it.tag.contains(typeTag)
        }.apply {
            LogUtils.d("findTcpDataInQueByTypeTag:${typeTag},sendQueue:${TcpContext.sendQueue.size},${this}")
        }
    }

    fun isTcpDataInQueByTypeTag(typeTag: String): Boolean {
        return findTcpDataInQueByTypeTag(typeTag) != null
    }

    companion object {
        private val TAG = TcpCore::class.java.simpleName
    }
}

