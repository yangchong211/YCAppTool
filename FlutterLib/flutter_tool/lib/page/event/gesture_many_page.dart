


import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/log/log_utils.dart';


class GestureManyPage extends StatefulWidget {

  @override
  GestureManyState createState() =>
      new GestureManyState();
}

class GestureManyState extends State<GestureManyPage> {
  final String tag = "GestureManyState";
  double _left = 0.0;
  double _leftB = 0.0;
  @override
  Widget build(BuildContext context) {
    return Stack(
      children: <Widget>[
        Positioned(
          top: 100,
          left: _left,
          child: GestureDetector(
            child: CircleAvatar(child: Text("A")), //要拖动和点击的widget
            onHorizontalDragUpdate: (DragUpdateDetails details) {
              setState(() {
                _left += details.delta.dx;
              });
            },
            onHorizontalDragEnd: (details){
              LogUtils.d("onHorizontalDragEnd",tag: tag);
            },
            onTapDown: (details){
              LogUtils.d("down",tag: tag);
            },
            onTapUp: (details){
              LogUtils.d("up",tag: tag);
            },
          ),
        ),
        ///通过Listener监听原始指针事件就行
        Positioned(
          top: 200.0,
          left: _leftB,
          child: Listener(
            onPointerDown: (details) {
              LogUtils.d("down--b",tag: tag);
            },
            onPointerUp: (details) {
              //会触发
              LogUtils.d("up---b",tag: tag);
            },
            child: GestureDetector(
              child: CircleAvatar(child: Text("B")),
              onHorizontalDragUpdate: (DragUpdateDetails details) {
                setState(() {
                  _leftB += details.delta.dx;
                });
              },
              onHorizontalDragEnd: (details) {
                LogUtils.d("onHorizontalDragEnd---b",tag: tag);
              },
            ),
          ),
        )
      ],
    );
  }
}
