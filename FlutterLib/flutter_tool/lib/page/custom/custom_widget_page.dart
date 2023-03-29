

import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/page/custom/custom_combination_widget.dart';
import 'package:yc_flutter_tool/page/custom/custom_paint_widget.dart';
import 'package:yc_flutter_tool/page/custom/custom_progress_widget.dart';
import 'package:yc_flutter_tool/page/custom/custom_render_widget.dart';
import 'package:yc_flutter_tool/widget/custom_raised_button.dart';

class CustomWidgetPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new CustomWidgetState();
  }

}

class CustomWidgetState extends State<CustomWidgetPage>{
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("自定义widget"),
      ),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          CustomRaisedButton(new CustomCombinationWidget(), "组合现有组件"),
          CustomRaisedButton(new CustomPaintWidget(), "自定义自绘组件"),
          CustomRaisedButton(new CustomRenderWidget(), "实现RenderObject组件"),
          CustomRaisedButton(new CustomProgressWidget(), "自定义分段进度指示器"),
        ],
      ),
    );
  }
}