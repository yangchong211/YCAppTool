
import 'package:flutter/material.dart';
import 'package:flutter_widget/progress/indicator/rf_indicator_bean.dart';
import 'package:yc_flutter_utils/screen/screen_adaptation_utils.dart';

class RFIndicatorUtils{

  static const Color color_30FF7A45 = Color(0xFFFFD7C7);
  static const Color color_FF7A45 = Color(0xFFFF7A45);
  static const Color color_E5 = Color(0xFFE5E5E5);
  static const Color color_00 = Color(0xFF000000);
  static const Color color_99 = Color(0xFF999999);

  static double pt2dp(double num){
    return ScreenAdaptationUtils().pt2dp(num);
  }

  static double pt2sp(double num){
    return ScreenAdaptationUtils().pt2sp(num);
  }

  /// 获取最后一个圆圈是橙色的索引。
  /// 拿到了这个索引，才可以做listView滚动都指定索引item操作
  static int getLastLight(List<RFIndicatorBean> list){
    if(list==null || list.isEmpty){
      return 0;
    }
    RFIndicatorBean bean = list.lastWhere((RFIndicatorBean element) =>
    element.iconHighLight == true , orElse: () => null);
    return bean?.dataIndex ?? 0;
  }


  /// 处理文字
  static String getText(String str){
    if(str==null || str.length==0){
      return "";
    }
    //数字金额，最长是6位，极限是8位
    if(str.length>8){
      return str.substring(0,8);
    }
    return str;
  }


}