import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/res/color/yc_colors.dart';

//Padding控件即填充控件，能给子控件插入给定的填充。
class PaddingPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('Padding布局'),
      ),
      body: new ListView(
        children: [
          new Text('EdgeInsetsGeometry是一个抽象类，开发中，我们一般都使用EdgeInsets类'),
          Container(
            color: YCColors.color_2EBFD9,
            child: new Padding(
              //all(double value) : 所有方向均使用相同数值的填充。
              padding: const EdgeInsets.all(80.0),
              child: new Text('Padding控件即填充控件，能给子控件插入给定的填充'),
            ),
          ),
          new Center(
            child: new Padding(
              //fromLTRB(double left, double top, double right, double bottom)：分别指定四个方向的填充。
              padding: EdgeInsets.fromLTRB(10, 20, 30, 40),
              child: new Text("这个是一个填充容器"),
            ),
          ),
          Column(
            //显式指定对齐方式为左对齐，排除对齐干扰
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              Padding(
                //左边添加8像素补白
                //only({left, top, right ,bottom })：可以设置具体某个方向的填充(可以同时指定多个方向)。
                padding: const EdgeInsets.only(left: 8.0),
                child: Text("Hello world"),
              ),
              Padding(
                //上下各添加8像素补白
                //symmetric({ vertical, horizontal })：用于设置对称方向的填充，vertical指top和bottom，horizontal指left和right。
                padding: const EdgeInsets.symmetric(vertical: 8.0),
                child: Text("I am Jack"),
              ),
              Padding(
                // 分别指定四个方向的补白
                padding: const EdgeInsets.fromLTRB(20.0,.0,20.0,20.0),
                child: Text("Your friend"),
              )
            ],
          ),
        ],
      )
    );
  }
}
