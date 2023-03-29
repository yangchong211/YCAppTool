import 'dart:ui';

import 'package:flutter/foundation.dart';
import 'package:yc_flutter_utils/screen/screen_adapation_strategy.dart';

class ScreenAdaptation {
  // 设计稿的尺寸
  final Size designSize;

  // 设计稿的缩放比
  final double scaleRatio;

  // 屏幕适配策略
  final ScreenAdaptationStrategy strategy;

  /// 不进行屏幕适配
  const ScreenAdaptation.none()
      : strategy = ScreenAdaptationStrategy.none,
        designSize = null,
        scaleRatio = null;

  /// 逻辑尺寸 == 尺寸 * scaleRatio
  ///
  /// sample：待解释
  const ScreenAdaptation.normal({
    @required this.scaleRatio,
  })  : designSize = null,
        strategy = ScreenAdaptationStrategy.normal;

  /// 等比缩放适配
  ///
  /// sample：待解释
  const ScreenAdaptation.scale({
    @required this.designSize,
  })  : strategy = ScreenAdaptationStrategy.scale,
        scaleRatio = null;
}
