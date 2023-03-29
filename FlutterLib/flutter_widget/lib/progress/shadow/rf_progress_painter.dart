

import 'package:flutter/material.dart';

/// 实现画笔
class RFLinearPainter extends CustomPainter {

  final Paint _paintBackground = new Paint();
  final Paint _paintLine = new Paint();
  final bool isRTL;
  final Color progressColor;
  final Color backgroundColor;
  final Radius barRadius;
  final LinearGradient linearGradient;
  final LinearGradient linearGradientBackgroundColor;
  final bool clipLinearGradient;

  RFLinearPainter({
    @required this.isRTL,
    @required this.progressColor,
    @required this.backgroundColor,
    @required this.barRadius,
    this.linearGradient,
    @required this.clipLinearGradient,
    this.linearGradientBackgroundColor,
  }) {
    _paintBackground.color = backgroundColor;
    _paintLine.color = progressColor.withOpacity(0.0);
  }

  /// 开始绘制
  @override
  void paint(Canvas canvas, Size size) {
    // 首先绘制背景
    Path backgroundPath = Path();
    backgroundPath.addRRect(RRect.fromRectAndRadius(
        Rect.fromLTWH(0, 0, size.width, size.height), barRadius));
    canvas.drawPath(backgroundPath, _paintBackground);

    // 线性梯度背景颜色
    if (linearGradientBackgroundColor != null) {
      Offset shaderEndPoint =
      clipLinearGradient ? Offset.zero : Offset(size.width, size.height);
      _paintBackground.shader = linearGradientBackgroundColor
          .createShader(Rect.fromPoints(Offset.zero, shaderEndPoint));
    }

    // 然后再绘制进度条线
    final xProgress = size.width ;
    Path linePath = Path();
    if (isRTL) {
      if (linearGradient != null) {
        _paintLine.shader = _createGradientShaderRightToLeft(size, xProgress);
      }
      linePath.addRRect(RRect.fromRectAndRadius(
          Rect.fromLTWH(
              size.width - size.width , 0, xProgress, size.height),
          barRadius));
    } else {
      if (linearGradient != null) {
        _paintLine.shader = _createGradientShaderLeftToRight(size, xProgress);
      }
      var rect = Rect.fromLTWH(0, 0, xProgress, size.height);
      var rRect = RRect.fromRectAndRadius(rect, barRadius);
      linePath.addRRect(rRect);
    }
    canvas.drawPath(linePath, _paintLine);
  }


  Shader _createGradientShaderRightToLeft(Size size, double xProgress) {
    Offset shaderEndPoint = clipLinearGradient
        ? Offset.zero : Offset(xProgress, size.height);
    return linearGradient?.createShader(
      Rect.fromPoints(
        Offset(size.width, size.height),
        shaderEndPoint,
      ),
    );
  }

  Shader _createGradientShaderLeftToRight(Size size, double xProgress) {
    Offset shaderEndPoint = clipLinearGradient
        ? Offset(size.width, size.height) : Offset(xProgress, size.height);
    return linearGradient?.createShader(
      Rect.fromPoints(
        Offset.zero,
        shaderEndPoint,
      ),
    );
  }

  //简单返回true，实践中应该根据画笔属性是否变化来确定返回true还是false
  @override
  bool shouldRepaint(CustomPainter oldDelegate) => true;
}

