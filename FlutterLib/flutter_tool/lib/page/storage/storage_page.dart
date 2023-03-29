import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/page/storage/file_storage.dart';
import 'package:yc_flutter_tool/page/storage/shared_perference_storage.dart';
import 'package:yc_flutter_tool/page/storage/sqf_lite_storage.dart';
import 'package:yc_flutter_tool/widget/custom_raised_button.dart';


class StoragePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("数据存储"),
      ),
      body: new Center(
        child: new Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            CustomRaisedButton(new FileStorage(), "文件存储"),
            CustomRaisedButton(new SharedPerferenceStorage(), "shared_preferences存储"),
            CustomRaisedButton(new SqfliteStorage(), "Sqflite数据库存储"),

          ],
        ),
      ),
    );
  }
}
