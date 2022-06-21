package com.yc.logclient.inter

import com.yc.logclient.bean.AppLogBean

interface OnEnqueueListener {
    fun doQueue(bean: AppLogBean?)
}