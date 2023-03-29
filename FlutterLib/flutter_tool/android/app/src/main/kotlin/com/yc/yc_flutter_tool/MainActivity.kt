package com.yc.yc_flutter_tool

//import io.flutter.embedding.android.FlutterActivity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.EventSink
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugins.GeneratedPluginRegistrant
import java.util.*


class MainActivity: FlutterActivity() {

    private val METHOD_CHANNEL = "com.yc.flutter/android"
    //事件通道，供原生主动调用flutter端使用
    private val EVENT_CHANNEL = "com.yc.flutter/android/event"
    private val BASIC_CHANNEL = "com.yc.flutter/android/basic"

    private val METHOD_SHOW_TOAST = "showToast"
    //简单加法计算，并返回两个数的和
    private val METHOD_NUMBER_ADD = "numberAdd"

    //原生主动向flutter发送消息
    private val METHOD_NATIVE_SEND_MESSAGE_FLUTTER = "nativeSendMessage2Flutter"

    private var eventChannelSink: EventSink? = null
    private var methodChannel: MethodChannel? = null
    private var eventChannel : EventChannel ? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main);
        GeneratedPluginRegistrant.registerWith(this)

        initMethodChannel()
        intiEventChannel()
        initBasicMessageChannel()
    }

    /*override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        GeneratedPluginRegistrant.registerWith(flutterEngine)
        initMethodChannel()
        intiEventChannel()
        initBasicMessageChannel()
    }*/

    private fun intiEventChannel() {
        // event 主要是native像flutter发送通知事件
        //eventChannel = EventChannel(flutterEngine!!.dartExecutor.binaryMessenger,EVENT_CHANNEL)
        eventChannel = EventChannel(flutterView,EVENT_CHANNEL)
        eventChannel?.setStreamHandler(object : EventChannel.StreamHandler {
            override fun onListen(o: Any, eventSink: EventSink) {
                eventChannelSink = eventSink
                eventSink.success("事件通道准备就绪")
                //在此不建议做耗时操作，因为当onListen回调被触发后，在此注册当方法需要执行完毕才算结束回调函数
                //的执行，耗时操作可能会导致界面卡死，这里读者需注意！！
            }

            override fun onCancel(o: Any) {

            }
        })
    }

    private fun initMethodChannel() {
        //methodChannel = MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, METHOD_CHANNEL)
        methodChannel = MethodChannel(flutterView, METHOD_CHANNEL)
        //接受fltuter端传递过来的方法，并做出响应逻辑处理
        //接受fltuter端传递过来的方法，并做出响应逻辑处理
        methodChannel?.setMethodCallHandler(MethodCallHandler { call, result ->
            println(call.method)
            if (call.method == METHOD_SHOW_TOAST) {
                if (call.hasArgument("msg") && !TextUtils.isEmpty(call.argument<Any>("msg").toString())) {
                    Toast.makeText(this@MainActivity, call.argument<Any>("msg").toString(), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "toast text must not null", Toast.LENGTH_SHORT).show()
                }
            } else if (call.method == METHOD_NUMBER_ADD) {
                val number1 = call.argument<Int>("number1")!!
                val number2 = call.argument<Int>("number2")!!
                //返回两个数相加后的值
                result.success(number1 + number2)
            } else if (call.method ==METHOD_NATIVE_SEND_MESSAGE_FLUTTER) {
                nativeSendMessage2Flutter()
            } else if (call.method == "new_page") {
                startActivity(Intent(this@MainActivity, NativeActivity::class.java))
            }
        })
    }

    /**
     * 主要是传递字符串和一些半结构体的数据
     */
    private fun initBasicMessageChannel() {
        //val basicMessageChannel = BasicMessageChannel(flutterEngine!!.dartExecutor.binaryMessenger, BASIC_CHANNEL,
        //        StandardMessageCodec.INSTANCE)
        val basicMessageChannel = BasicMessageChannel(flutterView, BASIC_CHANNEL,
                StandardMessageCodec.INSTANCE)

        //主动发送消息到flutter 并接收flutter消息回复
        basicMessageChannel.send("send basic message") { `object`: Any? ->
            println("MainActivity----receive reply msg from flutter:" + `object`.toString())
        }

        //接收flutter消息 并发送回复
        basicMessageChannel.setMessageHandler { `object`: Any?, reply: BasicMessageChannel.Reply<Any> ->
            println("MainActivity----receive msg from flutter:" + `object`.toString())
            reply.reply("reply：got your message")
        }
    }

    /**
     * 原生端向flutter主动发送消息；
     */
    private fun nativeSendMessage2Flutter() {
        println("MainActivity-------------nativeSendMessage2Flutter-")
        //主动向flutter发送一次更新后的数据
        eventChannelSink!!.success("原生端向flutter主动发送消息")
    }


    override fun onResume() {
        super.onResume()
        /**
         * 这里通过methodChannel从原生android获取flutter端传递过来的值，其实跟flutter端通过invokeMethod方法回调原生android定义好的方法原理类似
         * ，但是因为flutter是作为寄主呈现在原生android上，换句话说，在flutter的UI被渲染完成之后，定义在原生安卓的相关方法定义也必定已经加载完成
         * 再换句话说，flutter在调用原生的监听方法时，宿主上的相关方法肯定已经完成注册监听，可被唤起。
         *
         * 但是，原生android端作为宿主要监听flutter端提前定义好的方法，由于二者的生命周期寄托关系，在原生android端完成事件唤起的时候，flutter的相关
         * 方法定义未必被实现，换句话说，在原生android中通过invokeMethod唤起flutter的回调方法时，可能会存在方法为注册，导致为实现的情况出现
         *
         * 为了避免该情况的发生，建议在onResume中注册原生android的invokMethod方法，在原生android中页面切换时，可重复注册监听方法，确保在某一个时机
         * 能唤起flutter的方法
         *
         * 在此笔者认为在原生端唤起flutter的回调方法意义不大，如果一定需要从flutter获取数据，
         */
        val params: MutableMap<String, Any> = HashMap()
        //传递参数，并且创建回调方法
        params["params"] = "test params"
        methodChannel!!.invokeMethod("getFlutterResult", params, object : MethodChannel.Result {
            override fun success(o: Any?) {
                println("MainActivity-------------flutter 传递过来的值"+o.toString())
            }

            override fun error(errorCode: String, errorMsg: String?, o: Any?) {
                println("MainActivity------------error with--$errorMsg")
            }

            override fun notImplemented() {
                println("MainActivity-------------notImplemented-")
            }
        })
    }

}
