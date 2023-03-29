package com.zuoyebang.iot.mid.tcp.bean.send

import com.zuoyebang.iot.mid.tcp.TcpPacket
import com.zuoyebang.iot.mid.tcp.TcpType
import com.zuoyebang.iot.mid.tcp.bean.base.TcpSendBean


class TcpPing : TcpSendBean {

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