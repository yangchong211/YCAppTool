package com.zuoyebang.iot.mid.tcp


import com.yc.logclient.LogUtils
import com.zuoyebang.iot.mod.tcp.socket.TcpSocketAdapter
import com.zuoyebang.iot.mid.tcp.bean.receive.*
import com.zuoyebang.iot.mid.tcp.bean.send.sub.TcpLampControl
import com.zuoyebang.iot.mid.tcp.bean.send.TcpLogin
import com.zuoyebang.iot.mid.tcp.bean.send.TcpPing
import com.zuoyebang.iot.mid.tcp.bean.send.TcpSGeneralProtocol
import com.zuoyebang.iot.mid.tcp.convert.ReceiveTcpConvertor
import com.zuoyebang.iot.mid.tcp.convert.SendTcpConverter
import com.zuoyebang.iot.mod.tcp.TcpContext
import com.zuoyebang.iot.mod.tcp.inter.SocketAdapter
import com.zuoyebang.iot.mod.tcp.inter.TcpMessage
import java.lang.Exception

/**
 * 创建者:baixuefei
 * 创建日期:2021/3/30 1:03 PM
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
     * 发送台灯控制指令
     */
    fun sendLampControl(device_id: Long, brightness: Int, colour: Int, on: Int, locked: Int) {
        sendTcpPacket(
            TcpSGeneralProtocol(
                subType = TcpSubType.LAMP_CONTROL,
                data = TcpLampControl(device_id, brightness, colour, on, locked)
            ).toTcpPacket()
        )
    }

    /**
     * 发送台灯控制指令
     */
    fun sendLampControl(lampControl: TcpLampControl) {
        sendTcpPacket(
            TcpSGeneralProtocol(
                subType = TcpSubType.LAMP_CONTROL,
                data = lampControl,
            ).toTcpPacket()
        )
    }

    fun sendCall(req: Any) {
        sendTcpPacket(
            TcpSGeneralProtocol(
                subType = TcpSubType.CALL,
                data = req,
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

            //需要特殊处理的TcpPacket,在此处理
            when (this) {
                is TcpLoginRes -> {
                    if (status != 1) { //登录失败
                        when (error_code) {
                            1 -> { //账号已在别处登录，请重新登录
                                doDispatchMessage(TcpKickOff(code = error_code, msg = msg))
                            }
                            2 -> {//签名认证失败，请稍后重试

                            }
                            3 -> {//服务不可用，请稍后重试
                                enableTcp(false)
                                enableTcp(true, 4000)
                            }
                            6 -> { //缺少参数

                            }
                        }
                    }
                    mHasLogin = status == 1
                }
                is TcpKickOff -> {
                    mHasLogin = false
                }

                is TcpSGeneralProtocol<*> -> {
                    if (this.subType == TcpSubType.LAMP_CONTROL) {
                        var lampControl = this.data as TcpLampControl
                    }
                }
            }

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


    fun fakeKickOffByHttp() {
        doDispatchMessage(TcpKickOff(code = 401, "http_kick_off"))
    }

}