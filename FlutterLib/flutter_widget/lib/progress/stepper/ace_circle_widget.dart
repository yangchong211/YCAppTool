


import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

/// 双圆圈控件，外部圆圈可以控制是否显示
class ACECircleWidget extends StatelessWidget{

  /// 圆圈直径，内圈
  final double circleDiameter;
  /// 圆圈直径，外圈
  final double circleOutsideDiameter;
  /// 外圈是否隐藏，默认是不隐藏的
  final bool circleOutsideGone;
  /// 圆圈颜色，内圈
  final Color circleColor;
  /// 圆圈颜色，外圈
  final Color circleOutsideColor;

  ACECircleWidget({Key key,
    //默认圆圈直径
    this.circleDiameter,
    //默认外圆圈直径
    this.circleOutsideDiameter,
    //外圈是否隐藏，默认是不隐藏的
    this.circleOutsideGone = true,
    this.circleColor,
    this.circleOutsideColor,
  }) : assert(circleDiameter != null),
        assert(circleOutsideDiameter != null),
        super(key: key);

  @override
  Widget build(BuildContext context) {
    //circleOutsideGone = true，默认是不隐藏的，当为false是表示显示
    double _circleDiameter;
    double _circleOutsideDiameter;
    //如果外部开发者设置内圈直径大于外圈，那么就调换一下
    if (circleDiameter > circleOutsideDiameter){
      _circleDiameter = circleOutsideDiameter;
      _circleOutsideDiameter = circleDiameter;
    } else {
      _circleDiameter = circleDiameter;
      _circleOutsideDiameter = circleOutsideDiameter;
    }
    //如果外圈显示，则sizeBox是外圈宽高，否则是内圈宽高
    return SizedBox(
      width: circleOutsideGone ? _circleDiameter : _circleOutsideDiameter,
      height: circleOutsideGone ? _circleDiameter : _circleOutsideDiameter,
      child: Stack(
        alignment: AlignmentDirectional.center,
        children: [
          //外圈
          Offstage(
            //是否显示
            offstage: circleOutsideGone,
            child: Container(
              alignment: Alignment.center,
              width: _circleOutsideDiameter,
              height: _circleOutsideDiameter,
              decoration: BoxDecoration(
                color: circleOutsideColor,
                borderRadius: BorderRadius.all(
                  Radius.circular(_circleOutsideDiameter/2),
                ),
              ),
            ),
          ),

          //内圈
          Container(
            alignment: Alignment.center,
            width: _circleDiameter,
            height: _circleDiameter,
            decoration: BoxDecoration(
              color: circleColor,
              borderRadius: BorderRadius.all(
                Radius.circular(_circleDiameter/2),
              ),
            ),
          ),
        ],
      ),
    );
  }

}