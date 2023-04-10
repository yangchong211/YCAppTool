package com.yc.tcp

import androidx.annotation.IntDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


@IntDef(
    flag = true,
    value = [TcpType.TCP_ERROR, TcpType.HEART_BEAT_PING]
)
@Retention(RetentionPolicy.SOURCE)
annotation class TcpType {

    companion object {
        /**
         * Tcp 通用错误 Type
         */
        const val TCP_ERROR = 0
        /**
         * 设备心跳ping包
         */
        const val HEART_BEAT_PING = 1
        /**
         * 服务端心跳pong包
         */
        const val HEART_BEAT_PONG = 2
        const val LOGIN = 80
    }
}


@IntDef(
    flag = true,
    value = [TcpSubType.TCP_COMMON_DIALOG]
)
@Retention(RetentionPolicy.SOURCE)
annotation class TcpSubType {

    companion object {
        /**
         * 通用对话框提示
         */
        const val TCP_COMMON_DIALOG = 5
    }
}

@IntDef(
    flag = true,
    value = [TcpHandlerMessage.SEND_PING, TcpHandlerMessage.SERVER_PONG_TIMEOUT,
        TcpHandlerMessage.DISABLE_SOCKET, TcpHandlerMessage.ENABlE_SOCKET]
)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class TcpHandlerMessage {
    companion object {
        const val SEND_PING = 1//发送ping消息
        const val SERVER_PONG_TIMEOUT = 2//等待Server端pong消息超时
        const val DISABLE_SOCKET = 3//阻塞Tcp连接线程,并停用重连机制
        const val ENABlE_SOCKET = 4//开启连接线程,tcp重连机制启动
    }
}