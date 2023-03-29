import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/res/image/images.dart';
import 'package:yc_flutter_utils/file/file_utils.dart';


class ImagePage extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return new ImagePageState();
  }
}


class ImagePageState extends State<ImagePage> {

  Color textColor = Colors.red;

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
        appBar: new AppBar(title: new Text("ImagePage")),
        body: ListView(
          children: <Widget>[
            new Text("从网络加载图片"),
            new Image.network(
              "https://p1.ssl.qhmsg.com/dr/220__/t01d5ccfbf9d4500c75.jpg",
              width: 500,
              height: 500,
            ),
            new Text('从本地加载图片1'),
            new Image.asset(Images.image2,
              width: 500,
              height: 200,
            ),
            new Image.asset(Images.image3,
              width: 500,
              height: 200,
            ),
          ],
        ));
  }
}
