package com.yc.appserver

import android.app.Application
import com.yc.appfilelib.AppFileUtils
import com.yc.logclient.LogUtils
import com.yc.logclient.client.LogClient
import com.yc.toolutils.AppLogUtils
import com.zuoyebang.iot.mid.tcp.TcpFacade
import com.zuoyebang.iot.mod.tcp.trustmanager.TrustManagerProviderUnSafe

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
                    TcpFacade.TOKEN -> "eyJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjI4MDIwMywianRpIjoiYzg4NGE2YmQwOWM2NDNlNDg3NzU2MDlhN2E3NTBkYjEiLCJpYXQiOjE2NzkyOTgxNTEsInN1YiI6IjI4MDIwMyJ9.eOhv7aTTBueIXihGBsjrmI5HeUx68Fq6HUA0-y1DlrI"
                    TcpFacade.VERSION -> "4.0.1"
                    TcpFacade.SECRET -> "58f7bc234ef1496690b3e9f12b3b5d8b"
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