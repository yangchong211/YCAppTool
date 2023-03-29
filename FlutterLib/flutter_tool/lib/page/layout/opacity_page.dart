import 'package:flutter/material.dart';


class OpacityPage extends StatefulWidget {

  @override
  State<StatefulWidget> createState() {
    return new OpacityState();
  }

}

class OpacityState extends State<OpacityPage>{

  bool _offstage = false;

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
        appBar: new AppBar(title: new Text("不透明组件，可调节透明度")),
        body: ListView(
          children: <Widget>[
            new Text("不透明组件，可调节透明度"),
            Opacity(
                opacity: _offstage ? 1.0 : 0.5,// 不透明度，0.0 -- 1.0
                child:Container(
                  width: 200,
                  height: 200,
                  color: Colors.yellow,
                )
            ),
            RaisedButton(
              onPressed: () {
                setState(() {
                  _offstage = !_offstage;
                });
              },
              color: const Color(0xffff0000),
              child: new Text('调节透明度'),
            ),
          ],
        ));
  }

}