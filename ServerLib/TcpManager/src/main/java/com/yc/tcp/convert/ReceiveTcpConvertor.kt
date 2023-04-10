package com.yc.tcp.convert

import android.text.TextUtils
import com.yc.logclient.LogUtils
import com.yc.tcp.TcpPacket
import com.yc.tcp.TcpType
import com.yc.tcp.bean.receive.*
import com.yc.tcp.utils.JsonUtils
import com.yc.tcp.inter.TcpMessage

class ReceiveTcpConvertor : TcpConverter {

    companion object {
        val TAG: String = ReceiveTcpConvertor::class.java.simpleName
    }

    override fun packet2TcpMessage(tcpPacket: TcpPacket): TcpMessage? {
        return try {
            tcpPacket.parseReceive()
        } catch (e: Exception) {
            LogUtils.d("ReceiveTcpConvertor exception:${e.localizedMessage}")
            null
        }
    }

    private fun TcpPacket.parseReceive(): TcpMessage? {
        val jsonStr = String(this.payload)
        if (!TextUtils.isEmpty(jsonStr)) {
            LogUtils.e(TAG, "tcp接收的数据：${jsonStr}")
        } else {
            LogUtils.e(TAG, "tcp接收的数据为空")
        }
        return when (this.type) {
            TcpType.TCP_ERROR -> {  //错误处理
                JsonUtils.fromJson(jsonStr, TcpError::class.java)
            }
            TcpType.HEART_BEAT_PONG -> {//server端 心跳pong包
                TcpPong()
            }
            else -> {
                null
            }
        }
    }

}