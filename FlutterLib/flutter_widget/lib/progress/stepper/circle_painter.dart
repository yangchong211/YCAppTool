

import 'dart:math';

import 'package:flutter/material.dart';

class CirclePainter extends CustomPainter {
  final Color color;
  final double size;

  CirclePainter({this.color, this.size});

  @override
  bool hitTest(Offset point) => true;

  @override
  bool shouldRepaint(CirclePainter oldPainter) => oldPainter.color != color;

  @override
  void paint(Canvas canvas, Size size) {
    final double radius = this.size * 0.5;

    canvas.drawArc(
        Rect.fromCircle(center: Offset(radius, radius), radius: radius),
        0.0,
        2 * pi,
        false,
        Paint()
          ..color = color
          ..strokeCap = StrokeCap.round
          ..strokeWidth = 1.0
          ..style = PaintingStyle.stroke);
  }
}
