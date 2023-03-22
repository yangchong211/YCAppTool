package com.zuoyebang.iot.mid.tcp.ext

import com.zuoyebang.iot.mid.tcp.TcpHandlerMessage
import com.zuoyebang.iot.mid.tcp.TcpPacket
import com.zuoyebang.iot.mid.tcp.TcpSubType
import com.zuoyebang.iot.mid.tcp.TcpType
import com.zuoyebang.iot.mod.tcp.data.TcpDataBean


fun TcpDataBean.toTcpPacket(): TcpPacket {
    return TcpPacket.convertFromBytes(this.bytes)
}

fun Int.toTcpType(): String {
    return when (this) {
        TcpType.TCP_ERROR -> "TCP_ERROR(${this})"
        TcpType.HEART_BEAT_PING -> "HEART_BEAT_PING(${this})"
        TcpType.HEART_BEAT_PONG -> "HEART_BEAT_PONG(${this})"
        TcpType.KICK_OFF -> "KICK_OFF(${this})"
        TcpType.LOGIN -> "LOGIN(${this})"
        TcpType.GENERAL_PROTOCOL -> "GENERAL_PROTOCOL(${this})"
        else -> "UnKown(${this})"
    }
}

fun Int.toTcpSubType(): String {
    return when (this) {
        TcpSubType.LAMP_CHANGE_USER -> "LAMP_CHANGE_USER(${this})"
        TcpSubType.LAMP_CONTROL -> "LAMP_CONTROL(${this})"
        TcpSubType.BATCH_SEARCH_NEW_PICTURE -> "BATCH_SEARCH_NEW_PICTURE(${this})"
        else -> "UnKown(${this})"
    }
}

fun Int.toTcpMessage(): String {
    return when (this) {
        TcpHandlerMessage.SEND_PING -> "SEND_PING(${this})"
        TcpHandlerMessage.SERVER_PONG_TIMEOUT -> "RECEIVE_SERVER_PING_TIMEOUT(${this})"
        TcpHandlerMessage.DISABLE_SOCKET -> "DISABLE_SOCKET(${this})"
        TcpHandlerMessage.ENABlE_SOCKET -> "ENABlE_SOCKET(${this})"
        else -> "UnKnown(${this})"
    }
}