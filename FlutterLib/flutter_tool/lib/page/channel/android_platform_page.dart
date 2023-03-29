

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class AndroidPlatformPage extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return PageState();
  }

}

class PageState extends State<AndroidPlatformPage> {

  //来自native字符串
  String _fromNativeInfo = "";

  static final String METHOD_CHANNEL = "com.yc.flutter/android";
  static final String EVENT_CHANNEL = "com.yc.flutter/android/event";
  static final String BASIC_CHANNEL = "com.yc.flutter/android/basic";
  //原生android平台定义的供flutter端唤起的方法名
  static final String NATIVE_SHOW_TOAST = "showToast";
  //原生android平台定义的供flutter端唤起的方法名
  static final String NATIVE_METHOD_ADD = "numberAdd";
  //原生主动向flutter发送消息
  static final String NATIVE_SEND_MESSAGE_TO_FLUTTER = "nativeSendMessage2Flutter";
  //平台交互通道
  static final MethodChannel _MethodChannel = MethodChannel(METHOD_CHANNEL);
  //原生平台主动调用flutter端事件通道
  static final EventChannel _EventChannel = EventChannel(EVENT_CHANNEL);
  //事件
  static final BasicMessageChannel basicChannel = BasicMessageChannel(BASIC_CHANNEL, StandardMessageCodec());

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("平台交互"),
        centerTitle: true,
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: <Widget>[
          Text("从原生平台主动传递回来的值"),
          Text(_fromNativeInfo),
          RaisedButton(
            color: Colors.orangeAccent,
            child: Text("点击调用原生主动向flutter发消息方法"),
            onPressed: () {
              _MethodChannel.invokeMethod(NATIVE_SEND_MESSAGE_TO_FLUTTER);
            },
          ),
          SizedBox(height: 30),
          RaisedButton(
            color: Colors.orangeAccent,
            child: Text("flutter调用native平台Toast"),
            onPressed: () {
              showNativeToast("flutter调用原生android的Toast");
            },
          ),
          RaisedButton(
            color: Colors.orangeAccent,
            child: Text("flutter调用native计算两个数的和"),
            onPressed: () {
              getNumberResult(25, 36);
            },
          ),
          RaisedButton(
            color: Colors.orangeAccent,
            child: Text("flutter调用native打开原生androd页面"),
            onPressed: () {
              _MethodChannel.invokeMethod("new_page");
            },
          ),
          RaisedButton(
            color: Colors.orangeAccent,
            child: Text("使用basic通信---f调用n"),
            onPressed: () {
              sendMessage();
            },
          )
        ],
      ),
    );
  }

  @override
  void initState() {
    super.initState();
    _EventChannel.receiveBroadcastStream().listen(_onEvent, onError: _onError);

    //接受na端传递过来的方法，并做出响应逻辑处理
    _MethodChannel.setMethodCallHandler(nativeCallHandler);
    receiveMessage();
  }

  // 注册方法，等待被原生通过invokeMethod唤起
  Future<dynamic> nativeCallHandler(MethodCall methodCall) async {
    switch (methodCall.method) {
      case "getFlutterResult":
      //获取参数
        String paramsFromNative = await methodCall.arguments["params"];
        print("原生android传递过来的参数为------ $paramsFromNative");
        return "result from flutter";
        break;
    }
  }

  void _onEvent(Object object) {
    print(object.toString() + "-------------从原生主动传递过来的值");
    setState(() {
      _fromNativeInfo = object.toString()+"--onEvent";
    });
  }

  void _onError(Object object) {
    print(object.toString() + "-------------从原生主动传递过来的值");
    setState(() {
      _fromNativeInfo = object.toString()+"--onError";
    });
  }

  void showNativeToast(String content) {
    _MethodChannel.invokeMethod(NATIVE_SHOW_TOAST, {"msg": content});
  }


  // 调用平台方法计算两个数的和，并调用原生toast打印出结果
  void getNumberResult(int i, int j) async {
    Map<String, dynamic> map = {"number1": 12, "number2": 43};
    int result = await _MethodChannel.invokeMethod(NATIVE_METHOD_ADD, map);

    _MethodChannel.invokeMethod(NATIVE_SHOW_TOAST, {"msg": "12+43= $result"});
  }

  //发送消息到原生客户端 并且接收到原生客户端的回复
  Future<String> sendMessage() async {
    String reply = await basicChannel.send('this is flutter');
    print("receive reply msg from native:$reply");
    setState(() {
      _fromNativeInfo = reply;
    });
    return reply;
  }

  //接收原生消息 并发送回复
  void receiveMessage() async {
    basicChannel.setMessageHandler((msg) async {
      print("receive from Android:$msg");
      return "get native message";
    });
  }


}
















