import 'dart:ui';

class YCColors {

  YCColors._();

  static const Color color_F0 = Color(0xFFF0F0F0);
  static const Color color_33 = Color(0xFF333333);
  static const Color color_66 = Color(0xFF666666);
  static const Color color_99 = Color(0xFF999999);
  static const Color color_CC = Color(0xFFCCCCCC);
  static const Color color_EE = Color(0xEEEEEEEE);
  static const Color color_E5 = Color(0xFFE5E5E5);
  static const Color color_FF = Color(0xFFFFFFFF);
  static const Color color_FFEEEEEE = Color(0xFFEEEEEE);
  static const Color color_E625262D = Color(0xE625262D);
  static const Color color_FF6325 = Color(0xFFFF6325);
  static const Color color_FF6A60 = Color(0xFFFF6A60);
  static const Color color_2EBFD9 = Color(0xFF2EBFD9);
  static const Color color_F6F6F6 = Color(0xFFF6F6F6);
  static const Color color_63676E = Color(0xFF63676E);
  static const Color color_F93F3F = Color(0xffF93F3F);
  static const Color color_F0F0F0 = Color(0xFFF0F0F0);
  static const Color color_FFA357D6 = Color(0xFFA357D6);
  static const Color color_1AA357D6 = Color(0x1AA357D6);
  static const Color color_FF0000 = Color(0xFFFF0000);
  static const Color color_FF4444 = Color(0xFFFF4444);
  static const Color color_00 = Color(0xFF000000);

  ///将颜色转化为color
  static Color toColor(String color) {
    if(color==null || color.length==0){
      return null;
    }
    if(!color.contains("#")){
      return null;
    }
    var hexColor = color.replaceAll("#", "");
    //如果是6位，前加0xff
    if (hexColor.length == 6) {
      hexColor = "0xff" + hexColor;
      return Color(int.parse(hexColor));
    }
    //如果是8位，前加0x
    if (hexColor.length == 8) {
      return Color(int.parse("0x$hexColor"));
    }
    return null;
  }
}
