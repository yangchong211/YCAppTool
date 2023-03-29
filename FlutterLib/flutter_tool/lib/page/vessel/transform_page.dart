


import 'package:flutter/material.dart';
import 'dart:math' as math;

class TransformPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new TransformState();
  }

}

class TransformState extends State<TransformPage>{
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("变换容器-Transform"),
      ),
      body: new Center(
        child: new Column(
          children: [
            new Text("Transform可以在其子组件绘制时对其应用一些矩阵变换来实现一些特效。"),
            new Text(
              "这个是一个 Transform",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            Container(
              color: Colors.black,
              child: new Transform(
                alignment: Alignment.topRight, //相对于坐标系原点的对齐方式
                transform: new Matrix4.skewY(0.3), //沿Y轴倾斜0.3弧度
                child: new Container(
                  padding: const EdgeInsets.all(8.0),
                  color: Colors.deepOrange,
                  child: const Text('Apartment for rent!'),
                ),
              ),
            ),
            new Padding(
              padding: EdgeInsets.all(10),
            ),


            new Text(
              "这个是一个 Transform 平移",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            DecoratedBox(
              decoration:BoxDecoration(color: Colors.red),
              //默认原点为左上角，左移20像素，向上平移5像素
              child: Transform.translate(
                offset: Offset(-20.0, -5.0),
                child: Text("Hello world"),
              ),
            ),

            new Text(
              "这个是一个 Transform 旋转",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            DecoratedBox(
              decoration:BoxDecoration(color: Colors.red),
              child: Transform.rotate(
                //旋转90度
                angle:math.pi/2 ,
                child: Text("Hello world"),
              ),
            ),
            new Padding(
              padding: EdgeInsets.all(10),
            ),

            new Text(
              "这个是一个 Transform 缩放",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            DecoratedBox(
                decoration:BoxDecoration(color: Colors.red),
                child: Transform.scale(
                    scale: 1.5, //放大到1.5倍
                    child: Text("Hello world")
                )
            ),
            new Padding(
              padding: EdgeInsets.all(10),
            ),


            new Text(
              "这个是一个 Transform 注意事项",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            new Text("Transform的变换是应用在绘制阶段，而并不是应用在布局(layout)阶段，"
                "所以无论对子组件应用何种变化，其占用空间的大小和在屏幕上的位置都是固定不变的"
                "，因为这些是在布局阶段就确定的。"),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                DecoratedBox(
                    decoration:BoxDecoration(color: Colors.red),
                    child: Transform.scale(scale: 1.5,
                        child: Text("Hello world")
                    )
                ),
                Text("你好", style: TextStyle(color: Colors.green, fontSize: 18.0),)
              ],
            ),
            new Padding(
              padding: EdgeInsets.all(10),
            ),


            new Text(
              "这个是一个 RotatedBox",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            new Text("RotatedBox和Transform.rotate功能相似，它们都可以对子组件进行旋转变换，"
                "但是有一点不同：RotatedBox的变换是在layout阶段，会影响在子组件的位置和大小。"),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                DecoratedBox(
                  decoration: BoxDecoration(color: Colors.red),
                  //将Transform.rotate换成RotatedBox
                  child: RotatedBox(
                    quarterTurns: 1, //旋转90度(1/4圈)
                    child: Text("Hello world"),
                  ),
                ),
                Text("你好", style: TextStyle(color: Colors.green, fontSize: 18.0),)
              ],
            ),

          ],
        ),
      ),
    );
  }

}

