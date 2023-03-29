

import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/page/custom/custom_combination_widget.dart';
import 'package:yc_flutter_tool/page/dialog/dialog_toast_page.dart';
import 'package:yc_flutter_tool/page/dialog/toast_and_dialog_page.dart';
import 'package:yc_flutter_tool/widget/custom_raised_button.dart';

class DialogPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new DialogState();
  }

}

class DialogState extends State<DialogPage>{
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("弹窗和吐司"),
      ),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          CustomRaisedButton(new ToastAndDialogPage(), "原始弹窗"),
          CustomRaisedButton(new DialogToastPage(), "吐司"),
          CustomRaisedButton(new CustomCombinationWidget(), "弹唱dialog"),
          CustomRaisedButton(new CustomCombinationWidget(), "弹窗pupup"),
        ],
      ),
    );
  }
}