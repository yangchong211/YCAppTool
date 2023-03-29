

import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/page/basic/button_page.dart';
import 'package:yc_flutter_tool/page/basic/icon_page.dart';
import 'package:yc_flutter_tool/page/basic/image_page.dart';
import 'package:yc_flutter_tool/page/event/states_widget_page.dart';
import 'package:yc_flutter_tool/page/basic/switch_page.dart';
import 'package:yc_flutter_tool/page/basic/text_field_page.dart';
import 'package:yc_flutter_tool/page/basic/text_page.dart';
import 'package:yc_flutter_tool/widget/custom_raised_button.dart';
import 'package:yc_flutter_tool/widget/skeleton_animation_widget.dart';


class BasicSkeletonPage extends SkeletonAnimationComponent{
  @override
  Widget buildSkeletonContent(BuildContext context) {
    double screenWidth = MediaQuery.of(context).size.width;
    return SingleChildScrollView(
      physics: NeverScrollableScrollPhysics(),
      child: Container(
        color: skeletonBackgroundColor,
        child: new Center(
          child: new Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              SkeletonAnimationWidget(
                child: CustomRaisedButton(new StatesWidgetPage(), "状态(State)管理"),
                color: skeletonGrayColor,
                opacity: skeletonAnimationOpacity(0),
                height: 40,),
              SkeletonAnimationWidget(
                child: CustomRaisedButton(new TextPage(), "Text组件"),
                color: skeletonGrayColor,
                opacity: skeletonAnimationOpacity(0),
                height: 40,),
              SkeletonAnimationWidget(
                child: CustomRaisedButton(new TextFieldPage(), "TextField组件"),
                color: skeletonGrayColor,
                opacity: skeletonAnimationOpacity(0),
                height: 40,),
              SkeletonAnimationWidget(
                child: CustomRaisedButton(new ImagePage(), "Image组件"),
                color: skeletonGrayColor,
                opacity: skeletonAnimationOpacity(0),
                height: 40,),
              SkeletonAnimationWidget(
                child: CustomRaisedButton(new IconPage(), "Icon组件"),
                color: skeletonGrayColor,
                opacity: skeletonAnimationOpacity(0),
                height: 40,),
              SkeletonAnimationWidget(
                child: CustomRaisedButton(new ButtonPage(), "Button控件"),
                color: skeletonGrayColor,
                opacity: skeletonAnimationOpacity(0),
                height: 40,),
              SkeletonAnimationWidget(
                child: CustomRaisedButton(new SwitchPage(), "单选开关和复选框控件"),
                color: skeletonGrayColor,
                opacity: skeletonAnimationOpacity(0),
                height: 40,),
              SkeletonAnimationWidget(
                child: CustomRaisedButton(new IndicatorProgressPage(), "进度指示器"),
                color: skeletonGrayColor,
                opacity: skeletonAnimationOpacity(0),
                height: 40,),
            ],
          ),
        ),
      ),
    );
  }

  @override
  int skeletonAnimationWidgetNum() {
    return 8;
  }

}

class IndicatorProgressPage extends StatefulWidget {

  @override
  State<StatefulWidget> createState() {
    return new IndicatorProgressState();
  }

}

class IndicatorProgressState extends State<IndicatorProgressPage>{

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
        appBar: new AppBar(title: new Text("进度指示器")),
        body: ListView(
          children: <Widget>[
            new Text("两种进度指示器：LinearProgressIndicator和CircularProgressIndicator"),

          ],
        ));
  }

}
