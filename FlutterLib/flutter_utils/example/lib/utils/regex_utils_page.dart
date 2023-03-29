

import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/date/data_formats.dart';
import 'package:yc_flutter_utils/date/date_utils.dart';
import 'package:yc_flutter_utils/num/num_utils.dart';
import 'package:yc_flutter_utils/regex/regex_utils.dart';

class RegexPage extends StatefulWidget {


  @override
  State<StatefulWidget> createState() {
    return new _DatePageState();
  }
}

class _DatePageState extends State<RegexPage> {

  String str1 = "124321423";
  String str2 = "1243.21423";
  double d = 12312.3121;
  double d2 = 12312.3121;
  String card15 = "421142442342141";
  String card18 = "421142442342141256";

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    DateTime dateTime = new DateTime.now();
    return Scaffold(
      appBar: new AppBar(
        title: new Text("RegexUtils 正则校验工具类"),
        centerTitle: true,
      ),
      body: new Column(
        children: <Widget>[
          new Text("简单验证手机号：" + RegexUtils.isMobileSimple("13667225184").toString()),
          new Text("精确验证手机号：" + RegexUtils.isMobileExact("13667225184").toString()),
          new Text("验证电话号码：" + RegexUtils.isTel(str2).toString()),
          new Text("验证身份证号码 15 位：" + RegexUtils.isIDCard15(card15).toString()),
          new Text("简单验证身份证号码 18 位：" + RegexUtils.isIDCard18(card18).toString()),
          new Text("精确验证身份证号码 18 位：" + RegexUtils.isIDCard18Exact(card18).toString()),
          new Text("验证邮箱：" + RegexUtils.isEmail("yangchong211@163.com").toString()),
          new Text("验证 URL：" + RegexUtils.isURL("https://github.com/yangchong211").toString()),
          new Text("验证汉字：" + RegexUtils.isZh("逗比").toString()),
          new Text("验证用户名：" + RegexUtils.isUserName(str1).toString()),
          new Text("验证 IP 地址：" + RegexUtils.isIP("127.0.0.1").toString()),
        ],
      ),
    );
  }
}