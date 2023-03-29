


import 'package:flutter/material.dart';
import 'package:flutter_widget/dialog/dialog/custom_base_dialog.dart';

class CustomCombinationWidget extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return CombinationState();
  }

}

class CombinationState extends State<CustomCombinationWidget>{

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("组合现有组件"),
      ),
      body: new ListView(
        children: [
          new Text("自定义渐变按钮1"),
          GradientButton(
            colors: [Colors.orange, Colors.red],
            widget: Text("Submit"),
            onPressed: onTap,
            borderRadius: BorderRadius.circular(10.0),
          ),
          new Padding(padding: EdgeInsets.all(10)),
          GradientButton(
            colors: [Colors.brown, Colors.deepOrange],
            height: 50.0,
            widget: Text("Submit"),
            onPressed: onTap,
          ),

          new Padding(padding: EdgeInsets.all(20)),
          new Text("自定义渐变按钮1-加强版"),
          GradientButton2(
            colors: [Colors.orange, Colors.red],
            height: 50.0,
            widget: Text("Submit"),
            onPressed: onTap,
            borderRadius: BorderRadius.circular(10.0),
          ),
          new Padding(padding: EdgeInsets.all(10)),
          GradientButton2(
            colors: [Colors.brown, Colors.deepOrange],
            height: 50.0,
            widget: Text("Submit"),
            onPressed: onTap,
          ),

          new Padding(padding: EdgeInsets.all(30)),
          new Text("自定义滑动渐变按钮"),
          new Padding(padding: EdgeInsets.all(10)),


          GradientButton(
            colors: [Colors.brown, Colors.deepOrange],
            height: 50.0,
            widget: Text("组合自定义弹窗1"),
            onPressed: _showBaseDialog,
          ),


        ],
      ),
    );
  }

  onTap() {
    print("button click");
  }

  @override
  void dispose() {
    super.dispose();
  }

  void _showBaseDialog() {
    showDialog(
        context: context,
        barrierDismissible: false,
        builder: (BuildContext context) {
          return CustomBaseDialog(
            hiddenTitle: true,
            height: 150.0,
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16.0),
              child: const Text("请选择是否需要App中所有的清除缓存",
                  textAlign: TextAlign.center),
            ),
            onPressed: (){

            },
          );
        }
    );
  }
}

///自定义组建1
// 背景支持渐变色
// 手指按下时有涟漪效果
// 可以支持圆角
class GradientButton extends StatelessWidget{

  final double width;
  final double height;
  final Widget widget;
  // 渐变色数组
  final List<Color> colors;
  //点击回调
  final GestureTapCallback onPressed;
  //圆角效果
  final BorderRadius borderRadius;
  bool isPressOn = false;

  ///构造方法
  GradientButton({
    //该注解表示字段一定要输出
    @required this.widget,
    this.height,
    this.width,
    this.colors,
    this.onPressed,
    this.borderRadius,
  }) : assert(widget != null);


  @override
  Widget build(BuildContext context) {
    ThemeData theme = Theme.of(context);
    //确保colors数组不空
    List<Color> _colors = colors ??
        [theme.primaryColor, theme.primaryColorDark ?? theme.primaryColor];


    //选择装饰容器
    return new DecoratedBox(
      decoration: new BoxDecoration(
        //渐变
          gradient: LinearGradient(colors: _colors),
          //圆角
          borderRadius: borderRadius,
          //阴影
          boxShadow: [
            BoxShadow(
                color:colors[0],
                offset: Offset(1.0,1.0),
                blurRadius: 2.0
            )
          ]
      ),

      child: Material(
        type: MaterialType.transparency,
        //击时出现“水波纹”效果
        child: InkWell(
          splashColor: _colors.last,
          highlightColor: Colors.transparent,
          borderRadius: borderRadius,
          onTap: onPressed,
          //ConstrainedBox 是限制容器
          child: ConstrainedBox(
            constraints: BoxConstraints.tightFor(height: height, width: width),
            child: Center(
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: DefaultTextStyle(
                  style: TextStyle(fontWeight: FontWeight.bold),
                  child: widget,
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }

}

///自定义组建2
// 背景支持渐变色
// 手指按下时有涟漪效果
// 可以支持圆角
// 点击改变点击背景颜色
class GradientButton2 extends StatefulWidget{

  final double width;
  final double height;
  final Widget widget;
  // 渐变色数组
  final List<Color> colors;
  //点击回调
  final GestureTapCallback onPressed;
  //圆角效果
  final BorderRadius borderRadius;

  GradientButton2({
    Key key,
    this.widget,
    this.height,
    this.width,
    this.colors,
    this.onPressed,
    this.borderRadius,
    ///使用assert断言函数
  }) :assert(widget != null),
  ///super
  super(key: key);


  @override
  State<StatefulWidget> createState() {
    return GradientButtonState2();
  }

}

class GradientButtonState2 extends State<GradientButton2>{

  bool isPressOn = false;

  @override
  Widget build(BuildContext context) {
    ThemeData theme = Theme.of(context);
    //确保colors数组不空
    List<Color> _colors = widget.colors ??
        [theme.primaryColor, theme.primaryColorDark ?? theme.primaryColor];


    //选择装饰容器
    return new DecoratedBox(
      decoration: new BoxDecoration(
        //渐变
          gradient: LinearGradient(colors: _colors),
          //圆角
          borderRadius: widget.borderRadius,
          //阴影
          boxShadow: [
            BoxShadow(
                color:widget.colors[0],
                offset: Offset(1.0,1.0),
                blurRadius: 2.0
            )
          ]
      ),
      child: new GestureDetector(
        child: new Center(
          child: new Padding(
            padding: const EdgeInsets.all(8.0),
            child:  new Text("文本",
              style: new TextStyle(
                  // fontSize: isPressOn ? 18 : 12,
                  // fontWeight: isPressOn ? FontWeight.bold : FontWeight.normal,
              ),
            ),
          ),
        ),
        onPanDown: (DragDownDetails e) {
          //打印手指按下的位置(相对于屏幕)
          setState(() {
            isPressOn = true;
          });
        },
        onPanEnd: (DragEndDetails e){
          //打印滑动结束时在x、y轴上的速度
          setState(() {
            isPressOn = false;
          });
        },
        onTap: widget.onPressed,
      ),
    );
  }

}





