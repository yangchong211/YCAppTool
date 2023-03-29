


import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/page/animation/animated_decorated_page.dart';
import 'package:yc_flutter_tool/page/animation/animated_switcher_page.dart';
import 'package:yc_flutter_tool/page/animation/animation_introduce_page.dart';
import 'package:yc_flutter_tool/page/animation/animation_listener_page1.dart';
import 'package:yc_flutter_tool/page/animation/animation_listener_page2.dart';
import 'package:yc_flutter_tool/page/animation/hero_page.dart';
import 'package:yc_flutter_tool/page/animation/stagger_page.dart';
import 'package:yc_flutter_tool/widget/custom_raised_button.dart';

class AnimationPage extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return new AnimationState();
  }

}

class AnimationState extends State<AnimationPage>{
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("动画处理"),
      ),
      body: new Center(
        child: new Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            CustomRaisedButton(new AnimationIntroducePage(), "Flutter动画简介"),
            CustomRaisedButton(new AnimationListenerPage1(), "动画基本结构"),
            CustomRaisedButton(new AnimationListenerPage2(), "动画状态监听"),
            CustomRaisedButton(new HeroPage(), "Hero动画"),
            CustomRaisedButton(new StaggerPage(), "交织动画"),
            CustomRaisedButton(new AnimatedSwitcherPage(), "通用“动画切换”组件"),
            CustomRaisedButton(new AnimatedDecoratedPage(), "动画过渡组件"),
          ],
        ),
      ),
    );
  }

}