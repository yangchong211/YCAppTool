import 'package:flutter/material.dart';
import 'package:flutter_widget/progress/indicator/rf_indicator_utils.dart';
import 'package:flutter_widget/progress/percent/rf_percent_progress.dart';
import 'package:flutter_widget/progress/stepper/ace_circle_widget.dart';
import 'package:flutter_widget/progress/stepper/ace_constants.dart';
import 'package:flutter_widget/progress/stepper/ace_enum.dart';
import 'package:flutter_widget/progress/stepper/ace_step.dart';
import 'package:flutter_widget/progress/stepper/ace_step_theme_data.dart';
import 'package:flutter_widget/progress/stepper/stepper_line.dart';

/// https://www.jianshu.com/p/8cf18d7f0d13
class ACEStepper extends StatefulWidget {
  final List<ACEStep> steps;
  final ScrollPhysics physics;
  final ACEStepperType type;
  final int currentStep;
  final ValueChanged<int> onStepTapped;
  final VoidCallback onStepContinue, onStepCancel;
  final bool isContinue, isCancel;
  final double headerHeight;
  final ControlsWidgetBuilder controlsBuilder;
  final ACEStepThemeData themeData;
  final bool isAllContent;

  const ACEStepper({
    Key key,
    @required this.steps,
    this.physics,
    this.type = ACEStepperType.vertical,
    this.currentStep = 0,
    this.onStepTapped,
    this.onStepContinue,
    this.onStepCancel,
    this.isContinue = true,
    this.isCancel = true,
    this.headerHeight,
    this.controlsBuilder,
    this.themeData,
    this.isAllContent = false,
  });

  @override
  State<StatefulWidget> createState() => _ACEStepperState();
}

class _ACEStepperState extends State<ACEStepper> with TickerProviderStateMixin {
  List<GlobalKey> _keys;
  final Map<int, LineType> _oldLineStates = <int, LineType>{};
  final Map<int, IconType> _oldIconStates = <int, IconType>{};

  @override
  void initState() {
    super.initState();
    _keys =
        List<GlobalKey>.generate(widget.steps.length, (int i) => GlobalKey());
    for (int i = 0; i < widget.steps.length; i += 1) {
      _oldLineStates[i] = widget.steps[i].lineType;
      _oldIconStates[i] = widget.steps[i].iconType;
    }
  }

  @override
  void didUpdateWidget(ACEStepper oldWidget) {
    super.didUpdateWidget(oldWidget);
    assert(widget.steps.length == oldWidget.steps.length);
    for (int i = 0; i < oldWidget.steps.length; i += 1) {
      _oldLineStates[i] = oldWidget.steps[i].lineType;
      _oldIconStates[i] = oldWidget.steps[i].iconType;
    }
  }

  bool _isFirst(int index) => index == 0;

  bool _isLast(int index) => widget.steps.length - 1 == index;

  bool _isCurrent(int index) => widget.currentStep == index;

  @override
  Widget build(BuildContext context) {
    if (widget.type == ACEStepperType.vertical) {
      return _buildVertical();
    } else {
      return _buildHorizontal();
    }
  }

  Widget _buildLine(int index, bool state) {
    return Opacity(
      opacity: state ? 0.0 : 1.0,
      child: StepperLine(
          color: widget.themeData == null
              ? AceConstants.kLineColor
              : widget.themeData.lineColor ?? AceConstants.kLineColor,
          lineType: widget.steps[index].lineType,
          type: widget.type),
    );
  }

  // Widget getProgress() {
  //   return Container(
  //     color: Colors.red,
  //     child: RFPercentProgress(
  //       lineHeight: 4.0,
  //       percent: 10,
  //       barRadius: Radius.circular(4),
  //       backgroundColor: RFIndicatorUtils.color_E5,
  //       progressColor: RFIndicatorUtils.color_FF7A45,
  //     ),
  //   );
  // }

  Widget _buildCircle() {
    return ACECircleWidget(
      circleDiameter: 12,
      circleOutsideDiameter: 18,
      circleOutsideGone: false,
      circleColor: Color(0xFFFF7A45),
      circleOutsideColor: Color(0xB3FF7A45),
    );
  }

  Widget _buildVerticalHeader(int index) {
    return Container(
      child: Row(children: <Widget>[
        Container(
            width: AceConstants.kTopTipsWidth,
            padding:
                EdgeInsets.symmetric(horizontal: AceConstants.kCircleMargin),
            child: Center(
              child: widget.steps[index].topWidget,
            )),
        Column(mainAxisSize: MainAxisSize.max, children: <Widget>[
          _buildLine(index, _isFirst(index)),
          SizedBox.fromSize(
              size:
                  Size(AceConstants.kCircleMargin, AceConstants.kCircleMargin)),
          _buildCircle(),
          SizedBox.fromSize(
              size:
                  Size(AceConstants.kCircleMargin, AceConstants.kCircleMargin)),
          _buildLine(index, _isLast(index))
        ]),
        Container(
            margin:
                EdgeInsets.symmetric(horizontal: AceConstants.kCircleMargin),
            child: widget.steps[index].bottomWidget),
      ]),
    );
  }

  Widget _buildHorizontalHeader(int index) {
    return Container(
      // color: Colors.amberAccent,
      child: Column(
        children: <Widget>[
          //上边的widget
          Container(
            color: Colors.cyan,
            height: AceConstants.kTopTipsHeight,
            padding: EdgeInsets.symmetric(
              horizontal: AceConstants.kCircleMargin,
            ),
            child: Center(
              child: widget.steps[index].topWidget,
            ),
          ),
          //横向控件
          Container(
            // color: Colors.red,
            //水平布局
            child: Row(
              crossAxisAlignment: CrossAxisAlignment.center,
              mainAxisSize: MainAxisSize.min,
              children: <Widget>[
                //绘制左边的线
                _buildLine(index, _isFirst(index)),
                //间距
                SizedBox.fromSize(
                  size: Size(
                    AceConstants.kCircleMargin,
                    AceConstants.kCircleMargin,
                  ),
                ),
                //绘制圆圈
                _buildCircle(),
                //间距
                SizedBox.fromSize(
                  size: Size(
                    AceConstants.kCircleMargin,
                    AceConstants.kCircleMargin,
                  ),
                ),
                //绘制右边的线
                _buildLine(index, _isLast(index))
              ],
            ),
          ),
          // //下面的widget
          Container(
            color: Colors.cyan,
            margin: EdgeInsets.symmetric(vertical: AceConstants.kCircleMargin),
            child: widget.steps[index].bottomWidget,
          ),
        ],
      ),
    );
  }

  Widget _buildVerticalControls() {
    return (widget.controlsBuilder != null)
        ? widget.controlsBuilder(context,
            onStepContinue: widget.onStepContinue,
            onStepCancel: widget.onStepCancel)
        : Container(
            child: Row(children: <Widget>[
            widget.isContinue
                ? FlatButton(
                    onPressed: widget.onStepContinue, child: Text('继续'))
                : SizedBox.shrink(),
            widget.isCancel
                ? FlatButton(onPressed: widget.onStepCancel, child: Text('取消'))
                : SizedBox.shrink()
          ]));
  }

  Widget _buildVerticalBody(int index) {
    double circleDiameter = widget.themeData == null
        ? AceConstants.kCircleDiameter
        : widget.themeData.circleDiameter ?? AceConstants.kCircleDiameter;
    return Stack(children: <Widget>[
      PositionedDirectional(
          start: AceConstants.kTopTipsWidth +
              (circleDiameter - AceConstants.kLineWidth) * 0.5,
          top: Size.zero.width,
          bottom: Size.zero.width - 2,
          child: _isLast(index)
              ? SizedBox.shrink()
              : AspectRatio(
                  aspectRatio: 1,
                  child: SizedBox.expand(child: _buildLine(index, false)))),
      widget.isAllContent
          ? Container(
              margin: EdgeInsets.only(
                  left: AceConstants.kTopTipsWidth +
                      AceConstants.kCircleMargin * 2 +
                      circleDiameter),
              child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: <Widget>[
                    SizedBox.shrink(),
                    _buildVerticalControls()
                  ]))
          : AnimatedCrossFade(
              firstChild: SizedBox.shrink(),
              secondChild: Container(
                  margin: EdgeInsetsDirectional.only(
                      start: AceConstants.kTopTipsWidth +
                          AceConstants.kCircleMargin * 2 +
                          circleDiameter),
                  child: Column(children: <Widget>[
                    SizedBox.shrink(),
                    _buildVerticalControls()
                  ])),
              crossFadeState: _isCurrent(index)
                  ? CrossFadeState.showSecond
                  : CrossFadeState.showFirst,
              duration: Duration(milliseconds: 1))
    ]);
  }

  Widget _buildVertical() {
    return ListView(
        shrinkWrap: true,
        physics: widget.physics,
        children: <Widget>[
          for (int i = 0; i < widget.steps.length; i += 1)
            Column(key: _keys[i], children: <Widget>[
              InkWell(
                  child: _buildVerticalHeader(i),
                  onTap: () => (widget.onStepTapped != null)
                      ? widget.onStepTapped(i)
                      : null),
              _buildVerticalBody(i)
            ])
        ]);
  }

  Widget _buildHorizontal() {
    return Container(
      height: widget.headerHeight == null || widget.headerHeight <= 0.0
          ? AceConstants.kHeaderHeight
          : widget.headerHeight,
      child: ListView(
          //如果[primary]参数为true，则[controller]必须为空。
          primary: false,
          //该属性表示是否根据子组件的总长度来设置ListView的长度，默认值为false
          //默认情况下，ListView的会在滚动方向尽可能多的占用空间。
          //当ListView在一个无边界(滚动方向上)的容器中时，shrinkWrap必须为true。
          shrinkWrap: true,
          //设置为横向，默认使纵向
          scrollDirection: Axis.horizontal,
          children: <Widget>[
            for (int i = 0; i < widget.steps.length; i += 1)
              Column(key: _keys[i], children: <Widget>[
                InkWell(
                    child: Container(
                      padding: EdgeInsets.only(left: 0, right: 0),
                      child: _buildHorizontalHeader(i),
                    ),
                    onTap: () => (widget.onStepTapped != null)
                        ? widget.onStepTapped(i)
                        : null),
              ]),
          ]),
      // color: Colors.amber,
    );
  }
}
