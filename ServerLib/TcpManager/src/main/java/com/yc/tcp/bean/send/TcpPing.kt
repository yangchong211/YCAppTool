package com.yc.tcp.bean.send

import com.yc.tcp.TcpPacket
import com.yc.tcp.TcpType
import com.yc.tcp.bean.base.TcpSendBean


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