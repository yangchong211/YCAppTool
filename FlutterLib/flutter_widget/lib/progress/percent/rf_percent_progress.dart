import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter_widget/progress/indicator/rf_indicator_utils.dart';
import 'package:flutter_widget/progress/percent/rf_progress_painter.dart';



/// 百分比带有渐变效果的进度条
class RFPercentProgress extends StatefulWidget {

  /// 进度条百分比【服务端返回是0到100】
  final double percent;
  /// 进度条宽度
  final double width;
  /// 进度条高度
  final double lineHeight;
  /// 背景颜色
  final Color backgroundColor;
  /// 进度条颜色
  final Color progressColor;
  /// 是否执行动画
  final bool animation;
  /// 动画时间
  final int animationDuration;
  /// 圆角
  final Radius barRadius;
  /// 线性梯度背景颜色
  final LinearGradient linearGradientBackgroundColor;
  /// 进度条进度梯度背景颜色
  final LinearGradient linearGradient;
  /// 从右到左的线性动画，设置为真
  final bool isRTL;
  /// 动画结束了时候监听
  final VoidCallback onAnimationEnd;
  /// 右边的布局
  final Widget rightWidget;

  RFPercentProgress({
    Key key,
    this.percent = 0.0,
    this.lineHeight,
    this.width,
    this.backgroundColor = RFIndicatorUtils.color_E5,
    this.progressColor = RFIndicatorUtils.color_FF7A45,
    this.linearGradientBackgroundColor,
    this.linearGradient,
    this.animation = false,
    this.animationDuration = 500,
    this.isRTL = false,
    this.barRadius,
    this.onAnimationEnd,
    this.rightWidget,
  }) : super(key: key) {
    if (percent < 0.0 || percent > 100.0) {
      throw new Exception(
          "Percent must be a double between 0.0 and 100, it is not $percent");
    }
  }

  @override
  _RFPercentProgressState createState() => _RFPercentProgressState();
}

class _RFPercentProgressState extends State<RFPercentProgress>
    with SingleTickerProviderStateMixin, AutomaticKeepAliveClientMixin {
  AnimationController _animationController;
  Animation _animation;
  double _percent = 0.0;
  final _containerKey = GlobalKey();

  @override
  void dispose() {
    if (_animationController!=null){
      _animationController.dispose();
    }
    super.dispose();
  }

  @override
  void initState() {
    if (widget.animation) {
      var duration = Duration(milliseconds: widget.animationDuration);
      _animationController = AnimationController(vsync: this, duration: duration);
      Tween tween = Tween(begin: 0.0, end: (widget.percent/100.0));
      var curvedAnimation = CurvedAnimation(parent: _animationController
          , curve: Curves.linear);
      _animation = tween.animate(curvedAnimation,)..addListener(() {
        setState(() {
          _percent = _animation?.value;
        });
      });
      _animationController?.addStatusListener((status) {
        if (widget.onAnimationEnd != null &&
            status == AnimationStatus.completed) {
          widget.onAnimationEnd();
        }
      });
      _animationController?.forward();
    } else {
      _updateProgress();
    }
    super.initState();
  }

  void _checkIfNeedCancelAnimation(RFPercentProgress oldWidget) {
    if (oldWidget.animation &&
        !widget.animation &&
        _animationController != null) {
      _animationController?.stop();
    }
  }

  @override
  void didUpdateWidget(RFPercentProgress oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (oldWidget.percent != widget.percent) {
      if (_animationController != null) {
        _animationController?.duration =
            Duration(milliseconds: widget.animationDuration);
        var tween = Tween(begin: 0.0, end: widget.percent);
        var curvedAnimation = CurvedAnimation(parent: _animationController,
            curve: Curves.linear);
        _animation = tween.animate(curvedAnimation,);
        _animationController.forward(from: 0.0);
      } else {
        _updateProgress();
      }
    }
    _checkIfNeedCancelAnimation(oldWidget);
  }

  _updateProgress() {
    setState(() {
      _percent = widget.percent;
    });
  }

  @override
  Widget build(BuildContext context) {
    super.build(context);
    var items = List<Widget>.empty(growable: true);
    final hasSetWidth = widget.width != null;
    Widget containerWidget = Container(
      width: hasSetWidth ? widget.width : double.infinity,
      height: widget.lineHeight,
      child: CustomPaint(
        key: _containerKey,
        painter: RFLinearPainter(
          isRTL: widget.isRTL,
          progress: widget.animation ? _percent : (widget.percent/100.0),
          progressColor: widget.progressColor,
          linearGradient: widget.linearGradient,
          backgroundColor: widget.backgroundColor,
          barRadius: widget.barRadius ?? Radius.zero,
          linearGradientBackgroundColor: widget.linearGradientBackgroundColor,
          clipLinearGradient: false,
        ),
      ),
    );

    if (hasSetWidth) {
      items.add(containerWidget);
    } else {
      items.add(Expanded(
        child: containerWidget,
      ));
    }
    if (widget.rightWidget != null) {
      items.add(widget.rightWidget);
    }
    return Container(
      child: Row(
        mainAxisAlignment: MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: items,
      ),
    );
  }

  @override
  bool get wantKeepAlive => true;

}
