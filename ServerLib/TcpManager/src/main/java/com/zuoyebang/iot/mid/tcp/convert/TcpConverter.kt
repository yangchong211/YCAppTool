package com.zuoyebang.iot.mid.tcp.convert

import com.zuoyebang.iot.mid.tcp.TcpPacket
import com.zuoyebang.iot.mod.tcp.inter.TcpMessage


interface TcpConverter {
    fun packet2TcpMessage(tcpPacket: TcpPacket): TcpMessage?
}