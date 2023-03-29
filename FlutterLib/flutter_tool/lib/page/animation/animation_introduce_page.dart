



import 'package:flutter/material.dart';

class AnimationIntroducePage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new AnimationIntroduceState();
  }

}

class AnimationIntroduceState extends State<AnimationIntroducePage>{
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("Flutter动画简介"),
      ),
      body: new Center(
        child: new ListView(
          children: [
            new Text(
              "这个是一个 Animation",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            new Text("Animation是一个抽象类，它本身和UI渲染没有任何关系，"
                "而它主要的功能是保存动画的插值和状态；"
                "其中一个比较常用的Animation类是Animation<double>。"),

            new Text(
              "这个是一个 Curve",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            new Text("动画过程可以是匀速的、匀加速的或者先加速后减速等。"
                "Flutter中通过Curve（曲线）来描述动画过程，我们把匀速动画称为"
                "线性的(Curves.linear)，而非匀速动画称为非线性的。"),


            new Text(
              "这个是一个 AnimationController",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            new Text("AnimationController用于控制动画，它包含动画的启动forward()、"
                "停止stop() 、反向播放 reverse()等方法。AnimationController会在"
                "动画的每一帧，就会生成一个新的值。默认情况下，AnimationController在"
                "给定的时间段内线性的生成从0.0到1.0（默认区间）的数字。"),


            new Text(
              "这个是一个 Tween",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            new Text("默认情况下，AnimationController对象值的范围是[0.0，1.0]。"
                "如果我们需要构建UI的动画值在不同的范围或不同的数据类型，"
                "则可以使用Tween来添加映射以生成不同的范围或数据类型的值。"),



          ],
        ),
      ),
    );
  }
}

