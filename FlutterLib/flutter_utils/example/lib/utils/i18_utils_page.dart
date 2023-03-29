

import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/date/date_utils.dart';
import 'package:yc_flutter_utils/i18/template_time.dart';
import 'package:yc_flutter_utils/i18/extension.dart';


class I18Page extends StatefulWidget {

  I18Page();

  @override
  State<StatefulWidget> createState() {
    return new _PageState();
  }
}

class _PageState extends State<I18Page> {

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
    return new Scaffold(
        appBar: new AppBar(
          title: new Text("Localizations 国际化"),
        ),
        body: new Center(
          child: new Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: <Widget>[
              new Text(
                "获取国际化，文本内容:" + context.getString("name"),
              ),
              new Text(
                "获取国际化，文本内容2:" + context.getString("work"),
              ),
              new Text(
                "获取国际化，时间格式 :" +time1,
                style: new TextStyle(
                  fontSize: 18.0,
                ),
                textDirection: TextDirection.rtl,
              ),
              new Text(
                "获取国际化，时间格式 :" +time2,
                style: new TextStyle(
                  fontSize: 18.0,
                ),
                textDirection: TextDirection.rtl,
              ),
            ],
          ),
        ));
  }

}