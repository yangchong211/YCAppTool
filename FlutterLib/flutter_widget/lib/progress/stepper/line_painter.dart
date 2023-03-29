


import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter_widget/progress/stepper/ace_enum.dart';

class LinePainter extends CustomPainter {

  final Color color;
  final double radius;
  final ACEStepperType type;

  LinePainter({this.color, this.radius, this.type});

  @override
  bool hitTest(Offset point) => true;

  @override
  bool shouldRepaint(LinePainter oldPainter) => oldPainter.color != color;

  @override
  void paint(Canvas canvas, Size size) {
    double _circleLength = (type == ACEStepperType.horizontal)
        ? size.width.toDouble()
        : size.height.toDouble();
    double _circleSize = _circleLength / radius / 4 > 2
        ? _circleLength / radius / 4 - 1
        : _circleLength / radius / 4;
    Path _path = Path();
    for (int i = 0; i < _circleSize; i++) {
      _path.addArc(
          Rect.fromCircle(
              center: Offset(
                  type == ACEStepperType.horizontal
                      ? radius + 4 * radius * i
                      : radius,
                  type == ACEStepperType.horizontal
                      ? radius
                      : radius + 4 * radius * i),
              radius: radius),
          0.0,
          2 * pi);
    }
    canvas.drawPath(
        _path,
        Paint()
          ..color = color
          ..strokeCap = StrokeCap.round
          ..style = PaintingStyle.fill);
  }
}
