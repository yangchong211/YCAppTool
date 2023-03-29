
import 'package:flutter/material.dart';


///弹窗
///所有参数必须传递
class CustomBaseDialog extends StatefulWidget{

  CustomBaseDialog({
    Key key,
    this.title,
    this.onPressed,
    this.height,
    this.hiddenTitle : false,
    @required this.child
  }) : super(key : key);

  final String title;
  //点击的回调
  final Function onPressed;
  final Widget child;
  final double height;
  final bool hiddenTitle;

  @override
  _BaseDialog createState() => _BaseDialog();

}

class _BaseDialog extends State<CustomBaseDialog>{

  @override
  Widget build(BuildContext context) {
    //创建透明层
    return Scaffold(
      //透明类型
      backgroundColor: Colors.transparent,
      //创建布局
      body: Center(
        child: Container(
            decoration: BoxDecoration(
              //设置
              color: Colors.white,
              //设置半径
              borderRadius: BorderRadius.circular(8.0),
            ),
            //定义宽高
            width: 270.0,
            height: widget.height,
            padding: const EdgeInsets.only(top: 24.0),
            child: Column(
              children: <Widget>[
                //这个是内容区域
                Offstage(
                  offstage: widget.hiddenTitle,
                  child: Padding(
                    padding: const EdgeInsets.only(bottom: 8.0),
                    child: Text(
                      widget.hiddenTitle ? "" : widget.title,
                      style: new TextStyle(
                        fontSize: 18,
                        color: Color(0xFF333333),
                        fontWeight: FontWeight.bold
                      ),
                    ),
                  ),
                ),
                Expanded(child: widget.child),
                // 垂直间隔
                SizedBox(height: 8),
                Container(height: 0.6, color: Color(0xFFEEEEEE)),
                //取消和确定按钮
                Row(
                  children: <Widget>[
                    Expanded(
                      child: Container(
                        height: 48.0,
                        child: FlatButton(
                          child: Text(
                            "取消",
                            style: TextStyle(
                                fontSize: 18
                            ),
                          ),
                          textColor: Color(0xFF999999),
                          onPressed: (){
                            Navigator.of(context).pop();
                          },
                        ),
                      ),
                    ),
                    Container(
                      height: 48.0,
                      width: 0.6,
                      color: Color(0xFFEEEEEE),
                    ),
                    Expanded(
                      child: Container(
                        height: 48.0,
                        child: FlatButton(
                          child: Text(
                            "确定",
                            style: TextStyle(
                                fontSize: 18
                            ),
                          ),
                          textColor: Color(0xff4688FA),
                          onPressed: (){
                            widget.onPressed();
                          },
                        ),
                      ),
                    )
                  ],
                )
              ],
            )
        ),
      ),
    );
  }
}