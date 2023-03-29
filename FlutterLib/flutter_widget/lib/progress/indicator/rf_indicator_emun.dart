
/// 分段自定义进度条的颜色类型
enum IndicatorColorType {
  lightUp, //点亮
  normal,  //正常灰色
  black,   //黑色
}

/// 分段自定义进度条方向类型
enum IndicatorType {
  vertical, //竖直方向
  horizontal,  //横向方向
}

/// 文本对齐方式[主要是指等级文本和数字文本对齐方式]
enum TextAlignmentType{
  left,//左对齐
  right,//右对齐
  center,//居中对齐
}

/// 进度条圆角类型
enum BorderRadiusType{
  leftRadius,//左边有圆角
  rightRadius,//右边有圆角
  allRadius,//都有圆角
}

/// 样式
/// 第一个样式：上边文字【显示】 + 下面文字【显示】 + 进度条【显示】 + 圆圈【不显示】
/// 第二个到倒数第二个样式：上边文字【显示】 + 下面文字【显示】 + 进度条【显示】 + 圆圈【显示】
/// 最后一个样式：上边文字【显示】 + 下面文字【显示】 + 进度条【不显示】 + 圆圈【显示】
enum ItemWidgetType{
  firstWidgetType,
  normaWidgetType,
  lastWidgetType,
}



