import 'package:flutter/material.dart';

class ColunmLayoutPage extends StatelessWidget {

  /// textDirection：表示水平方向子组件的布局顺序(是从左往右还是从右往左)，默认为系统当前Locale环境的文本方向(如中文、英语都是从左往右，而阿拉伯语是从右往左)。
  /// mainAxisSize：表示Row在主轴(水平)方向占用的空间，默认是MainAxisSize.max，表示尽可能多的占用水平方向的空间，此时无论子 widgets 实际占用多少水平空间，Row的宽度始终等于水平方向的最大宽度；而MainAxisSize.min表示尽可能少的占用水平空间，当子组件没有占满水平剩余空间，则Row的实际宽度等于所有子组件占用的的水平空间；
  /// mainAxisAlignment：表示子组件在Row所占用的水平空间内对齐方式，如果mainAxisSize值为MainAxisSize.min，则此属性无意义，因为子组件的宽度等于Row的宽度。只有当mainAxisSize的值为MainAxisSize.max时，此属性才有意义，MainAxisAlignment.start表示沿textDirection的初始方向对齐，如textDirection取值为TextDirection.ltr时，则MainAxisAlignment.start表示左对齐，textDirection取值为TextDirection.rtl时表示从右对齐。而MainAxisAlignment.end和MainAxisAlignment.start正好相反；MainAxisAlignment.center表示居中对齐。读者可以这么理解：textDirection是mainAxisAlignment的参考系。
  /// verticalDirection：表示Row纵轴（垂直）的对齐方向，默认是VerticalDirection.down，表示从上到下。
  /// crossAxisAlignment：表示子组件在纵轴方向的对齐方式，Row的高度等于子组件中最高的子元素高度，它的取值和MainAxisAlignment一样(包含start、end、 center三个值)，不同的是crossAxisAlignment的参考系是verticalDirection，即verticalDirection值为VerticalDirection.down时crossAxisAlignment.start指顶部对齐，verticalDirection值为VerticalDirection.up时，crossAxisAlignment.start指底部对齐；而crossAxisAlignment.end和crossAxisAlignment.start正好相反；
  /// children ：子组件数组。

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("水平方向布局"),
      ),

      //布局方向  Row:水平布局 Column：垂直布局
      body: new Column(
        //子组件沿着 Cross 轴（在 Row 中是纵轴）如何摆放，其实就是子组件对齐方式，可选值有：
        //CrossAxisAlignment.start：子组件在 Row 中顶部对齐
        //CrossAxisAlignment.end：子组件在 Row 中底部对齐
        //CrossAxisAlignment.center：子组件在 Row 中居中对齐
        //CrossAxisAlignment.stretch：拉伸填充满父布局
        //CrossAxisAlignment.baseline：在 Row 组件中会报错
        crossAxisAlignment: CrossAxisAlignment.start,
        //子组件沿着 Main 轴（在 Row 中是横轴）如何摆放，其实就是子组件排列方式，可选值有：
        //MainAxisAlignment.start：靠左排列
        //MainAxisAlignment.end：靠右排列
        //MainAxisAlignment.center：居中排列
        //MainAxisAlignment.spaceAround：每个子组件左右间隔相等，也就是 margin 相等
        //MainAxisAlignment.spaceBetween：两端对齐，也就是第一个子组件靠左，最后一个子组件靠右，剩余组件在中间平均分散排列
        //MainAxisAlignment.spaceEvenly：每个子组件平均分散排列，也就是宽度相等
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        //Main 轴大小，可选值有：
        //MainAxisSize.max：相当于 Android 的 match_parent
        //MainAxisSize.min：相当于 Android 的 wrap_content
        mainAxisSize: MainAxisSize.max,
        //不太理解
        // textBaseline: TextBaseline.alphabetic,
        //子组件排列顺序，可选值有：
        //TextDirection.ltr：从左往右开始排列
        //TextDirection.rtl：从右往左开始排列
        textDirection: TextDirection.ltr,
        //确定如何在垂直方向摆放子组件，以及如何解释 start 和 end，指定 height 可以看到效果，可选值有：
        //VerticalDirection.up：Row 从下至上开始摆放子组件，此时我们看到的底部其实是顶部
        //VerticalDirection.down：Row 从上至下开始摆放子组件，此时我们看到的顶部就是顶部
        verticalDirection: VerticalDirection.down,
        children: <Widget>[
          new Text(
            "Colunm跟Row都是一个盛放children widget的array，不同的是一个是在水平方向（horizontal），另一个是竖直方向（vertical）",
            style: new TextStyle(
              fontSize: 15.0,
            ),
            textAlign: TextAlign.center,
          ),
          new RaisedButton(
            onPressed: () {
              print('点击红色按钮');
            },
            color: const Color(0xffff0000),
            child: new Text('红色按钮'),
          ),
          new RaisedButton(
            onPressed: () {
              print("点击蓝色按钮");
            },
            color: const Color(0xff000099),
            child: new Text('蓝色按钮'),
          ),
          new RaisedButton(
            onPressed: () {
              print("点击粉色按钮");
            },
            color: const Color(0xffee9999),
            child: new Text('粉色按钮'),
          )
        ],
      ),
    );
  }
}




