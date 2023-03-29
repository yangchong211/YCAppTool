
import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';


/// 平台获取类型工具类
class PlatformUtils {

  /// 判断是否是Android
  static bool isAndroid(){
    return Platform.isAndroid;
  }

  /// 判断是否是Android
  static bool isAndroid2(BuildContext context){
    return (getTargetPlatform(context: context).contains("android"));
  }

  /// 判断是否是iOS
  static bool isIOS(){
    return Platform.isIOS;
  }

  /// 判断是否是iOS
  static bool isIOS2(BuildContext context){
    return (getTargetPlatform(context: context).contains("ios"));
  }

  /// 返回值或运行基于平台的函数。
  /// 如果传递了上下文，它将通过 Theme.of(context).platform 获取平台。
  /// 否则，它将使用 defaultTargetPlatform。
  static TargetPlatform get({BuildContext context}) {
    return context != null ? Theme.of(context).platform : defaultTargetPlatform;
  }

  /// 返回值或运行基于平台的函数。
  /// 如果传递了上下文，它将通过 Theme.of(context).platform 获取平台。
  /// 否则，它将使用 defaultTargetPlatform。
  static String getTargetPlatform({BuildContext context}) {
    return context != null ? Theme.of(context).platform.toString()
        : defaultTargetPlatform.toString();
  }

  static T select<T>({BuildContext context, dynamic android, dynamic ios,
    dynamic fuchsia, dynamic web, dynamic macOS,dynamic windows,
    dynamic linux,dynamic defaultWhenNull}) {
    var func;
    if (kIsWeb) {
      func = web;
    } else {
      func = {
        TargetPlatform.iOS: ios,
        TargetPlatform.android: android,
        TargetPlatform.fuchsia: fuchsia,
        TargetPlatform.macOS: macOS,
        TargetPlatform.windows: windows,
        TargetPlatform.linux: linux,
      }[get(context: context)];
    }
    if (func is Function) {
      return func();
    }
    if (func == null && defaultWhenNull is Function) {
      return defaultWhenNull();
    }
    return func ?? defaultWhenNull;
  }
}
