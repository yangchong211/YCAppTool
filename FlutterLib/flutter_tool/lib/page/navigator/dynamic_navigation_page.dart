import 'package:flutter/material.dart';

class DynamicNaviattionPage extends StatelessWidget {
  var username;
  var password;

  DynamicNaviattionPage({Key key, this.username, this.password})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("动态路由"),
      ),
      body: new Center(
        child: new Column(
          children: <Widget>[
            new MaterialButton(
              onPressed: () {
                Navigator.pop(context, "动态路由跳转页面返回值");
              },
              child: new Text("点我返回"),
              color: Colors.lightGreen,
            ),
            new Text("上页传递过来的username   $username"),
            new Text("上页传递过来的password   $password"),
          ],
        ),
      ),
    );
  }
}
