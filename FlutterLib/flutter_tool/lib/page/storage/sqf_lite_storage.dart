import 'package:flutter/material.dart';
import 'package:path_provider/path_provider.dart';
import 'dart:io';
import 'package:sqflite/sqflite.dart';

class SqfliteStorage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => StorageState();
}

class StorageState extends State {
  var _textFieldController = new TextEditingController();
  var _storageString = '';

  /**
   * 利用Sqflite数据库存储数据
   */
  saveString() async {
    final db = await getDataBase('my_db.db');
    //写入字符串
    db.transaction((trx) {
      trx.rawInsert(
          'INSERT INTO user(name) VALUES("${_textFieldController.value.text.toString()}")');
    });
  }

  /**
   * 获取存在Sqflite数据库中的数据
   */
  Future getString() async {
    final db = await getDataBase('my_db.db');
    var dbPath = db.path;
    setState(() {
      db.rawQuery('SELECT * FROM user').then((List<Map> lists) {
        print('----------------$lists');
        var listSize = lists.length;
        //获取数据库中的最后一条数据
        _storageString = lists[listSize - 1]['name'] +
            "\n现在数据库中一共有${listSize}条数据" +
            "\n数据库的存储路径为${dbPath}";
      });
    });
  }

  /**
   * 初始化数据库存储路径
   */
  Future<Database> getDataBase(String dbName) async {
    //获取应用文件目录类似于Ios的NSDocumentDirectory和Android上的 AppData目录
    final fileDirectory = await getApplicationDocumentsDirectory();

    //获取存储路径
    final dbPath = fileDirectory.path;

    //构建数据库对象
    Database database = await openDatabase(dbPath + "/" + dbName, version: 1,
        onCreate: (Database db, int version) async {
      await db.execute("CREATE TABLE user (id INTEGER PRIMARY KEY, name TEXT)");
    });

    return database;
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('Sqflite数据库存储'),
      ),
      body: new Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Text("Sqflite数据库存储", textAlign: TextAlign.center),
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
          Text('从Sqflite数据库中获取的值为  $_storageString'),
        ],
      ),
    );
  }
}
