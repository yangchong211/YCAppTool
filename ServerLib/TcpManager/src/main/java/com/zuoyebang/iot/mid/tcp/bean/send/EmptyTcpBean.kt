package com.zuoyebang.iot.mid.tcp.bean.send

import com.zuoyebang.iot.mid.tcp.TcpPacket
import com.zuoyebang.iot.mid.tcp.bean.base.TcpSBean

/**
 * 创建者:baixuefei
 * 创建日期:2021/8/24 10:21 上午
 */
class EmptyTcpBean : TcpSBean {
    override fun getPrivateTag(): String {
        return "${getTypeTag()}_${System.currentTimeMillis()}"
    }

    override fun toTcpPacket(): TcpPacket {
        return TcpPacket(
            privateTag = getPrivateTag(),
            length = 3,
            type = -1,
            payload = ByteArray(0)
        )
    }
}