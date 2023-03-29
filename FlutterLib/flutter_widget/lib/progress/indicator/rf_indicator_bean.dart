


class RFIndicatorBean{

  /// 上面的文案，数字
  String aboveText;
  /// 下面的文案，等级
  String bottomText;
  /// 进度条进度比例，默认是0，注意是0到100，服务端直接下发
  double percentage;
  /// 外圈是否隐藏，默认是不隐藏的
  bool circleOutsideGone;
  /// 索引，用于判断第一个item，最后一个item和中间item的样式
  int dataIndex = 0;
  /// 圆圈是否高亮，用于设置圆圈颜色
  bool iconHighLight ;
  /// 文本是否高亮，用于设置文字文本颜色
  bool textHighLight ;

  RFIndicatorBean(String aboveTextStr , String bottomTextStr ,
      double percent , bool outsideGone ,bool iconLight, bool textLight){
    aboveText = aboveTextStr;
    bottomText = bottomTextStr;
    percentage = percent;
    circleOutsideGone = outsideGone;
    iconHighLight = iconLight;
    textHighLight = textLight;
  }
}