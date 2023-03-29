import 'package:flutter/material.dart';
import 'package:flutter_widget/progress/indicator/rf_indicator_utils.dart';
import 'package:flutter_widget/progress/percent/rf_progress_painter.dart';
import 'package:flutter_widget/progress/stepper/ace_enum.dart';
import 'package:flutter_widget/progress/stepper/line_painter.dart';

// line height
const double _kLineHeight = 25.0;
// horizontal header height
const double _kHeaderHeight = 120.0;
// line width
const double _kLineWidth = 1.0;

class StepperLine extends StatelessWidget {

  final Color color;
  final LineType lineType;
  final ACEStepperType type;

  StepperLine(
      {@required this.color,
      this.type = ACEStepperType.horizontal,
      this.lineType = LineType.normal
      });

  @override
  Widget build(BuildContext context) {
    double _width =
        (type == ACEStepperType.horizontal) ? _kLineHeight : _kLineWidth;
    double _height =
        (type == ACEStepperType.horizontal) ? _kLineWidth : _kLineHeight;
    double _diameter = (type == ACEStepperType.horizontal) ? _height : _width;
    if (lineType == LineType.normal) {
      return Container(width: _width, height: _height, color: color);
    } else {
      return Container(
        width: _width,
        height: _height,
        // width: 50,
        // height: 18,
        // color: Colors.red,
        child: CustomPaint(
          painter: LinePainter(
            color: color,
            radius: _diameter * 0.5,
            type: type,
          ),
          // painter: RFLinearPainter(
          //   progress: 100,
          //   progressColor: RFIndicatorUtils.color_FF7A45,
          //   backgroundColor: RFIndicatorUtils.color_E5,
          //   barRadius: Radius.circular(_diameter * 0.5),
          //   clipLinearGradient: false,
          // ),
        ),
      );
    }
  }
}
