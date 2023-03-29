


import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/res/image/images.dart';

class AnimationListenerPage2 extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new AnimationListenerState2();
  }

}

class AnimationListenerState2 extends State<AnimationListenerPage2>{
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("动画基本结构及状态监听"),
      ),
      body: new Center(
        child: new ListView(
          children: [
            new Text(
              "这个是一个 动画状态监听",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            new Text("可以通过Animation的addStatusListener()方法来添加动画状态改变监听器。"
                "Flutter中，有四种动画状态，在AnimationStatus枚举类中定义"),
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


  Widget getFuture(){
    var scaleAnimationRoute = new ScaleAnimationRoute2();
    return scaleAnimationRoute;
  }

}


class ScaleAnimationRoute2 extends StatefulWidget {
  @override
  _ScaleAnimationRouteState2 createState() => new _ScaleAnimationRouteState2();
}

//需要继承TickerProvider，如果有多个AnimationController，则应该使用TickerProviderStateMixin。
class _ScaleAnimationRouteState2 extends State<ScaleAnimationRoute2>  with SingleTickerProviderStateMixin{

  Animation<double> animation;
  AnimationController controller;

  initState() {
    super.initState();
    controller = new AnimationController(
        duration: const Duration(seconds: 5), vsync: this);
    //图片宽高从0变到300
    animation = new Tween(begin: 0.0, end: 200.0).animate(controller)..addListener(() {
      setState(()=>{

      });
    });
    animation.addStatusListener((status) {
      if (status == AnimationStatus.completed) {
        //动画执行结束时反向执行动画
        controller.reverse();
      } else if (status == AnimationStatus.dismissed) {
        //动画恢复到初始状态时执行动画（正向）
        controller.forward();
      }
    });
    //启动动画(正向执行)
    controller.forward();
  }

  @override
  Widget build(BuildContext context) {
    return new Center(
      child: Image.asset(Images.person,
          width: animation.value,
          height: animation.value
      ),
    );
  }

  dispose() {
    //路由销毁时需要释放动画资源
    controller.dispose();
    super.dispose();
  }
}
