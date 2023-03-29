import 'dart:math';
import 'dart:ui' as ui;

import 'package:flutter/widgets.dart';
import 'package:yc_flutter_utils/screen/screen_adapation_strategy.dart';
import 'package:yc_flutter_utils/screen/screen_adaption.dart';


/// 屏幕适配工具类
class ScreenAdaptationUtils {

  static ScreenAdaptationUtils _instance;
  static ScreenAdaptation _adaptation;

  // 根据屏幕最小尺寸，得到的缩放比
  static double _swScaleRadio;

  // 字体缩放因子，启用后会根据系统设置字体大小，改变页面字体大小 (暂不启用)
  static double _textScaleFactor;

  ScreenAdaptationUtils._();

  factory ScreenAdaptationUtils() {
    return _instance;
  }

  static void init(ScreenAdaptation adaptation) {
    // 初始化屏幕适配参数
    _instance = ScreenAdaptationUtils._();
    _adaptation = adaptation;

    _initScaleFactor(true);
  }

  static void _initScaleFactor(bool force) {
    // 如果类型不为 scale
    if (_adaptation.strategy != ScreenAdaptationStrategy.scale) {
      return;
    }

    // 如果不需要强制刷新，且已经存在缩放的情况，则不处理
    if (!force && _swScaleRadio != null && _textScaleFactor != null) {
      return;
    }

    // 获取窗口对象
    var window = WidgetsBinding.instance?.window ?? ui.window;
    if (window == null || _instance == null) return;

    ui.Size designSize = _adaptation.designSize;

    // 获取缩放因子
    final double pixelRatio = window.devicePixelRatio;
    // 获取物理尺寸
    final ui.Size pixelSize = window.physicalSize;
    // 获取文本缩放因子
    final double textScaleFactor = window.textScaleFactor;

    /// 将物理尺寸单位，转换成 Flutter 的逻辑尺寸单位
    // pt =  px / scaleRatio
    var screenSize =
        Size(pixelSize.width / pixelRatio, pixelSize.height / pixelRatio);

    /// 计算 sw 适配的缩放因子
    // 屏幕最小宽度
    var minScreenWidth = min(screenSize.width, screenSize.height);
    // 设计稿最小宽度
    var minDesignWidth = min(designSize.width, designSize.height);
    // 计算 sw 缩放比
    var swScaleRatio = minScreenWidth / minDesignWidth;

    _swScaleRadio = swScaleRatio;
    _textScaleFactor = textScaleFactor;
  }

  /// 将 Flutter 单位，转换成屏幕适配后的值，命名为单位 dp
  double pt2dp(num size) {
    double result;
    switch (_adaptation.strategy) {
      case ScreenAdaptationStrategy.scale:
        result = _adaptationScale(size, false);
        break;
      case ScreenAdaptationStrategy.normal:
        result = _adaptationNormal(size);
        break;
      default:
        result = _adaptationNone(size);
        break;
    }
    return result;
  }

  /// 将 Flutter 单位，转换成屏幕适配后的值，命名为单位 sp
  double pt2sp(num size) {
    double result;
    switch (_adaptation.strategy) {
      case ScreenAdaptationStrategy.scale:
        result = _adaptationScale(size, true);
        break;
      case ScreenAdaptationStrategy.normal:
        result = _adaptationNormal(size);
        break;
      default:
        result = _adaptationNone(size);
        break;
    }
    return result;
  }

  // 屏幕适配 none 策略下的计算
  double _adaptationNone(num size) {
    return size.toDouble();
  }

  // 屏幕适配 normal 策略下的计算
  double _adaptationNormal(num size) {
    return size * _adaptation.scaleRatio;
  }

  // 屏幕适配 scale 策略下的计算
  double _adaptationScale(num size, bool isSp) {
    // 初始化缩放因子
    _initScaleFactor(false);

    double result = size * _swScaleRadio;

    if (isSp) {
      result *= _textScaleFactor;
    }

    return result;
  }
}
