


import 'package:flutter/material.dart';

class WillPopScopePage extends StatefulWidget{


  @override
  State<StatefulWidget> createState() {
    return new WillPopScopeState();
  }

}

class WillPopScopeState extends State<WillPopScopePage>{
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("导航返回拦截-WillPopScope"),
      ),
      body: new Center(
        child: new ListView(
          children: [
            new Text("为了避免用户误触返回按钮而导致APP退出，在很多APP中都拦截了用户点击返回键的按钮，然后进行一些防误触判断，比如当用户在某一个时间段内点击两次时，才会认为用户是要退出（而非误触）。"),
            new Text(
              "这个是一个 导航返回拦截（WillPopScope）",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            Container(
              color: Colors.blue[50],
              child: new WillPopScopeTestRoute(),
            ),

          ],
        ),
      ),
    );
  }

}

class WillPopScopeTestRoute extends StatefulWidget {
  @override
  WillPopScopeTestRouteState createState() {
    return new WillPopScopeTestRouteState();
  }
}

class WillPopScopeTestRouteState extends State<WillPopScopeTestRoute> {
  DateTime _lastPressedAt; //上次点击时间

  @override
  Widget build(BuildContext context) {
    // onWillPop是一个回调函数，当用户点击返回按钮时调用（包括导航返回按钮及Android物理返回按钮），
    // 该回调需要返回一个Future对象，如果返回的Future最终值为false时，
    // 则当前路由不出栈(不会返回)，最终值为true时，当前路由出栈退出。
    return new WillPopScope(
        onWillPop: () async {
          if (_lastPressedAt == null ||
              DateTime.now().difference(_lastPressedAt) > Duration(seconds: 1)) {
            //两次点击间隔超过1秒则重新计时
            _lastPressedAt = DateTime.now();
            return false;
          }
          return true;
        },
        child: Container(
          alignment: Alignment.center,
          child: Text("1秒内连续按两次返回键退出"),
        )
    );
  }
}
