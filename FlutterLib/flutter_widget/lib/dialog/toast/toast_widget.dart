import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/widgets.dart';

typedef ToastAnimationStatusListener = void Function(AnimationStatus status);

class ToastWidget extends StatelessWidget {

  final String msg;
  final Color backgroundColor;
  final TextStyle textStyle;
  final double backgroundRadius;
  final Color shadowColor;
  final OverlayState overlayState;

  static AnimationController _controller;
  // 缩放动画
  static Animation _scaleAnim;
  // 透明度变化
  static Animation<double> _opacityAnimation;
  static int animMilliseconds = 300;

  // 构造方法
  ToastWidget(this.overlayState, this.msg, this.backgroundColor,
      this.textStyle, this.backgroundRadius, this.shadowColor) {
    // 初始化动画相关
    _controller = AnimationController(
        vsync: overlayState,
        duration: Duration(milliseconds: animMilliseconds));
    CurvedAnimation curvedAnimation = CurvedAnimation(
        parent: _controller, curve: Curves.linearToEaseOut);
    // 缩放
    _scaleAnim = Tween(begin: 0.2, end: 1.0).animate(curvedAnimation);
    // 透明度
    _opacityAnimation = Tween(begin: 0.2, end: 1.0).animate(curvedAnimation);
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      alignment: Alignment.center,
      width: MediaQuery.of(context).size.width,
      child: AnimatedBuilder(
          animation: _scaleAnim,
          builder: (ctx, child) {
            return Transform.scale(
              scale: _scaleAnim.value,
              child: AnimatedBuilder(
                animation: _opacityAnimation,
                builder: (ctx, child) {
                  return Opacity(
                    opacity: _opacityAnimation.value,
                    child: Container(
                      decoration: BoxDecoration(
                          // 背景色
                          color: backgroundColor,
                          borderRadius: BorderRadius.all(Radius.circular(backgroundRadius)),
                          //阴影设置
                          boxShadow: [
                            BoxShadow(
                              color: shadowColor,
                              offset: Offset(0, 6),
                              blurRadius: backgroundRadius,
                            ),
                          ]),
                      child: Container(
                        padding: EdgeInsets.fromLTRB(20, 20, 20, 20),
                        constraints: BoxConstraints(
                          minWidth: 400,
                          maxWidth: 520,
                        ),
                        child: Text(msg,
                            softWrap: true,
                            style: textStyle,
                            textAlign: TextAlign.center,
                        ),
                      ),
                    ),
                  );
                },
              ),
            );
          }),
    );
  }

  // 正向执行动画
  void playForwadAnimation() {
    _controller.forward();
  }

  // 反向执行动画
  void playReverseAnimation() {
    _controller.reverse();
  }

  // 监听动画
  void addAnimationStatusListener(ToastAnimationStatusListener listener) {
    _controller.addStatusListener((status) {
      listener(status);
    });
  }

  // 动画完成
  void disposeAnimation() {
    _controller.dispose();
  }
}
