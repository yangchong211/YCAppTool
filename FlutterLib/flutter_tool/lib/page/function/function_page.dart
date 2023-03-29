


import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/page/function/future_stream_page.dart';
import 'package:yc_flutter_tool/page/function/inherited_page.dart';
import 'package:yc_flutter_tool/page/function/provider_page.dart';
import 'package:yc_flutter_tool/page/function/theme_page.dart';
import 'package:yc_flutter_tool/page/function/will_pop_scope_page.dart';
import 'package:yc_flutter_tool/widget/custom_raised_button.dart';

class FunctionPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new FunctionState();
  }

}

class FunctionState extends State<FunctionPage>{
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("功能型组件"),
      ),
      body: new Center(
        child: new ListView(
          children: [
            CustomRaisedButton(new WillPopScopePage(), "导航返回拦截-WillPopScope"),
            CustomRaisedButton(new InheritedPage(), "数据共享-InheritedWidget"),
            CustomRaisedButton(new ProviderPage(), "跨组件状态共享-Provider"),
            CustomRaisedButton(new ThemePage(), "颜色和主题-Color-Theme"),
            CustomRaisedButton(new FutureStreamPage(), "异步UI更新-FutureBuilder-StreamBuilder"),

          ],
        ),
      ),
    );
  }

}