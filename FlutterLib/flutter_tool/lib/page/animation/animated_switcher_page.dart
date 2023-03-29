


import 'package:flutter/material.dart';

class AnimatedSwitcherPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new AnimatedSwitcherState();
  }

}

class AnimatedSwitcherState extends State<AnimatedSwitcherPage>{
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("通用“动画切换”组件"),
      ),
      body: new Center(
        child: new ListView(
          children: [
            new Text(
              "这个是一个 AnimatedSwitcher 动画",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            new Text("AnimatedSwitcher 可以同时对其新、旧子元素添加显示、隐藏动画。"),
            new Container(
              height: 200,
              child: new AnimatedSwitcherCounterRoute(),
            ),

            new Text(
              "这个是一个 StreamBuilder",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            new Text("介绍"),



          ],
        ),
      ),
    );
  }
}

class AnimatedSwitcherCounterRoute extends StatefulWidget {

  const AnimatedSwitcherCounterRoute({Key key}) : super(key: key);

  @override
  _AnimatedSwitcherCounterRouteState createState() => _AnimatedSwitcherCounterRouteState();
}

class _AnimatedSwitcherCounterRouteState extends State<AnimatedSwitcherCounterRoute> {
  int _count = 0;

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          AnimatedSwitcher(
            // 新child显示动画时长
            duration: const Duration(milliseconds: 500),
            // 旧child隐藏的动画时长
            reverseDuration: new Duration(milliseconds: 200),
            // 动画构建器
            transitionBuilder: (Widget child, Animation<double> animation) {
              //执行缩放动画
              return ScaleTransition(child: child, scale: animation);
            },
            child: Text(
              '$_count',
              //显示指定key，不同的key会被认为是不同的Text，这样才能执行动画
              key: ValueKey<int>(_count),
              style: Theme.of(context).textTheme.headline4,
            ),
          ),
          RaisedButton(
            child: const Text('+1',),
            onPressed: () {
              setState(() {
                _count += 1;
              });
            },
          ),
        ],
      ),
    );
  }
}