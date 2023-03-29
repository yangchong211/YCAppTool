

import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/date/data_formats.dart';
import 'package:yc_flutter_utils/date/date_utils.dart';
import 'package:yc_flutter_utils/encrypt/encrypt_utils.dart';
import 'package:yc_flutter_utils/object/object_utils.dart';
import 'package:yc_flutter_utils/text/text_utils.dart';

class TextPage extends StatefulWidget {

  TextPage();

  @override
  State<StatefulWidget> createState() {
    return new _PageState();
  }
}

class _PageState extends State<TextPage> {

  String string = "yangchong";
  String h = "yang,chong";
  List list1 = new List();
  List list2 = null;
  Map map1 = new Map();
  Map map2 ;
  String card = "632912783288887";
  String phone = "13667225184";

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    list1.add("yangchong");
    map1["name"] = "yangchong";
    return Scaffold(
      appBar: new AppBar(
        title: new Text("EncryptUtils 加解密工具类"),
        centerTitle: true,
      ),
      body: new Column(
        children: <Widget>[
          new Text("判断文本内容是否为空：" + TextUtils.isEmpty(string).toString()),
          new Text("判断字符串是以ya开头：" + TextUtils.startsWith(string,"ya").toString()),
          new Text("判断字符串是否包含ya：" + TextUtils.contains(string,"ya").toString()),
          new Text("判断字符串是否包含ya：" + TextUtils.abbreviate(string,4).toString()),
          new Text("判断字符串是否包含ya：" + TextUtils.abbreviate(string,7).toString()),
          new Text("比较两个字符串是否相同：" + TextUtils.compare(string,"hah").toString()),
          new Text("比较两个字符串是否相同：" + TextUtils.compare("hah","hah").toString()),
          new Text("比较两个字符串是否相同：" + TextUtils.hammingDistance("yangchong","chongyang").toString()),
          new Text("格式化银行卡：" + TextUtils.formatDigitPattern(card).toString()),
          new Text("格式化手机号，从后面开始：" + TextUtils.formatDigitPatternEnd(phone).toString()),
          new Text("每隔4位加空格：" + TextUtils.formatSpace4(card).toString()),
          new Text("每隔3三位加逗号：" + TextUtils.formatComma3(phone).toString()),
          new Text("隐藏手机号中间n位：" + TextUtils.hideNumber(phone).toString()),
          new Text("隐藏手机号中间n位：" + TextUtils.hideNumber(phone,start: 2,end: 5).toString()),
          new Text("按照正则切割字符串：" + TextUtils.split(h,",").toString()),
          new Text("反转字符串：" + TextUtils.reverse(string).toString()),
        ],
      ),
    );
  }
}