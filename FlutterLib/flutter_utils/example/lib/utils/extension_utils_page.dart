

import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/extens/extension_int.dart';
import 'package:yc_flutter_utils/extens/extension_list.dart';
import 'package:yc_flutter_utils/extens/extension_map.dart';
import 'package:yc_flutter_utils/num/num_utils.dart';

class ExtensionPage extends StatefulWidget {

  @override
  State<StatefulWidget> createState() {
    return new _DatePageState();
  }
}

class _DatePageState extends State<ExtensionPage> {

  String str1 = "124321423";
  String str2 = "1243.21423";
  double d = 12312.3121;
  double d2 = 12312.3121;
  int a1 = 1881;
  int a2 = 25;
  List<dynamic> list = new List();
  Map map = new Map();

  @override
  void initState() {
    super.initState();
    list.add("1");
    list.add(100);
    list.add("yangchong");
    list.add(13.14);
    list.add("doubi");

    map["1"] = "yc";
    map["2"] = "doubi";
    map["3"] = 1;
    map["4"] = 2.5;
  }

  @override
  Widget build(BuildContext context) {
    DateTime dateTime = new DateTime.now();
    return Scaffold(
      appBar: new AppBar(
        title: new Text("DateUtils工具类"),
        centerTitle: true,
      ),
      body: new Column(
        children: <Widget>[
          new Text("检查int是否为回文：" + a2.isPalindrome().toString()),
          new Text("检查所有数据是否具有相同的值：" + a1.isOneAKind().toString()),
          new Text("转换int值为二进制：" + a1.toBinary().toString()),
          new Text("转换int值为二进制int：" + a1.toBinaryInt().toString()),
          new Text("转换int值为二进制字符串：" + a1.fromBinary().toString()),


          new Text("将list转化为json字符串：" + list.toJsonString().toString()),
          new Text("将list转化为json字符串：" + list.getJsonPretty().toString()),
          //new Text("获取num列表的总值(int/double)：" + list.valueTotal().toString()),
          new Text("判断对象是否为null：" + list.isNull().toString()),
          new Text("检查数据是否为空或空(空或只包含空格)：" + list.isNullOrBlank().toString()),

          new Text("将map转化为json字符串：" + map.toJsonString().toString()),
          new Text("将map转化为json字符串换行：" + map.getJsonPretty().toString()),
          new Text("检查数据是否为空或空：" + map.isNull().toString()),
          new Text("检查数据是否为空或空：" + map.isNullOrBlank().toString()),


          new Text("将数字字符串转int：" + NumUtils.getIntByValueString("yangchong").toString()),
          new Text("数字字符串转double：" + NumUtils.getDoubleByValueString(str2).toString()),
          new Text("数字保留x位小数：" + NumUtils.getNumByValueString(str2,fractionDigits: 2).toString()),
          new Text("浮点数字保留x位小数：" + NumUtils.getNumByValueDouble(d,2).toString()),
          new Text("浮点数字保留x位小数：" + NumUtils.getNumByValueDouble(d,2).toString()),
          new Text("判断是否是否是0：" + NumUtils.isZero(d).toString()),
        ],
      ),
    );
  }
}