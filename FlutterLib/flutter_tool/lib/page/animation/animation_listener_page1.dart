


import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter_widget/dialog/snack/snack_bar_utils.dart';
import 'package:yc_flutter_tool/res/image/images.dart';
import 'package:yc_flutter_utils/bus/event_bus_helper.dart';
import 'package:yc_flutter_utils/flutter_utils.dart';

class AnimationListenerPage1 extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new AnimationListenerState1();
  }

}

class AnimationListenerState1 extends State<AnimationListenerPage1>{

  String animStatus;

  @override
  void initState() {
    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("动画基本结构"),
      ),
      body: new Center(
        child: new ListView(
          children: [
            new Text(
              "这个是一个 动画基本结构",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            new Text("演示一下最基础的动画实现方式：$animStatus" ),
            new RaisedButton(
                onPressed: () {
                  bus.emit("animation1", (arg) {});
                },
                child: new Text("开启动画")
            ),
            Container(
              height: 200,
              color: Colors.blue[50],
              child: getFuture1(),
            ),


            new Text(
              "这个是一个 AnimatedWidget简化",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            new Text("上面动画通过addListener()和setState() 来更新UI这一步其实是通用的，"
                "如果每个动画中都加这么一句是比较繁琐的。AnimatedWidget类封装了"
                "调用setState()的细节，并允许我们将widget分离出来"),
            new RaisedButton(
                onPressed: () {
                  bus.emit("animation2", (arg) {});
                },
                child: new Text("开启动画")
            ),
            Container(
              height: 200,
              color: Colors.blue[50],
              child: getFuture2(),
            ),


          ],
        ),
      ),
    );
  }


  Widget getFuture1(){
    var scaleAnimationRoute = new ScaleAnimationRoute();
    return scaleAnimationRoute;
  }

  Widget getFuture2(){
    var scaleAnimationRoute2 = new ScaleAnimationRoute1();
    return scaleAnimationRoute2;
  }

}

class ScaleAnimationRoute extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return ScaleState();
  }

}

class ScaleState extends State<ScaleAnimationRoute> with SingleTickerProviderStateMixin{

  Animation<double> animation;
  AnimationController controller;

  @override
  void initState() {
    super.initState();
    controller = new AnimationController(duration: Duration(seconds: 3),vsync: this);
    //使用弹性曲线
    //animation = CurvedAnimation(parent: controller, curve: Curves.bounceIn);
    animation = new Tween(begin: 0.0,end: 300.0).animate(controller);
    animation.addListener(() {
      setState(() {
        //空刷新
      });
    });
    animation.addStatusListener((status) {
      LogUtils.d("addStatusListener : ${status.toString()}");
      if(status == AnimationStatus.forward){
        //动画正在正向执行
      } else if (status == AnimationStatus.dismissed){
        //动画在起始点停止
        SnackBarUtils.showSnackBarDialog(context, "动画结束了");
      }
    });
    controller.forward();

    bus.on("animation1", (arg) {
      //todo 重新开始动画如何操作
      controller.reverse();
    });
  }

  @override
  void dispose() {
    super.dispose();
    //路由销毁时需要释放动画资源
    controller.dispose();
    bus.off("animation1");
  }

  @override
  Widget build(BuildContext context) {
    return new Center(
      child: new Image.asset(
        Images.person,
        width: animation.value,
        height: animation.value,
      ),
    );
  }
}


/// 通过addListener()和setState() 来更新UI这一步其实是通用的，如果每个动画中都加这么一句是比较繁琐的。
/// AnimatedWidget类封装了调用setState()的细节，并允许我们将widget分离出来，重构后的代码如下：
class AnimatedImage extends AnimatedWidget {
  AnimatedImage({Key key, Animation<double> animation})
      : super(key: key, listenable: animation);

  Widget build(BuildContext context) {
    final Animation<double> animation = listenable;
    return new Center(
      child: Image.asset(Images.person,
          width: animation.value,
          height: animation.value
      ),
    );
  }
}


class ScaleAnimationRoute1 extends StatefulWidget {
  @override
  _ScaleAnimationRouteState1 createState() => new _ScaleAnimationRouteState1();
}

class _ScaleAnimationRouteState1 extends State<ScaleAnimationRoute1> with SingleTickerProviderStateMixin {

  Animation<double> animation;
  AnimationController controller;

  initState() {
    super.initState();
    controller = new AnimationController(
        duration: const Duration(seconds: 3), vsync: this);
    //图片宽高从0变到300
    animation = new Tween(begin: 0.0, end: 300.0).animate(controller);
    //启动动画
    controller.forward();
  }

  @override
  Widget build(BuildContext context) {
    // 使用AnimatedWidget简化
    //return AnimatedImage(animation: animation,);

    // 用AnimatedBuilder重构
    return getFuture3(animation);
  }

  dispose() {
    //路由销毁时需要释放动画资源
    controller.dispose();
    super.dispose();
  }

  /// 用AnimatedBuilder重构
  Widget getFuture3(Animation<double> animation) {
    return AnimatedBuilder(
      animation: animation,
      child: Image.asset( Images.person,),
      builder: (BuildContext ctx, Widget child) {
        return new Center(
          child: Container(
            height: animation.value,
            width: animation.value,
            child: child,
          ),
        );
      },
    );
  }


}