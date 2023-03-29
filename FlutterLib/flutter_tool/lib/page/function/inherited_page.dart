


import 'package:flutter/material.dart';

class InheritedPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new InheritedState();
  }

}

class InheritedState extends State<InheritedPage>{
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("数据共享（InheritedWidget）"),
      ),
      body: new Center(
        child: new ListView(
          children: [
            new Text("InheritedWidget是Flutter中非常重要的一个功能型组件，"
                "它提供了一种数据在widget树中从上到下传递、共享的方式，"
                "比如我们在应用的根widget中通过InheritedWidget共享了一个数据，"
                "那么我们便可以在任意子widget中来获取该共享的数据！"),
            new Text(
              "数据共享（InheritedWidget）",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            Container(
              color: Colors.blue[50],
              child: new InheritedWidgetTestRoute(),
            ),
          ],
        ),
      ),
    );
  }

}

class ShareDataWidget extends InheritedWidget {

  ShareDataWidget({
    @required this.data,
    Widget child
  }) :super(child: child);

  final int data; //需要在子树中共享的数据，保存点击次数

  //定义一个便捷方法，方便子树中的widget获取共享数据
  static ShareDataWidget of(BuildContext context) {
    return context.dependOnInheritedWidgetOfExactType<ShareDataWidget>();
  }

  //该回调决定当data发生变化时，是否通知子树中依赖data的Widget
  @override
  bool updateShouldNotify(ShareDataWidget old) {
    //如果返回true，则子树中依赖(build函数中有调用)本widget
    //的子widget的`state.didChangeDependencies`会被调用
    return old.data != data;
  }
}

class _TestWidget extends StatefulWidget {
  @override
  __TestWidgetState createState() => new __TestWidgetState();
}

class __TestWidgetState extends State<_TestWidget> {
  @override
  Widget build(BuildContext context) {
    //使用InheritedWidget中的共享数据
    var string = ShareDataWidget
        .of(context)
        .data
        .toString();
    return Text(string);
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    //父或祖先widget中的InheritedWidget改变(updateShouldNotify返回true)时会被调用。
    //如果build中没有依赖InheritedWidget，则此回调不会被调用。
    print("Dependencies change");
  }
}

class InheritedWidgetTestRoute extends StatefulWidget {
  @override
  _InheritedWidgetTestRouteState createState() => new _InheritedWidgetTestRouteState();
}

class _InheritedWidgetTestRouteState extends State<InheritedWidgetTestRoute> {
  int count = 0;

  @override
  Widget build(BuildContext context) {
    return  Center(
      //使用ShareDataWidget

      child: ShareDataWidget(
        data: count,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Padding(
              padding: const EdgeInsets.only(bottom: 20.0),
              child: _TestWidget(),//子widget中依赖ShareDataWidget
            ),
            RaisedButton(
              child: Text("Increment"),
              //每点击一次，将count自增，然后重新build,ShareDataWidget的data将被更新
              onPressed: () => setState(() => ++count),
            )
          ],
        ),
      ),
    );
  }
}
