package com.yc.logclient.inter

import com.yc.logclient.bean.AppLogBean

/**
 * 职责描述: LogBean的队列
 */
interface ILogQueue {
    fun put(bean: AppLogBean?)
}