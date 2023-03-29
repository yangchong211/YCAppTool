

import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/date/data_formats.dart';
import 'package:yc_flutter_utils/date/date_utils.dart';
import 'package:yc_flutter_utils/log/log_utils.dart';
import 'package:yc_flutter_utils/num/num_utils.dart';
import 'package:yc_flutter_utils/sp/sp_utils.dart';
import 'package:yc_flutter_utils/system/system_utils.dart';
import 'package:yc_flutter_utils/toast/snack_utils.dart';
import 'package:yc_flutter_utils_example/model/city.dart';
import 'package:yc_flutter_utils/extens/extension_map.dart';
import 'package:shared_preferences/shared_preferences.dart';

class SystemPage extends StatefulWidget {

  @override
  State<StatefulWidget> createState() {
    return new _DatePageState();
  }
}

class _DatePageState extends State<SystemPage> {

  String str1 = "124321423";
  String str2 = "1243.21423";
  double d = 12312.3121;
  double d2 = 12312.3121;
  List<String> list = new List();
  List<dynamic> list2 = new List();
  Map map = new Map();
  bool isWidget = false;
  bool putSuccess;

  @override
  void initState() {
    super.initState();
    list.add("yc");
    list.add("doubi");
    list2.add(1);
    list2.add(true);
    list2.add("yc");
    map["1"] = "1";
    map["2"] = "2";
    LogUtils.i("initState 1 ");
    Future(() async {
      LogUtils.i("initState 2 ");
      await SpUtils.init();
      LogUtils.i("initState 3 ");
    });
    LogUtils.i("initState 4 ");
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: new AppBar(
        title: new Text("sp存储工具类"),
        centerTitle: true,
      ),
      body: new ListView(
        children: <Widget>[
          MaterialButton(
            onPressed: click1,
            child: new Text("剪切到剪切板"),
            color: Colors.cyan,
          ),
          MaterialButton(
            onPressed: click2,
            child: new Text("隐藏软键盘"),
            color: Colors.cyan,
          ),
          MaterialButton(
            onPressed: click3,
            child: new Text("打开软键盘"),
            color: Colors.cyan,
          ),
          MaterialButton(
            onPressed: click4,
            child: new Text("清除数据"),
            color: Colors.cyan,
          ),
        ],
      ),
    );
  }

  void click1() {
    SystemUtils.copyToClipboard("你好");
  }

  void click2() {
    SystemUtils.hideKeyboard();
  }

  void click3() {
    SystemUtils.showKeyboard();
  }

  void click4() {
    SystemUtils.clearClientKeyboard();
  }
}