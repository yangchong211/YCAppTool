package com.yc.tcp

import com.yc.logclient.LogUtils
import com.yc.tcp.inter.TcpLogDDelegate
import com.yc.tcp.inter.TcpLogDelegate
import com.yc.tcp.inter.TcpLogIndelicate


object TcpLog {

    const val TAG = " tcp_yc_log "

    private var logDelegate: TcpLogDDelegate? = null
    private var logEDelegate: TcpLogDelegate? = null
    private var logIDelegate: TcpLogIndelicate? = null
    private var enableLog: Boolean = true

    fun enableLog(enable: Boolean): TcpLog {
        enableLog = enable
        return this
    }

    fun setLogDDelegate(logD: TcpLogDDelegate): TcpLog {
        logDelegate = logD
        return this
    }

    fun setLogIDelegate(logI: TcpLogIndelicate): TcpLog {
        logIDelegate = logI
        return this
    }

    fun setLogEDelegate(logE: TcpLogDelegate): TcpLog {
        logEDelegate = logE
        return this
    }


    fun i(msg: String) {
        if (!enableLog) return
        if (logIDelegate != null) {
            logIDelegate?.invoke(TAG, msg, getAppendStackIndex())
        } else {
            LogUtils.i(TAG, msg)
        }
    }

    fun i(tag: String, msg: String) {
        if (!enableLog) return
        if (logIDelegate != null) {
            logIDelegate?.invoke(TAG + tag, msg, getAppendStackIndex())
        } else {
            LogUtils.i(TAG + tag, msg)
        }
    }

    fun d(msg: String) {
        if (!enableLog) return
        if (logDelegate != null) {
            logDelegate?.invoke(TAG, msg, getAppendStackIndex())
        } else {
            LogUtils.d(TAG, msg)
        }
    }

    fun d(tag: String, msg: String) {
        if (!enableLog) return
        if (logDelegate != null) {
            logDelegate?.invoke(TAG + tag, msg, getAppendStackIndex())
        } else {
            LogUtils.d(TAG + tag, msg)
        }
    }

    fun e(msg: String) {
        if (!enableLog) return
        if (logEDelegate != null) {
            logEDelegate?.invoke(TAG, msg, null, getAppendStackIndex())
        } else {
            LogUtils.d(TAG, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (!enableLog) return
        if (logEDelegate != null) {
            logEDelegate?.invoke(TAG + tag, msg, null, getAppendStackIndex())
        } else {
            LogUtils.d(TAG + tag, msg)
        }
    }

    fun e(msg: String, e: Throwable?) {
        if (!enableLog) return
        if (logEDelegate != null) {
            logEDelegate?.invoke(TAG, msg, null, getAppendStackIndex())
        } else {
            LogUtils.d(TAG, msg, e)
        }
    }

    fun e(tag: String, msg: String, e: Throwable?) {
        if (!enableLog) return
        if (logEDelegate != null) {
            logEDelegate?.invoke(TAG + tag, msg, e, getAppendStackIndex())
        } else {
            LogUtils.e(TAG + tag, msg, e)
        }

    }

    private fun getAppendStackIndex(): Int {
        return 3
    }
}
