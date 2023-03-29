

import 'package:flutter/material.dart';
import 'package:flutter_widget/progress/indicator/rf_indicator_utils.dart';
import 'package:flutter_widget/progress/percent/rf_percent_progress.dart';

class RFPercentTextWidget extends StatelessWidget{

  /// 文案
  final String text;
  /// 进度条百分比【服务端返回是0到100】
  final double percent;
  /// 是否是异常情况
  final bool isError;

  RFPercentTextWidget({
    Key key,
    this.text,
    this.percent,
    this.isError = false,
  }) :assert(text != null),
      assert(percent != null),
      super(key: key);

  @override
  Widget build(BuildContext context) {
    return RFPercentProgress(
      lineHeight: 4.0,
      percent: percent ?? 0.0,
      barRadius: Radius.circular(4),
      backgroundColor: RFIndicatorUtils.color_E5,
      progressColor: isError ? RFIndicatorUtils.color_00 : RFIndicatorUtils.color_FF7A45,
      rightWidget: getTextWidget(),
    );
  }

  Widget getTextWidget(){
    return Padding(
      padding: EdgeInsets.fromLTRB(10, 0,0,0),
      child: SizedBox(
        width: RFIndicatorUtils.pt2dp(73),
        child: Text(
          text ?? "",
          style: TextStyle(
            fontSize: RFIndicatorUtils.pt2sp(12),
            color: Color(0xFF666666),
          ),
        ),
      ),
    );
  }

}







