import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/file/file_utils.dart';

class FileStoragePage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => StorageState();
}

class StorageState extends State<FileStoragePage> {

  bool isSuccess1 = false;
  String string1 = "null";
  bool isSuccess2 = false;
  String string2 = "null";
  bool isSuccess3 = false;
  String string3 = "null";
  bool isSuccess4 = false;
  String string4 = "null";

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('FileUtils 文件工具类'),
      ),
      body: new ListView(
        children: <Widget>[
          new Text("存储json文件1，状态 ：$isSuccess1"),
          MaterialButton(
            onPressed: save1,
            child: new Text("存储json文件"),
            color: Colors.cyan,
          ),
          new Text("获取json文件1，状态  ：$string1"),
          MaterialButton(
            onPressed: get1,
            child: new Text("获取json文件"),
            color: Colors.cyan,
          ),
          MaterialButton(
            onPressed: clear1,
            child: new Text("清除json文件"),
            color: Colors.cyan,
          ),

          new Text("存储json文件2，状态 ：$isSuccess2"),
          MaterialButton(
            onPressed: save2,
            child: new Text("存储json文件2"),
            color: Colors.cyan,
          ),
          new Text("获取json文件2，状态  ：$string2"),
          MaterialButton(
            onPressed: get2,
            child: new Text("获取json文件2"),
            color: Colors.cyan,
          ),

          new Text("存储字符串文件1，状态 ：$isSuccess3"),
          MaterialButton(
            onPressed: save3,
            child: new Text("存储字符串文件"),
            color: Colors.cyan,
          ),
          new Text("获取字符串文件1，状态  ：$string3"),
          MaterialButton(
            onPressed: get3,
            child: new Text("获取字符串文件"),
            color: Colors.cyan,
          ),

          new Text("存储字符串文件2，状态 ：$isSuccess4"),
          MaterialButton(
            onPressed: save4,
            child: new Text("存储字符串文件2"),
            color: Colors.cyan,
          ),
          new Text("获取字符串文件2，状态  ：$string4"),
          MaterialButton(
            onPressed: get4,
            child: new Text("获取字符串文件2"),
            color: Colors.cyan,
          ),
        ],
      ),
    );
  }


  void save1() {
    var mapHeatModel = new MapHeatModel();
    mapHeatModel.version = "100";
    List<MapPositionModel> focusCenter = new List();
    for(int i=0 ; i<2 ; i++){
      MapPositionModel model = new MapPositionModel();
      model.lng = i.toDouble();
      model.lat = i.toDouble();
      focusCenter.add(model);
    }
    mapHeatModel.focusCenter = focusCenter;
    var encode = mapHeatModel.encode();
    var future = FileUtils.writeJsonFileDir(encode, "map1.json");
    setState(() {
      if(future == null){
        isSuccess1 = false;
      } else {
        isSuccess1 = true;
      }
    });
  }


  void get1() {
    FileUtils.readStringDir("map1.json").then((value){
      setState(() {
        string1 = value;
      });
    });
  }

  void clear1() {
    FileUtils.clearFileDataDir("map1.json").then((value){
      setState(() {
        isSuccess1 = value;
      });
    });
  }

  void save2() async{
    var mapHeatModel = new MapHeatModel();
    mapHeatModel.version = "100";
    List<MapPositionModel> focusCenter = new List();
    for(int i=0 ; i<2 ; i++){
      MapPositionModel model = new MapPositionModel();
      model.lng = i.toDouble();
      model.lat = i.toDouble();
      focusCenter.add(model);
    }
    mapHeatModel.focusCenter = focusCenter;
    var encode = mapHeatModel.encode();
    String appDocDir = await FileUtils.getAppDocDir();
    String filePath = appDocDir + '/map2.json';
    var future = FileUtils.writeJsonCustomFile(encode,filePath);
    setState(() {
      if(future == null){
        isSuccess2 = false;
      } else {
        isSuccess2 = true;
      }
    });
  }


  void get2() async{
    String appDocDir = await FileUtils.getAppDocDir();
    String filePath = appDocDir + '/map2.json';
    FileUtils.readStringCustomFile(filePath).then((value){
      setState(() {
        string2 = value;
      });
    });
  }

  void save3() async{
    var future = FileUtils.writeStringDir("fadsfadsfasd","map3.txt");
    setState(() {
      if(future == null){
        isSuccess3 = false;
      } else {
        isSuccess3 = true;
      }
    });
  }


  void get3() async{
    FileUtils.readStringDir("map3.txt").then((value){
      setState(() {
        string3 = value;
      });
    });
  }

  void save4() async{
    String appDocDir = await FileUtils.getAppDocDir();
    String filePath = appDocDir + '/map4.txt';
    var future = FileUtils.writeStringFile("dfadsfadsfdswe32",filePath);
    setState(() {
      if(future == null){
        isSuccess4 = false;
      } else {
        isSuccess4 = true;
      }
    });
  }


  void get4() async{
    String appDocDir = await FileUtils.getAppDocDir();
    String filePath = appDocDir + '/map4.txt';
    FileUtils.readStringCustomFile(filePath).then((value){
      setState(() {
        string4 = value;
      });
    });
  }

}

class MapHeatModel{
  String version;
  List<MapPositionModel> focusCenter;

  Object encode() {
    final Map<String, dynamic> ret = <String, dynamic>{};
    ret['version'] = version;
    ret['focusCenter'] = focusCenter?.map((e) => e?.encode())?.toList();
    return ret;
  }

  static MapHeatModel decode(Object message){
    if (message == null) return null;
    final Map<dynamic, dynamic> rawMap = message as Map<dynamic, dynamic>;
    final Map<String, dynamic> map = Map.from(rawMap);
    return MapHeatModel()
      ..version = map['version'] == null
          ?null
          :map['version'] as String

      ..focusCenter = map['focusCenter'] == null
          ?[]
          :List.from(map['focusCenter'])
          ?.map((e) => e == null
          ? null
          : MapPositionModel.decode(e))
          ?.toList()
    ;
  }
}

class MapPositionModel {
  double lat;
  double lng;

  Object encode() {
    final Map<String, dynamic> ret = <String, dynamic>{};
    ret['lat'] = lat;
    ret['lng'] = lng;
    return ret;
  }

  static MapPositionModel decode(Object message){
    if (message == null) return null;
    final Map<dynamic, dynamic> rawMap = message as Map<dynamic, dynamic>;
    final Map<String, dynamic> map = Map.from(rawMap);
    return MapPositionModel()
      ..lat = map['lat'] == null
          ?0.0
          :map['lat'] as double

      ..lng = map['lng'] == null
          ?0.0
          :map['lng'] as double
    ;
  }
}
