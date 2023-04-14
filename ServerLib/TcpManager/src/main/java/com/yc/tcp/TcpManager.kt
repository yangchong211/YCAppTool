package com.yc.tcp

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import com.yc.logclient.LogUtils
import com.yc.networklib.AppNetworkUtils
import com.yc.tcp.ext.toTcpMessage
import com.yc.tcp.ext.toTcpPacket
import com.yc.tcp.receiver.ConnectionChangeReceiver
import com.yc.tcp.data.TcpDataBean
import com.yc.tcp.inter.ITcpSocket
import com.yc.tcp.inter.SocketAdapter
import com.yc.tcp.inter.TcpListener
import com.yc.tcp.inter.TcpMessage
import com.yc.tcp.socket.BasicTcpSocket

/**
 * TcpManager 业务相关,负责完成一些组包和拆包功能
 *
 * 主要职责
 * - 数据组包:TcpPacket->TcpData
 * - 数据解包:TcpData-> TcpPacket
 * - app前后台切换监控
 * - app网络状态监控
 */
abstract class TcpManager : TcpListener {

    private var mHeartClient: HeartClient? = null
    private var mTcpCore: TcpCore? = null
    private var mHandlerThread: HandlerThread? = null
    private var mHandler: Handler? = null

    //app切到后台断开Tcp
    private var mDisableTcpOnBackground: Boolean = false
    private var mForegroundMonitor: ForegroundMonitor = ForegroundMonitor { becomeForeground ->
        val hasNet = AppNetworkUtils.isConnected()
        LogUtils.d(
            TAG,
            "Foreground Monitor, become foreground:${becomeForeground},hasNet:${hasNet},mDisableTcpOnBackground:${mDisableTcpOnBackground},isTcpConnected:${isTcpConnected()}"
        )

        if (becomeForeground) { //有网络,app从后台切到前台,若tcp未连接,则尝试启动Tcp连接
            //回到前台
            if (hasNet && !isTcpConnected()) {
                sendEnableTcpMessage()
            }
        } else {
            //回到后台
            if (mDisableTcpOnBackground) {
                //app刚起动时,可能会迅速经历 "前台"->"后台"->"前台",此处切后台关闭Tcp,需做延时处理
                sendDisableTcpMessage(3 * 1000)
            }
        }
        mHeartClient?.foregroundCheckPong(becomeForeground)
    }

    /**
     * 注册网络监听
     */
    private var mConnectChangeReceiver = ConnectionChangeReceiver { hasNetWork ->
        LogUtils.d("ConnectionChangeReceiver, hasNetWork:${hasNetWork},isForeground:${mForegroundMonitor.isForeground},mDisableTcpOnBackground:${mDisableTcpOnBackground}")
        if (hasNetWork) {
            if (!mDisableTcpOnBackground || mForegroundMonitor.isForeground) //app在后台且后台不断开tcp 或者 app在前台
                sendEnableTcpMessage()
        } else { //没有网络 直接断开Tcp 连接
            sendDisableTcpMessage()
        }
    }

    //是否已经登录成功
    private var mHasLogin: Boolean = false
        set(value) {
            field = value
            LogUtils.d("set mHasLogin = $field")
        }

    fun init() {
        LogUtils.d("init")
        val adapter = getAdapter()
        //创建socket
        val mSocket: ITcpSocket = BasicTcpSocket(adapter)
        //创建tcpCore
        mTcpCore = TcpCore(mSocket)
        mTcpCore?.setTcpListener(this)

        mHandlerThread = HandlerThread("TcpManager")
        mHandlerThread?.start()
        mHandler = object : Handler(mHandlerThread!!.looper) {
            override fun handleMessage(msg: Message) {
                LogUtils.d(" --- handleMessage ${msg.what.toTcpMessage()}")
                when (msg.what) {
                    TcpHandlerMessage.SEND_PING -> {            //触发延时发送心跳
                        sendHeartPing()
                        mHeartClient?.updateSendPing()
                    }
                    TcpHandlerMessage.SERVER_PONG_TIMEOUT -> {  //服务端心跳超时,Tcp断开重连
                        mTcpCore?.resetConnectPongTimeOut()
                    }
                    TcpHandlerMessage.DISABLE_SOCKET -> {       //阻塞Tcp连接线程,并停用重连机制
                        mTcpCore?.setEnable(false)
                    }
                    TcpHandlerMessage.ENABlE_SOCKET -> {        //开启连接线程,tcp重连机制启动
                        mTcpCore?.setEnable(true)
                    }
                    else -> {

                    }
                }
            }
        }
        mHeartClient = HeartClient(mHandler = mHandler!!)
        //开启tcpCore
        mTcpCore!!.start()
        registerConnectReceiver()
        registerForegroundMonitor()
        mHandler?.removeCallbacksAndMessages(null)
    }

    fun stop() {
        mTcpCore?.release()
        unRegisterForegroundMonitor()
        unRegisterConnectReceiver()
    }

    fun setDisableTcpOnBackGround(should: Boolean) {
        LogUtils.d("setDisableTcpOnBackGround:${should},isForeground:${isForeground()}")
        mDisableTcpOnBackground = should
        //应用在后台时,设置mDisableTcpOnBackground = true, 立即断开tcp连接,并阻断重连机制
        if (should && !isForeground()) {
            sendDisableTcpMessage()
        }
    }

    private fun isForeground(): Boolean {
        return mForegroundMonitor.isForeground
    }

    fun activityHasResumed(): Boolean {
        return mForegroundMonitor.isFirstActivityResumed
    }

    /**
     * 发送开启TCP连接消息
     */
    private fun sendEnableTcpMessage(delay: Long = 0) {
        LogUtils.d(TAG, "sendEnableTcpMessage delay:${delay}")
        mHandler?.removeMessages(TcpHandlerMessage.DISABLE_SOCKET)
        mHandler?.removeMessages(TcpHandlerMessage.ENABlE_SOCKET)
        mHandler?.sendEmptyMessageDelayed(TcpHandlerMessage.ENABlE_SOCKET, delay)
    }

    /**
     * 发送断开TCP连接的消息
     */
    private fun sendDisableTcpMessage(delay: Long = 0) {
        LogUtils.d(TAG, "sendDisableTcpMessage delay $delay")
        mHandler?.removeMessages(TcpHandlerMessage.ENABlE_SOCKET)
        mHandler?.removeMessages(TcpHandlerMessage.DISABLE_SOCKET)
        mHandler?.sendEmptyMessageDelayed(TcpHandlerMessage.DISABLE_SOCKET, delay)
    }

    fun enableTcp(enable: Boolean, delay: Long = 0) {
        if (enable) {
            sendEnableTcpMessage(delay)
        } else {
            sendDisableTcpMessage(delay)
        }
    }

    /**
     * 发送tcpPacket
     */
    fun sendTcpPacket(tcpPacket: TcpPacket) {
        LogUtils.d("Tcp_send:${tcpPacket}")
        val tcpDataBean = tcpPacket.toTcpData()
        mTcpCore?.send(tcpDataBean)
    }


    /**
     * 注册网络变化监听
     */
    private fun registerConnectReceiver() {
        TcpContext.mContext?.apply {
            @Suppress("DEPRECATION") val filter =
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            registerReceiver(mConnectChangeReceiver, filter)
        }
    }

    /**
     * 注销网络状态监听
     */
    private fun unRegisterConnectReceiver() {
        TcpContext.mContext?.apply {
            unregisterReceiver(mConnectChangeReceiver)
        }
    }

    private fun registerForegroundMonitor() {
        TcpContext.mContext?.let {
            mForegroundMonitor.register(it)
        }
    }

    private fun unRegisterForegroundMonitor() {
        TcpContext.mContext?.let {
            mForegroundMonitor.unRegister(it)
        }
    }

    override fun onConnectState(success: Boolean) {
        LogUtils.d("onConnectState:${success}")
        //连接成功,需要发送登录和ping
        if (success) {
            sendLogin()
            sendHeartPing()
        } else {
            //连接断开,Login状态复位
            mHasLogin = false
        }
    }

    override fun readSuccess(data: TcpDataBean) {
        //读取数据包成功,更新心跳包(延时发送)
        mHeartClient?.apply {
            updateReceivedPong()
        }
    }

    /**
     * 读消息异常
     */
    override fun readFailure(e: Exception?) {
        //读取数据包出错,移除心跳包
        mHeartClient?.apply {
            removeSendPing()
            removeServerPong()
        }
    }

    override fun writeSuccess(data: TcpDataBean) {
        val convertFromTcpData = TcpPacket.convertFromTcpData(data)
        onTcpPacketSendSuccess(convertFromTcpData)
        mHeartClient?.apply {
            updateSendPing()
        }
    }

    /**
     * 写消息(发送tcp)异常
     */
    override fun writeFailure(data: TcpDataBean, e: Exception?) {
        val convertFromTcpData = TcpPacket.convertFromTcpData(data)
        onTcpPacketSendFailed(convertFromTcpData, e)
        mHeartClient?.apply {
            removeSendPing()
            removeServerPong()
        }
    }

    /**
     * 收到了Tcp 数据包,进行数据包分发
     */
    override fun onDispatchMsg(tcpData: TcpDataBean) {
        val tcpPacket = tcpData.toTcpPacket()
        disPatchTcpPacket(tcpPacket)
    }

    /**
     * 在待发送对队列查询某个类型的TcpData
     */
    fun findTcpDataInQueByTypeTag(typeTag: String): TcpDataBean? {
        return mTcpCore?.findTcpDataInQueByTypeTag(typeTag)
    }

    /**
     * 查询某个类型的TcpData处于待发送队列中
     */
    fun isTcpDataInQueByTypeTag(clazz: Class<out TcpMessage>): Boolean {
        return mTcpCore?.isTcpDataInQueByTypeTag(clazz.simpleName) ?: false
    }

    /**
     * 发送心跳包
     */
    abstract fun sendHeartPing()

    abstract fun sendLogin()

    abstract fun getAdapter(): SocketAdapter

    abstract fun disPatchTcpPacket(tcpPacket: TcpPacket)

    abstract fun onTcpPacketSendFailed(tcpPacket: TcpPacket, e: Exception?)

    abstract fun onTcpPacketSendSuccess(tcpPacket: TcpPacket)

    companion object {
        val TAG: String = TcpManager::class.java.simpleName
    }

    private fun isTcpConnected(): Boolean {
        return mTcpCore?.isTcpConnected() ?: false
    }

}