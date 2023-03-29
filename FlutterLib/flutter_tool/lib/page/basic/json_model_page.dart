import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/res/color/yc_colors.dart';

class JsonModelPage extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return new JsonModelPageState();
  }

}


class JsonModelPageState extends State<JsonModelPage>{

  String text1 = "初始化值";

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
        appBar: new AppBar(
          title: new Text("Hello Flutter"),
        ),
        body: new Center(
          child: new Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: <Widget>[
              new Text(
                "这个是一个文本控件",
                style: new TextStyle(
                  fontSize: 18.0,
                  color: YCColors.color_FF0000,
                ),
                maxLines: 3,
                textDirection: TextDirection.rtl,
              ),
              new RaisedButton(
                  onPressed: () {

                  },
                  child: new Text("获取屏幕的宽高属性")
              ),
              new Text( "这个是文本"+text1),
            ],
          ),
        ));
  }

}
