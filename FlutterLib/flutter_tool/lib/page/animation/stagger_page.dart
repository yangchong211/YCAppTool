


import 'dart:ffi';

import 'package:flutter/material.dart';

class StaggerPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new StaggerState();
  }

}

class StaggerState extends State<StaggerPage>{
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("交织动画"),
      ),
      body: new Center(
        child: new ListView(
          children: [
            new Text(
              "这个是一个 交织动画",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            new Text("实现一个柱状图增长的动画。StaggerAnimation中定义了三个动画，"
                "分别是对Container的height、color、padding属性设置的动画，"
                "然后通过Interval来为每个动画指定在整个动画过程中的起始点和终点。"),
            Container(
              height: 200,
              color: Colors.blue[50],
              child: getFuture(),
            ),
          ],
        ),
      ),
    );
  }

  // Widget getFuture(){
  //   var staggerRoute = new StaggerRoute1();
  //   return staggerRoute;
  // }

  Widget getFuture(){
    var staggerRoute = new StaggerRoute2();
    return staggerRoute;
  }
}


class StaggerRoute2 extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return MyState();
  }
}

class MyState extends State<StaggerRoute2> with SingleTickerProviderStateMixin{

  AnimationController _controller;

  @override
  void initState() {
    super.initState();
    _controller = new AnimationController(
        duration: const Duration(milliseconds: 2000),
        vsync: this
    );
  }

  @override
  void dispose() {
    super.dispose();
    _controller.dispose();
  }


  Future<Null> _playAnimation() async {
    try {
      //先正向执行动画
      await _controller.forward().orCancel;
      //再反向执行动画
      await _controller.reverse().orCancel;
    } on TickerCanceled {
      // the animation got canceled, probably because we were disposed
    }
  }

  @override
  Widget build(BuildContext context) {
    return new GestureDetector(
      onTap: () {
        _playAnimation();
      },
      child: new Center(
        child: new Container(
          width: 200,
          height: 200,
          decoration: BoxDecoration(
            color: Colors.black.withOpacity(0.1),
            border: Border.all(
              color:  Colors.black.withOpacity(0.5),
            ),
          ),
          child: AnimWidget(_controller),
        ),
      ),
    );
  }
}

class AnimWidget extends StatefulWidget{

  AnimationController controller;

  AnimWidget(AnimationController controller){
    this.controller = controller;
  }

  @override
  State<StatefulWidget> createState() {
    return AnimState();
  }
}

// 开始时高度从0增长到300像素，同时颜色由绿色渐变为红色；这个过程占据整个动画时间的60%。
// 高度增长到300后，开始沿X轴向右平移100像素；这个过程占用整个动画时间的40%。
class AnimState extends State<AnimWidget>{

  Animation<double> animationHeight;
  Animation<Color> animationColor;
  Animation<EdgeInsets> animationPadding;

  @override
  void initState() {
    super.initState();
    animationHeight = new Tween<double>(begin: 0.0, end: 200.0)
        .animate(
        CurvedAnimation(
          parent: widget.controller,
          curve: Interval(
            0.0, 0.6, //间隔，前60%的动画时间
            curve: Curves.ease,
          ),
        )
    );
    animationColor = new ColorTween(begin: Colors.brown,end: Colors.amber)
        .animate(
      CurvedAnimation(
        parent: widget.controller,
        curve: Interval(
          0.0, 0.6,//间隔，前60%的动画时间
          curve: Curves.ease,
        ),
      ),
    );

    animationPadding = new Tween<EdgeInsets>(
      begin:EdgeInsets.only(left: .0),
      end:EdgeInsets.only(left: 100.0),
    ).animate(
      CurvedAnimation(
        parent: widget.controller,
        curve: Interval(
          0.0, 0.4,//间隔，前60%的动画时间
          curve: Curves.ease,
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return AnimatedBuilder(
      builder: _builder,
      animation: widget.controller,
    );
  }

  Widget _builder(BuildContext context, Widget child) {
    return new Container(
      alignment: Alignment.bottomCenter,
      padding: animationPadding.value ,
      child: Container(
        color: animationColor.value,
        width: 50.0,
        height: animationHeight.value,
      ),
    );
  }


}



