

import 'package:flutter/material.dart';

class SnackBarUtils{

  static void showSnackBarDialog(BuildContext context , String title) {
    Scaffold.of(context).showSnackBar(new SnackBar(
      content: new Text(title),
      action: new SnackBarAction(
          label: "撤销",
          onPressed: () {
            print("点击撤回---------------");
          }),
    ));
  }

}