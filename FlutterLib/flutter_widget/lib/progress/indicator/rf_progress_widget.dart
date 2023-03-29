import 'package:flutter/material.dart';
import 'package:flutter_widget/progress/indicator/rf_indicator_emun.dart';

class RFProgressWidget extends StatefulWidget {

  /// 进度条宽度
  final double width;
  /// 进度条高度
  final double height;
  /// 进度条背景色
  final Color bgColor;
  /// 进度条进度色
  final Color frColor;
  /// 进度条圆角
  final double borderRadius;
  /// 进度条进度比例，默认是0，注意是0到100
  final double percentage;
  /// 是否均分适配屏幕宽度
  final bool matchWidthParent;
  /// 进度条圆角类型
  final BorderRadiusType radiusType;

  RFProgressWidget({
    Key key,
    @required this.width,
    @required this.height,
    this.bgColor,
    this.frColor,
    this.borderRadius,
    this.percentage = 0,
    //是均分适配屏幕宽度
    this.matchWidthParent = false,
    //进度条圆角类型
    this.radiusType = BorderRadiusType.allRadius,
  })  : assert(width != null),
        assert(height != null),
        super(key: key);

  @override
  State<StatefulWidget> createState() {
    return RFProgressState();
  }
}

class RFProgressState extends State<RFProgressWidget> {
  GlobalKey _globalKey = GlobalKey();

  /// 由于小于4个item，进度条是使用Expanded伸缩布局适应宽度
  /// 计算之后的控件宽度，主要是需要宽度来计算进度条百分比的宽度
  double _sizeWidth = 1;
  /// 背景色圆角
  BorderRadiusGeometry borderRadius;
  /// 进度条圆角
  BorderRadiusGeometry progressBorderRadius;
  double percentHorizontal;

  @override
  void initState() {
    super.initState();
    // 延时300ms执行返回，保证layout完成
    // if (widget.matchWidthParent) {
    //   Future.delayed(Duration(milliseconds: 300), () {
    //     getWidgetSize();
    //     if (_sizeWidth > 1 && mounted) {
    //       setState(() {});
    //     }
    //   });
    // }
    //通过addPostFrameCallback做一些安全的操作，会在当前Frame绘制完后进行回调，并之后回调一次。
    if (widget.matchWidthParent) {
      WidgetsBinding.instance.addPostFrameCallback((callback){
        print("addPostFrameCallback be invoke");
        getWidgetSize();
        if (_sizeWidth > 1 && mounted) {
          setState(() {});
        }
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return _buildContentWidget();
  }

  void getWidgetSize() {
    Size size =
        _globalKey.currentContext?.findRenderObject()?.paintBounds?.size;
    if (size != null) {
      _sizeWidth = size.width;
    }
  }

  Widget _buildContentWidget() {
    if (widget.radiusType == BorderRadiusType.leftRadius) {
      //左边有圆角
      borderRadius =
          BorderRadius.horizontal(left: Radius.circular(widget.borderRadius));
      if(widget.percentage==100){
        progressBorderRadius =
            BorderRadius.horizontal(left: Radius.circular(widget.borderRadius));
      } else {
        progressBorderRadius =
            BorderRadius.all(Radius.circular(widget.borderRadius));
      }
    } else if (widget.radiusType == BorderRadiusType.rightRadius) {
      //右边有圆角
      borderRadius =
          BorderRadius.horizontal(right: Radius.circular(widget.borderRadius));
      progressBorderRadius =
          BorderRadius.horizontal(right: Radius.circular(widget.borderRadius));
    } else {
      borderRadius = BorderRadius.all(Radius.circular(widget.borderRadius));
      progressBorderRadius =
          BorderRadius.all(Radius.circular(widget.borderRadius));
    }

    // 计算百分比
    double percent = 0;
    if (widget.percentage > 100.0) {
      percent = 100;
    } else if (widget.percentage < 0.0) {
      percent = 0;
    } else {
      percent = widget.percentage;
    }
    if (widget.matchWidthParent) {
      percentHorizontal = _sizeWidth * (percent / 100);
    } else {
      percentHorizontal = widget.width * (percent / 100);
    }

    //区分list和flow布局
    if (widget.matchWidthParent) {
      return _getFlowWidget();
    }
    return _getNormalWidget();
  }

  /// 让进度条去充满布局，用于Flow + Expanded
  Widget _getFlowWidget() {
    return Expanded(
      key: _globalKey,
      child: Container(
          // 让控件填充满整个界面
          constraints: BoxConstraints(
            minWidth: double.infinity,
          ),
          height: widget.height,
          child: Stack(
            children: <Widget>[
              //背景色布局
              Container(
                height: widget.height,
                decoration: BoxDecoration(
                    borderRadius: borderRadius, color: widget.bgColor),
              ),
              //进度条布局
              Container(
                width: percentHorizontal,
                height: widget.height,
                decoration: BoxDecoration(
                  borderRadius: progressBorderRadius,
                  color: widget.frColor,
                ),
              ),
            ],
          )),
    );
  }

  /// 进度条设置多少展示多少，用于listView中item的显示
  Widget _getNormalWidget() {
    return Container(
        width: widget.width,
        height: widget.height,
        child: Stack(
          children: <Widget>[
            //背景色布局
            Container(
              width: widget.width,
              height: widget.height,
              decoration: BoxDecoration(
                  borderRadius: borderRadius, color: widget.bgColor),
            ),
            //进度条布局
            Container(
              width: percentHorizontal,
              height: widget.height,
              decoration: BoxDecoration(
                borderRadius: progressBorderRadius,
                color: widget.frColor,
              ),
            ),
          ],
        ));
  }
}
