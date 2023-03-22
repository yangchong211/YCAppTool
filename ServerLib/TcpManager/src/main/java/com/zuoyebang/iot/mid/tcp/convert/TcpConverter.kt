package com.zuoyebang.iot.mid.tcp.convert

import com.zuoyebang.iot.mid.tcp.TcpPacket
import com.zuoyebang.iot.mod.tcp.inter.TcpMessage

/**
 * 创建者:baixuefei
 * 创建日期:2021/8/24 10:18 上午
 */
interface TcpConverter {
    fun packet2TcpMessage(tcpPacket: TcpPacket): TcpMessage?
}