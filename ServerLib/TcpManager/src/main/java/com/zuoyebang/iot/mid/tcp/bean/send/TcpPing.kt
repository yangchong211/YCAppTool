package com.zuoyebang.iot.mid.tcp.bean.send

import com.zuoyebang.iot.mid.tcp.TcpPacket
import com.zuoyebang.iot.mid.tcp.TcpType
import com.zuoyebang.iot.mid.tcp.bean.base.TcpSBean

/**
 * 创建者:baixuefei
 * 创建日期:2021/3/30 1:53 PM
 */
class TcpPing : TcpSBean {
    override fun getPrivateTag(): String {
        return "${getTypeTag()}_${System.currentTimeMillis()}"
    }

    override fun toTcpPacket(): TcpPacket {
        return TcpPacket(
            privateTag = getPrivateTag(),
            type = TcpType.HEART_BEAT_PING,
            payload = toBytes()
        )
    }

    override fun toString(): String {
        return this::class.java.simpleName
    }
}