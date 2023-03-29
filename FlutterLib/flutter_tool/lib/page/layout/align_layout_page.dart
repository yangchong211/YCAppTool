import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/res/image/images.dart';

class AlignLayoutPage extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('Align布局'),
      ),
      body: new ListView(
        children: <Widget>[
          new Text("Align 组件可以调整子组件的位置，并且可以根据子组件的宽高来确定自身的的宽高"),
          Container(
            height: 120,
            width: 120,
            color: Colors.blue[50],
            child: Align(
              alignment: Alignment.topRight,
              child: new Image.asset(
                  Images.image1,
                width: 60,
                height: 30,
              ),
            ),
          ),
          new Text("不显式指定宽高，而通过同时指定widthFactor和heightFactor 为2也是可以达到同样的效果"),
          Container(
            height: 120.0,
            width: 120.0,
            color: Colors.blue[50],
            child:  Align(
              widthFactor: 2,
              heightFactor: 2,
              alignment: Alignment.center,
              child: FlutterLogo(
                size: 60,
              ),
            ),
          ),
          new Text("FractionalOffset偏移达到效果"),
          Container(
            height: 120.0,
            width: 120.0,
            color: Colors.blue[50],
            child: Align(
              alignment: FractionalOffset(0.2, 0.6),
              child: FlutterLogo(
                size: 60,
              ),
            ),
          ),
        ],
      ),
    );
  }
}