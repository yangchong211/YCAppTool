

import 'package:flutter/material.dart';

///定义一个路由类FadeRoute
///无论是MaterialPageRoute、CupertinoPageRoute，还是PageRouteBuilder，
///它们都继承自PageRoute类，而PageRouteBuilder其实只是PageRoute的一个包装，
///我们可以直接继承PageRoute类来实现自定义路由
class FadeRoute extends PageRoute {

  FadeRoute({
    @required this.builder,
    this.transitionDuration = const Duration(milliseconds: 300),
    this.opaque = true,
    this.barrierDismissible = false,
    this.barrierColor,
    this.barrierLabel,
    this.maintainState = true,
  });

  /// 跳转目标widget
  final WidgetBuilder builder;

  /// 动画时间
  @override
  final Duration transitionDuration;

  /// 是否不透明的
  @override
  final bool opaque;

  @override
  final bool barrierDismissible;

  /// 屏障的颜色
  @override
  final Color barrierColor;

  /// 屏障的标签
  @override
  final String barrierLabel;

  /// 是否状态保留
  @override
  final bool maintainState;

  @override
  Widget buildPage(BuildContext context, Animation<double> animation,
      Animation<double> secondaryAnimation) => builder(context);

  @override
  Widget buildTransitions(BuildContext context, Animation<double> animation,
      Animation<double> secondaryAnimation, Widget child) {
    return FadeTransition(
      //动画效果
      opacity: animation,
      //跳转目标页面
      child: builder(context),
    );
  }
}


