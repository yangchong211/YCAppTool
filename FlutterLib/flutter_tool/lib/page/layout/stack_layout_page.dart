import 'package:flutter/material.dart';

class StackLayoutPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
        appBar: new AppBar(
          title: new Text('层叠布局'),
        ),
        body: Column(
          children: <Widget>[
            new Text(
              "Stack即层叠布局，跟原生Android里面的FrameLayout如出一辙，能够将子widget层叠排列。"
                  "如果不指定显示位置，默认布局在左上角，如果希望子空间显示在具体的位置，"
                  "我们可以通过Positioned控件包裹子widget，然后根据定位的子控件的top、right、bottom、left属性来将它们放置在Stack的合适位置上",
              style: new TextStyle(
                fontSize: 18.0,
              ),
              textAlign: TextAlign.center,
            ),
            new Center(
              child: new Stack(
                children: <Widget>[
                  new Image.network(
                    'https://avatar.csdn.net/6/0/6/1_xieluoxixi.jpg',
                    scale: 0.5,
                  ),
                  new Positioned(
                      left: 35.0,
                      right: 35.0,
                      top: 45.0,
                      child: new Text(
                        '第二层内容区域',
                        style: new TextStyle(
                          fontSize: 20.0,
                          fontFamily: 'serif',
                        ),
                      )),
                  new Positioned(
                      left: 55.0,
                      right: 55.0,
                      top: 55.0,
                      child: new Text(
                        '第三层 third child',
                        style: new TextStyle(
                          fontSize: 20.0,
                          color: Colors.blue,
                          fontFamily: 'serif',
                        ),
                      ))
                ],
              ),
            ),
            new Stack(
              children: [
                new Align(
                  alignment: new FractionalOffset(0.0, 0.5),
                  child: new Text(
                    '我在左边缘中心',
                    style: new TextStyle(fontSize: 20.0),
                  ),
                ),
                new Align(
                  alignment: new FractionalOffset(1.0, 1.0),
                  child: new Text(
                    '我在右下角',
                    style: new TextStyle(fontSize: 18.0),
                  ),
                ),
              ],
            ),
          ],
        ));
  }
}
