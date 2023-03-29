package com.zuoyebang.iot.mid.tcp.bean.base

import com.zuoyebang.iot.mid.tcp.TcpPacket
import com.zuoyebang.iot.mid.tcp.utils.JsonUtils
import com.zuoyebang.iot.mod.tcp.inter.TcpMessage

/**
 * 向服务端发送的Tcp消息的基类
 * TcpSBean 对应TcpPacket中的 PayLoad 部分
 *
 * 0 bytes   1        2         3         4                           length+3
 * +---------+---------+---------+---------+---------+ +---------+---------+
 * |           length*           | version | type    | | Payload ***       |
 * +---------+---------+---------+---------+---------+ +---------+---------+
 *
 */
interface TcpSendBean : TcpMessage {
    /**
     * TcpSBean的唯一标识
     */
    fun getPrivateTag(): String

    /**
     * 转换成TcpData
     */
    fun toTcpPacket(): TcpPacket

    fun getTypeTag(): String {
        return this::class.java.simpleName
    }

    private fun toJsonStr(): String? {
        return JsonUtils.toJson(this)
    }

    fun toBytes(): ByteArray {
        return toJsonStr()?.toByteArray() ?: ByteArray(0)
    }
}