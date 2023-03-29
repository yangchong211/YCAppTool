package com.zuoyebang.iot.mid.tcp.convert

import com.yc.logclient.LogUtils
import com.zuoyebang.iot.mid.tcp.TcpPacket
import com.zuoyebang.iot.mid.tcp.TcpType
import com.zuoyebang.iot.mid.tcp.bean.send.*
import com.zuoyebang.iot.mod.tcp.inter.TcpMessage
import java.lang.Exception

class SendTcpConverter : TcpConverter {

    companion object {
        val TAG: String = SendTcpConverter::class.java.simpleName
    }

    override fun packet2TcpMessage(tcpPacket: TcpPacket): TcpMessage? {
        return try {
            tcpPacket.parseSend()
        } catch (e: Exception) {
            LogUtils.d("SendTcpConverter exception:${e.localizedMessage}")
            null
        }
    }

    private fun TcpPacket.parseSend(): TcpMessage? {
        val jsonStr = String(this.payload)
        LogUtils.e(TAG, "tcp发送的数据：$jsonStr")
        return when (this.type) {
            TcpType.HEART_BEAT_PING -> {//server端 心跳pong包
                TcpPing()
            }
            else -> {
                null
            }
        }
    }

}