


import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_widget/progress/indicator/rf_indicator_bean.dart';
import 'package:flutter_widget/progress/indicator/rf_indicator_utils.dart';
import 'package:flutter_widget/progress/indicator/rf_progress_indicator.dart';
import 'package:flutter_widget/progress/percent/rf_percent_progress.dart';
import 'package:flutter_widget/progress/shadow/rf_shadow_widget.dart';
import 'package:flutter_widget/progress/shadow/rf_shadow_widget.dart';
import 'package:flutter_widget/progress/shadow/rf_shadow_widget.dart';
import 'package:flutter_widget/progress/stepper/ace_enum.dart';
import 'package:flutter_widget/progress/stepper/ace_step.dart';
import 'package:flutter_widget/progress/stepper/ace_step_theme_data.dart';
import 'package:flutter_widget/progress/stepper/ace_stepper.dart';
import 'package:flutter_widget/res/color/flutter_colors.dart';

class CustomProgressWidget extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return CustomProgressState();
  }

}

class CustomProgressState extends State<CustomProgressWidget>{

  List<RFIndicatorBean> _list5 = new List();
  List<RFIndicatorBean> _list2 = new List();
  List<RFIndicatorBean> _list3 = new List();
  List<RFIndicatorBean> _list4 = new List();


  /// 测试数据
  List<RFIndicatorBean> _getTestList5(){
    List<RFIndicatorBean> _list = new List();
    //第一个数据是占位单位
    _list.add(RFIndicatorBean("Mx" , "level",100,false,true,false));
    //第二个数据item填充
    _list.add(RFIndicatorBean("500" , "1",70,false,true,true));
    _list.add(RFIndicatorBean("1000" , "2",0,true,false,false));
    _list.add(RFIndicatorBean("2000" , "3",0,true,false,false));
    _list.add(RFIndicatorBean("4000" , "4",0,true,false,false));
    _list.add(RFIndicatorBean("7000" , "5",0,true,false,false));
    return _list;
  }

  /// 测试数据
  List<RFIndicatorBean> _getTestList4(){
    List<RFIndicatorBean> _list = new List();
    //第一个数据是占位单位
    _list.add(RFIndicatorBean("Mx" , "单位",100,false,true,false));
    //第二个数据item填充
    _list.add(RFIndicatorBean("500" , "1",100,true,true,true));
    _list.add(RFIndicatorBean("1000" , "2",80,true,true,true));
    _list.add(RFIndicatorBean("2000" , "3",0,true,false,false));
    _list.add(RFIndicatorBean("4000" , "4",0,true,false,false));
    _list.add(RFIndicatorBean("7000" , "5",0,true,false,false));
    return _list;
  }

  /// 测试数据
  List<RFIndicatorBean> _getTestList4_2(){
    List<RFIndicatorBean> _list = new List();
    //第一个数据是占位单位
    _list.add(RFIndicatorBean("Mx" , "level",100,false,true,false));
    //第二个数据item填充
    _list.add(RFIndicatorBean("500" , "1",100,true,true,true));
    _list.add(RFIndicatorBean("1000" , "2",0,false,true,true));
    _list.add(RFIndicatorBean("2000" , "3",0,true,false,false));
    _list.add(RFIndicatorBean("4000" , "4",0,true,false,false));
    _list.add(RFIndicatorBean("7000" , "5",0,true,false,false));
    return _list;
  }


  /// 测试数据
  List<RFIndicatorBean> _getTestList3_1(){
    List<RFIndicatorBean> _list = new List();
    //第一个数据是占位单位
    _list.add(RFIndicatorBean("Mx" , "level",100,false,true,false));
    //第二个数据item填充
    _list.add(RFIndicatorBean("500" , "1",50,true,true,true));
    _list.add(RFIndicatorBean("1000" , "2",0,true,false,false));
    _list.add(RFIndicatorBean("2000" , "3",0,true,false,false));
    return _list;
  }


  /// 测试数据
  List<RFIndicatorBean> _getTestList3_2(){
    List<RFIndicatorBean> _list = new List();
    //第一个数据是占位单位
    _list.add(RFIndicatorBean("dan" , "level",100,false,true,false));
    //第二个数据item填充
    _list.add(RFIndicatorBean("500" , "1",100,true,true,true));
    _list.add(RFIndicatorBean("1000" , "2",80,true,true,true));
    _list.add(RFIndicatorBean("2000" , "3",0,true,false,false));
    return _list;
  }

  /// 测试数据
  List<RFIndicatorBean> _getTestList3_3(){
    List<RFIndicatorBean> _list = new List();
    //第一个数据是占位单位
    _list.add(RFIndicatorBean("Mx" , "level",100,false,true,false));
    //第二个数据item填充
    _list.add(RFIndicatorBean("500" , "1",100,true,true,true));
    _list.add(RFIndicatorBean("1000" , "2",0,false,true,true));
    _list.add(RFIndicatorBean("2000" , "3",0,true,false,false));
    return _list;
  }


  @override
  void initState() {
    super.initState();
    //第一个参数数字
    //第二个参数等级
    //第三个参数进度（0到100）
    //第四个参数外圆圈是否显示
    //第五个参数圆圈是否高亮
    //第六个参数文本是否高亮
    _list5.add(RFIndicatorBean("Mx" , "单位",100,false,true,false));
    _list5.add(RFIndicatorBean("500" , "1",100,false,true,true));
    _list5.add(RFIndicatorBean("1000" , "2",30,false,true,true));
    _list5.add(RFIndicatorBean("2000" , "3",0,true,false,false));
    _list5.add(RFIndicatorBean("4000" , "4",0,true,false,false));
    _list5.add(RFIndicatorBean("7000" , "5",0,true,false,false));


    _list2.add(RFIndicatorBean("Mx" , "level",100,false,true,false));
    _list2.add(RFIndicatorBean("500" , "1",40,false,true,true));
    _list2.add(RFIndicatorBean("1000" , "2",0,true,false,false));


    _list3.add(RFIndicatorBean("Mx" , "你好",100,false,true,false));
    _list3.add(RFIndicatorBean("500" , "1",40,true,true,true));
    _list3.add(RFIndicatorBean("1000" , "2",0,true,false,false));
    _list3.add(RFIndicatorBean("2000" , "3",0,true,false,false));


    _list4.add(RFIndicatorBean("Mx" , "level",100,false,false,false));
    _list4.add(RFIndicatorBean("500" , "1",100,true,true,false));
    _list4.add(RFIndicatorBean("1000" , "2",20,true,true,false));
    _list4.add(RFIndicatorBean("2000" , "3",0,true,false,false));
    _list4.add(RFIndicatorBean("2000" , "4",0,true,false,false));
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("自定义分段进度指示器"),
      ),
      body: ListView(
        children: <Widget>[
          // Text("自定义指示器颜色，进度条宽高，以及进度条圆角"),
          // Container(
          //   child: RFProgressIndicator(
          //     //设置数据，必须
          //     list: _list5,
          //     //默认圆圈直径
          //     circleDiameter: 12,
          //     //默认外圆圈直径
          //     circleOutsideDiameter: 18,
          //     //进度条宽度
          //     progressWidth: 120,
          //     //进度条高度
          //     progressHeight: 10,
          //     //点亮时候的颜色【圆圈和进度条点亮颜色一样】
          //     lightUpColor: FlutterColors.color_F93F3F,
          //     //未点亮正常灰色【圆圈和进度条未点亮颜色一样】
          //     normalColor: FlutterColors.color_E5,
          //     //进度条圆角
          //     borderRadius: 10,
          //     //容器的padding
          //     padding: EdgeInsets.fromLTRB(10, 0, 10, 0),
          //     //第一个item是否按照正常模式显示，如果其他地方要是用，则可以设置
          //     firstItemNormal: true,
          //     //是否是异常情况
          //     isError: false,
          //     //字体的颜色
          //     textColor: FlutterColors.color_99,
          //     //字体的大小
          //     textSize: 14,
          //     containerHeight: 94,
          //   ),
          //   color: Colors.transparent,
          //   // padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
          // ),
          // Text("最简单用法，只传入数据集合，超过4组数据"),
          // _getWidget(),
          Container(
            child: RFProgressIndicator(
              list: _list5,
            ),
            padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
          ),
          // Container(
          //   child: RFProgressIndicator(
          //     list: _getTestList5(),
          //   ),
          //   padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
          // ),
          // Container(
          //   child: RFProgressIndicator(
          //     list: _list4,
          //   ),
          //   padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
          // ),
          // Container(
          //   child: RFProgressIndicator(
          //     list: _getTestList4(),
          //   ),
          //   padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
          // ),
          // Container(
          //   child: RFProgressIndicator(
          //     list: _getTestList4_2(),
          //   ),
          //   padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
          // ),
          // Container(
          //   child: RFProgressIndicator(
          //     list: _getTestList4(),
          //     isError: true,
          //   ),
          //   padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
          // ),
          // Text("最简单用法，只传入数据集合，4组数据"),
          // Container(
          //   child: RFProgressIndicator(
          //     list: _list4,
          //     matchWidthParent: true,
          //   ),
          //   padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
          // ),
          // Container(
          //   child: RFProgressIndicator(
          //     list: _list4,
          //     matchWidthParent: true,
          //     firstItemNormal: true,
          //   ),
          //   padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
          // ),
          // Text("最简单用法，只传入数据集合，3组数据"),
          // Container(
          //   child: RFProgressIndicator(
          //     list: _list3,
          //   ),
          //   padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
          // ),
          // Container(
          //   child: RFProgressIndicator(
          //     list: _getTestList3_1(),
          //     matchWidthParent: true,
          //   ),
          //   padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
          // ),
          // Container(
          //   child: RFProgressIndicator(
          //     list: _getTestList3_2(),
          //     matchWidthParent: true,
          //   ),
          //   padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
          // ),
          // Container(
          //   child: RFProgressIndicator(
          //     list: _getTestList3_3(),
          //     matchWidthParent: true,
          //   ),
          //   padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
          // ),
          // Container(
          //   child: RFProgressIndicator(
          //     list: _getTestList3_2(),
          //     isError: true,
          //   ),
          //   padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
          // ),
          // Text("最简单用法，只传入数据集合，2组数据"),
          // Container(
          //   child: RFProgressIndicator(
          //     list: _list2,
          //   ),
          //   padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
          // ),
          // Container(
          //   child: RFProgressIndicator(
          //     list: _list2,
          //     matchWidthParent: false,
          //   ),
          //   padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
          // ),
          // Text("自定义指示器颜色，进度条宽高"),
          // Container(
          //   child: RFProgressIndicator(
          //     list: _list5,
          //     lightUpColor: FlutterColors.color_FF7A45,
          //     normalColor: FlutterColors.color_99,
          //   ),
          //   padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
          //   // color: FlutterColors.color_FF7A45,
          // ),
          // Text("最简单用法，只传入数据集合，设置第一个item显示圆圈"),
          // Container(
          //   child: RFProgressIndicator(
          //     list: _list2,
          //     firstItemNormal: true,
          //   ),
          //   color: Colors.transparent,
          //   padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
          // ),
          // Text("最简单用法，传入数据集合，设置异常情况"),
          // Container(
          //   child: RFProgressIndicator(
          //     list: _list5,
          //     firstItemNormal: true,
          //     isError: true,
          //   ),
          //   // color: FlutterColors.color_F6F6F6,
          //   padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
          // ),
          // Container(
          //   height: 300,
          //   child: _aceStepperWid02(),
          // ),
          // Text("自定义进度条，普通进度条"),
          // RFShadowWidget(
          //   // width: 200,
          //   lineHeight: 8.0,
          //   barRadius: Radius.circular(4),
          //   backgroundColor: FlutterColors.color_E5,
          //   progressColor: FlutterColors.color_00,
          //   linearGradient: LinearGradient(
          //     begin: Alignment.centerLeft,
          //     end: Alignment.centerRight,
          //     colors: [
          //       FlutterColors.color_10FF7A45,
          //       FlutterColors.color_FF7A45,
          //     ],
          //   ),
          // ),
          // RFShadowWidget(
          //   width: 48,
          //   lineHeight: 98,
          //   linearGradient: LinearGradient(
          //     begin: Alignment.centerLeft,
          //     end: Alignment.centerRight,
          //     colors: [
          //       Color(0xFFFFFFFF),
          //       Color(0xD9FFFFFF),
          //     ],
          //   ),
          // ),


          // Text("自定义进度条，带有渐变效果"),
          // RFPercentProgress(
          //   width: 200,
          //   lineHeight: 8.0,
          //   percent: 30,
          //   barRadius: Radius.circular(4),
          //   backgroundColor: FlutterColors.color_E5,
          //   progressColor: FlutterColors.color_00,
          //   linearGradient: LinearGradient(
          //     begin: Alignment.centerLeft,
          //     end: Alignment.centerRight,
          //     colors: [
          //       FlutterColors.color_10FF7A45,
          //       FlutterColors.color_FF7A45,
          //     ],
          //   ),
          // ),
          // Padding(
          //   padding: EdgeInsets.fromLTRB(20, 10, 20, 10),
          //   child: RFPercentTextWidget(
          //     text: "11.2 hours",
          //     percent: 20,
          //   ),
          // ),
          // Padding(
          //   padding: EdgeInsets.fromLTRB(20, 10, 20, 10),
          //   child: RFPercentTextWidget(
          //     text: "11.2 hours",
          //     percent: 20,
          //     isError: true,
          //   ),
          // ),
          // Padding(
          //   padding: EdgeInsets.fromLTRB(20, 10, 20, 10),
          //   child: RFPercentTextWidget(
          //     text: "3.1/ 4hours",
          //     // text: null,
          //     percent: 30,
          //   ),
          // ),
        ],
      ),
    );
  }


  Widget _getWidget(){
    bool offstage = false;
    return Stack(
      alignment: offstage
          ? AlignmentDirectional.centerStart
          : AlignmentDirectional.centerEnd,
      children: [
        Container(
          padding: EdgeInsets.only(top: 15,left: 1),
          child: RFProgressIndicator(
            list: _list5,
          ),
        ),
        Positioned(
          left: 0,
          child: RFShadowWidget(
            width: 48,
            lineHeight: 98,
            isRTL: true,
            linearGradient: LinearGradient(
              begin: Alignment.centerLeft,
              end: Alignment.centerRight,
              colors: [
                Color(0x00FFFFFF),
                Color(0xD9FFFFFF),
              ],
            ),
          ),
        ),
        Offstage(
          //是否显示，当list条目大于4时，显示阴影。这个是右边的
          offstage: offstage,
          child: RFShadowWidget(
            width: 48,
            lineHeight: 98,
            isRTL: false,
            linearGradient: LinearGradient(
              begin: Alignment.centerLeft,
              end: Alignment.centerRight,
              colors: [
                Color(0x00FFFFFF),
                Color(0xD9FFFFFF),
              ],
            ),
          ),
        ),
      ],
    );
  }

  Widget _aceStepperWid02() {
    return ACEStepper(
        type: ACEStepperType.horizontal,
        controlsBuilder: (BuildContext context,
            {VoidCallback onStepContinue, VoidCallback onStepCancel}) {
          return Container();
        },
        themeData: ACEStepThemeData(
            lineColor: Colors.green,
          
        ),
        steps: [
          ACEStep(
              bottomWidget: Text('1'),
              // lineType: LineType.normal,
              iconType: IconType.text,
              isActive: true,
              topWidget: Text('这个是上面标题')
          ),
          ACEStep(
              bottomWidget: Text('2'),
              lineType: LineType.circle,
              iconType: IconType.text,
              isActive: true,
              topWidget: Text('2000')
          ),
          ACEStep(
              bottomWidget: Text('3'),
              lineType: LineType.circle,
              iconType: IconType.text,
              isActive: false,
              topWidget: Text('3000')
          ),
          ACEStep(
              bottomWidget: Text('4'),
              lineType: LineType.circle,
              iconType: IconType.ass_url,
              isActive: true,
              topWidget: Text('3000000'))
        ]);
  }

}




class RFPercentTextWidget extends StatelessWidget{

  /// 文案
  final String text;
  /// 进度条百分比【服务端返回是0到100】
  final double percent;
  /// 是否是异常情况
  final bool isError;

  RFPercentTextWidget({
    Key key,
    this.text,
    this.percent,
    this.isError = false,
  }) :assert(text != null),
        assert(percent != null),
        super(key: key);

  @override
  Widget build(BuildContext context) {
    return RFPercentProgress(
      lineHeight: 4.0,
      percent: percent ?? 0.0,
      barRadius: Radius.circular(4),
      backgroundColor: RFIndicatorUtils.color_E5,
      progressColor: isError ? RFIndicatorUtils.color_00 : RFIndicatorUtils.color_FF7A45,
      rightWidget: getTextWidget(),
    );
  }

  Widget getTextWidget(){
    return Padding(
      padding: EdgeInsets.fromLTRB(10, 0,0,0),
      child: SizedBox(
        width: RFIndicatorUtils.pt2dp(73),
        child: Text(
          text ?? "",
          style: TextStyle(
            fontSize: RFIndicatorUtils.pt2sp(12),
            color: Color(0xFF666666),
          ),
        ),
      ),
    );
  }

}




