import 'package:flutter/material.dart';

//Opacity控件能调整子控件的不透明度，使子控件部分透明，不透明度的量从0.0到1.1之间，0.0表示完全透明，1.1表示完全不透明。
class OpacityLayoutDemo extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      backgroundColor: Colors.white,
      appBar: new AppBar(
        title: new Text('Opacity'),
      ),
      body: new Center(
        child: new Stack(
          alignment: AlignmentDirectional.center,
          children: <Widget>[
            new Text("我在透明区域下方"),
            new Opacity(
              opacity: 0.5,
              child: new Container(
                width: 200.0,
                height: 220.0,
                decoration: new BoxDecoration(color: Colors.redAccent),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
