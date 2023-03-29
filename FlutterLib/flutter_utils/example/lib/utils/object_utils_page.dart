

import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/date/data_formats.dart';
import 'package:yc_flutter_utils/date/date_utils.dart';
import 'package:yc_flutter_utils/encrypt/encrypt_utils.dart';
import 'package:yc_flutter_utils/object/object_utils.dart';

class ObjectPage extends StatefulWidget {

  ObjectPage();

  @override
  State<StatefulWidget> createState() {
    return new _PageState();
  }
}

class _PageState extends State<ObjectPage> {

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
    return Scaffold(
      appBar: new AppBar(
        title: new Text("EncryptUtils 加解密工具类"),
        centerTitle: true,
      ),
      body: new Column(
        children: <Widget>[
          new Text("判断字符串是否为空：" + ObjectUtils.isEmptyString(string).toString()),
          new Text("判断集合是否为空：" + ObjectUtils.isEmptyList(list1).toString()),
          new Text("判断字典是否为空：" + ObjectUtils.isEmptyMap(map1).toString()),
          new Text("判断object对象是否为空：" + ObjectUtils.isEmpty(map1).toString()),
          new Text("判断object是否不为空：" + ObjectUtils.isNotEmpty(map1).toString()),
          new Text("比较两个集合是否相同：" + ObjectUtils.compareListIsEqual(list1,list2).toString()),
          new Text("获取object的长度：" + ObjectUtils.getLength(string).toString()),
        ],
      ),
    );
  }
}