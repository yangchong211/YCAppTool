package com.zuoyebang.iot.mid.tcp

import androidx.annotation.IntDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * 创建者:baixuefei
 * 创建日期:2021/3/30 2:31 PM
 */
@IntDef(
    flag = true,
    value = [TcpType.TCP_ERROR, TcpType.HEART_BEAT_PING, TcpType.KICK_OFF, TcpType.LOGIN, TcpType.GENERAL_PROTOCOL]
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

        /**
         * 设备被踢
         * 当一个SN进行登录操作时，会判断该SN是否已经存在一个Socket连接，如果有，则将前一个登录进行踢下线操作。设备收到该消息后，不要做重登录操作，避免相互踢下线，而需要给用户提示设备存在服务异常。
         */

        const val KICK_OFF = 5

        /**
         * 设备上行
         */
        const val LOGIN = 80


        /**
         * 通用协议
         */
        const val GENERAL_PROTOCOL = 123
    }
}


@IntDef(
    flag = true,
    value = [TcpSubType.LAMP_CHANGE_USER, TcpSubType.LAMP_CONTROL, TcpSubType.BATCH_SEARCH_NEW_PICTURE,
        TcpSubType.TCP_COMMON_DIALOG, TcpSubType.DEVICE_STATUS_CHANGED, TcpSubType.CALL,TcpSubType.LAMP_OTA
    , TcpSubType.UPLOAD_LOG,TcpSubType.PAD, TcpSubType.WATCH_LOCATION,TcpSubType.WATCH_CHANGE_PHONE]
)
@Retention(RetentionPolicy.SOURCE)
annotation class TcpSubType {

    companion object {

        /**
         * Tcp 台灯切换用户
         */
        const val LAMP_CHANGE_USER = 1

        /**
         * Tcp 台灯控制
         */
        const val LAMP_CONTROL = 2

        /**
         * 上传了新的批搜
         */
        const val BATCH_SEARCH_NEW_PICTURE = 3
        /**
         * Z021 台灯ota状态变化
         */
        const val LAMP_OTA = 4

        /**
         * 通用对话框提示
         */
        const val TCP_COMMON_DIALOG = 5

        /**
         * 设备离线在线变更通知
         */
        const val DEVICE_STATUS_CHANGED = 6

        /**
         * 通话
         */
        const val CALL = 2001

        /**
         * 学习平板
         */
        const val PAD = 5001
        /**
         * 手表定位
         */
        const val WATCH_LOCATION = 6012

        /**
         * 日志上传指令
         */
        const val UPLOAD_LOG = 2008

        /**
         * 手表改变号码指令
         */
        const val WATCH_CHANGE_PHONE = 4024
    }
}

@IntDef(
    flag = true,
    value = [TcpHandlerMessage.SEND_PING, TcpHandlerMessage.SERVER_PONG_TIMEOUT, TcpHandlerMessage.DISABLE_SOCKET, TcpHandlerMessage.ENABlE_SOCKET]
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