


import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';


class GestureClashPage extends StatefulWidget {

  @override
  GestureClashState createState() =>
      new GestureClashState();
}

class GestureClashState extends State<GestureClashPage> {
  double _top = 100.0;
  double _left = 100.0;

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: <Widget>[
        Positioned(
          top: _top,
          left: _left,
          child: GestureDetector(
            child: CircleAvatar(child: Text("A")),
            //垂直方向拖动事件
            onVerticalDragUpdate: (DragUpdateDetails details) {
              setState(() {
                _top += details.delta.dy;
              });
            },
            onHorizontalDragUpdate: (DragUpdateDetails details) {
              setState(() {
                _left += details.delta.dx;
              });
            },
          ),
        )
      ],
    );
  }
}

