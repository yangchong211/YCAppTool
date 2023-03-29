package com.yc.yc_flutter_tool

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

class FlutterCalcPlugin : MethodCallHandler {
    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "flutter_calc_plugin")
            channel.setMethodCallHandler(FlutterCalcPlugin())
        }
    }
    



    override fun onMethodCall(call: MethodCall, result: Result) {
        //提交代码3
        if (call.method == "getPlatformVersion") {
            //提交代码4
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else if (call.method == "getResult") {
            //提交代码5
            var a = call.argument<Int>("a")
            var b = call.argument<Int>("b")
            result.success((a!! + b!!).toString())
        } else {
            //提交代码6
            result.notImplemented()
        }
        //提交代码7
    }
    //提交代码8
    //提交代码9
}
