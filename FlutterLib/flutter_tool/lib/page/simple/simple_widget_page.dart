import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/page/basic/button_page.dart';
import 'package:yc_flutter_tool/page/event/gesture_detector_page.dart';
import 'package:yc_flutter_tool/page/basic/image_page.dart';
import 'package:yc_flutter_tool/page/simple/layout_page.dart';
import 'package:yc_flutter_tool/page/use/scaffold_page.dart';
import 'package:yc_flutter_tool/page/simple/sliverWidget/sliver_page.dart';
import 'package:yc_flutter_tool/page/basic/text_field_page.dart';
import 'package:yc_flutter_tool/page/basic/text_page.dart';
import 'package:yc_flutter_tool/page/dialog/toast_and_dialog_page.dart';
import 'package:yc_flutter_tool/widget/custom_raised_button.dart';



void main() {
  runApp(new MaterialApp(home: new SimpleWidgetMainPage()));
}

class SimpleWidgetMainPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("基础组件"),
      ),
      body: new Center(
        child: new ListView(
          children: <Widget>[
            CustomRaisedButton(new LayoutPage(), "layout布局"),
            CustomRaisedButton(new ToastAndDialogPage(), "ToastAndDialogPage"),
            CustomRaisedButton(new SliverPage(), "Sliver Widget"),
          ],
        ),
      ),
    );
  }
}
