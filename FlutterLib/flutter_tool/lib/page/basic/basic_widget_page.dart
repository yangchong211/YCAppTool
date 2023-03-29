


import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/page/basic/i18_localization_page.dart';
import 'package:yc_flutter_tool/page/basic/icon_page.dart';
import 'package:yc_flutter_tool/page/basic/image_page.dart';
import 'package:yc_flutter_tool/page/basic/indicator_progress_page.dart';
import 'package:yc_flutter_tool/page/event/states_widget_page.dart';
import 'package:yc_flutter_tool/page/basic/switch_page.dart';
import 'package:yc_flutter_tool/page/basic/text_page.dart';
import 'package:yc_flutter_tool/page/basic/text_field_page.dart';
import 'package:yc_flutter_tool/page/basic/button_page.dart';
import 'package:yc_flutter_tool/widget/custom_raised_button.dart';
import 'package:yc_flutter_utils/log/log_utils.dart';

class BasicWidgetPage extends StatefulWidget{

  String title;

  BasicWidgetPage(this.title);

  @override
  State<StatefulWidget> createState() {
    return new BasicWidgetState();
  }

}

class BasicWidgetState extends State<BasicWidgetPage>{

  @override
  void initState() {
    super.initState();
    LogUtils.d('BasicWidgetState---initState');
  }

  @override
  void didChangeDependencies() {
    LogUtils.d('BasicWidgetState---didChangeDependencies');
    super.didChangeDependencies();
  }

  @override
  void deactivate() {
    LogUtils.d('BasicWidgetState---deactivate');
    super.deactivate();
  }

  @override
  void dispose() {
    LogUtils.d('BasicWidgetState---dispose');
    super.dispose();
  }


  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("基础组件 ${widget.title}"),
      ),
      body: new Center(
        child: new Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            CustomRaisedButton(new TextPage(), "Text组件"),
            CustomRaisedButton(new TextFieldPage(), "TextField组件"),
            CustomRaisedButton(new ImagePage(), "Image组件"),
            CustomRaisedButton(new IconPage(), "Icon组件"),
            CustomRaisedButton(new ButtonPage(), "Button控件"),
            CustomRaisedButton(new SwitchPage(), "单选开关和复选框控件"),
            CustomRaisedButton(new IndicatorProgressPage(), "进度指示器"),
            CustomRaisedButton(new LocalizationsPage(), "国际化"),
          ],
        ),
      ),
    );
  }

}