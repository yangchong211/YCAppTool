import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

/// 骨架屏组件
/// 用法:
///   1. 继承Component
///   2. 实现buildSkeletonContent()和skeletonAnimationWidgetNum()方法
///   3. 将通过skeletonAnimationOpacity(第index个执行动效)方法获取opacity， 赋值给SkeletonAnimationWidget.opacity


///使用案例
class SkeletonPage extends SkeletonAnimationComponent {
  @override
  Widget buildSkeletonContent(BuildContext context) {
    return SingleChildScrollView(
      physics: NeverScrollableScrollPhysics(),
      child: Container(
        color: Color(int.parse("#E5E5E8")),
        child: Column(children: [
          SkeletonAnimationWidget(
            color: skeletonGrayColor,
            opacity: skeletonAnimationOpacity(0),
            height: 30,
          )
        ]),
      ),
    );
  }

  @override
  int skeletonAnimationWidgetNum() {
    return 1;
  }
}

///骨架屏封装
abstract class SkeletonAnimationComponent extends StatefulWidget {
  
  final Color skeletonGrayColor = hexToColor('#E5E5E8');
  final Color skeletonBackgroundColor = hexToColor('#F9F9FA');
  List<double> animationOpacityList = []; // opacity数组

  SkeletonAnimationComponent({
    Key key,
  }) : super(key: key);

  /// 设置执行动效的SkeletonAnimationWidget的数量
  int skeletonAnimationWidgetNum();

  /// 创建骨架屏UI
  Widget buildSkeletonContent(BuildContext context);

  /// 组件销毁
  void component_dispose() => {};

  /// 获取当前显示的opacity
  double skeletonAnimationOpacity(int cardIndex) {
    if (animationOpacityList.isNotEmpty && cardIndex < animationOpacityList.length) {
      return animationOpacityList[cardIndex];
    } else {
      return 1.0;
    }
  }

  ///字符串转换成color
  static Color hexToColor(String color, {Color defaultColor}) {
    if (color == null ||
        color.length != 7 ||
        int.tryParse(color.substring(1, 7), radix: 16) == null) {
      return defaultColor;
    }
    return Color(int.parse(color.substring(1, 7), radix: 16) + 0xFF000000);
  }

  @override
  _SkeletonAnimationComponentState createState() => _SkeletonAnimationComponentState();
}

class _SkeletonAnimationComponentState extends State<SkeletonAnimationComponent> {
  // 倒计时相关
  Timer _animationTimer;

  /// opacity源数组。数组中元素的顺序就是SkeletonAnimationWidget的opacity变化的顺序
  final List<double> _opacityList = [0.6, 0.85, 1.0, 1.0, 0.85, 0.6];

  /// opacity动画数组
  int _animWidgetNum = 0;

  @override
  void initState() {
    super.initState();
    _animWidgetNum = widget.skeletonAnimationWidgetNum();

    /// 计算每次setState需要的opacity
    var head = 0;
    _animationTimer = Timer.periodic(Duration(milliseconds: 150), (timer) {
      try {
        // 清空上一次动画数组
        widget.animationOpacityList.clear();

        for (var i = 0; i < _animWidgetNum; i++) {
          widget.animationOpacityList.add(0.4);
        }

        var start = head;
        if (head >= _animWidgetNum) {
          start = _animWidgetNum - 1;
        }

        setState(() {
          // 获取新的动画数组，刷新UI
          for (var i = start; i >= 0 && head - i < _opacityList.length; i--) {
            var removeIndex = i > (widget.animationOpacityList.length - 1)
                ? (widget.animationOpacityList.length - 1)
                : i;
            widget.animationOpacityList.removeAt(removeIndex);
            widget.animationOpacityList.insert(removeIndex, _opacityList[head - i]);
          }
        });

        head = head + 1;
        if (head - _opacityList.length > _animWidgetNum - 1) {
          head = 0;
        }
      } catch (e) {
        print('skeleton component opacity error:[info:$e]');
        _destroyTimer();
        setState(() {});
      }
    });
  }

  @override
  void dispose() {
    _destroyTimer();
    super.dispose();
    widget.component_dispose();
  }

  /// 销毁定时器
  void _destroyTimer() {
    _animationTimer?.cancel();
    _animationTimer = null;
  }

  @override
  Widget build(BuildContext context) => widget.buildSkeletonContent(context);
  
}

/// Skeleton动效执行组件
/// * 提示：执行skeleton动效的Widget需要用此组件包裹，并且将skeletonAnimationOpacity(index)的值传给opacity属性
class SkeletonAnimationWidget extends StatelessWidget {
  
  final Color color;
  final double width;
  final double height;
  final EdgeInsetsGeometry padding;
  final Widget child;
  final double opacity;

  SkeletonAnimationWidget({this.child, this.color, this.width, 
    this.height, this.padding, this.opacity});

  @override
  Widget build(BuildContext context) {
    return Opacity(
      opacity: opacity ?? 1.0,
      child: Container(
        color: color,
        width: width,
        height: height,
        padding: padding,
        child: child,
      ),
    );
  }
}
