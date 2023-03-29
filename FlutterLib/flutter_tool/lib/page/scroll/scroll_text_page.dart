import 'package:flutter/material.dart';

class ScrollTextPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new ScrollTextState();
  }

}

class ScrollTextState extends State<ScrollTextPage>{
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("可滚动组件简介"),
      ),
      body: new Center(
        child: new ListView(
          children: [

          ],
        ),
      ),
    );
  }

}