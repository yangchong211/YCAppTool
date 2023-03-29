import 'dart:ui';

import 'package:flutter/animation.dart';
import 'package:flutter/cupertino.dart';
import 'package:yc_flutter_utils/router/animation_type.dart';

final Tween<double> _tweenFade = Tween<double>(begin: 0, end: 1.0);

final Tween<Offset> _primaryTweenSlideFromBottomToTop =
    Tween<Offset>(begin: const Offset(0.0, 1.0), end: Offset.zero);

final Tween<Offset> _secondaryTweenSlideFromBottomToTop =
    Tween<Offset>(begin: Offset.zero, end: const Offset(0.0, -1.0));

final Tween<Offset> _primaryTweenSlideFromTopToBottom =
    Tween<Offset>(begin: const Offset(0.0, -1.0), end: Offset.zero);

final Tween<Offset> _secondaryTweenSlideFromTopToBottom =
    Tween<Offset>(begin: Offset.zero, end: const Offset(0.0, 1.0));

final Tween<Offset> _primaryTweenSlideFromRightToLeft =
    Tween<Offset>(begin: const Offset(1.0, 0.0), end: Offset.zero);

final Tween<Offset> _secondaryTweenSlideFromRightToLeft =
    Tween<Offset>(begin: Offset.zero, end: const Offset(-1.0, 0.0));

final Tween<Offset> _primaryTweenSlideFromLeftToRight =
    Tween<Offset>(begin: const Offset(-1.0, 0.0), end: Offset.zero);

final Tween<Offset> _secondaryTweenSlideFromLeftToRight =
    Tween<Offset>(begin: Offset.zero, end: const Offset(1.0, 0.0));

class AnimationPageRoute<T> extends PageRoute<T> {
  AnimationPageRoute({
    /// 必要参数要用@required 标注
    @required this.builder,
    this.isExitPageAffectedOrNot = true,
    //从右到左的滑动
    this.animationType = AnimationType.SlideRL,
    RouteSettings settings,
    this.maintainState = true,
    bool fullscreenDialog = false,
  })  : assert(builder != null),

        /// 使用assert断言函数，
        assert(isExitPageAffectedOrNot != null),
        assert(animationType != null),
        assert(maintainState != null),
        assert(fullscreenDialog != null),
        assert(opaque),
        super(settings: settings, fullscreenDialog: fullscreenDialog);

  final WidgetBuilder builder;

  /// 该参数只针对当[AnimationType]为[SlideLR]或[SlideRL]新页面及当前页面动画均有效
  final bool isExitPageAffectedOrNot;

  /// 动画类型
  final AnimationType animationType;

  @override
  final bool maintainState;

  @override
  Duration get transitionDuration => const Duration(milliseconds: 300);

  @override
  Color get barrierColor => null;

  @override
  String get barrierLabel => null;

  @override
  bool canTransitionTo(TransitionRoute<dynamic> nextRoute) =>
      nextRoute is AnimationPageRoute && !nextRoute.fullscreenDialog;

  @override
  Widget buildPage(BuildContext context, Animation<double> animation,
      Animation<double> secondaryAnimation) {
    final Widget result = builder(context);
    assert(() {
      if (result == null) {
        throw FlutterError.fromParts(<DiagnosticsNode>[
          ErrorSummary(
              'The builder for route "${settings.name}" returned null.'),
          ErrorDescription('Route builders must never return null.')
        ]);
      }
      return true;
    }());
    return Semantics(
        scopesRoute: true, explicitChildNodes: true, child: result);
  }

  @override
  Widget buildTransitions(BuildContext context, Animation<double> animation,
      Animation<double> secondaryAnimation, Widget child) {
    if (animationType == AnimationType.Fade)
      FadeTransition(
          opacity: CurvedAnimation(
            parent: animation,
            curve: Curves.linearToEaseOut,
            reverseCurve: Curves.easeInToLinear,
          ).drive(_tweenFade),
          child: child);
    final Curve curve = Curves.linear, reverseCurve = Curves.linear;
    final TextDirection textDirection = Directionality.of(context);
    Tween<Offset> primaryTween = _primaryTweenSlideFromRightToLeft,
        secondTween = _secondaryTweenSlideFromRightToLeft;
    if (animationType == AnimationType.SlideBT) {
      primaryTween = _primaryTweenSlideFromBottomToTop;
    } else if (animationType == AnimationType.SlideTB) {
      primaryTween = _primaryTweenSlideFromTopToBottom;
    } else if (animationType == AnimationType.SlideLR) {
      primaryTween = _primaryTweenSlideFromLeftToRight;
      secondTween = _secondaryTweenSlideFromLeftToRight;
    }
    Widget enterAnimWidget = SlideTransition(
        position: CurvedAnimation(
          parent: animation,
          curve: curve,
          reverseCurve: reverseCurve,
        ).drive(primaryTween),
        textDirection: textDirection,
        child: child);
    if (isExitPageAffectedOrNot != true ||
        ([AnimationType.SlideTB, AnimationType.SlideBT]
            .contains(animationType))) return enterAnimWidget;
    return SlideTransition(
        position: CurvedAnimation(
          parent: secondaryAnimation,
          curve: curve,
          reverseCurve: reverseCurve,
        ).drive(secondTween),
        textDirection: textDirection,
        child: enterAnimWidget);
  }

  @override
  String get debugLabel => '${super.debugLabel}(${settings.name})';
}
