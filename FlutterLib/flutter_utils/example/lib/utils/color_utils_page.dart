

import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/color/color_utils.dart';
import 'package:yc_flutter_utils/date/data_formats.dart';
import 'package:yc_flutter_utils/date/date_utils.dart';
import 'package:yc_flutter_utils/encrypt/encrypt_utils.dart';
import 'package:yc_flutter_utils/object/object_utils.dart';

class ColorPage extends StatefulWidget {

  ColorPage();

  @override
  State<StatefulWidget> createState() {
    return new _PageState();
  }
}

class _PageState extends State<ColorPage> {

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: new AppBar(
        title: new Text("ColorUtils 颜色工具类"),
        centerTitle: true,
      ),
      body: new Column(
        children: <Widget>[
          new Text("hexToColor转化1：",
            style: new TextStyle(
              color: ColorUtils.hexToColor('#A357D6'),
            ),
          ),
          new Text("将颜色转化为color1：",
            style: new TextStyle(
              color: ColorUtils.toColor('#FF6325'),
            ),
          ),
          new Text("hexToColor转化1：",
            style: new TextStyle(
              color: ColorUtils.hexToColor('#50A357D6'),
            ),
          ),
          new Text("将颜色转化为color1：",
            style: new TextStyle(
              color: ColorUtils.toColor('#50FF6325'),
            ),
          ),
          new Text("hexToColor转化2，错误格式：",
            style: new TextStyle(
              color: ColorUtils.hexToColor('#E5E5E81'),
            ),
          ),
          new Text("将颜色转化为color2，错误格式：",
            style: new TextStyle(
              color: ColorUtils.toColor('#E5E5E11'),
            ),
          ),
          new Text("将color颜色转变为字符串：" + ColorUtils.colorString(Colors.brown),

          ),
          new Text("检查字符串是否为十六进制：" + ColorUtils.isHexadecimal("#E5E5E8").toString()),
          new Text("检查字符串是否为十六进制：" + ColorUtils.isHexadecimal("#E52E5E81").toString()),
          new Text("检查字符串是否为十六进制：" + ColorUtils.isHexadecimal("#E52E5E811").toString()),
        ],
      ),
    );
  }
}