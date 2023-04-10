package com.yc.tcp.inter

import com.yc.tcp.data.TcpDataBean
import java.io.DataInputStream
import java.io.Serializable
import java.lang.Exception

typealias TcpMessageCallBack = (TcpMessage) -> Unit
typealias TcpStateCallBack = (TcpState) -> Unit
typealias DisconnectTcp = () -> Unit
typealias MessageDispatchCallBack = (tcpData: TcpDataBean) -> Unit
typealias TcpLogDDelegate = (tag: String, msg: String, appendStackIndex: Int) -> Unit
typealias TcpLogDelegate = (tag: String, msg: String, e: Throwable?, appendStackIndex: Int) -> Unit
typealias TcpLogIndelicate = (tag: String, msg: String, appendStackIndex: Int) -> Unit

interface ITcpConfig{
    fun getServerHost(): String
    fun getServerPort(): Int
}

interface ITcpSocket {
    fun isTcpConnected(): Boolean
    fun connect(ip: String?, port: Int, timeOut: Int)
    fun connectTls(ip: String?, port: Int, timeOut: Int)
    fun disconnect()
    fun write(buffer: ByteArray?): Boolean
    fun read(receive: IReceive, readCallback: ReadCallBack, readError: (TcpError, String?) -> Unit)
    fun getInputStream(): DataInputStream?
}

interface IReceive {
    fun receive(data: TcpDataBean)
}

interface ISend {
    //直接将TcpData加入到发送队列
    fun sendTcpData(data: TcpDataBean)
    //去重发送,如果队列中存在相同tag的tcpData,则不重复发送
    fun sendIfNotExists(data: TcpDataBean)
    //将tcpData 加入到发送队列头部,优先发送
    fun sendByPriority(data: TcpDataBean)
}

interface SocketAdapter {
    fun read(mInput: DataInputStream?, receive: IReceive, readCallback: ReadCallBack)
}

interface TcpEventListener {
    fun onSendSuccess(tcpMessage: TcpMessage)
    fun onSendFailed(tcpMessage: TcpMessage, exception: Exception?)
}


interface TcpMessage : Serializable{

}

interface TcpListener {
    fun onConnectState(success: Boolean)
    fun readSuccess(data: TcpDataBean)
    fun readFailure(e: Exception?)
    fun writeSuccess(data: TcpDataBean)
    fun writeFailure(data: TcpDataBean, e: Exception?)
    fun onDispatchMsg(tcpData: TcpDataBean)
}