import 'package:flutter/material.dart';

class ButtonPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("Button Widget"),
      ),
      floatingActionButton: new FloatingActionButton(
          onPressed: () {}, child: new Icon(Icons.adb), tooltip: "点击"),
      body: SingleChildScrollView(
        child: new Column(
          children: <Widget>[
            new Text("简单样式，即漂浮按钮，它默认带有阴影和灰色背景。",
                textAlign: TextAlign.center,
                style: new TextStyle(color: Colors.brown, fontSize: 20.0)),
            new RaisedButton(
                onPressed: () {

                },
                child: new Text("RaisedButton")
            ),
            new Text("扁平按钮，默认背景透明并不带阴影。按下后，会有背景色",
                textAlign: TextAlign.center,
                style: new TextStyle(color: Colors.brown, fontSize: 20.0)),
            new FlatButton(
                onPressed: () {

                },
                child: new Text("FlatButton")
            ),
            new Text("IconButton是一个可点击的Icon，不包括文字，默认没有背景，点击后会出现背景",
                textAlign: TextAlign.center,
                style: new TextStyle(color: Colors.brown, fontSize: 20.0)),
            Container(
              color: Colors.brown,
              child: IconButton(
                padding: EdgeInsets.all(0),
                iconSize: 27,
                color: Colors.red,
                //icon 不可点击颜色，onPressed == null 时生效
                disabledColor: Colors.black,
                //点击时闪过的颜色
                splashColor: Colors.yellow,
                //按下去高亮的颜色
                highlightColor: Colors.green,
                //icon 颜色
                icon: Icon(Icons.thumb_up),
                onPressed: () {

                },
              ),
            ),

            new Text("RaisedButton、FlatButton、OutlineButton都有一个icon 构造函数，通过它可以轻松创建带图标的按钮",
                textAlign: TextAlign.center,
                style: new TextStyle(color: Colors.brown, fontSize: 20.0)),
            RaisedButton.icon(
              icon: Icon(Icons.send),
              label: Text("发送"),
              onPressed: _onPressed,
            ),
            OutlineButton.icon(
              icon: Icon(Icons.add),
              label: Text("添加"),
              onPressed: _onPressed,
            ),
            FlatButton.icon(
              icon: Icon(Icons.info),
              label: Text("详情"),
              onPressed: _onPressed,
            ),
            new MaterialButton(
                onPressed: () {}, child: new Text("MaterialButton")),
            new RawMaterialButton(
                onPressed: () {}, child: new Text("RawMaterialButton")),
            new Text("OutlineButton默认有一个边框，不带阴影且背景透明。按下后，边框颜色会变亮、同时出现背景和阴影(较弱)",
                textAlign: TextAlign.center,
                style: new TextStyle(color: Colors.brown, fontSize: 20.0)),
            FlatButton(
              color: Colors.blue,
              highlightColor: Colors.blue[700],
              colorBrightness: Brightness.dark,
              splashColor: Colors.grey,
              child: Text("Submit"),
              //这个是设置圆角的
              shape:RoundedRectangleBorder(borderRadius: BorderRadius.circular(15.0)),
              onPressed: () {},
            ),

            new OutlineButton(
              onPressed: () {},
              child: new Text("OutlineButton 按钮1"),
            ),
            new Text("自定义按钮外观",
                textAlign: TextAlign.center,
                style: new TextStyle(color: Colors.brown, fontSize: 20.0)),
            new SizedBox(height: 20),
            new Text("升级样式",
                textAlign: TextAlign.center,
                style: new TextStyle(color: Colors.brown, fontSize: 20.0)),
            new RaisedButton(
              color: Colors.blueAccent,
              //按钮的背景颜色
              padding: EdgeInsets.all(15.0),
              //按钮距离里面内容的内边距
              textColor: Colors.white,
              //文字的颜色
              textTheme: ButtonTextTheme.normal,
              //按钮的主题
              onHighlightChanged: (bool b) {
                //水波纹高亮变化回调
              },
              disabledTextColor: Colors.black54,
              //按钮禁用时候文字的颜色
              disabledColor: Colors.black54,
              //按钮被禁用的时候显示的颜色
              highlightColor: Colors.green,
              //点击或者toch控件高亮的时候显示在控件上面，水波纹下面的颜色
              splashColor: Colors.amberAccent,
              //水波纹的颜色
              colorBrightness: Brightness.light,
              //按钮主题高亮
              elevation: 10.0,
              //按钮下面的阴影
              highlightElevation: 10.0,
              //高亮时候的阴影
              disabledElevation: 10.0,
              //按下的时候的阴影
              shape: new RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(20.0)),
              //设置形状
              onPressed: () {},
              child: new Text("RaisedButton"),
            ),
            new FlatButton(
                color: Colors.lightGreen,
                textColor: Colors.red,
                onPressed: () {}, child: new Text("FlatButton")),
            new OutlineButton(
                textColor: Colors.blue,
                highlightedBorderColor: Colors.deepOrange,
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(20.0)
                ),
                borderSide: new BorderSide(
                  color: Colors.red,
                  width: 1,
                ),
                onPressed: () {},
                child: new Text("OutlineButton 按钮2")
            ),
            MaterialButton(
                color: Colors.yellow,
                textColor: Colors.red,
                onPressed: () {},
                child: new Text("MaterialButton")
            ),
            RawMaterialButton(
                fillColor: Colors.deepOrange,
                onPressed: () {},
                child: new Text("RawMaterialButton",style: new TextStyle(color: Colors.white),)
            )
          ],
        ),
      ),
    );
  }

  _onPressed(){

  }
}
