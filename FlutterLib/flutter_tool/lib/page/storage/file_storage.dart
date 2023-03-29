import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:path_provider/path_provider.dart';
import 'dart:io';

class FileStorage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => StorageState();
}

class StorageState extends State<FileStorage> {
  var _textFieldController = new TextEditingController();
  var _storageString = '';

  /**
   * todo 学习下 https://blog.csdn.net/niceyoo/article/details/93048998
   * 利用文件存储数据
   */
  saveString() async {
    final file = await getFile('file.text');
    //写入字符串
    file.writeAsString(_textFieldController.value.text.toString());
    // var string = _textFieldController.value.text.toString();
    // FileCache.saveString(string);
  }

  /**
   * 获取存在文件中的数据
   */
  Future getString() async {
    // final file = await getFile('file.text');
    // var filePath  = file.path;
    // setState(() {
    //   file.readAsString().then((String value) {
    //     _storageString = value +'\n文件存储路径：'+filePath;
    //   });
    // });
    print("获取存在文件中的数据-----");
    var string = await FileCache.getStringNew();
    print("获取存在文件中的数据"+string.toString());
    setState(() {
      _storageString = string;
    });
  }

  /**
   * 初始化文件路径
   */
  Future<File> getFile(String fileName) async {
    //获取应用文件目录类似于Ios的NSDocumentDirectory和Android上的 AppData目录
    final fileDirectory = await getApplicationDocumentsDirectory();

    //获取存储路径
    final filePath = fileDirectory.path;

    //或者file对象（操作文件记得导入import 'dart:io'）
    return new File(filePath + "/"+fileName);
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('文件存储'),
      ),
      body: new Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Text("文件存储", textAlign: TextAlign.center),
          TextField(
            controller: _textFieldController,
          ),
          MaterialButton(
            onPressed: saveString,
            child: new Text("存储"),
            color: Colors.cyan,
          ),
          MaterialButton(
            onPressed: getString,
            child: new Text("获取"),
            color: Colors.deepOrange,
          ),
          Text("文件存储，存储model", textAlign: TextAlign.center),
          TextField(
            controller: _textFieldController,
          ),
          MaterialButton(
            onPressed: saveStringModel,
            child: new Text("存储model"),
            color: Colors.cyan,
          ),
          MaterialButton(
            onPressed: getStringModel,
            child: new Text("获取model"),
            color: Colors.deepOrange,
          ),
          MaterialButton(
            onPressed: clearModel,
            child: new Text("清除model文件"),
            color: Colors.deepOrange,
          ),
          Text('从文件存储中获取的值为  $_storageString'),
        ],
      ),
    );
  }

  void saveStringModel() {
    var mapPositionModel = new MapPositionModel();
    mapPositionModel.lat = 1;
    mapPositionModel.lng = 2;
    var mapHeatModel = new MapHeatModel();
    var list = new List<MapPositionModel>();
    list.add(mapPositionModel);
    mapHeatModel.focusCenter = list;
    mapHeatModel.version = "520";
    FileCache.saveHeatString(mapHeatModel);
  }


  void getStringModel() async {
    print("获取存在文件中的bean数据-----");
    var model = await FileCache.getHeatString();
    print("获取存在文件中的bean数据-----"+model.version + "--"+model.focusCenter.length.toString());
  }

  void clearModel() async{
    //获取文件
    FileCache.clearHeat();
  }

}

class FileCache{

  //利用文件存储数据
  static saveString(String str) async {
    final file = await getFile('file.json');
    //写入字符串
    file.writeAsString(str);
  }

  ///使用async、await，返回是一个Future对象
  //获取存在文件中的数据
  static Future<String> getString() async {
    final file = await getFile('file.json');
    /*var _storageString;
    file.readAsString().then((String value) {
      _storageString = value; //+'\n文件存储路径：'+filePath;
      return _storageString;
    });*/
    String contents = await file.readAsString();
    return contents;
  }

  static Future<String> getStringNew() async {
    final file = await getFile('file.json');
    /*var _storageString;
    file.readAsString().then((String value) {
      _storageString = value; //+'\n文件存储路径：'+filePath;
      return _storageString;
    });*/
    String contents = await file.readAsString();
    return contents;
  }

  //初始化文件路径
  static Future<File> getFile(String fileName) async {
    //获取应用文件目录类似于Ios的NSDocumentDirectory和Android上的 AppData目录
    final fileDirectory = await getApplicationDocumentsDirectory();
    //获取存储路径
    final filePath = fileDirectory.path;
    //或者file对象（操作文件记得导入import 'dart:io'）
    return new File(filePath + "/"+fileName);
  }


  static saveHeatString(MapHeatModel model) async {
    if(model==null){
      return;
    }
    //获取文件
    var file = await getFile("hot.json");
    print("saveHeatString---file--"+file.path);
    //将model转化成json字符串
    Map<String, dynamic> user = model.encode();
    var encode = json.encode(user);
    //写入字符串
    file.writeAsString(encode);
  }


  static Future<MapHeatModel> getHeatString() async {
    //获取文件
    final file = await getFile("hot.json");
    print("getHeatString---file--"+file.path);
    //var filePath  = file.path;
    //从文件读取数据
    // file.readAsString().then((String value) {
    //   Map map = json.decode(value);
    //   MapHeatModel model = MapHeatModel.decode(map);
    // });
    String value = await file.readAsString();
    Map map = json.decode(value);
    MapHeatModel model = MapHeatModel.decode(map);
    return model;
  }

  static clearHeat() async{
    //获取文件
    final file = await getFile("hot.json");
    file.delete();
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
