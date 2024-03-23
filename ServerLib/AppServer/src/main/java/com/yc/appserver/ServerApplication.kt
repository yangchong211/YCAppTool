package com.yc.appserver

import android.app.Application
import com.yc.toolutils.file.AppFileUtils
import com.yc.logclient.LogUtils
import com.yc.logclient.client.LogClient
import com.yc.toolutils.system.AppLogUtils
import com.yc.tcp.TcpFacade
import com.yc.tcp.trustmanager.TrustManagerProviderUnSafe

class ServerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initLogUtils()
        initTcp()
    }

    private fun initLogUtils() {
        //通过跨进程写日志
        val aLogPath = AppFileUtils.getExternalFilePath(this, "YcLog")
        LogUtils.initLogEngine(this)
        LogUtils.setLogSaveFolder(aLogPath)
        LogUtils.setShowLog(true)
        LogUtils.enableTooLargeAutoDevide(true)
        LogUtils.setAutoDivideRatio(0.5f)
        LogUtils.setLogTag("tcp_yc_log: ")
        LogUtils.setBaseStackIndex(LogClient.mBaseIndex)
        LogUtils.setLogPre("YcLog")
    }

    /**
     * 初始化tcp
     */
    private fun initTcp() {
        TcpFacade.putExtra(TcpFacade.UDID, "E1274F3EF737603D08FE758455F67AF9|0")
            .setDynamicInfoDelegate { key ->
                when (key) {
                    TcpFacade.TOKEN -> "eyJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjI4MDIwMywianRpIjoiMDZkODBiNjNmYzQzNDkyM2JkMTAzY2M5OTYyMzFkNGIiLCJpYXQiOjE2Nzk5OTg3MjYsInN1YiI6IjI4MDIwMyJ9.V2jAt3ULDUr2xUVloXBL4TS16tVtz5nfpe87-bMVtSc"
                    TcpFacade.VERSION -> "4.0.1"
                    TcpFacade.SECRET -> "3d7e77d3fe0644bb89a4be31f640c97e"
                    else -> null
                }
            }
            .setDisableTcpOnBackGround(false)
            .setTrustManagerProvider(TrustManagerProviderUnSafe())
            .init(this)
        //注册tcp全局监听
        TcpFacade.registerTcpMessage(this) {
            AppLogUtils.d("registerTcpMessage:${it}")
        }
    }
}