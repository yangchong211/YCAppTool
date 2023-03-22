package com.zuoyebang.iot.mid.tcp.bean.send

import com.google.gson.annotations.SerializedName
import com.zuoyebang.iot.mid.tcp.TcpPacket
import com.zuoyebang.iot.mid.tcp.TcpType
import com.zuoyebang.iot.mid.tcp.bean.base.TcpSBean

/**
 * 创建者:baixuefei
 * 创建日期:2021/5/12 9:03 PM
 */

data class TcpSGeneralProtocol<T>(
    @SerializedName("sub_type")
    val subType: Int,
    @SerializedName("data")
    val data: T
) : TcpSBean {
    override fun getPrivateTag(): String {
        return "${getTypeTag()}_${System.currentTimeMillis()}"
    }

    override fun getTypeTag(): String {
        return StringBuffer().append(super.getTypeTag()).append("#")
            .append(data!!::class.java.simpleName).toString()
    }

    override fun toTcpPacket(): TcpPacket {
        return TcpPacket(
            privateTag = getPrivateTag(),
            type = TcpType.GENERAL_PROTOCOL,
            payload = toBytes()
        )
    }
}