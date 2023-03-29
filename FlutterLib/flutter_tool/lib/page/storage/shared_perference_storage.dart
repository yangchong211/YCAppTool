import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class SharedPerferenceStorage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => StorageState();
}

class StorageState extends State<SharedPerferenceStorage> {
  var _textFieldController = new TextEditingController();
  var _storageString = '';
  final STORAGE_KEY = 'storage_key';

  /**
   * 利用SharedPreferences存储数据
   */
  Future saveString() async {
    SharedPreferences sharedPreferences = await SharedPreferences.getInstance();
    sharedPreferences.setString(
        STORAGE_KEY, _textFieldController.value.text.toString());
  }

  /**
   * 获取存在SharedPreferences中的数据
   */
  Future getString() async {
    SharedPreferences sharedPreferences = await SharedPreferences.getInstance();
    setState(() {
      _storageString = sharedPreferences.get(STORAGE_KEY);
    });
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('shared_preferences存储'),
      ),
      body: new Column(
        children: <Widget>[
          Text("shared_preferences存储", textAlign: TextAlign.center),
          TextField(
            controller: _textFieldController,
          ),
          MaterialButton(
            onPressed: saveString,
            child: new Text("存储"),
            color: Colors.pink,
          ),
          MaterialButton(
            onPressed: getString,
            child: new Text("获取"),
            color: Colors.lightGreen,
          ),
          Text('shared_preferences存储的值为  $_storageString'),


        ],
      ),
    );
  }
}
