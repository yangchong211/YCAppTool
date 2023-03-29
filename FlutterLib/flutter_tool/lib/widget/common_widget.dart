// import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

/**
 * 封装CommonWidget的思想类似java里的构造者模式，可以让widget直接实现链式调用
 */
class CommonWidget {
  Widget _widget;


  Function _onTapFunc;
  Function _onDoubleTapFunc;
  Function _onLongPressFunc;

  CommonWidget(Widget widget) {
    this._widget = widget;
  }



  ///add padding属性
  CommonWidget padding(
      {Key key, double left = 0.0, double top = 0.0, double right = 0.0, double bottom = 0.0}) {
    var padding = EdgeInsets.only(left: left, top: top, right: right, bottom: bottom);
    _widget = new Padding(key: key, padding: padding, child: _widget);
    return this;
  }

  ///增加padingall
  CommonWidget paddAll({Key key, double all = 0.0}) {
    var padding = EdgeInsets.all(all);
    _widget = new Padding(key: key, padding: padding, child: _widget);
    return this;
  }

  ///增加align 当前布局相对位置
  ///FractionalOffset.centerRight
  CommonWidget align({Key key, AlignmentGeometry alignment = Alignment.center}) {
    _widget = new Align(key: key, alignment: alignment, child: _widget);
    return this;
  }

  ///位置
  CommonWidget positioned(
      {Key key,
        double left,
        double top,
        double right,
        double bottom,
        double width,
        double height}) {
    _widget = new Positioned(
        key: key,
        left: left,
        top: top,
        right: right,
        bottom: bottom,
        width: width,
        height: height,
        child: _widget);
    return this;
  }

  ///stack 相当于frameLayout布局

  ///填充布局
  CommonWidget expanded({Key key, int flex = 1}) {
    _widget = new Expanded(key: key, flex: flex, child: _widget);
    return this;
  }

  ///是否显示布局 true为不显示 false为显示
  CommonWidget offstage({Key key, bool offstage = true}) {
    _widget = new Offstage(key: key, offstage: offstage, child: _widget);
    return this;
  }

  ///透明度 0 是完全透明 1 完全不透明
  CommonWidget opacity({Key key, @required double opacity, alwaysIncludeSemantics = false}) {
    _widget = new Opacity(
        key: key, opacity: opacity, alwaysIncludeSemantics: alwaysIncludeSemantics, child: _widget);
    return this;
  }

  ///基准线布局
  CommonWidget baseline({
    Key key,
    @required double baseline,
    @required TextBaseline baselineType,
  }) {
    _widget =
    new Baseline(key: key, baseline: baseline, baselineType: baselineType, child: _widget);
    return this;
  }

  ///设置宽高比
  CommonWidget aspectRatio({Key key, @required double aspectRatio}) {
    _widget = new AspectRatio(key: key, aspectRatio: aspectRatio, child: _widget);
    return this;
  }

  ///矩阵转换
  CommonWidget transform({
    Key key,
    @required Matrix4 transform,
    origin,
    alignment,
    transformHitTests = true,
  }) {
    _widget = new Transform(
        key: key,
        transform: transform,
        origin: origin,
        alignment: alignment,
        transformHitTests: transformHitTests,
        child: _widget);
    return this;
  }

  ///居中 todo: center
  CommonWidget center({Key key, double widthFactor, double heightFactor}) {
    _widget =
    new Center(key: key, widthFactor: widthFactor, heightFactor: heightFactor, child: _widget);
    return this;
  }

  ///布局容器
  CommonWidget container({
    Key key,
    alignment,
    padding,
    Color color,
    Decoration decoration,
    foregroundDecoration,
    double width,
    double height,
    BoxConstraints constraints,
    margin,
    transform,
  }) {
    _widget = new Container(
        key: key,
        alignment: alignment,
        padding: padding,
        color: color,
        decoration: decoration,
        foregroundDecoration: foregroundDecoration,
        width: width,
        height: height,
        constraints: constraints,
        margin: margin,
        transform: transform,
        child: _widget);
    return this;
  }

  ///设置具体尺寸
  CommonWidget sizedBox({Key key, double width, double height}) {
    _widget = new SizedBox(key: key, width: width, height: height, child: _widget);
    return this;
  }

  ///设置最大最小宽高布局
  CommonWidget constrainedBox({
    Key key,
    minWidth = 0.0,
    maxWidth = double.infinity,
    minHeight = 0.0,
    maxHeight = double.infinity,
  }) {
    BoxConstraints constraints = new BoxConstraints(
        minWidth: minWidth, maxWidth: maxWidth, minHeight: minHeight, maxHeight: maxHeight);
    _widget = new ConstrainedBox(key: key, constraints: constraints, child: _widget);
    return this;
  }

  ///限定最大宽高布局
  CommonWidget limitedBox({
    Key key,
    maxWidth = double.infinity,
    maxHeight = double.infinity,
  }) {
    _widget = new LimitedBox(key: key, maxWidth: maxWidth, maxHeight: maxHeight, child: _widget);
    return this;
  }

  ///百分比布局
  CommonWidget fractionallySizedBox(
      {Key key, alignment = Alignment.center, double widthFactor, double heightFactor}) {
    _widget = new FractionallySizedBox(
        key: key,
        alignment: alignment,
        widthFactor: widthFactor,
        heightFactor: heightFactor,
        child: _widget);
    return this;
  }

  ///缩放布局
  CommonWidget fittedBox({Key key, fit = BoxFit.contain, alignment = Alignment.center}) {
    _widget = new FittedBox(key: key, fit: fit, alignment: alignment, child: _widget);
    return this;
  }

  ///旋转盒子 1次是90度
  CommonWidget rotatedBox({
    Key key,
    @required int quarterTurns,
  }) {
    _widget = new RotatedBox(key: key, quarterTurns: quarterTurns, child: _widget);
    return this;
  }

  ///装饰盒子 细节往外抛 decoration 编写放在外面
  CommonWidget decoratedBox({
    Key key,
    @required Decoration decoration,
    position = DecorationPosition.background,
  }) {
    _widget =
    new DecoratedBox(key: key, decoration: decoration, position: position, child: _widget);
    return this;
  }

  ///圆形剪裁
  CommonWidget clipOval(
      {Key key, CustomClipper<Rect> clipper, Clip clipBehavior = Clip.antiAlias}) {
    _widget = new ClipOval(key: key, clipper: clipper, clipBehavior: clipBehavior, child: _widget);
    return this;
  }

  ///圆角矩形剪裁
  CommonWidget clipRRect(
      {Key key,
        @required BorderRadius borderRadius,
        CustomClipper<RRect> clipper,
        Clip clipBehavior = Clip.antiAlias}) {
    _widget = new ClipRRect(
        key: key,
        borderRadius: borderRadius,
        clipper: clipper,
        clipBehavior: clipBehavior,
        child: _widget);
    return this;
  }

  ///矩形剪裁  todo: 需要自定义clipper 否则无效果
  CommonWidget clipRect(
      {Key key, @required CustomClipper<Rect> clipper, Clip clipBehavior = Clip.hardEdge}) {
    _widget = new ClipRect(key: key, clipper: clipper, clipBehavior: clipBehavior, child: _widget);
    return this;
  }

  ///路径剪裁  todo: 需要自定义clipper 否则无效果
  CommonWidget clipPath(
      {Key key, @required CustomClipper<Path> clipper, Clip clipBehavior = Clip.antiAlias}) {
    _widget = new ClipPath(key: key, clipper: clipper, clipBehavior: clipBehavior, child: _widget);
    return this;
  }

  ///animatedOpacity 淡入淡出
  CommonWidget animatedOpacity({
    Key key,
    @required double opacity,
    Curve curve = Curves.linear,
    @required Duration duration,
  }) {
    _widget = new AnimatedOpacity(
        key: key, opacity: opacity, curve: curve, duration: duration, child: _widget);
    return this;
  }

  ///页面简单切换效果
  CommonWidget hero({Key key, @required Object tag}) {
    _widget = new Hero(key: key, tag: tag, child: _widget);
    return this;
  }

  ///点击事件
  CommonWidget onClick({Key key, onTap, onDoubleTap, onLongPress}) {
    _widget = new GestureDetector(
      key: key,
      child: _widget,
      onTap: onTap ?? _onTapFunc,
      onDoubleTap: onDoubleTap ?? _onDoubleTapFunc,
      onLongPress: onLongPress ?? _onLongPressFunc,
    );
    return this;
  }

  ///添加点击事件
  CommonWidget onTap(Function func, {Key key}) {
    _onTapFunc = func;
    _widget = new GestureDetector(
      key: key,
      child: _widget,
      onTap: _onTapFunc,
      onDoubleTap: _onDoubleTapFunc,
      onLongPress: _onLongPressFunc,
    );
    return this;
  }

  ///双击
  CommonWidget onDoubleTap(Function func, {Key key}) {
    _onDoubleTapFunc = func;
    _widget = new GestureDetector(
      key: key,
      child: _widget,
      onTap: _onTapFunc,
      onDoubleTap: _onDoubleTapFunc,
      onLongPress: _onLongPressFunc,
    );
    return this;
  }

  ///长按
  CommonWidget onLongPress(Function func, {Key key}) {
    _onLongPressFunc = func;
    _widget = new GestureDetector(
      key: key,
      child: _widget,
      onTap: _onTapFunc,
      onDoubleTap: _onDoubleTapFunc,
      onLongPress: _onLongPressFunc,
    );
    return this;
  }

  Widget build() {
    return _widget;
  }
}

