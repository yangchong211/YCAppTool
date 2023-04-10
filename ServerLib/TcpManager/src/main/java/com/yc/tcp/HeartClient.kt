package com.yc.tcp

import android.os.Handler
import com.yc.logclient.LogUtils

/**
 *
 * Tcp 心跳机制规则:
 *
 * app 客户端:
 * - 当无业务数据发送时,每隔4.5分钟 发送一次Ping 消息
 * - 有业务数据发送时,可省略Ping消息发送
 * - 服务端检测一定时间(如9分钟)没有收到app端的心跳数据，则断开连接
 *
 * server端:
 * - 收到 app端的 ping消息,固定回复一个Pong消息
 * - 收到 app端的业务消息,需要进行消息回复
 * - app 端一定时间(如9分钟)未收到服务端有效Pong心跳,则断开连接
 *
 */
class HeartClient(private val mHandler: Handler) {

    //记录上一次服务端pong的时间
    private var lastPongTime: Long = -1L
        set(value) {
            field = value
            LogUtils.d(TAG, "set lastPongTime :${lastPongTime}")
        }

    /**
     * 发送ping
     */
    fun updateSendPing() {
        mHandler.removeMessages(TcpHandlerMessage.SEND_PING)
        mHandler.sendEmptyMessageDelayed(TcpHandlerMessage.SEND_PING, SEND_PING_TIME)
    }

    /**
     * 移除ping
     */
    fun removeSendPing() {
        LogUtils.d("removeSendPing")
        mHandler.removeMessages(TcpHandlerMessage.SEND_PING)
        inValidLastPong()
    }

    /**
     * 移除pong
     */
    fun removeServerPong() {
        LogUtils.d("removeServerPong")
        mHandler.removeMessages(TcpHandlerMessage.SERVER_PONG_TIMEOUT)
        inValidLastPong()
    }

    /**
     * 收到了服务端的心跳
     */
    fun updateReceivedPong() {
        lastPongTime = System.currentTimeMillis()
        mHandler.removeMessages(TcpHandlerMessage.SERVER_PONG_TIMEOUT)
        mHandler.sendEmptyMessageDelayed(TcpHandlerMessage.SERVER_PONG_TIMEOUT, RECEIVE_PONG_TIME_OUT_DELAY)
    }

    /**
     * 切到前台时，判断一次超时
     */
    fun foregroundCheckPong(foreGround: Boolean) {
        if (foreGround) {
            LogUtils.d("foregroundCheckPong:${foreGround}")
            if (isLastPongValid() && isLastPongTimeOut()) {
                inValidLastPong()
                mHandler.removeMessages(TcpHandlerMessage.SERVER_PONG_TIMEOUT)
                mHandler.sendEmptyMessage(TcpHandlerMessage.SERVER_PONG_TIMEOUT)
            }
        }
    }

    private fun isLastPongValid(): Boolean {
        return lastPongTime != -1L
    }

    private fun inValidLastPong() {
        lastPongTime = -1
    }

    /**
     * 判断当前时间减去上一次收到pong消息时间，是否大于连接超时时间。如果是则表示超时需要断开socket。
     */
    private fun isLastPongTimeOut(): Boolean {
        val now = System.currentTimeMillis()
        val diffTime = now - lastPongTime
        return (diffTime > RECEIVE_PONG_TIME_OUT_DELAY).apply {
            LogUtils.d("isLastPongTimeOut:${this},diffTime:${diffTime} ms,RECEIVE_PONG_TIME_OUT_DELAY:${RECEIVE_PONG_TIME_OUT_DELAY}")
        }
    }

    companion object {
        const val SEND_PING_TIME: Long = (60) * 1000 //每隔1分钟上传一次心跳
        //2分钟(两次上行心跳ping间隔)未收到Server端心跳Pong,则认为socket实际上已断开,主动断开Tcp 进行重连
        const val RECEIVE_PONG_TIME_OUT_DELAY = SEND_PING_TIME * 2
        const val TAG: String = "HeartClient"
    }

}


