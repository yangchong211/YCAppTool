import 'package:flutter/material.dart';
import 'package:flutter_widget/progress/indicator/rf_flow_expand_widget.dart';
import 'package:flutter_widget/progress/indicator/rf_indicator_bean.dart';
import 'package:flutter_widget/progress/indicator/rf_indicator_emun.dart';
import 'package:flutter_widget/progress/indicator/rf_indicator_utils.dart';
import 'package:flutter_widget/progress/indicator/rf_list_item_widget.dart';


/// 支持设置圆圈（内圆圈，外圆圈）的直径，颜色，外圈是否显示
/// 支持设置圆圈上面文案（单位），下面文案（等级），文案支持设置自定义字体
/// 支持设置进度条宽，高，进度条颜色，背景色，以及进度比例
/// 分段进度指示器支持横行滑动，可以改变
class RFProgressIndicator extends StatefulWidget {

  /// 圆圈直径，内圈
  final double circleDiameter;
  /// 圆圈直径，外圈
  final double circleOutsideDiameter;
  /// 数据
  final List<RFIndicatorBean> list;
  /// 进度条宽度
  final double progressWidth;
  /// 进度条高度
  final double progressHeight;
  /// 容器的padding
  final EdgeInsets padding;

  /// 根据UI目前就这三种类型颜色
  /// 点亮时候的颜色【圆圈和进度条点亮颜色一样】
  final Color lightUpColor;
  /// 未点亮正常灰色【圆圈和进度条未点亮颜色一样】
  final Color normalColor;
  /// 黑色
  final Color blackColor;
  /// 文本的正常颜色
  final Color textColor;
  /// 文字的大小
  final double textSize;

  /// 进度条圆角
  final double borderRadius;
  /// 第一个item是否按照正常模式显示
  /// 上边文字【显示】 + 下面文字【显示】 + 进度条【显示】 + 圆圈【显示】
  final bool firstItemNormal;
  /// 是否是异常情况，默认是false
  final bool isError;
  /// 进度条进度梯度背景颜色
  final LinearGradient linearGradient;
  /// 设置容器的高度
  final double containerHeight;
  /// 是否均分适配屏幕宽度
  final bool matchWidthParent;
  /// 绘制完成回调
  final Function postFrame;
  /// 是否自动滚动到最后一个item
  final bool isScrollLastLight;

  RFProgressIndicator({
    Key key,
    //默认圆圈直径
    this.circleDiameter,
    //默认外圆圈直径
    this.circleOutsideDiameter,
    //数据，这个数据必须要传递
    @required this.list,
    //进度条宽度，如果没有传，那么就取默认值
    this.progressWidth,
    //进度条高度，如果没有传，那么就取默认值
    this.progressHeight,
    //点亮时候的颜色【圆圈和进度条点亮颜色一样】
    this.lightUpColor = RFIndicatorUtils.color_FF7A45,
    //未点亮正常灰色【圆圈和进度条未点亮颜色一样】
    this.normalColor = RFIndicatorUtils.color_E5,
    //黑色
    this.blackColor = RFIndicatorUtils.color_00,
    //文本的正常颜色
    this.textColor = RFIndicatorUtils.color_99,
    //文字的大小
    this.textSize,
    //进度条圆角
    this.borderRadius,
    //容器的padding
    this.padding,
    //第一个item是否按照正常模式显示，如果其他地方要是用，则可以设置
    this.firstItemNormal = false,
    //是否是异常情况，默认是false
    this.isError = false,
    //进度条进度渐变效果
    this.linearGradient,
    //设置容器的高度
    this.containerHeight,
    //是均分适配屏幕宽度
    this.matchWidthParent = true,
    //绘制完成回调
    this.postFrame,
    this.isScrollLastLight = false,
  }) : assert(list != null),
        super(key: key);

  @override
  State<StatefulWidget> createState() {
    return CustomProgressState();
  }

}

class CustomProgressState extends State<RFProgressIndicator> {

  ScrollController _controller = new ScrollController();
  /// 下一个item左边进度条进度【listview】
  double nextListLeftPercent;
  /// 下一个item左边进度条进度【flow+expand】
  double nextExpandLeftPercent;
  /// 设置item类型
  ItemWidgetType widgetType;
  /// 设置对齐模式
  TextAlignmentType textAlignmentType;
  /// 将组装的百分比进度条拆分成左右两个进度条比例，这个很关键。
  /// 计算左边和右边进度条百分比
  double leftPercent;
  double rightPercent;
  /// 渲染是否完成
  bool isRenderFinished = false;

  @override
  void initState() {
    super.initState();
    if(widget.list!=null && widget.list.length>0){
      for(int i=0 ; i<widget.list.length ; i++){
        //给数据设置索引
        widget.list[i].dataIndex = i;
      }
    }
    //监听滚动事件，打印滚动位置
    _controller.addListener(() {
      //print(_controller.offset); //打印滚动位置
      //超过一屏幕，且处在边缘的进度条要是渐变颜色。怎么判断某个进度条处在屏幕未展示全的逻辑呢？
    });

    //监听滚动事件，打印滚动位置
    var lastLightIndex = RFIndicatorUtils.getLastLight(widget.list);
    //UI 临时告知，如果是在第一个圆圈【第二个item】，则不进行滚动。其他情况找到位置滚动
    //条目必须大于4，最后一个亮的圈的索引必须大于0
    if (widget.list.length>4 && lastLightIndex>0 && widget.isScrollLastLight) {
      WidgetsBinding.instance.addPostFrameCallback((callback){
        //print("addPostFrameCallback be invoke");
        //渲染完成后滚动listView
        //到指定位置，要先知道某一行位置在屏幕上的位置才行，固定的长度可以计算出来
        _controller.position.animateTo(
          //这里计算一下滚动的距离 = (完整进度条距离 + 圆圈距离 + 左右间距) * 几个 - 定位点item左边进度条距离
          //为何要减去定位点item左边进度条距离，是为了让居中的数字全部显示出来
          //一个进度条 = checkProgressWidth() * 2
          lastLightIndex * (checkProgressWidth() * 2 + checkCircleDiameter() + 16) - checkProgressWidth(),
          duration:  new Duration(seconds: 2),
          curve: Curves.ease,
        );
      });
    }

    WidgetsBinding.instance.addPostFrameCallback((mag) {
      isRenderFinished = true;
      //绘制完成回调
      if (widget.postFrame != null) {
        widget.postFrame.call();
      }
    });
  }

  @override
  void dispose() {
    //为了避免内存泄露，需要调用_controller.dispose
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    if(widget.list == null || widget.list.length == 0){
      return SizedBox();
    }
    Widget child;
    if(widget.list.length<=4 && widget.matchWidthParent){
      child = _getFlowView();
    } else {
      child = _getListView();
    }
    return Container(
      height: checkContainerHeight(),
      padding: widget.padding ?? EdgeInsets.all(0),
      child: child,
    );
  }

  /// 如果是大于4个，或者用户设置不均分，那么就是用listView
  /// 注意：（list控件比如只有两个item，无法设置均分）
  Widget _getListView() {
    List<Widget> list = widget.list.map((e) =>
        _getItemWidget(e.aboveText, e.bottomText,
            e.percentage,e.circleOutsideGone,e.dataIndex,
            e.iconHighLight,e.textHighLight)).toList();
    return ListView(
      //设置为横向，默认使纵向
      scrollDirection: Axis.horizontal,
      //该属性表示是否根据子组件的总长度来设置ListView的长度，默认值为false
      //默认情况下，ListView的会在滚动方向尽可能多的占用空间。
      //当ListView在一个无边界(滚动方向上)的容器中时，shrinkWrap必须为true。
      shrinkWrap: true,
      //滑动监听
      controller: _controller,
      padding: const EdgeInsets.all(0.0),
      children: list,
    );
  }


  /// listView的item数据
  Widget _getItemWidget(String aboveTextStr , String bottomTextStr ,
      double percent , bool outsideGone , int index ,
      bool iconLight, bool textLight ){
    //根据服务端设置是否高亮获取对应的颜色
    IndicatorColorType iconColorType = getIconColorType(iconLight);
    IndicatorColorType iconTextType = getTextColorType(textLight,percent);
    Color textColor = getColor(iconTextType,true);
    Color iconColor = getColor(iconColorType,false);
    calculateData(index, percent);
    return new RFListItemWidget(
      index: index,
      listLength: widget.list.length,
      //设置widget类型
      itemWidgetType: widgetType,
      //第一个索引的文本是左对齐，其他的是居中对齐
      textAlignmentType: textAlignmentType,
      circleDiameter: checkCircleDiameter(),
      circleOutsideDiameter: checkCircleOutsideDiameter(),
      aboveText: aboveTextStr,
      bottomText: bottomTextStr,
      circleOutsideGone: outsideGone,
      aboveColor: textColor,
      bottomColor: textColor,
      circleColor: iconColor,
      circleOutsideColor: RFIndicatorUtils.color_30FF7A45,
      lightUpColor: widget.lightUpColor,
      normalColor: widget.normalColor,
      blackColor: widget.blackColor,
      borderRadius: checkBorderRadius(),
      leftPercent: leftPercent,
      rightPercent: rightPercent,
      progressHeight: checkProgressHeight(),
      progressWidth: checkProgressWidth(),
      isError: widget.isError,
      linearGradient: widget.linearGradient,
      firstItemNormal: widget.firstItemNormal,
      iconColorLight: iconLight,
    );
  }

  /// 如果小于4个，自动适配均分控件，填充满。这里使用 Flex布局 + Expanded
  /// 注意，均分只能够使进度条均分
  Widget _getFlowView(){
    List<Widget> widgetList = widget.list.map((e) =>
        _getFlowItemWidget(e.aboveText, e.bottomText,
            e.percentage,e.circleOutsideGone,e.dataIndex,
            e.iconHighLight,e.textHighLight)).toList();
    return Flex(
      direction: Axis.horizontal,
      children: widgetList,
    );
  }

  Widget _getFlowItemWidget(String aboveTextStr , String bottomTextStr ,
      double percent , bool outsideGone , int index ,
      bool iconLight, bool textLight ){
    //根据服务端设置是否高亮获取对应的颜色
    IndicatorColorType iconColorType = getIconColorType(iconLight);
    IndicatorColorType iconTextType = getTextColorType(textLight,percent);
    Color textColor = getColor(iconTextType,true);
    Color iconColor = getColor(iconColorType,false);
    calculateData(index, percent);
    return new RFFlowExpandWidget(
      index: index,
      listLength: widget.list.length,
      //设置widget类型
      itemWidgetType: widgetType,
      //第一个索引的文本是左对齐，其他的是居中对齐
      textAlignmentType: textAlignmentType,
      circleDiameter: checkCircleDiameter(),
      circleOutsideDiameter: checkCircleOutsideDiameter(),
      aboveText: aboveTextStr,
      bottomText: bottomTextStr,
      circleOutsideGone: outsideGone,
      aboveColor: textColor,
      bottomColor: textColor,
      circleColor: iconColor,
      circleOutsideColor: RFIndicatorUtils.color_30FF7A45,
      lightUpColor: widget.lightUpColor,
      normalColor: widget.normalColor,
      blackColor: widget.blackColor,
      borderRadius: checkBorderRadius(),
      leftPercent: leftPercent,
      rightPercent: rightPercent,
      progressHeight: checkProgressHeight(),
      progressWidth: checkProgressWidth(),
      isError: widget.isError,
      linearGradient: widget.linearGradient,
      matchWidthParent: widget.matchWidthParent,
      iconColorLight: iconLight,
    );
  }

  void calculateData(int index , double percent){
    if(index == 0){
      widgetType = ItemWidgetType.firstWidgetType;
      textAlignmentType = TextAlignmentType.left;
    } else if(index+1 == widget.list.length){
      widgetType = ItemWidgetType.lastWidgetType;
      textAlignmentType = TextAlignmentType.right;
    } else {
      widgetType = ItemWidgetType.normaWidgetType;
      textAlignmentType = TextAlignmentType.center;
    }

    //将组装的百分比进度条拆分成左右两个进度条比例，这个很关键。
    //计算左边和右边进度条百分比
    if(index == 0){
      //第一个由于左边进度条不显示，因此百分比为0
      leftPercent = 0;
      if(percent == 50){
        //特殊处理
        rightPercent = 95;
        //下个item左边的百分比是0
        nextListLeftPercent = 0;
      } else if(percent > 50){
        rightPercent = 100;
        //将多余的进度赋值到下一个item
        nextListLeftPercent = (percent - 50) * 2;
      } else {
        rightPercent = percent * 2;
        //下个item左边的百分比是0
        nextListLeftPercent = 0;
      }
    } else if(index+1 == widget.list.length){
      //最后一个由于右边边进度条不显示，因此百分比为0
      leftPercent = nextListLeftPercent;
      rightPercent = 0;
    } else {
      if(percent == 50){
        //特殊处理
        leftPercent = 100;
        rightPercent = 95;
        //下个item左边的百分比是0
        nextListLeftPercent = 0;
      } else if(percent > 50){
        leftPercent = 100;
        rightPercent = 100;
        //将多余的进度赋值到下一个item
        nextListLeftPercent = (percent - 50) * 2;
      } else {
        leftPercent = nextListLeftPercent;
        rightPercent = percent * 2;
        //下个item左边的百分比是0
        nextListLeftPercent = 0;
      }
    }
  }

  IndicatorColorType getIconColorType(bool isHighLight){
    if(isHighLight){
      return IndicatorColorType.lightUp;
    } else {
      return IndicatorColorType.normal;
    }
  }

  IndicatorColorType getTextColorType(bool isHighLight, double percent){
    if(isHighLight){
      //如果是高亮
      return IndicatorColorType.lightUp;
    } else {
      //如果不是高亮
      if(percent == 100){
        return IndicatorColorType.black;
      } else {
        return IndicatorColorType.normal;
      }
    }
  }

  Color getColor(IndicatorColorType colorType , bool isText){
    if(widget.isError){
      //如果是异常情况，直接特殊处理
      return isText ? widget.textColor : widget.normalColor;
    }
    if(colorType == IndicatorColorType.lightUp){
      return widget.lightUpColor;
    } else if(colorType == IndicatorColorType.normal){
      return isText ? widget.textColor : widget.normalColor;
    } else if(colorType == IndicatorColorType.black){
      return widget.blackColor;
    } else {
      return widget.normalColor;
    }
  }

  /// 默认圆圈直径，内圈是12dp
  double checkCircleDiameter(){
    if(widget.circleDiameter==null || widget.circleDiameter <= 0){
      return RFIndicatorUtils.pt2dp(12);
    } else {
      return widget.circleDiameter;
    }
  }

  /// 默认圆圈直径，外圈是18dp
  double checkCircleOutsideDiameter(){
    if(widget.circleOutsideDiameter==null || widget.circleOutsideDiameter <= 0){
      return RFIndicatorUtils.pt2dp(18);
    } else {
      return widget.circleOutsideDiameter;
    }
  }

  /// 默认进度条的宽度是68dp
  double checkProgressWidth(){
    if(widget.progressWidth==null ||  widget.progressWidth <= 0){
      return RFIndicatorUtils.pt2dp(34);
    } else {
      return widget.progressWidth;
    }
  }

  /// 默认进度条的高度是4dp
  double checkProgressHeight(){
    if(widget.progressHeight==null ||  widget.progressHeight <= 0){
      return RFIndicatorUtils.pt2dp(4);
    } else {
      return widget.progressHeight;
    }
  }

  /// 默认进度条圆角是4dp
  double checkBorderRadius(){
    if(widget.borderRadius==null ||  widget.borderRadius <= 0){
      return RFIndicatorUtils.pt2dp(4);
    } else {
      return widget.borderRadius;
    }
  }

  /// 默认文字是14sp
  double checkTextSize(){
    if(widget.textSize==null ||  widget.textSize <= 0){
      return RFIndicatorUtils.pt2sp(14);
    } else {
      return widget.textSize;
    }
  }

  /// 默认容器高度是94dp
  double checkContainerHeight(){
    if(widget.containerHeight==null ||  widget.containerHeight <= 0){
      return RFIndicatorUtils.pt2sp(94);
    } else {
      return widget.containerHeight;
    }
  }
}

