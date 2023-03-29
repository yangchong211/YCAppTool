import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/res/color/yc_colors.dart';
import 'package:yc_flutter_utils/flutter_utils.dart';
import 'package:yc_flutter_utils/i18/extension.dart';
import 'package:yc_flutter_utils/i18/template_time.dart';

class LocalizationsPage extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return new LocalizationsPageState();
  }

}


class LocalizationsPageState extends State<LocalizationsPage>{

  String name = "初始化值";
  String time1 = "";
  String time2 = "";

  @override
  void initState() {
    super.initState();
    var dateTime = DateUtils.getNowDateMs();
    time1 = LocalizationTime.getFormat1(dateTime);
    time2 = LocalizationTime.getFormat2(dateTime);
  }

  @override
  Widget build(BuildContext context) {
    //todo 放到这里为何会报错？？？
    //name = context.getString("name");
    //var work = context.getString("work");
    return new Scaffold(
        appBar: new AppBar(
          title: new Text("Localizations 国际化"),
        ),
        body: new Center(
          child: new Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: <Widget>[
              // new Text(
              //   "获取国际化，文本内容:" + context.getString("name"),
              //   style: new TextStyle(
              //     fontSize: 18.0,
              //     color: YCColors.color_FF0000,
              //   ),
              //   textDirection: TextDirection.rtl,
              // ),
              // new Text(
              //   "获取国际化，文本内容2:" + context.getString("work"),
              //   style: new TextStyle(
              //     fontSize: 18.0,
              //     color: YCColors.color_FF0000,
              //   ),
              //   textDirection: TextDirection.rtl,
              // ),
              new Text(
                "获取国际化，时间格式1:" +time1,
                style: new TextStyle(
                  fontSize: 18.0,
                  color: YCColors.color_FF0000,
                ),
                textDirection: TextDirection.rtl,
              ),
              new Text(
                "获取国际化，时间格式2:" +time2,
                style: new TextStyle(
                  fontSize: 18.0,
                  color: YCColors.color_FF0000,
                ),
                textDirection: TextDirection.rtl,
              ),
            ],
          ),
        ));
  }

}
