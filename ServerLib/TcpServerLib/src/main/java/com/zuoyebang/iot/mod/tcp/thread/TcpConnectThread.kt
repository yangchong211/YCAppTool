package com.zuoyebang.iot.mod.tcp.thread

import com.zuoyebang.iot.mod.tcp.TcpConfig
import com.zuoyebang.iot.mod.tcp.TcpContext
import com.zuoyebang.iot.mod.tcp.TcpLog
import com.zuoyebang.iot.mod.tcp.inter.*
import com.zuoyebang.iot.mod.tcp.strategy.ConnectStrategy

/**
 * ConnectThread 完成两件事
 * (1)tcp的连接操作,以及连接成功与失败的回调
 * (2)连接成功后,向"接收队列"中循环入栈数据帧,并反馈读取数据成功与失败的状态
 */
class TcpConnectThread(
    private val mSocket: ITcpSocket,
    private val tcpContext: TcpContext,
    private val mConnectCallback: ConnectCallBack,
    private val mReceive: IReceive,
    private val mReadCallback: ReadCallBack) : Thread("ConnectThread") {


    private var mQuit = false
    private var mConnStrategy: ConnectStrategy = ConnectStrategy()
    private var mException: Exception? = null

    //是否启动Tcp连接
    private var isEnable: Boolean = true

    override fun run() {

        while (!mQuit) {

            TcpLog.e(
                TAG,
                "${TcpConfig.THREAD_RUN},isEnable:${isEnable}:${this}-${System.identityHashCode(this)}"
            )
            try {
                setTcpState(TcpState.NOT_CONNECT)
                mConnStrategy.checkEnable(isEnable)
                //设置最长Tcp连接超时时长为3分钟
                setTcpState(TcpState.CONNECTING)
                TcpLog.d(TAG, "重新连接tcp...:${this}-${System.identityHashCode(this)}")
                mSocket.connectTls(
                    TcpConfig.getServerHost(),
                    TcpConfig.getServerPort(),
                    60 * 1000
                ) // 连接服务器
                mConnStrategy.reset()
            } catch (e: Exception) {
                mException = e
                e.printStackTrace()
            } finally {

            }

            TcpLog.d(
                TAG,
                "isException:${mException != null},${this}-${System.identityHashCode(this)}"
            )
            if (mException != null) {
                setTcpState(TcpState.NOT_CONNECT)
                resetConnect(TcpError.ERROR_CONNECT, mException!!.localizedMessage)
                mConnectCallback.connectFail(mException!!)
                mException = null
            } else { //连接成功
                setTcpState(TcpState.CONNECTED)
                mConnectCallback.connectSuccess()
                TcpLog.d(TAG, "enter mSocket.read ,${this}-${System.identityHashCode(this)}")
                mSocket.read(mReceive, mReadCallback) { type, msg ->
                    resetConnect(type, msg)
                }
                TcpLog.d(TAG, "exit mSocket.read ,${this}-${System.identityHashCode(this)}")
            }
        }

        TcpLog.e(
            TAG,
            "${TcpConfig.THREAD_OVER},isEnable:${isEnable},mQuit:${mQuit} ,${this}-${
                System.identityHashCode(this)
            }"
        )
    }


    fun resetConnect(type: TcpError, errorMsg: String?) {
        TcpLog.d(
            TAG,
            "resetConnect errorType:$type,errorMsg:$errorMsg ${this}-${System.identityHashCode(this)}"
        )
        setTcpState(TcpState.NOT_CONNECT)
        mSocket.disconnect()
    }

    fun quit() {
        mQuit = true
        setTcpState(TcpState.NOT_CONNECT)
        interrupt()
    }

    fun setEnable(enable: Boolean) {
        TcpLog.d("setEnable:${enable},curEnable:${isEnable}")
        if (isEnable != enable) {
            isEnable = enable
            mConnStrategy.setEnable(enable) {
                mSocket.disconnect()
                setTcpState(TcpState.NOT_CONNECT)
            }
        }
    }

    private fun setTcpState(state: TcpState) {
        tcpContext.updateTcpState(state)
    }

    companion object {
        private val TAG = TcpConnectThread::class.java.simpleName
    }

}

