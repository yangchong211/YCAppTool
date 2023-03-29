import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter_widget/progress/indicator/rf_indicator_utils.dart';
import 'package:flutter_widget/progress/shadow/rf_progress_painter.dart';




/// 百分比带有渐变效果的进度条
class RFShadowWidget extends StatefulWidget {

  /// 进度条宽度
  final double width;
  /// 进度条高度
  final double lineHeight;
  /// 背景颜色
  final Color backgroundColor;
  /// 进度条颜色
  final Color progressColor;
  /// 圆角
  final Radius barRadius;
  /// 线性梯度背景颜色
  final LinearGradient linearGradientBackgroundColor;
  /// 进度条进度梯度背景颜色
  final LinearGradient linearGradient;
  /// 从右到左的线性动画，设置为真
  final bool isRTL;
  /// 右边的布局
  final Widget rightWidget;

  RFShadowWidget({
    Key key,
    this.lineHeight,
    this.width,
    this.backgroundColor = RFIndicatorUtils.color_E5,
    this.progressColor = RFIndicatorUtils.color_FF7A45,
    this.linearGradientBackgroundColor,
    this.linearGradient,
    this.isRTL = false,
    this.barRadius,
    this.rightWidget,
  }) : super(key: key);

  @override
  _RFShadowWidgetState createState() => _RFShadowWidgetState();
}

class _RFShadowWidgetState extends State<RFShadowWidget>{

  final _containerKey = GlobalKey();

  @override
  Widget build(BuildContext context) {
    var items = List<Widget>.empty(growable: true);
    final hasSetWidth = widget.width != null;
    Widget containerWidget = Container(
      width: hasSetWidth ? widget.width : double.infinity,
      height: widget.lineHeight,
      child: CustomPaint(
        key: _containerKey,
        painter: RFLinearPainter(
          isRTL: widget.isRTL,
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

}
