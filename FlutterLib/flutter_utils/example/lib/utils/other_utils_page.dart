

import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/date/data_formats.dart';
import 'package:yc_flutter_utils/date/date_utils.dart';
import 'package:yc_flutter_utils/encrypt/encrypt_utils.dart';
import 'package:yc_flutter_utils/object/object_utils.dart';
import 'package:yc_flutter_utils/platform/platform_utils.dart';
import 'package:yc_flutter_utils/utils/random_utils.dart';

class OtherPage extends StatefulWidget {


  @override
  State<StatefulWidget> createState() {
    return new _PageState();
  }
}

class _PageState extends State<OtherPage> {

  String string = "yangchong";
  List list1 = new List();
  List list2 = null;
  Map map1 = new Map();
  Map map2 ;


  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    list1.add("yangchong");
    map1["name"] = "yangchong";

    final value = PlatformUtils.select(
      ios: "ios",
      android: "android",
      web: "web",
      fuchsia: "fuchsia",
      macOS: () => "macOS",
      windows: () => "windows",
      linux: () => "linux",
    );

    final valueFromFunction = PlatformUtils.select(
      ios: () => "ios",
      android: () => "android1",
      web: () => "web",
      fuchsia: () => "fuchsia",

    );
    return Scaffold(
      appBar: new AppBar(
        title: new Text("其他工具类"),
        centerTitle: true,
      ),
      body: new Column(
        children: <Widget>[
          new Text("生成一个表示十六进制颜色的随机整数：" + RandomUtils.randomColor().toString()),
          new Text("生成指定长度或随机长度的随机字符串：" + RandomUtils.randomString(length: 6).toString()),
          new Text("返回值或运行基于平台的函数  value: $value, valueFromFunction: $valueFromFunction"),
          new Text("判断是否是Android: ${PlatformUtils.isAndroid()}"),
          new Text("判断是否是Android2: ${PlatformUtils.isAndroid2(context)}"),
          new Text("判断是否是iOS: ${PlatformUtils.isIOS()}"),
          new Text("判断是否是iOS: ${PlatformUtils.isIOS2(context)}"),
          new Text("获取平台的渠道: ${PlatformUtils.get(context: context)}"),
          new Text("获取平台的渠道内容: ${PlatformUtils.getTargetPlatform(context: context)}"),
        ],
      ),
    );
  }
}