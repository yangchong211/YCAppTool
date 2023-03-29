import 'package:flutter/material.dart';

class GestureDetectorPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => new MyState();
}

class MyState extends State {
  var _infoMessage = "";
  var _infoMessage2 = "";
  var _infoMessage3 = "";
  var _infoMessage4 = "";
  var _left;
  var _top;
  void _printMessage(String message) {
    setState(() {
      _infoMessage = message;
    });
  }

  void _printMessage2(String message) {
    setState(() {
      _infoMessage2 = message;
    });
  }

  void _printMessage3(String message) {
    setState(() {
      _infoMessage3 = message;
    });
  }

  void _printMessage4(String message) {
    setState(() {
      _infoMessage4 = message;
    });
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
        appBar: new AppBar(
          title: new Text("Gestures"),
        ),
        body: new Column(
          children: <Widget>[
            new Center(
                child: new GestureDetector(
              child: new Container(
                alignment: Alignment.center,
                color: Colors.brown,
                height: 100,
                child: new Text(
                  "点击、双击、长按...",
                  style: new TextStyle(fontSize: 20.0),
                ),
              ),
              //点击
              onTap: () {
                _printMessage("GestureDetectorPage------onTap");
              },
                  //双击
              onDoubleTap: () {
                _printMessage("GestureDetectorPage------onDoubleTap");
              },
                  //长安街
              onLongPress: () {
                _printMessage("GestureDetectorPage-----onLongPress");
              },
            )),
            new Text(
              _infoMessage,
              style: new TextStyle(color: Colors.red, fontSize: 18.0),
            ),
            new Center(
                child: new GestureDetector(
                  child: new Container(
                    alignment: Alignment.center,
                    color: Colors.brown,
                    height: 100,
                    child: new Text(
                      "拖动、滑动...",
                      style: new TextStyle(fontSize: 20.0),
                    ),
                  ),
                  //手指按下时会触发此回调
                  onPanDown: (DragDownDetails e) {
                    //打印手指按下的位置(相对于屏幕)
                    _printMessage2("用户手指按下：${e.globalPosition}");
                  },
                  //手指滑动时会触发此回调
                  onPanUpdate: (DragUpdateDetails e) {
                    //用户手指滑动时，更新偏移，重新构建
                    setState(() {
                      _left += e.delta.dx;
                      _top += e.delta.dy;
                      _printMessage2("用户手指按下：$_left-----$_top");
                    });
                  },
                  onPanEnd: (DragEndDetails e){
                    //打印滑动结束时在x、y轴上的速度
                    _printMessage2("用户手指按下"+e.velocity.toString());
                  },
                )),
            new Text(
              _infoMessage2,
              style: new TextStyle(color: Colors.red, fontSize: 18.0),
            ),
            new Center(
                child: new GestureDetector(
                  child: new Container(
                    alignment: Alignment.center,
                    color: Colors.brown,
                    height: 100,
                    child: new Text(
                      "单一方向拖动...",
                      style: new TextStyle(fontSize: 20.0),
                    ),
                  ),
                  onVerticalDragStart: (details) {
                    _printMessage3("Start---在垂直方向开始位置:" +
                        details.globalPosition.toString());
                  },
                  onVerticalDragEnd: (details) {
                    _printMessage3("End---在垂直方向结束位置:" +
                        details.primaryVelocity.toString());
                  },
                  onVerticalDragUpdate: (DragUpdateDetails details){
                    _printMessage3("Update---在垂直方向结束位置:" +
                        details.delta.dy.toString());
                  },
                )),
            new Text(
              _infoMessage3,
              style: new TextStyle(color: Colors.red, fontSize: 18.0),
            ),
            new Center(
                child: new GestureDetector(
                  child: new Container(
                    alignment: Alignment.center,
                    color: Colors.brown,
                    height: 100,
                    child: new Text(
                      "缩放...",
                      style: new TextStyle(fontSize: 20.0),
                    ),
                  ),
                  onScaleUpdate: (ScaleUpdateDetails details) {
                    //缩放倍数在0.8到10倍之间
                    _printMessage4("Update---在垂直方向结束位置:" +
                        (200*details.scale.clamp(0.8, 10.0)).toString() );
                  },
                )),
            new Text(
              _infoMessage4,
              style: new TextStyle(color: Colors.red, fontSize: 18.0),
            ),
          ],
        ));
  }
}
