package com.yc.yc_flutter_tool

import android.app.Activity

import android.app.Application
import io.flutter.view.FlutterMain

class FlutterApplication : Application() {
    var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()
        FlutterMain.startInitialization(this)
    }

}
