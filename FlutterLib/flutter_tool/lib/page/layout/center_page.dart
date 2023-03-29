import 'package:flutter/material.dart';

//Center既中心定位控件，能够将子控件放在其内部中心。
class CenterLayoutPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('中心布局'),
      ),
      body: new ListView(
        children: [
          new Text("Center继承自Align，它比Align只少了一个alignment 参数"),
          new Center(
            child: new Text('Center布局使用比较简单，场景也比较单一，一般用于协助其他子widget布局，包裹其child widget显示在上层布局的中心位置'),
          ),
          DecoratedBox(
            decoration: BoxDecoration(color: Colors.red),
            child: Center(
              child: Text("xxx"),
            ),
          ),
          new Padding(padding: EdgeInsets.all(10)),
          DecoratedBox(
            decoration: BoxDecoration(color: Colors.amberAccent),
            child: Center(
              widthFactor: 1,
              heightFactor: 1,
              child: Text("xxx"),
            ),
          ),
        ],
      ),
    );
  }
}
