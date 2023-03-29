import 'package:flutter/material.dart';


/// Row 是一个可以沿水平方向展示它的子组件的组件。
/// 它还可以灵活布局，如果要让某个子组件填充满剩余剩余空间，请使用 Expanded 组件包裹该组件即可。
/// Row 组件是不可以滚动的，所以在 Row 组件中一般不会放置过多子组件，如果需要滚动的话应该考虑使用 ListView。

class RowPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
        appBar: new AppBar(title: new Text("Row布局")),
        //布局方向  Row:水平布局 Column：垂直布局
        body: new ListView(
          children: [
            SizedBox(
              height: 50,
            ),
            Row(
              // crossAxisAlignment：子组件沿着 Cross 轴（在 Row 中是纵轴）如何摆放，其实就是子组件对齐方式，可选值有：
              // CrossAxisAlignment.start：子组件在 Row 中顶部对齐。
              // CrossAxisAlignment.end：子组件在 Row 中底部对齐。
              // CrossAxisAlignment.center：子组件在 Row 中居中对齐。
              // CrossAxisAlignment.stretch：拉伸填充满父布局。
              // CrossAxisAlignment.baseline：在 Row 组件中会报错。
              crossAxisAlignment: CrossAxisAlignment.start,
              // mainAxisAlignment：子组件沿着 Main 轴（在 Row 中是横轴）如何摆放，其实就是子组件排列方式，可选值有：
              // MainAxisAlignment.start：靠左排列。
              // MainAxisAlignment.end：靠右排列。
              // MainAxisAlignment.center：居中排列。
              // MainAxisAlignment.spaceAround：每个子组件左右间隔相等，也就是 margin 相等。
              // MainAxisAlignment.spaceBetween：两端对齐，也就是第一个子组件靠左，最后一个子组件靠右，剩余组件在中间平均分散排列。
              // MainAxisAlignment.spaceEvenly：每个子组件平均分散排列，也就是宽度相等。
              mainAxisAlignment: MainAxisAlignment.start,
              // mainAxisSize：Main 轴大小，可选值有：
              // MainAxisSize.max：相当于 Android 的 match_parent。
              // MainAxisSize.min：相当于 Android 的 wrap_content。
              mainAxisSize: MainAxisSize.min,
              // textDirection：子组件排列顺序，可选值有：
              // TextDirection.ltr：从左往右开始排列。
              // TextDirection.rtl：从右往左开始排列。
              textDirection: TextDirection.ltr,
              // verticalDirection：确定如何在垂直方向摆放子组件，以及如何解释 start 和 end，指定 height 可以看到效果，可选值有：
              // VerticalDirection.up：Row 从下至上开始摆放子组件，此时我们看到的底部其实是顶部。
              // VerticalDirection.down：Row 从上至下开始摆放子组件，此时我们看到的顶部就是顶部。
              verticalDirection: VerticalDirection.up,
              children: <Widget>[
                new Text("从网络加载图片1"),
                new Text('从本地加载图片2'),
              ],
            ),
            new Padding(
                padding: EdgeInsets.fromLTRB(10, 10, 10, 5)
            ),
            new Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                new Text("从网络加载图片1"),
                new Text('从本地加载图片2'),
              ],
            ),
            new Padding(
                padding: EdgeInsets.fromLTRB(10, 10, 10, 5)
            ),
            Row(
              mainAxisSize: MainAxisSize.min,
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                Text(" hello world "),
                Text(" I am Jack "),
              ],
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.end,
              textDirection: TextDirection.rtl,
              children: <Widget>[
                Text(" hello world "),
                Text(" I am Jack "),
              ],
            ),
            Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              verticalDirection: VerticalDirection.up,
              children: <Widget>[
                Text(" hello world ", style: TextStyle(fontSize: 30.0),),
                Text(" I am Jack "),
              ],
            ),
          ],
        ));
  }
}
