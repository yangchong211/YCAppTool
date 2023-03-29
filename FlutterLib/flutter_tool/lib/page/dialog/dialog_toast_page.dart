

import 'package:flutter/material.dart';
import 'package:flutter_widget/dialog/toast/toast_utils.dart';
import 'package:flutter_widget/notification/notification_factory.dart';

class DialogToastPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new DialogToastState();
  }

}

class DialogToastState extends State<DialogToastPage>{
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("弹窗和吐司"),
      ),
      body: new Center(
        child: new ListView(
          children: [
            new RaisedButton(
                onPressed: () {
                  RFToast.show("吐司一下",context);
                },
                child: new Text("吐司")
            ),
            new RaisedButton(
                onPressed: () {
                  RFToast.dismiss();
                },
                child: new Text("立马销毁吐司")
            ),

            MaterialButton(
              onPressed: () {
                NotificationFactory.showTopNotification(context,
                    "这个是通知栏");
              },
              child: new Text("通知栏1"),
              color: Colors.redAccent,
            ),
            MaterialButton(
              onPressed: () {
                NotificationFactory.showTopWarningNotification(context,
                    "这个是通知栏");
              },
              child: new Text("通知栏2"),
              color: Colors.redAccent,
            ),
          ],
        ),
      ),
    );
  }

}
