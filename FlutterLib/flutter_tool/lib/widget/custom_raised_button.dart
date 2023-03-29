import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/router/animation_type.dart';
import 'package:yc_flutter_utils/router/navigator_utils.dart';

//定义自带路由跳转button
// ignore: must_be_immutable
class CustomRaisedButton extends StatelessWidget {

  var _shapeBorder = new RoundedRectangleBorder(
      borderRadius: BorderRadius.all(Radius.circular(20.0)));
  var _textStyle = new TextStyle(color: Colors.white, fontSize: 16.0);
  var _btnTitle;
  var _pageNavigator;

  CustomRaisedButton(this._pageNavigator, this._btnTitle);

  @override
  Widget build(BuildContext context) {
    return RaisedButton(
      onPressed: () {
        // NavigatorUtils.push(context, _pageNavigator);
        NavigatorUtils.pushAnimationType(context, _pageNavigator, AnimationType.SlideRL);
      },
      child: Text(
        _btnTitle,
        style: _textStyle,
      ),
      color: Colors.lightGreen,
      highlightColor: Colors.green,
      shape: _shapeBorder,
    );
  }
}
