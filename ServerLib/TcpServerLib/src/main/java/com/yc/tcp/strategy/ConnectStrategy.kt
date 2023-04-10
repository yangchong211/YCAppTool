package com.yc.tcp.strategy

import com.yc.logclient.LogUtils
import com.yc.tcp.inter.DisconnectTcp


class ConnectStrategy {

    private val mLock = Object()
    var connectFailTimes = 0

    /**
     * 主要供外部控制调用，主动断开tcp或者启动tcp连接
     * isEnable = true, 启动连接线程,尝试进行Tcp连接；isEnable = false 断开tcp连接,阻塞连接进程
     */
    fun setEnable(isEnable: Boolean, disconnectTcp: DisconnectTcp) {
        synchronized(mLock) {
            LogUtils.d(TAG, "setEnable:${isEnable} ")
            if (isEnable) {
                mLock.notifyAll()
            } else {
                disconnectTcp.invoke()
            }
        }
    }


    /**
     * 连续10次 连接tcp 失败,则延时10分钟 再进行tcp连接
     * 目前的节奏:连续10次连接->停顿30s->连续10次连接->停顿30s
     */
    private fun tryBlockThreadByMultiFailed() {
        connectFailTimes++
        LogUtils.d("tryBlockThreadByMultiFailed:${connectFailTimes}")
        if (connectFailTimes > FailedThreshold) {
            try {
                synchronized(mLock) {
                    LogUtils.d(
                        TAG,
                        "tryBlockThreadByMultiFailed block here by $FAIL_WAIT_INTERVAL ms"
                    )
                    mLock.wait(FAIL_WAIT_INTERVAL)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                //触发一次定时阻塞后,连接失败计数需要清零
                reset()
            }
        }
    }


    /**
     * 主要供 连接线程内部自检使用, enable = false 阻塞连接线程,enable = true 恢复连接线程
     * 根据enable状态 判断是否阻塞连接线程
     */
    fun checkEnable(enable: Boolean) {
        //disable Tcp 连接,直接调用lock,无期限阻塞
        if (!enable) {
            try {
                synchronized(mLock) { //isEnable为false,则wait 挂起线程。
                    LogUtils.d(
                        TAG,
                        "checkEnable:${enable},block here"
                    )
                    mLock.wait()
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
                LogUtils.e("checkEnable:", e)
            }
        } else {
            //enable Tcp 连接时,检查失败次数是否达到上限，若达上限,需要阻塞一段时间
            LogUtils.d(
                TAG,
                "checkEnable enable:${enable},try connect"
            )
            tryBlockThreadByMultiFailed()
        }
    }

    fun reset() {
        connectFailTimes = 0
    }

    companion object {
        val TAG: String = ConnectStrategy::class.java.simpleName
        const val FailedThreshold: Int = 10
        const val FAIL_WAIT_INTERVAL = 30 * 1000.toLong()
    }
}

