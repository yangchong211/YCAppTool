

import 'package:flutter/material.dart';
import 'package:flutter_widget/progress/indicator/rf_circle_widget.dart';
import 'package:flutter_widget/progress/indicator/rf_indicator_emun.dart';
import 'package:flutter_widget/progress/indicator/rf_indicator_utils.dart';
import 'package:flutter_widget/progress/indicator/rf_progress_widget.dart';

/// Flex布局 + Expanded 均分自适应布局
class RFFlowExpandWidget extends StatelessWidget{

  /// 圆圈直径，内圈
  final double circleDiameter;
  /// 圆圈直径，外圈
  final double circleOutsideDiameter;
  /// 外圈是否隐藏，默认是不隐藏的
  final bool circleOutsideGone;
  /// 上面的文案
  final String aboveText;
  /// 下面的文案
  final String bottomText;
  /// 上面的文案颜色
  final Color aboveColor;
  /// 下面的文案颜色
  final Color bottomColor;
  /// 圆圈颜色，内圈
  final Color circleColor;
  /// 圆圈颜色，外圈
  final Color circleOutsideColor;
  /// 上下文本对齐方式
  final TextAlignmentType textAlignmentType;
  /// 根据UI目前就这三种类型颜色
  /// 点亮时候的颜色【圆圈和进度条点亮颜色一样】
  final Color lightUpColor;
  /// 未点亮正常灰色【圆圈和进度条未点亮颜色一样】
  final Color normalColor;
  /// 黑色
  final Color blackColor;
  /// 进度条圆角
  final double borderRadius;
  /// 注意：进度条百分比 = 左边 + 右边，两边进度条比例
  /// 左边进度条进度比例，默认是0，注意是0到50
  final double leftPercent;
  /// 右边进度条进度比例，默认是0，注意是0到50
  final double rightPercent;
  /// 进度条宽度
  final double progressWidth;
  /// 进度条高度
  final double progressHeight;
  /// 索引
  final int index;
  /// 总长度
  final int listLength;
  /// item类型
  final ItemWidgetType itemWidgetType;
  /// 是否是异常情况，默认是false
  final bool isError;
  /// 进度条进度梯度背景颜色
  final LinearGradient linearGradient;
  /// 文字的大小
  final double textSize;
  /// 是否均分适配屏幕宽度
  final bool matchWidthParent;
  final bool iconColorLight;

  RFFlowExpandWidget({Key key,
    this.index = 0,
    this.listLength = 0,
    //默认圆圈直径
    this.circleDiameter,
    //默认外圆圈直径
    this.circleOutsideDiameter,
    //外圈是否隐藏，默认是不隐藏的
    this.circleOutsideGone = true,
    //上面的文案
    this.aboveText,
    //下面的文案
    this.bottomText,
    //上面的文案颜色
    this.aboveColor,
    //下面的文案颜色
    this.bottomColor,
    //圆圈颜色，内圈
    this.circleColor,
    //圆圈颜色，外圈
    this.circleOutsideColor,
    //点亮时候的颜色【圆圈和进度条点亮颜色一样】
    this.lightUpColor = RFIndicatorUtils.color_FF7A45,
    //未点亮正常灰色【圆圈和进度条未点亮颜色一样】
    this.normalColor = RFIndicatorUtils.color_E5,
    //黑色
    this.blackColor = RFIndicatorUtils.color_00,
    //进度条圆角
    this.borderRadius,
    //百分比
    this.leftPercent = 0,
    this.rightPercent = 0,
    //进度条宽度，如果没有传，那么就取默认值
    this.progressWidth,
    //进度条高度，如果没有传，那么就取默认值
    this.progressHeight,
    //上下文本对齐方式
    this.textAlignmentType,
    //默认item是正常样式：上边文字【显示】 + 下面文字【显示】 + 进度条【显示】 + 圆圈【显示】
    this.itemWidgetType = ItemWidgetType.normaWidgetType,
    //是否是异常情况，默认是false
    this.isError = false,
    //进度条进度渐变效果
    this.linearGradient,
    //文字的大小
    this.textSize,
    //是均分适配屏幕宽度
    this.matchWidthParent = true,
    this.iconColorLight,
  }) : super(key: key);

  bool _isFirst(int index) => index == 0;

  bool _isLast(int index) => listLength - 1 == index;


  @override
  Widget build(BuildContext context) {
    print("item percent $index , percent : "
        "left = $leftPercent , right = $rightPercent");
    if (_isFirst(index)){
      //第一个样式：上边文字【显示】 + 下面文字【显示】 + 进度条【显示】 + 圆圈【不显示】
      return _getFirstItem();
    } else if(_isLast(index)){
      //最后一个样式：上边文字【显示】 + 下面文字【显示】 + 进度条【不显示】 + 圆圈【显示】
      return _getLastItem();
    }
    //第二个到倒数第二个样式：上边文字【显示】 + 下面文字【显示】 + 进度条【显示】 + 圆圈【显示】
    return _getItem();
  }


  Widget _getFirstItem(){
    return Expanded(
        flex: 1,
        child: Container(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              //上面的文案
              _getTopText(TextAlignmentType.left),
              Expanded(
                flex: 1,
                child: Row(
                  children: [
                    //这个作用是为了让上下文本跟普通item对齐
                    SizedBox(
                      width: 1,
                      height: circleOutsideDiameter,
                    ),
                    _getProgressWidget(BorderRadiusType.leftRadius,rightPercent),
                  ],
                ),
              ),

              //下面的文案
              _getBottomText(TextAlignmentType.left),
            ],
          ),
        )
    );
  }


  Widget _getItem(){
    return Expanded(
      flex: 2,
      child: Container(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            //上面的文案
            _getTopText(TextAlignmentType.center),
            Expanded(
              flex: 2,
              child: Row(
                children: [
                  //左边的线。第一个item不显示左边的线
                  _getProgressWidget(BorderRadiusType.rightRadius,leftPercent),

                  //中间的圈
                  Container(
                    padding: EdgeInsets.only(
                      left: RFIndicatorUtils.pt2dp(7),
                      right: RFIndicatorUtils.pt2dp(7),
                    ),
                    child: _getCircleWidget(),
                  ),

                  //右边的线，最后一个item不显示右边的线
                  _getProgressWidget(BorderRadiusType.leftRadius,rightPercent),
                ],
              ),
            ),

            //下面的文案
            _getBottomText(TextAlignmentType.center),
          ],
        ),
      )
    );
  }


  Widget _getLastItem(){
    return Expanded(
        flex: 1,
        child: Container(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.end,
            children: [
              //上面的文案
              _getTopText(TextAlignmentType.right),
              Expanded(
                flex: 1,
                child: Row(
                  children: [
                    //左边的线。第一个item不显示左边的线
                    _getProgressWidget(BorderRadiusType.rightRadius,leftPercent),

                    //中间的圈
                    Container(
                      //这个作用是为了让上下文本跟普通item对齐
                      height: circleOutsideDiameter,
                      padding: EdgeInsets.only(
                        left: RFIndicatorUtils.pt2dp(7),
                        right: RFIndicatorUtils.pt2dp(7),
                      ),
                      child: _getCircleWidget(),
                    ),
                  ],
                ),
              ),

              //下面的文案
              _getBottomText(TextAlignmentType.right),
            ],
          ),
        )
    );
  }


  /// 内圆圈和外圆圈
  Widget _getCircleWidget() {
    return RFCircleWidget(
      circleDiameter: circleDiameter,
      circleOutsideDiameter: circleOutsideDiameter,
      circleOutsideGone: isError ? true : circleOutsideGone,
      circleColor: isError ? normalColor : circleColor,
      circleOutsideColor: circleOutsideColor,
    );
  }

  /// 获取自定义进度条
  Widget _getProgressWidget(BorderRadiusType radiusType,double percent){
    return RFProgressWidget(
      width: progressWidth,
      height: progressHeight,
      bgColor: normalColor ,
      frColor: isError ? blackColor : lightUpColor,
      borderRadius: borderRadius,
      percentage: percent,
      radiusType: radiusType,
      matchWidthParent: matchWidthParent,
    );
  }

  /// 获取圆圈顶部的文本widget
  Widget _getTopText(TextAlignmentType type){
    AlignmentGeometry alignment;
    if(type == TextAlignmentType.left){
      alignment = Alignment.bottomLeft;
    } else if(type == TextAlignmentType.right){
      alignment = Alignment.bottomRight;
    } else {
      alignment = Alignment.bottomCenter;
    }
    return Container(
      // color: Colors.cyan,
      height: RFIndicatorUtils.pt2dp(38),
      //底部对齐
      alignment: alignment,
      padding: EdgeInsets.only(
        right: _isLast(index) ? RFIndicatorUtils.pt2dp(7): 0,
        bottom: RFIndicatorUtils.pt2dp(6),
      ),
      child: Text(
        aboveText ?? "",
        style: TextStyle(
          fontSize: textSize,
          color: aboveColor,
          fontWeight: _getFontWeight(),
        ),
        maxLines: 1,
      ),
    );
  }

  /// 获取圆圈底部的文本widget
  Widget _getBottomText(TextAlignmentType type){
    AlignmentGeometry alignment;
    if(type == TextAlignmentType.left){
      alignment = Alignment.topLeft;
    } else if(type == TextAlignmentType.right){
      alignment = Alignment.topRight;
    } else {
      alignment = Alignment.topCenter;
    }
    return Container(
      // color: Colors.red,
      height: RFIndicatorUtils.pt2dp(38),
      //底部对齐
      alignment: alignment,
      padding: EdgeInsets.only(
        right: _isLast(index) ? RFIndicatorUtils.pt2dp(7): 0,
        top: RFIndicatorUtils.pt2dp(6),
      ),
      child: SizedBox(
        //设置进度条一般的宽度
        child: Text(
          bottomText ?? "",
          style: TextStyle(
            fontSize: textSize,
            color: bottomColor,
            fontWeight: _getFontWeight(),
          ),
          maxLines: 1,
        ),
      ),
    );
  }

  FontWeight _getFontWeight(){
    FontWeight fontWeight;
    if(_isFirst(index) || isError){
      //如果是第一个，或则异常情况，则默认正常字体
      fontWeight = FontWeight.w400;
    } else {
      //判断圆圈是否是亮的
      if(iconColorLight){
        fontWeight = FontWeight.w700;
      } else {
        fontWeight = FontWeight.w300;
      }
    }
    return fontWeight;
  }

}
