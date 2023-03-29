

import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/date/date_utils.dart';
import 'package:yc_flutter_utils/i18/template_time.dart';
import 'package:yc_flutter_utils/i18/extension.dart';
import 'package:yc_flutter_utils/screen/screen_utils.dart';


class ScreenPage extends StatefulWidget {

  ScreenPage();

  @override
  State<StatefulWidget> createState() {
    return new _PageState();
  }

}

class _PageState extends State<ScreenPage> {

  String screen = "初始化值";

  @override
  void initState() {
    super.initState();
    //延时500毫秒执行
    Future.delayed(const Duration(milliseconds: 2500), () {
      set(context);
    });
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
        appBar: new AppBar(
          title: new Text("ScreenUtils 屏幕工具类"),
        ),
        body: new Center(
          child: new Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: <Widget>[
              new Text(screen),
            ],
          ),
        ));
  }

  void set(BuildContext context) {
    ScreenUtils.instance.init(context);
    var screenWidthDp = ScreenUtils.screenWidthDp;
    var screenHeightDp = ScreenUtils.screenHeightDp;
    var pixelRatio = ScreenUtils.pixelRatio;
    var screenWidth = ScreenUtils.screenWidth;
    var screenHeight = ScreenUtils.screenHeight;
    var statusBarHeight = ScreenUtils.statusBarHeight;
    var bottomBarHeight = ScreenUtils.bottomBarHeight;
    var textScaleFactory = ScreenUtils.textScaleFactory;
    screen =
        "当前设备宽度 dp:"+screenWidthDp.toString() + "\n" +
            "当前设备高度 dp:"+screenHeightDp.toString() + "\n" +
            "设备的像素密度 :"+pixelRatio.toString() + "\n" +
            "当前设备宽度 px:"+screenWidth.toString() + "\n" +
            "当前设备高度 px:"+screenHeight.toString() + "\n" +
            "底部安全区距离 dp:"+bottomBarHeight.toString() + "\n" +
            "字体的缩放比例:"+textScaleFactory.toString() + "\n" +
            "状态栏高度 dp:"+statusBarHeight.toString() + "\n"
    ;
    setState(() {

    });
  }

}