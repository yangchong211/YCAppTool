


import 'package:flutter/material.dart';
// circle active color
const Color _kCircleActiveColor = Colors.redAccent;
// circle color
const Color _kCircleColor = Colors.grey;
// circle content active color
const Color _kContentActiveColor = Colors.redAccent;
// circle content color
const Color _kContentColor = Colors.grey;
// line color
const Color _kLineColor = Colors.redAccent;
// circle diameter
const double _kCircleDiameter = 26.0;


class ACEStepThemeData {

  final Color circleColor,
      circleActiveColor,
      contentColor,
      contentActiveColor,
      lineColor;
  final double circleDiameter;

  ACEStepThemeData(
      {this.circleColor = _kCircleColor,
        this.lineColor = _kLineColor,
        this.circleActiveColor = _kCircleActiveColor,
        this.contentColor = _kContentColor,
        this.contentActiveColor = _kContentActiveColor,
        this.circleDiameter = _kCircleDiameter});
}