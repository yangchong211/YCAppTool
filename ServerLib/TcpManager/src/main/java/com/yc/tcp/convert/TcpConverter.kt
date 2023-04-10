package com.yc.tcp.convert

import com.yc.tcp.TcpPacket
import com.yc.tcp.inter.TcpMessage


interface TcpConverter {
    fun packet2TcpMessage(tcpPacket: TcpPacket): TcpMessage?
}