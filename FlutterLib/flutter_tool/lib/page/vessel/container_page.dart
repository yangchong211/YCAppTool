


import 'package:flutter/material.dart';

class ContainerPage extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return new ContainerState();
  }

}

class ContainerState extends State<ContainerPage>{
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("组合类容器-Container"),
      ),
      body: new Center(
        child: new Column(
          // crossAxisAlignment: CrossAxisAlignment.stretch,
          // mainAxisAlignment: MainAxisAlignment.center,
          children: [
            new Text("Container是一个组合类容器，它本身不对应具体的RenderObject，"
                "它是DecoratedBox、ConstrainedBox、Transform、Padding、Align等"
                "组件组合的一个多功能容器，所以我们只需通过一个Container组件"
                "可以实现同时需要装饰、变换、限制的场景。"),

            new Text(
              "这个是一个 Container",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            Container(
              margin: EdgeInsets.only(top: 50.0, left: 80.0), //容器外填充
              constraints: BoxConstraints.tightFor(width: 200.0, height: 150.0), //卡片大小
              decoration: BoxDecoration(//背景装饰
                  gradient: RadialGradient( //背景径向渐变
                      colors: [Colors.red, Colors.orange],
                      center: Alignment.topLeft,
                      radius: .98
                  ),
                  boxShadow: [ //卡片阴影
                    BoxShadow(
                        color: Colors.black54,
                        offset: Offset(2.0, 2.0),
                        blurRadius: 4.0
                    )
                  ]
              ),
              transform: Matrix4.rotationZ(.2), //卡片倾斜变换
              alignment: Alignment.center, //卡片内文字居中
              child: Text( //卡片文字
                "5.20", style: TextStyle(color: Colors.white, fontSize: 40.0),
              ),
            ),

            new Padding(padding: EdgeInsets.fromLTRB(0, 30, 0, 10)),
            new Text(
              "Container组件margin和padding属性的区别",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            Container(
              margin: EdgeInsets.all(10.0), //容器外补白
              color: Colors.orange,
              child: Text("Hello world!"),
            ),
            Container(
              padding: EdgeInsets.all(10.0), //容器内补白
              color: Colors.orange,
              child: Text("Hello world!"),
            ),
            Padding(
              padding: EdgeInsets.all(10.0),
              child: DecoratedBox(
                decoration: BoxDecoration(color: Colors.orange),
                child: Text("Hello world!"),
              ),
            ),
            DecoratedBox(
              decoration: BoxDecoration(color: Colors.orange),
              child: Padding(
                padding: const EdgeInsets.all(10.0),
                child: Text("Hello world!"),
              ),
            ),
          ],
        ),
      ),
    );
  }

}


