package com.yc.tcp

import android.app.Application
import com.yc.logclient.LogUtils
import com.yc.tcp.inter.TcpEventListener
import com.yc.tcp.inter.TcpMessageCallBack
import com.yc.tcp.inter.TcpState
import com.yc.tcp.inter.TcpStateCallBack
import com.yc.tcp.trustmanager.TrustManagerProvider
import com.yc.tcp.trustmanager.TrustManagerProviderNormal

/**
 * TcpFascade 外观类:
 * - 负责全局初始化操作 init()
 * - 负责获取UnionTcpManager
 * - 负责注册/反注册 监听
 */
object TcpFacade {

    private var tcpManager: UnionTcpManager? = null
    private var dynamicInfoDelegate: DynamicInfoDelegate? = null
    private var mDisableTcpOnBackground: Boolean = false

    fun init(context: Application) {
        TcpContext.mContext = context
        checkValid()
        tcpManager = UnionTcpManager()
        tcpManager?.init()
        tcpManager?.setDisableTcpOnBackGround(mDisableTcpOnBackground)
    }

    fun setDisableTcpOnBackGround(should: Boolean): TcpFacade {
        mDisableTcpOnBackground = should
        tcpManager?.setDisableTcpOnBackGround(should)
        return this
    }

    fun getTcpManager(): UnionTcpManager {
        return tcpManager!!
    }

    fun setTrustManagerProvider(trustFactory: TrustManagerProvider): TcpFacade {
        TcpContext.mTrustManagerProvider = trustFactory
        return this
    }

    fun registerTcpMessage(owner: Any, callBack: TcpMessageCallBack) {
        TcpContext.registerTcpMessageWatcher(owner, callBack)
    }

    fun unRegisterTcpMessage(owner: Any) {
        TcpContext.unRegisterTcpMessageWatcher(owner)
    }

    fun registerTcpState(owner: Any, tcpStateCallBack: TcpStateCallBack) {
        TcpContext.registerTcpState(owner, tcpStateCallBack)
    }

    fun unRegisterTcpState(owner: Any) {
        TcpContext.unRegisterTcpState(owner)
    }

    fun registerTcpEventListener(owner: Any, listener: TcpEventListener) {
        TcpContext.registerTcpEventListener(owner, listener)
    }

    fun unRegisterTcpEventListener(owner: Any) {
        TcpContext.unRegisterTcpEventListener(owner)
    }

    fun getTcpState(): TcpState {
        return TcpContext.getTcpState()
    }

    fun putExtra(key: String, value: Any): TcpFacade {
        TcpContext.putExtra(key, value)
        return this
    }

    fun removeExtra(key: String): TcpFacade {
        TcpContext.removeExtra(key)
        return this
    }

    fun getExtra(key: String): Any? {
        return TcpContext.getExtra(key)
    }

    fun setDynamicInfoDelegate(delegate: DynamicInfoDelegate): TcpFacade {
        dynamicInfoDelegate = delegate
        return this
    }

    fun getDynamicInfo(key: String): Any? {
        return dynamicInfoDelegate?.invoke(key)
    }

    private fun checkValid() {
        if (TcpContext.mTrustManagerProvider == null) {
            TcpContext.mTrustManagerProvider = TrustManagerProviderNormal()
            LogUtils.d("mTrustManagerProvider == null,set it TrustManagerProviderNormal ")
        }
    }

    const val UDID = "UDID"
    const val VERSION = "VERSION"
    const val TOKEN = "TOKEN"
    const val SECRET = "SECRET"

}

typealias DynamicInfoDelegate = (key: String) -> Any?

