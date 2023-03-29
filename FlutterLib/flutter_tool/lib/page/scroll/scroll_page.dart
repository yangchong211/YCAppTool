


import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/page/scroll/custom_scroll_view_page.dart';
import 'package:yc_flutter_tool/page/scroll/grid_view_page.dart';
import 'package:yc_flutter_tool/page/scroll/list_view_page.dart';
import 'package:yc_flutter_tool/page/scroll/scroll_listener_page.dart';
import 'package:yc_flutter_tool/page/scroll/scroll_text_page.dart';
import 'package:yc_flutter_tool/page/scroll/single_child_scroll_page.dart';
import 'package:yc_flutter_tool/widget/custom_raised_button.dart';

class ScrollPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new ScrollState();
  }

}

class ScrollState extends State<ScrollPage>{
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("可滚动组件"),
      ),
      body: new Center(
        child: new Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            CustomRaisedButton(new ScrollTextPage(), "可滚动组件简介"),
            CustomRaisedButton(new SingleChildScrollPage(), "SingleChildScrollView"),
            CustomRaisedButton(new ListViewLayoutPage(), "ListView"),
            CustomRaisedButton(new GridViewPage(), "GridView"),
            CustomRaisedButton(new CustomScrollViewPage(), "CustomScrollView"),
            CustomRaisedButton(new ScrollListenerPage(), "滚动组件监听"),
          ],
        ),
      ),
    );
  }

}

