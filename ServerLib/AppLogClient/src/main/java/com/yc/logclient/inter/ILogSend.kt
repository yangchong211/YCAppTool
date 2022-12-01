package com.yc.logclient.inter

import com.yc.logclient.bean.AppLogBean
import java.util.*

/**
 * 职责描述: 将日志对象 从LogClient端发送到LogServer端
 */
interface ILogSend {
    fun sendLogToService(beans: ArrayList<AppLogBean?>?)
}