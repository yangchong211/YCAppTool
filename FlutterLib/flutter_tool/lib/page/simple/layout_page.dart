import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/page/layout/align_layout_page.dart';
import 'package:yc_flutter_tool/page/simple/layout/aspec_radio_layout_page.dart';
import 'package:yc_flutter_tool/page/layout/center_page.dart';
import 'package:yc_flutter_tool/page/scroll/list_view_page.dart';
import 'package:yc_flutter_tool/page/simple/layout/opacity_page.dart';
import 'package:yc_flutter_tool/page/vessel/padding_page.dart';
import 'package:yc_flutter_tool/page/vessel/box_decoration_page.dart';
import 'package:yc_flutter_tool/page/layout/stack_layout_page.dart';

class LayoutPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("Flutter"),
      ),
      body: new Center(
          child: new Column(
        children: <Widget>[
          Text(
            "各种Layout布局",
            style:
                new TextStyle(fontSize: 30.0, color: Colors.deepOrangeAccent),
          ),
          new SizedBox(
            height: 1.0,
            child: new Container(
              color: Colors.blue,
            ),
          ),
          new FlatButton(
              onPressed: () {
                Navigator.push(
                    context,
                    new MaterialPageRoute(
                        builder: (context) => new AlignLayoutPage()));
              },
              child: new Text("Align布局")),
          new FlatButton(
              onPressed: () {
                Navigator.push(
                    context,
                    new MaterialPageRoute(
                        builder: (context) => new CenterLayoutPage()));
              },
              child: new Text("Center布局")),
          new FlatButton(
              onPressed: () {
                Navigator.push(
                    context,
                    new MaterialPageRoute(
                        builder: (context) => new OpacityLayoutDemo()));
              },
              child: new Text("Opacity布局")),
          new FlatButton(
              onPressed: () {
                Navigator.push(
                    context,
                    new MaterialPageRoute(
                        builder: (context) => new AspectRadioLayoutPage()));
              },
              child: new Text("AspecRadioLayoutPage布局"))
        ],
      )),
    );
  }
}
