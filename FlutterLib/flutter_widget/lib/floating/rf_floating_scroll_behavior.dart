import 'package:flutter/cupertino.dart';

class RFFloatingScrollBehavior extends ScrollBehavior {
  // 不展示 Android 超出半圆效果
  @override
  Widget buildViewportChrome(
      BuildContext context, Widget child, AxisDirection axisDirection) {
    return child;
  }
}
