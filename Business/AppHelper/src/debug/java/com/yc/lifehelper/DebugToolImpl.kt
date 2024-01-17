package com.yc.lifehelper

import android.content.Context
import android.content.Intent

class DebugToolImpl : InterDebugTool {

    override fun toApmPage(context: Context) {
        context.startActivity(Intent(context, ApmTestActivity::class.java))
    }
}