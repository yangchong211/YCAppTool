import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_widget/dialog/toast/toast_widget.dart';


enum RFToastStyle {
  light,
  dark,
}

class RFToast {

  static void show(String msg, BuildContext context,
      {RFToastStyle style = RFToastStyle.light}) {
    Color backgroundColor = style == RFToastStyle.light ? Colors.white : Colors.brown;
    Color textColor = style == RFToastStyle.light ? Colors.black : Colors.white;
    Color shadowColor = style == RFToastStyle.light ?  Colors.black12 :  Colors.black45.withOpacity(0.04);
    ToastView createToastView = ToastView.createToastView(
            msg,
            context,
            backgroundColor,
            TextStyle(
              fontSize: 16,
              color: textColor,
              decoration: TextDecoration.none,
            ),
            6,
            shadowColor
    );
    createToastView.show();
  }

  static void dismiss(){
    ToastView.dismiss();
  }
}

class ToastView {

  static OverlayEntry _overlayEntry;
  static bool _isShowing = false;
  static int visibleMilliseconds = 3000;
  //开启一个新toast的当前时间，对比是否已经展示了足够时间
  static DateTime _startedTime;
  static ToastWidget _toastWidget;

  // 单例
  static final ToastView _singleton = new ToastView._internal();

  factory ToastView() {
    return _singleton;
  }

  ToastView._internal();

  static ToastView createToastView(
      String msg,
      BuildContext context,
      Color backgroundColor,
      TextStyle textStyle,
      double backgroundRadius,
      Color shadowColor) {
    // 移除上一次显示，重置状态
    _isShowing = false;
    _overlayEntry?.remove();
    _overlayEntry = null;

    // 初始化属性
    OverlayState overlayState = Overlay.of(context);
    _toastWidget = ToastWidget(overlayState, msg, backgroundColor,
        textStyle, backgroundRadius, shadowColor);
    _overlayEntry = OverlayEntry(builder: (BuildContext context) {
      return _toastWidget;
    });

    overlayState.insert(_overlayEntry);
    return ToastView();
  }

  void show() async {
    _startedTime = DateTime.now();
    _isShowing = true;
    // 监听动画执行状态
    _toastWidget.addAnimationStatusListener((status) {
      if (status == AnimationStatus.dismissed) {
        // 销毁组件
        dismiss();
      }
    });
    // 正向执行动画
    _toastWidget.playForwadAnimation();

    await Future.delayed(Duration(milliseconds: visibleMilliseconds));
    if (DateTime.now().difference(_startedTime).inMilliseconds >
        visibleMilliseconds) {
      // 反向执行动画
      _toastWidget.playReverseAnimation();
      // ...销毁组件方法在AnimationController.addListiner中
    }
  }

  static dismiss() async {
    if (!_isShowing) {
      return;
    }
    _isShowing = false;
    //不能重复remove
    _overlayEntry?.remove();
    _overlayEntry = null;
    _toastWidget.disposeAnimation();
  }

}
