

import 'package:flutter/material.dart';
import 'package:flutter_widget/progress/stepper/ace_enum.dart';

class ACEStep {

  final Widget bottomWidget;
  final Widget topWidget;
  final LineType lineType;
  final IconType iconType;
  final bool isActive;
  final double percentage;

  const ACEStep({
    @required this.bottomWidget,
    this.topWidget,
    this.lineType = LineType.normal,
    this.iconType = IconType.text,
    this.isActive = false,
    this.percentage = 0.0,
  });
}
