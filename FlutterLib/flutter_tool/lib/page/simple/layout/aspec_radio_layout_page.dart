import 'package:flutter/material.dart';

//AspectRatio控件能强制子小部件的宽度和高度具有给定的宽高比，以宽度与高度的比例表示。
class AspectRadioLayoutPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('AspectRadio布局'),
      ),
      body: new AspectRatio(
        aspectRatio: 4.0 / 3.0,
        child: new Container(
          child: new Text(
            "AspectRatio控件能强制子小部件的宽度和高度具有给定的宽高比，以宽度与高度的比例表示",
            style: new TextStyle(fontSize: 20.0, color: Colors.white),
          ),
          decoration: new BoxDecoration(color: Colors.lightBlue),
        ),
      ),
    );
  }
}
