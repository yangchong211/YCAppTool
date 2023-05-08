package com.yc.tcp.socket

import com.yc.logclient.LogUtils
import com.yc.tcp.TcpContext
import com.yc.tcp.inter.*
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
        //InetSocketAddress是在InetAddress基础上封装了端口号。所以说InetSocketAddress是(IP地址+端口号)类型，也就是端口地址类型。
        //表面看InetSocketAddress多了一个端口号，端口的作用：一台拥有IP地址的主机可以提供许多服务，比如Web服务、FTP服务、SMTP服务等，这些服务完全可以通过1个IP地址来实现。
        //那么主机怎么区分不同的网络服务？显然不能只靠IP地址，因此IP地址与网络服务的关系是一对多的关系。
        //实际上是通过"IP地址+端口号"来区分不同的服务的。
        val address: SocketAddress = InetSocketAddress(ip, port)
        //设置tcp无延迟
        mSocket?.tcpNoDelay = true
        // 连接指定IP和端口
        mSocket?.connect(address, timeOut)
        LogUtils.e(TAG, "TCP连接成功: ip=$ip port=$port,socket:${this}-${System.identityHashCode(this)}")
        //判断socket是否是连接状态
        if (isTcpConnected()) {
            // 获取网络输出流
            mOutput = DataOutputStream(mSocket?.getOutputStream())
            // 获取网络输入流
            mInput = DataInputStream(mSocket?.getInputStream())
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
                // 获取网络输出流
                mOutput = DataOutputStream(getOutputStream())
                // 获取网络输入流
                mInput = DataInputStream(getInputStream())
                LogUtils.i(TAG, "TCP连接成功 tls: ip=$ip port=$port ,socket:${this}-${System.identityHashCode(this)}")
            }
        }
    }

    /**
     * 判断socket是否是连接状态
     */
    override fun isTcpConnected(): Boolean {
        return if (mSocket == null || mSocket!!.isClosed) false else mSocket!!.isConnected
    }

    /**
     * 断开链接
     */
    @Synchronized
    override fun disconnect() {
        LogUtils.i(TAG, "disconnect 断开Tcp连接 ,socket:${this}-${System.identityHashCode(this)}")
        try {
            //断开 客户端发送到服务器 的连接，即关闭输出流对象OutputStream
            mOutput?.close()
            //断开 服务器发送到客户端 的连接，即关闭输入流读取器对象InputStream
            mInput?.close()
            mSocket?.apply {
                //最终关闭整个Socket连接
                if (!isClosed) close()
                LogUtils.i(TAG, "socket.close()")
            }
        } catch (e: SocketException) {
            LogUtils.e(TAG, e)
        } catch (e: IOException) {
            LogUtils.e(TAG, e)
        } catch (e: Exception) {
            LogUtils.e(TAG, e)
        } finally {
            mOutput = null
            mInput = null
            mSocket = null
        }
    }

    /**
     * 向tcp写数据
     */
    @Synchronized
    override fun write(buffer: ByteArray?): Boolean {
        //从Socket 获得输出流对象 OutputStream
        //mOutput 该对象作用：发送数据
        return if (mOutput != null) {
            //写入需要发送的数据到输出流对象中
            mOutput?.write(buffer)
            //发送数据到服务端
            mOutput?.flush()
            true
        } else {
            false
        }
    }

    /**
     * 从tcp 读数据,没有socket没有数据时,会阻塞在read()
     */
    override fun read(receive: IReceive, readCallback: ReadCallBack,
                      readError: (TcpError, String?) -> Unit) {
        //步骤1：创建输入流对象InputStream。这个是mInput
        try {
            // 步骤2：创建输入流读取器对象 并传入输入流对象
            // 该对象作用：获取服务器返回的数据
            mAdapter.read(mInput!!, receive, readCallback)
        } catch (e: Exception) {
            LogUtils.e(TAG, e)
            readCallback.readError(e)
            readError.invoke(TcpError.ERROR_READ, e.localizedMessage)
        }
        readError.invoke(TcpError.ERROR_READ_END,
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

