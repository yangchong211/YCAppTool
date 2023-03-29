


import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/page/vessel/box_decoration_page.dart';
import 'package:yc_flutter_tool/page/vessel/box_page.dart';
import 'package:yc_flutter_tool/page/vessel/clip_page.dart';
import 'package:yc_flutter_tool/page/vessel/container_page.dart';
import 'package:yc_flutter_tool/page/vessel/padding_page.dart';
import 'package:yc_flutter_tool/page/vessel/transform_page.dart';
import 'package:yc_flutter_tool/widget/custom_raised_button.dart';

class VesselWidgetPage extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return new VesselWidgetState();
  }

}

class VesselWidgetState extends State<VesselWidgetPage>{
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("容器组件"),
      ),
      body: new Center(
        child: new Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            CustomRaisedButton(new PaddingPage(), "填充容器-Padding"),
            CustomRaisedButton(new BoxPage(), "尺寸限制类容器-box"),
            CustomRaisedButton(new BoxDecorationPage(), "装饰容器-DecoratedBox"),
            CustomRaisedButton(new TransformPage(), "变换容器-Transform"),
            CustomRaisedButton(new ContainerPage(), "组合类容器-Container"),
            CustomRaisedButton(new ClipPage(), "剪裁容器-Clip"),
          ],
        ),
      ),
    );
  }

}