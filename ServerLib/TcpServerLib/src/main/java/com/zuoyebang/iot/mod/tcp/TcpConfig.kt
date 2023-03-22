package com.zuoyebang.iot.mod.tcp

import androidx.annotation.IntDef
import com.yc.logclient.LogUtils
import com.zuoyebang.iot.mod.tcp.ServerHostConfig.Companion.SERVER_HOST_TEST

object TcpConfig {
    const val TCPVersion: Byte = 1
    /**
     * tcp正式环境的host
     */
    private const val SERVER_HOST: String = "iot-im.zybang.com"
    /**
     * tips环境的host
     */
    private const val SERVER_HOST_TIPS: String = "iot-im-tips.zuoyebang.com"
    private const val SERVER_PORT = 443
    /**
     * tcp测试环境host pad
     */
    private const val SERVER_HOST_TEST = "iot-im-test01.zybang.com"
    private const val SERVER_PORT_TEST = 8000
    /**
     * tcp测试环境host 各设备  https://wiki.zuoyebang.cc/pages/viewpage.action?pageId=399878484
     *
     *iot-im-t01.suanshubang.com:8101
     */
    private const val SERVER_DEVICES_HOST_TEST = "iot-im-t01.suanshubang.com"
    private const val SERVER_DEVICES_PORT_TEST = 8101

    @ServerHostConfig
    private var currHost: Int = ServerHostConfig.SERVER_HOST_TEST
    fun setHostConfig(@ServerHostConfig currHost: Int) {
        this.currHost = currHost
        LogUtils.d("host..${currHost}")
    }

    fun getServerHost(): String {
        return when (currHost) {
            ServerHostConfig.SERVER_HOST_TEST -> {
                SERVER_HOST_TEST
            }
            ServerHostConfig.SERVER_DEVICES_HOST_TEST -> {
                SERVER_DEVICES_HOST_TEST
            }
            ServerHostConfig.SERVER_HOST_TIPS -> {
                SERVER_HOST_TIPS
            }
            else -> {
                SERVER_HOST
            }
        }
    }

    fun getServerPort(): Int {
        return when (currHost) {
            ServerHostConfig.SERVER_HOST_TEST -> {
                SERVER_PORT_TEST
            }
            ServerHostConfig.SERVER_DEVICES_HOST_TEST -> {
                SERVER_DEVICES_PORT_TEST
            }
            else -> {
                SERVER_PORT
            }
        }
    }
    const val THREAD_RUN = " Run "
    const val THREAD_OVER = " Over "
}

@IntDef(flag = true,
    value = [SERVER_HOST_TEST, ServerHostConfig.SERVER_HOST_TIPS, ServerHostConfig.SERVER_HOST, ServerHostConfig.SERVER_DEVICES_HOST_TEST])
@Retention(AnnotationRetention.SOURCE)
annotation class ServerHostConfig {
    companion object {
        const val SERVER_DEVICES_HOST_TEST = 0
        const val SERVER_HOST_TEST = 3
        const val SERVER_HOST_TIPS = 1
        const val SERVER_HOST = 1 shl 1
    }
}