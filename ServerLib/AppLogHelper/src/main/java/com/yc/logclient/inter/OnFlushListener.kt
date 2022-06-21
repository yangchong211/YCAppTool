package com.yc.logclient.inter

import com.yc.logclient.bean.AppLogBean
import java.util.*

interface OnFlushListener {
    fun doFlush(logBeans: ArrayList<AppLogBean?>?)
}