import 'package:flutter/material.dart';

class StaticNavigatorPageWithResult extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("静态路由带返回参数"),
      ),
      body: new Center(
        child: new OutlineButton(
          onPressed: () {
            Navigator.of(context).pop("上个页面结束后返回的数据");
          },
          child: Text("点我返回上个页面结束后返回的数据"),
        ),
      ),
    );
  }
}
