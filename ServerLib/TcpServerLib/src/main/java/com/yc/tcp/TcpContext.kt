package com.yc.tcp

import android.app.Application
import com.yc.logclient.LogUtils
import com.yc.tcp.data.TcpDataBean
import com.yc.tcp.inter.*
import com.yc.tcp.trustmanager.TrustManagerProvider
import com.yc.tcp.trustmanager.TrustManagerProviderNormal
import java.lang.Exception
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.LinkedBlockingQueue


object TcpContext {

    var mContext: Application? = null
    private var tcpState: TcpState = TcpState.UNKNOWN
    var readQueue: LinkedBlockingQueue<TcpDataBean> = LinkedBlockingQueue()
    var sendQueue: LinkedBlockingQueue<TcpDataBean> = LinkedBlockingQueue()
    private var mTcpMessageCallBacks = ConcurrentHashMap<Any, TcpMessageCallBack>()
    private var mTcpStateCallBacks = ConcurrentHashMap<Any, TcpStateCallBack>()
    private var mTcpEventListener = ConcurrentHashMap<Any, TcpEventListener>()
    var mTrustManagerProvider: TrustManagerProvider = TrustManagerProviderNormal()
    //用于临时存储一些 额外的信息
    private var mExtMap = HashMap<String, Any>()

    /**
     * 注册Tcp消息监听
     */
    fun registerTcpMessageWatcher(owner: Any, callBack: TcpMessageCallBack) {
        mTcpMessageCallBacks[owner] = callBack
    }

    /**
     * 注销Tcp消息监听
     */
    fun unRegisterTcpMessageWatcher(owner: Any) {
        if (mTcpMessageCallBacks.containsKey(owner)) {
            mTcpMessageCallBacks.remove(owner)
        }
    }

    /**
     * 注册Tcp 状态监听
     */
    fun registerTcpState(owner: Any, tcpStateCallBack: TcpStateCallBack) {
        mTcpStateCallBacks[owner] = tcpStateCallBack
        //粘性回调,注册通知时 立即返回一次tcpState
        tcpStateCallBack.invoke(tcpState)
    }

    /**
     * 注销Tcp 状态监听
     */
    fun unRegisterTcpState(owner: Any) {
        mTcpMessageCallBacks.remove(owner)
    }


    /**
     * 注册tcp事件监听
     */
    fun registerTcpEventListener(owner: Any, tcpEventListener: TcpEventListener) {
        mTcpEventListener[owner] = tcpEventListener
    }

    /**
     * 注销tcp事件监听
     */
    fun unRegisterTcpEventListener(owner: Any) {
        mTcpEventListener.remove(owner)
    }


    /**
     * 分发Tcp 消息
     */
    fun onMessage(tcpMessage: TcpMessage) {
        //LogUtils.d("onMessage:${tcpMessage}")
        mTcpMessageCallBacks.forEach {
            it.value.invoke(tcpMessage)
        }
    }

    fun onMessageSendFailed(tcpMessage: TcpMessage, exception: Exception?) {
        mTcpEventListener.forEach {
            it.value.onSendFailed(tcpMessage, exception)
        }
    }

    fun onMessageSendSuccess(tcpMessage: TcpMessage) {
        mTcpEventListener.forEach {
            it.value.onSendSuccess(tcpMessage)
        }
    }

    /**
     * 获取当前Tcp状态
     */
    fun getTcpState(): TcpState = tcpState

    fun updateTcpState(state: TcpState) {
        if (tcpState != state) {
            tcpState = state
            onTcpState(tcpState)
            LogUtils.d("updateTcpState:${state}")
        }
    }

    private fun onTcpState(tcpState: TcpState) {
        mTcpStateCallBacks.forEach {
            it.value.invoke(tcpState)
        }
    }

    //存储额外信息
    fun putExtra(key: String, value: Any) {
        mExtMap[key] = value
    }

    //删除额外信息
    fun removeExtra(key: String) {
        mExtMap.remove(key)
    }

    fun getExtra(key: String): Any? {
        return mExtMap[key]
    }
}



