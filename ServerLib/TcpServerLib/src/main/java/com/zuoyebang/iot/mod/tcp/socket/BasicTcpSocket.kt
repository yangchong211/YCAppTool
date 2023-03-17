package com.zuoyebang.iot.mod.tcp.socket

import com.zuoyebang.iot.mod.tcp.TcpContext
import com.zuoyebang.iot.mod.tcp.TcpLog
import com.zuoyebang.iot.mod.tcp.inter.*
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress
import java.net.SocketException
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocket


class BasicTcpSocket(private val mAdapter: SocketAdapter) : ITcpSocket {

    companion object {
        val TAG: String = BasicTcpSocket::class.java.simpleName
    }

    private var mSocket: Socket? = null
    private var mInput: DataInputStream? = null
    private var mOutput: DataOutputStream? = null


    @Synchronized
    override fun connect(ip: String?, port: Int, timeOut: Int) {
        mSocket = Socket()
        val address: SocketAddress = InetSocketAddress(ip, port)
        mSocket!!.tcpNoDelay = true
        mSocket!!.connect(address, timeOut) // 连接指定IP和端口
        TcpLog.i(TAG, "TCP连接成功: ip=$ip port=$port,socket:${this}-${System.identityHashCode(this)}")
        if (isTcpConnected()) {
            mOutput = DataOutputStream(mSocket!!.getOutputStream()) // 获取网络输出流
            mInput = DataInputStream(mSocket!!.getInputStream()) // 获取网络输入流
        }
    }

    @Synchronized
    override fun connectTls(ip: String?, port: Int, timeOut: Int) {

        val context: SSLContext = SSLContext.getInstance("SSL")
        context.init(null, TcpContext.mTrustManagerProvider.create(), null)
        val socketFactory = context.socketFactory

        mSocket = socketFactory.createSocket(ip, port) as SSLSocket
        mSocket?.apply {
            //设置超时时间
            //soTimeout = 10000
            tcpNoDelay = true
            if (isTcpConnected()) {
                mOutput = DataOutputStream(getOutputStream()) // 获取网络输出流
                mInput = DataInputStream(getInputStream()) // 获取网络输入流
                TcpLog.i(
                    TAG,
                    "TCP连接成功 tls: ip=$ip port=$port ,socket:${this}-${System.identityHashCode(this)}"
                )
            }
        }

    }

    override fun isTcpConnected(): Boolean {
        return if (mSocket == null || mSocket!!.isClosed) false else mSocket!!.isConnected
    }


    @Synchronized
    override fun disconnect() {
        TcpLog.i(TAG, "disconnect 断开Tcp连接 ,socket:${this}-${System.identityHashCode(this)}")
        try {
            mOutput?.close()
            mInput?.close()
            mSocket?.apply {
                if (!isClosed) close()
                TcpLog.i(TAG, "socket.close()")
            }
        } catch (e: SocketException) {
            TcpLog.e(TAG, e)
        } catch (e: IOException) {
            TcpLog.e(TAG, e)
        } catch (e: Exception) {
            TcpLog.e(TAG, e)
        } finally {
            mOutput = null
            mInput = null
            mSocket = null
        }
    }

    //向tcp写数据
    @Synchronized
    override fun write(buffer: ByteArray?): Boolean {
        return if (mOutput != null) {
            mOutput?.write(buffer)
            mOutput?.flush()
            true
        } else {
            false
        }
    }

    //从tcp 读数据,没有socket没有数据时,会阻塞在read()
    override fun read(
        receive: IReceive,
        readCallback: ReadCallBack,
        readError: (TcpError, String?) -> Unit
    ) {
        try {
            mAdapter.read(mInput!!, receive, readCallback)
        } catch (e: Exception) {
            TcpLog.e(TAG, e)
            readCallback.readError(e)
            readError.invoke(TcpError.ERROR_READ, e.localizedMessage)
        }
        readError.invoke(
            TcpError.ERROR_READ_END,
            "socket stream read over ,socket:${this}-${System.identityHashCode(this)}"
        )
    }

    override fun getInputStream(): DataInputStream? {
        return if (isTcpConnected()) {
            mInput
        } else {
            null
        }
    }

}

