

import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/date/data_formats.dart';
import 'package:yc_flutter_utils/date/date_utils.dart';
import 'package:yc_flutter_utils/num/num_utils.dart';

class NumPage extends StatefulWidget {

  NumPage();

  @override
  State<StatefulWidget> createState() {
    return new _DatePageState();
  }
}

class _DatePageState extends State<NumPage> {

  String str1 = "124321423";
  String str2 = "1243.21423";
  double d = 12312.3121;
  double d2 = 12312.3121;


  @override
  void initState() {
    super.initState();
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
          new Text("将数字字符串转int：" + NumUtils.getIntByValueString(str1).toString()),
          new Text("将数字字符串转int：" + NumUtils.getIntByValueString("yangchong").toString()),
          new Text("数字字符串转double：" + NumUtils.getDoubleByValueString(str2).toString()),
          new Text("数字保留x位小数：" + NumUtils.getNumByValueString(str2,fractionDigits: 2).toString()),
          new Text("浮点数字保留x位小数：" + NumUtils.getNumByValueDouble(d,2).toString()),
          new Text("浮点数字保留x位小数：" + NumUtils.getNumByValueDouble(d,2).toString()),
          new Text("判断是否是否是0：" + NumUtils.isZero(d).toString()),
          new Text("两个数相加（防止精度丢失）：" + NumUtils.addNum(d,d2).toString()),
          new Text("两个数相减（防止精度丢失）：" + NumUtils.subtractNum(d,d2).toString()),
          new Text("两个数相乘（防止精度丢失）：" + NumUtils.multiplyNum(d,d2).toString()),
          new Text("两个数相除（防止精度丢失）：" + NumUtils.divideNum(d,d2).toString()),
        ],
      ),
    );
  }
}