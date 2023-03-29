/// 直接用的 flutter 原生 bottom_sheet.dart 类，进行改造

import 'dart:async';
import 'dart:math';
import 'dart:ui' show lerpDouble;

import 'package:flutter/foundation.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/scheduler.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_widget/floating/rf_floating_scroll_behavior.dart';
import 'package:flutter_widget/floating/rf_floating_scroll_physics.dart';
import 'package:flutter_widget/res/color/flutter_colors.dart';



// 入场动画时间
const Duration _floatingEnterDuration = Duration(milliseconds: 300);
// 出场动画时间
const Duration _floatingExitDuration = Duration(milliseconds: 300);
// 转场动画速度曲线
const Curve _floatingCurve = Cubic(0.0, 0.0, 0.2, 1.0);
// 手指最小滑动速度
const double _minFlingVelocity = 700.0;
// 关闭页面的滑动距离阈值。(0.5 相当于页面滑动 50% 后释放收起)
const double _closeProgressThreshold = 0.5;

// 起始拖拽触发的回调
typedef RFFloatingStartHandler = void Function();
// 结束拖拽触发的回调
typedef RFFloatingDragEndHandler = void Function({
  bool isClosing,
});

/// 半浮层边框 UI
/// 1. 处理外边框 UI 样式
/// 2. 处理手势滑动事件
/// 3. 处理动画控制器
class _RFFloatingFrameWidget extends StatefulWidget {
  const _RFFloatingFrameWidget({
    Key key,
    this.animationController,
    this.enableDrag = true,
    this.onDragStart,
    this.onDragEnd,
    @required this.onClosing,
    @required this.builder,
  })  : assert(enableDrag != null),
        assert(onClosing != null),
        assert(builder != null),
        super(key: key);

  /// 入场/出场动画控制器
  final AnimationController animationController;

  /// 关闭回调
  final VoidCallback onClosing;

  /// 内容 UI 构建器
  final WidgetBuilder builder;

  /// 是否支持拖拽
  final bool enableDrag;

  /// 起始拖拽回调
  final RFFloatingStartHandler onDragStart;

  /// 结束拖拽回调
  final RFFloatingDragEndHandler onDragEnd;

  @override
  _RFFloatingFrameWidgetState createState() => _RFFloatingFrameWidgetState();

  // 动画控制器
  static AnimationController createAnimationController(TickerProvider vsync) {
    return AnimationController(
      duration: _floatingEnterDuration,
      reverseDuration: _floatingExitDuration,
      debugLabel: 'RFFloating',
      vsync: vsync,
    );
  }
}

class _RFFloatingFrameWidgetState extends State<_RFFloatingFrameWidget> {
  static const TAG = "RFFloatingFrameWidget";
  final GlobalKey _childKey = GlobalKey(debugLabel: 'RFFloatng child');

  bool isDragging = false;

  double get _childHeight {
    final RenderBox renderBox =
        _childKey.currentContext.findRenderObject() as RenderBox;
    return renderBox.size.height;
  }

  bool get _dismissUnderway =>
      widget.animationController.status == AnimationStatus.reverse;

  void _handleDragStart() {
    if (widget.onDragStart != null) {
      widget.onDragStart();
    }
    isDragging = true;
  }

  void _handleDragUpdate(double primaryDelta) {
    assert(widget.enableDrag);
    if (_dismissUnderway) return;

    double result = widget.animationController.value -
        primaryDelta / (_childHeight ?? primaryDelta);

    double realResult = min(max(0.0, result), 1.0);
    // 拖拽会改变动画的显示
    widget.animationController.value = realResult;
  }

  void _handleDragEnd(double velocity) {
    assert(widget.enableDrag);

    // 如果动画未完成，支持继续执行
    if (widget.animationController.isCompleted &&
        (_dismissUnderway || !isDragging)) return;

    bool isClosing = false;
    isDragging = false;

    // 如果滑动速度，大于最小滑动速度
    if (velocity > _minFlingVelocity) {
      // 如果手势速度，大于最小支持的手势速度，则触发 Animation#fling()，执行收起动画
      final double flingVelocity = -velocity / _childHeight;
      // 如果当前手势移动了，则触发动画
      if (widget.animationController.value > 0.0) {
        widget.animationController.fling(velocity: flingVelocity);
      }

      if (flingVelocity < 0.0) {
        isClosing = true;
      }
    } else if (widget.animationController.value < _closeProgressThreshold) {
      // 如果拖拽距离，小于最小支持的手势速度，则触发 Animation#fling()，执行恢复动画

      if (widget.animationController.value > 0.0)
        widget.animationController.fling(velocity: -1.0);
      isClosing = true;
    } else {
      /// 如果拖拽距离超出 0.5，则执行 animation 剩余动画。
      widget.animationController.forward();
    }

    print("_handleDragEnd:$velocity  ${widget.animationController.value}");

    if (widget.onDragEnd != null) {
      widget.onDragEnd(
        isClosing: isClosing,
      );
    }

    if (isClosing) {
      widget.onClosing();
    }
  }

  VelocityTracker _velocityTracker;
  DateTime _startTime;

  /// 处理浮层内部滚动事件
  void _handleScrollUpdate(ScrollNotification notification) {
    ScrollMetrics metrics = notification.metrics;

    print("notification:${notification.runtimeType}");

    // 如果不支持拖拽，或者非竖直滚动事件
    if (!widget.enableDrag || metrics.axis != Axis.vertical) {
      return;
    }

    // 不监听用户滚动通知
    if (notification is UserScrollNotification) {
      return;
    }

    // 是否滑动翻转
    final isScrollReversed = metrics.axisDirection == AxisDirection.down;

    // 如果当前在拖拽，此时恢复正常，则触发还原操作

    final offset = isScrollReversed
        ? metrics.pixels
        : metrics.maxScrollExtent - metrics.pixels;

    if (offset <= 0) {
      // 如果是拖拽结束事件
      if (notification is ScrollEndNotification) {
        final dragDetails = notification.dragDetails;

        if (dragDetails != null) {
          print("ScrollEndNotification:$dragDetails");
          _handleDragEnd(dragDetails.velocity.pixelsPerSecond.dy ?? 0.0);
          _velocityTracker = null;
          _startTime = null;
          return;
        }
      }

      // Otherwise the calculate the velocity with a VelocityTracker
      if (_velocityTracker == null) {
        //final pointerKind = defaultPointerDeviceKind(context);
        // ignore: deprecated_member_use
        _velocityTracker = VelocityTracker();
        _startTime = DateTime.now();
      }

      // 滚动距离
      double scrollPosition;
/*
      if (notification is ScrollUpdateNotification) {
        dragDetails = notification.dragDetails;
      }
*/
      // 获取超出的距离
      if (notification is OverscrollNotification) {
        scrollPosition = -notification.overscroll;
      }

      assert(_velocityTracker != null);
      assert(_startTime != null);
      final startTime = _startTime;
      final velocityTracker = _velocityTracker;

      // 如果未获取到拖拽事件，则表示非用户拖拽
      if (scrollPosition != null) {
        // 触发起始拖拽
        if (!isDragging) {
          _handleDragStart();
        }

        final duration = startTime.difference(DateTime.now());
        velocityTracker.addPosition(duration, Offset(0, offset));

        // 触发更新拖拽
        _handleDragUpdate(scrollPosition);
      } else if (isDragging) {
        final velocity = velocityTracker.getVelocity().pixelsPerSecond.dy;
        _velocityTracker = null;
        _startTime = null;
        _handleDragEnd(velocity);
      }
    }
    return;
  }

  bool extentChanged(DraggableScrollableNotification notification) {
    if (notification.extent == notification.minExtent) {
      widget.onClosing();
    }
    return false;
  }

  @override
  Widget build(BuildContext context) {
    // 边框 UI
    final Widget floatingFrame = KeyedSubtree(
      key: _childKey,
      child: widget.builder(context),
    );

    // 是否支持拖拽事件
    return !widget.enableDrag
        ? floatingFrame
        : GestureDetector(
            onVerticalDragStart: (DragStartDetails details) {
              _handleDragStart();
            },
            // 监听手势起始拖拽事件
            onVerticalDragUpdate: (DragUpdateDetails details) {
              _handleDragUpdate(details.primaryDelta ?? 0);
            },
            // 监听手势滑动事件
            onVerticalDragEnd: (DragEndDetails details) {
              print(TAG+"onVerticalDragEnd");
              _handleDragEnd(details?.velocity?.pixelsPerSecond?.dy ?? 0);
            },
            // 监听手势终止拖拽事件
            child: NotificationListener<ScrollNotification>(
              onNotification: (Notification notification) {
                _handleScrollUpdate(notification);
                return false;
              },
              child: floatingFrame,
            ),
            excludeFromSemantics: true,
          );
  }
}

/// 半浮层布局
class _RFFloatingLayout extends SingleChildLayoutDelegate {
  _RFFloatingLayout(this.context, this.spec, this.progress);

  final BuildContext context;
  final double progress;
  final RFFloatingSpec spec;

  /// 对子控件进行约束
  @override
  BoxConstraints getConstraintsForChild(BoxConstraints constraints) {
    /// 获取屏幕的高度
    double screenHeight = MediaQuery.of(context).size.height;

    /// 获取状态栏高度
    double statusBarHeight = MediaQuery.of(context).padding.top;

    /// 容器的最大高度
    double maxHeight = screenHeight - statusBarHeight - 20;

    // TODO：当前只实现 Phone 的半浮层，暂不处理 Pad，暂不关心尺寸
    double minHeight;

    if (spec == RFFloatingSpec.fixed) {
      minHeight = maxHeight;
      print("minHeight:$minHeight   maxHeight:${maxHeight}");
    } else {
      minHeight = 240;
    }

    return BoxConstraints(
      minWidth: constraints.maxWidth,
      maxWidth: constraints.maxWidth,
      minHeight: minHeight,
      maxHeight: maxHeight,
    );
  }

  @override
  Offset getPositionForChild(Size size, Size childSize) {
    // 根据动画的进度，确定布局的偏移位置
    return Offset(0.0, size.height - childSize.height * progress);
  }

  @override
  bool shouldRelayout(_RFFloatingLayout oldDelegate) {
    return progress != oldDelegate.progress;
  }
}

/// 半浮层展示 UI
class _RFFloating<T> extends StatefulWidget {
  const _RFFloating({
    Key key,
    this.spec,
    this.route,
    this.enableDrag = true,
  })  : assert(enableDrag != null),
        super(key: key);

  final _RFFloatingRoute<T> route;
  final RFFloatingSpec spec;
  final bool enableDrag;

  @override
  _RFFloatingState<T> createState() => _RFFloatingState<T>();
}

class _RFFloatingState<T> extends State<_RFFloating<T>> {
  // 动画曲线
  ParametricCurve<double> animationCurve = _floatingCurve;

  /// 辅助功能，通知弹窗展示。
  String _getRouteLabel(MaterialLocalizations localizations) {
    switch (Theme.of(context).platform) {
      case TargetPlatform.iOS:
      case TargetPlatform.macOS:
        return '';
      case TargetPlatform.android:
      case TargetPlatform.fuchsia:
      case TargetPlatform.linux:
      case TargetPlatform.windows:
        return localizations.dialogLabel;
    }
    return null;
  }

  void handleDragStart() {
    // 用户拖拽时使用的动效
    animationCurve = Curves.linear;
  }

  void handleDragEnd({bool isClosing}) {
    // 拖拽结束时，使用的动画速度曲线
    animationCurve = _RFFloatingSuspendedCurve(
      widget.route.animation.value,
      curve: _floatingCurve,
    );
  }

  @override
  Widget build(BuildContext context) {
    assert(debugCheckHasMediaQuery(context));
    assert(debugCheckHasMaterialLocalizations(context));

    final MediaQueryData mediaQuery = MediaQuery.of(context);
    final MaterialLocalizations localizations =
        MaterialLocalizations.of(context);
    final String routeLabel = _getRouteLabel(localizations);

    return AnimatedBuilder(
      animation: widget.route.animation, // 使用 RFFloatingRoute 的转场动画
      child: _RFFloatingFrameWidget(
        /// 半浮层的外边框
        animationController: widget.route._animationController,
        onClosing: () {
          /// 默认实现，关闭半浮层逻辑
          if (widget.route.isCurrent) {
            Navigator.pop(context);
          }
        },
        builder: widget.route.builder,
        enableDrag: widget.enableDrag,
        onDragStart: handleDragStart,
        onDragEnd: handleDragEnd,
      ),
      builder: (BuildContext context, Widget child) {
        // 获取当前动画进度
        final double animationValue = animationCurve.transform(
            mediaQuery.accessibleNavigation
                ? 1.0
                : widget.route.animation.value);

        return Semantics(
          scopesRoute: true,
          namesRoute: true,
          label: routeLabel,
          explicitChildNodes: true,
          child: ClipRect(
            child: CustomSingleChildLayout(
              delegate: _RFFloatingLayout(
                context,
                widget.spec,
                animationValue,
              ),
              child: child,
            ),
          ),
        );
      },
    );
  }
}

/// 半浮层跳转路由，继承 PopupRoute，表示展示在 Navigator 的页面之上
class _RFFloatingRoute<T> extends PopupRoute<T> {
  _RFFloatingRoute({
    this.builder,
    this.theme,
    this.barrierLabel,
    this.modalBarrierColor,
    this.isDismissible = true,
    this.enableDrag = true,
    this.spec,
    RouteSettings settings,
  })  : assert(isDismissible != null),
        assert(enableDrag != null),
        super(settings: settings);

  final WidgetBuilder builder;
  final ThemeData theme;
  final Color modalBarrierColor;
  final bool isDismissible;
  final bool enableDrag;
  final RFFloatingSpec spec;

  @override
  Duration get transitionDuration => _floatingEnterDuration;

  @override
  Duration get reverseTransitionDuration => _floatingExitDuration;

  @override
  bool get barrierDismissible => isDismissible;

  @override
  final String barrierLabel;

  @override
  Color get barrierColor => modalBarrierColor ?? Colors.black54;

  AnimationController _animationController;

  /// 创建转场动画
  @override
  AnimationController createAnimationController() {
    assert(_animationController == null);
    // 路由的转场动画，通过调用 createAnimationController() 创建转场控制器
    _animationController =
        _RFFloatingFrameWidget.createAnimationController(navigator.overlay);
    return _animationController;
  }

  /// 创建转场页面
  @override
  Widget buildPage(BuildContext context, Animation<double> animation,
      Animation<double> secondaryAnimation) {
    // 创建半浮层页面
    Widget bottomSheet = MediaQuery.removePadding(
      context: context,
      removeTop: false, // 不移除顶部 StatusBar 的 padding 信息，后面需要使用
      child: _RFFloating<T>(
        route: this,
        spec: spec,
        enableDrag: enableDrag,
      ),
    );

    if (theme != null) bottomSheet = Theme(data: theme, child: bottomSheet);
    return bottomSheet;
  }
}

// TODO(guidezpl): Look into making this public. A copy of this class is in
/// 半浮层动画速度曲线
/// 用于拖拽结束时，收起/展开的速度控制
class _RFFloatingSuspendedCurve extends ParametricCurve<double> {
  /// Creates a suspended curve.
  const _RFFloatingSuspendedCurve(
    this.startingPoint, {
    this.curve = Curves.easeOutCubic,
  })  : assert(startingPoint != null),
        assert(curve != null);

  /// The progress value at which [curve] should begin.
  ///
  /// This defaults to [Curves.easeOutCubic].
  final double startingPoint;

  /// The curve to use when [startingPoint] is reached.
  final Curve curve;

  @override
  double transform(double t) {
    assert(t >= 0.0 && t <= 1.0);
    assert(startingPoint >= 0.0 && startingPoint <= 1.0);

    if (t < startingPoint) {
      return t;
    }

    if (t == 1.0) {
      return t;
    }

    final double curveProgress = (t - startingPoint) / (1 - startingPoint);
    final double transformed = curve.transform(curveProgress);
    return lerpDouble(startingPoint, 1, transformed);
  }

  @override
  String toString() {
    return '${describeIdentity(this)}($startingPoint, $curve)';
  }
}

/// 半浮层容器
class _RFFloatingContainer extends StatelessWidget {
  final Widget child;

  final Radius topRadius;
  final Color backgroundColor;

  _RFFloatingContainer({this.topRadius, this.backgroundColor, this.child});

  @override
  Widget build(BuildContext context) {
    return Material(
      color: Colors.transparent,
      child: ClipRRect(
        borderRadius: BorderRadius.vertical(top: topRadius),
        child: Container(
          color: backgroundColor,
          child: child,
        ),
      ),
    );
  }
}

// 半浮层配置项
class RFFloatingConfiguration extends InheritedWidget {
  /// The [behavior] and [child] arguments must not be null.
  const RFFloatingConfiguration({
    Key key,
    @required this.scrollPhysics,
    @required Widget child,
  }) : super(key: key, child: child);

  final ScrollPhysics scrollPhysics;

  static ScrollPhysics of(BuildContext context) {
    final RFFloatingConfiguration configuration =
        context.dependOnInheritedWidgetOfExactType<RFFloatingConfiguration>();

    return configuration?.scrollPhysics ?? RFFloatingScrollPhysics();
  }

  @override
  bool updateShouldNotify(RFFloatingConfiguration oldWidget) {
    assert(scrollPhysics != null);
    return scrollPhysics.runtimeType != oldWidget.scrollPhysics.runtimeType ||
        (scrollPhysics != oldWidget.scrollPhysics);
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties.add(
        DiagnosticsProperty<ScrollPhysics>('scrollPhysics', scrollPhysics));
  }
}

/// 作用：展示半浮层
///
/// 参数：
///
/// @params context：上下文
/// @params builder：半浮层内容构器
/// @params spec：半浮层规格
/// @params isDismissible：是否支持点击背景区域关闭
/// @params enableDrag：是否支持拖拽
Future<T> showRFFloating<T>({
  @required BuildContext context,
  @required WidgetBuilder builder,
  RFFloatingSpec spec = RFFloatingSpec.auto,
  double elevation,
  bool useRootNavigator = false,
  bool isDismissible = true,
  bool enableDrag = true,
  RouteSettings routeSettings,
}) {
  assert(context != null);
  assert(builder != null);
  assert(useRootNavigator != null);
  assert(isDismissible != null);
  assert(enableDrag != null);
  assert(debugCheckHasMediaQuery(context));
  assert(debugCheckHasMaterialLocalizations(context));

  // 使用路由创建半浮层页面，并创建自定义的跳转 Route
  return Navigator.of(context, rootNavigator: useRootNavigator).push(
    _RFFloatingRoute<T>(
      builder: (builderContext) {
        // 创建半浮层容器
        return RFFloatingConfiguration(
          scrollPhysics: RFFloatingScrollPhysics(
            parent: ScrollConfiguration.of(context)
                .getScrollPhysics(builderContext),
          ),
          child: _RFFloatingContainer(
            topRadius: Radius.circular(24),
            backgroundColor: Colors.white,
            child: ScrollConfiguration(
              // 屏蔽 Android 的半圆超出效果
              behavior: RFFloatingScrollBehavior(),
              child: builder(builderContext),
            ), // 屏蔽 Android 的半圆超出效果
          ),
        );
      },
      theme: Theme.of(context),
      barrierLabel: MaterialLocalizations.of(context).modalBarrierDismissLabel,
      isDismissible: isDismissible,
      modalBarrierColor: FlutterColors.toColor("#7F000000"),
      enableDrag: enableDrag,
      settings: routeSettings,
      spec: spec,
    ),
  );
}

/// 半浮层规格
///
/// auto：半浮层高度随内容高度决定，默认会提供一个最小高度
/// fixed：半浮层高度撑到最大
enum RFFloatingSpec { auto, fixed }
