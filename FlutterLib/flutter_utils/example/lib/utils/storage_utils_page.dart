

import 'dart:io';

import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/file/storage_utils.dart';

class StoragePage extends StatefulWidget {

  @override
  State<StatefulWidget> createState() {
    return new _PageState();
  }
}

class _PageState extends State<StoragePage> {

  String get1;
  String get2;
  String get3;
  String string1;
  String string2;
  String string3;
  String string4;
  String string5;

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: new AppBar(
        title: new Text("StorageUtils 文件管理工具类"),
        centerTitle: true,
      ),
      body: new ListView(
        children: <Widget>[
          MaterialButton(
            onPressed: getClick1,
            child: new Text("设备上临时目录的路径"),
            color: Colors.cyan,
          ),
          new Text("设备上临时目录的路径：$get1"),
          MaterialButton(
            onPressed: getClick2,
            child: new Text("获取应用程序的目录"),
            color: Colors.cyan,
          ),
          new Text("获取应用程序的目录：$get2"),
          MaterialButton(
            onPressed: getClick3,
            child: new Text("应用程序可以访问顶层存储的目录的路径"),
            color: Colors.cyan,
          ),
          new Text("应用程序可以访问顶层存储的目录的路径：$get3"),
          MaterialButton(
            onPressed: click1,
            child: new Text("同步创建文件夹"),
            color: Colors.cyan,
          ),
          new Text("同步创建文件夹：$string1"),
          MaterialButton(
            onPressed: click2,
            child: new Text("异步创建文件夹"),
            color: Colors.cyan,
          ),
          new Text("异步创建文件夹：$string2"),
          MaterialButton(
            onPressed: click3,
            child: new Text("创建临时目录"),
            color: Colors.cyan,
          ),
          new Text("创建临时目录文件：$string3"),
          MaterialButton(
            onPressed: click4,
            child: new Text("创建临时目录"),
            color: Colors.cyan,
          ),
          new Text("创建应用目录文件：$string4"),
        ],
      ),
    );
  }

  void getClick1() async{
    String appDocDir = await StorageUtils.getTempPath();
    setState(() {
      get1 = appDocDir;
    });
  }

  void getClick2() async{
    String appDocDir = await StorageUtils.getAppDocPath();
    setState(() {
      get2 = appDocDir;
    });
  }

  void getClick3() async{
    String appDocDir = await StorageUtils.getStoragePath();
    setState(() {
      get3= appDocDir;
    });
  }

  void click1() async{
    String appDocDir = await StorageUtils.getTempPath();
    String filePath = appDocDir + '/yc.json';
    Directory createAppDocDir = await StorageUtils.createDirSync(filePath);
    setState(() {
      string1 = "路径" + createAppDocDir.path;
    });
  }

  void click2() async{
    String appDocDir = await StorageUtils.getTempPath();
    String filePath = appDocDir + '/doubi.json';
    var createDir = StorageUtils.createDir(filePath);
    setState(() {
      string2 = createDir.path;
    });
  }

  void click3() async{
    Directory appDocDir = await StorageUtils.createTempDir(dirName: "haha.txt");
    setState(() {
      string3 = appDocDir.path;
    });
  }


  void click4() async {
    Directory appDocDir = await StorageUtils.createAppDocDir(dirName: "haha.txt");
    setState(() {
      string4 = appDocDir.path;
    });
  }

  void click5() {

  }
  void click6() {

  }
}