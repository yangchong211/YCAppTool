


import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/res/image/images.dart';

class ClipPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new ClipState();
  }

}

class ClipState extends State<ClipPage>{
  @override
  Widget build(BuildContext context) {
    // 头像
    Widget avatar =  new Image.asset(
      Images.person,
      width: 50,
      height: 50,
    );
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("剪裁容器-Clip"),
      ),
      body: new Center(
        child: new ListView(
          children: [
            new Text(
              "这个是一个 剪裁为圆形",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            ClipOval(child: avatar), //剪裁为圆形
            new Text(
              "这个是一个 剪裁为圆角矩形",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            ClipRRect( //剪裁为圆角矩形
              borderRadius: BorderRadius.circular(5.0),
              child: avatar,
            ),
            new Text(
              "这个是一个 剪裁为圆角矩形",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                Align(
                  alignment: Alignment.topLeft,
                  widthFactor: .5,//宽度设为原来宽度一半，另一半会溢出
                  child: avatar,
                ),
                Text("你好世界", style: TextStyle(color: Colors.green),)
              ],
            ),
            new Text(
              "这个是一个 剪裁为圆角矩形",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                ClipRect(//将溢出部分剪裁
                  child: Align(
                    alignment: Alignment.topLeft,
                    widthFactor: .5,//宽度设为原来宽度一半
                    child: avatar,
                  ),
                ),
                Text("你好世界",style: TextStyle(color: Colors.green))
              ],
            ),
          ],
        ),
      ),
    );
  }

}