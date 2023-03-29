



import 'dart:async';
import 'package:flutter/services.dart';

/// 获取na一些环境参数
class EnvironmentPlugin {

  static MethodChannel _environmentChannel = MethodChannel("com.yc.flutter/environment_plugin", JSONMethodCodec());

  static Future<Map<String, dynamic>> getEnvironment() async {
    return _environmentChannel.invokeMethod("environment");
  }

}

/// 加减法plugin
class FlutterCalcPlugin {

  static const MethodChannel _channel = const MethodChannel('com.yc.flutter/flutter_calc_plugin');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  //计算两个数的和
  static Future<String>  getResult(int a, int b) async {
    Map<String, dynamic> map = {"a": a, "b": b};
    String result = await _channel.invokeMethod("getResult", map);
    print(result+"----------aa--");
    return result;
  }
}