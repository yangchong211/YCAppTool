



import 'dart:math';
import 'dart:ui';

import 'package:flutter/material.dart';

/// 计算工具类
class CalculateUtils {

  /// 获取有限的单行的文本大小
  static Size getTextSize(String text, TextStyle style) {
    TextPainter painter = TextPainter(
      text: TextSpan(text: text, style: style),
      textDirection: TextDirection.ltr,
      maxLines: 1,
      ellipsis: '...',
    );
    painter.layout();
    return painter.size;
  }

  /// 计算文本高度
  /// 因为后期要下沉，因此暂时没有使用项目上层的工具类。避免后期迁移代码缺失
  static double calculateTextWidth(String value,
      fontSize, FontWeight fontWeight,
      double maxWidth, int maxLines) {
    //创建painter
    TextPainter painter = TextPainter(
      maxLines: maxLines,
      textDirection: TextDirection.ltr,
      text: TextSpan(
        text: value,
        style: TextStyle(
          fontWeight: fontWeight,
          fontSize: fontSize,
        ),
      ),
    );
    painter.layout(maxWidth: maxWidth);
    return painter.width;
  }


  /// 计算文本宽度
  static double calculateTextLocaleWidth(BuildContext context,String value,
      fontSize, FontWeight fontWeight,
      double maxWidth, int maxLines) {
    //创建painter
    TextPainter painter = TextPainter(
      locale: Localizations.localeOf(context, nullOk: true),
      maxLines: maxLines,
      textDirection: TextDirection.ltr,
      text: TextSpan(
        text: value,
        style: TextStyle(
          fontWeight: fontWeight,
          fontSize: fontSize,
        ),
      ),
    );
    painter.layout(maxWidth: maxWidth);
    return painter.width;
  }


  /// 计算文本高度
  static double calculateTextHeight1(BuildContext context,String value,
      fontSize, FontWeight fontWeight,
      double maxWidth, int maxLines) {
    //创建painter
    TextPainter painter = TextPainter(
      locale: Localizations.localeOf(context, nullOk: true),
      maxLines: maxLines,
      textDirection: TextDirection.ltr,
      text: TextSpan(
        text: value,
        style: TextStyle(
          fontWeight: fontWeight,
          fontSize: fontSize,
        ),
      ),
    );
    painter.layout(maxWidth: maxWidth);
    return painter.height;
  }

  /// 计算文本高度
  static double calculateTextHeight2(BuildContext context, String text,
      int maxLines, double maxWidth, TextStyle style) {
    maxWidth = max(0.0,maxWidth);
    TextPainter painter = TextPainter(
        locale: Localizations.localeOf(context, nullOk: true),
        maxLines: maxLines,
        textDirection: TextDirection.ltr,
        textAlign: TextAlign.start,
        textScaleFactor: window.textScaleFactor,
        text: TextSpan(text: text, style: style));
    painter.layout(maxWidth: maxWidth);
    return painter.height;
  }


}