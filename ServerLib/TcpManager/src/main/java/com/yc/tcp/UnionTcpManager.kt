package com.yc.tcp


import com.yc.logclient.LogUtils
import com.yc.tcp.bean.send.TcpLogin
import com.yc.tcp.socket.TcpSocketAdapter
import com.yc.tcp.bean.send.TcpPing
import com.yc.tcp.convert.ReceiveTcpConvertor
import com.yc.tcp.convert.SendTcpConverter
import com.yc.tcp.inter.SocketAdapter
import com.yc.tcp.inter.TcpMessage
import java.lang.Exception

/**
 * 主要职责:负责具体业务tcp的发送和解析分发
 * - 暴露方法(可带参数) 将业务需要的tcp消息 转化为 TcpPacket 数据帧结构(见sendLogin()方法) -> 通过sendTcpPacket() 进行消息发送
 * - 接收sever端的tcp消息, 对TcpPacket 解析成相应的实体,进行分发 (见disPatchTcpPacket()方法)
 */
class UnionTcpManager : TcpManager() {

    companion object {
        val TAG: String = UnionTcpManager::class.java.simpleName
    }

    private var sendTcpConverter: SendTcpConverter = SendTcpConverter()
    private var receiveTcpConverter: ReceiveTcpConvertor = ReceiveTcpConvertor()

    //*******************发送指令***********************//
    /**
     * 发送心跳Ping
     */
    override fun sendHeartPing() {
        sendTcpPacket(TcpPing().toTcpPacket())
    }

    /**
     * 发送Login指令
     */
    override fun sendLogin() {
        sendTcpPacket(
            TcpLogin(
                udid = TcpFacade.getExtra(TcpFacade.UDID) as String,
                token = TcpFacade.getDynamicInfo(TcpFacade.TOKEN) as String,
                device_type = 1,
                stamp = System.currentTimeMillis(),
                app_version = TcpFacade.getDynamicInfo(TcpFacade.VERSION) as String,
            ).toTcpPacket()
        )
    }

    /**
     * 指令解析适配器,用于解析ByteArray字节码流
     */
    override fun getAdapter(): SocketAdapter {
        return TcpSocketAdapter()
    }

    /**
     * 收到TcpPacket后进行事件解析和分发
     */
    override fun disPatchTcpPacket(tcpPacket: TcpPacket) {
        LogUtils.d(TAG, "Tcp_receive :${tcpPacket}")
        receiveTcpConverter.packet2TcpMessage(tcpPacket)?.run {
            //事件分发
            doDispatchMessage(this)
        }
    }

    override fun onTcpPacketSendFailed(tcpPacket: TcpPacket, exception: Exception?) {
        LogUtils.d(TAG, "Tcp_sendFailed :${tcpPacket},exception:${exception?.localizedMessage}")
        TcpContext.apply {
            sendTcpConverter.packet2TcpMessage(tcpPacket)?.let {
                onMessageSendFailed(it, exception)
            }
        }
    }

    override fun onTcpPacketSendSuccess(tcpPacket: TcpPacket) {
        LogUtils.d(TAG, "Tcp_sendSuccess:${tcpPacket}")
        TcpContext.apply {
            sendTcpConverter.packet2TcpMessage(tcpPacket)?.let {
                onMessageSendSuccess(tcpMessage = it)
            }
        }
    }

    private fun doDispatchMessage(tcpMsg: TcpMessage) {
        TcpContext.onMessage(tcpMsg)
    }

}