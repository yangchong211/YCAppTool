


import 'package:flutter/material.dart';

class ScrollListenerPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new ScrollListenerState();
  }

}

class ScrollListenerState extends State<ScrollListenerPage>{

  int type = 1;

  @override
  Widget build(BuildContext context) {
    Widget body;
    if (type == 1){
      body = getWidget1();
    } else if(type == 2){
      body = getWidget2();
    }else {
      body = getWidget1();
    }

    return new Scaffold(
      appBar: new AppBar(
        title: new Text("滚动组件监听"),
        actions: <Widget>[
          IconButton(icon: Icon(Icons.message),
            onPressed: () {
              _onFabClick();
            }
          ),
          new Text("切换"+type.toString()),
        ]
      ),
      body: body,
      // floatingActionButton: new FloatingActionButton(
      //   //点击
      //   onPressed: _onFabClick,
      //   tooltip: 'Increment',
      //   child: new Text("切换"+type.toString()),
      // ), //
    );
  }

  void _onFabClick(){
    if(type>2){
      type = 0;
    }
    setState(() {
      type = type + 1;
    });
  }

  Widget getWidget1(){
    var scrollControllerTestRoute = new ScrollControllerTestRoute();
    return scrollControllerTestRoute;
  }

  Widget getWidget2(){
    var scrollNotificationTestRoute = new ScrollNotificationTestRoute();
    return scrollNotificationTestRoute;
  }


}

class ScrollControllerTestRoute extends StatefulWidget {
  @override
  ScrollControllerTestRouteState createState() {
    return new ScrollControllerTestRouteState();
  }
}

class ScrollControllerTestRouteState extends State<ScrollControllerTestRoute> {
  ScrollController _controller = new ScrollController();
  bool showToTopBtn = false; //是否显示“返回到顶部”按钮

  @override
  void initState() {
    super.initState();
    //监听滚动事件，打印滚动位置
    _controller.addListener(() {
      print(_controller.offset); //打印滚动位置
      if (_controller.offset < 1000 && showToTopBtn) {
        setState(() {
          showToTopBtn = false;
        });
      } else if (_controller.offset >= 1000 && showToTopBtn == false) {
        setState(() {
          showToTopBtn = true;
        });
      }
    });
  }

  @override
  void dispose() {
    //为了避免内存泄露，需要调用_controller.dispose
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Scrollbar(
        child: ListView.builder(
            itemCount: 100,
            itemExtent: 50.0, //列表项高度固定时，显式指定高度是一个好习惯(性能消耗小)
            controller: _controller,
            itemBuilder: (context, index) {
              return ListTile(title: Text("yangchong"+"$index"),);
            }
        ),
      ),
      floatingActionButton: !showToTopBtn ? null : FloatingActionButton(
          child: Icon(Icons.arrow_upward),
          onPressed: () {
            //返回到顶部时执行动画
            _controller.animateTo(.0,
                duration: Duration(milliseconds: 200),
                curve: Curves.ease
            );
          }
      ),
    );
  }
}


class ScrollNotificationTestRoute extends StatefulWidget {
  @override
  _ScrollNotificationTestRouteState createState() =>
      new _ScrollNotificationTestRouteState();
}

class _ScrollNotificationTestRouteState extends State<ScrollNotificationTestRoute> {
  String _progress = "0%"; //保存进度百分比

  @override
  Widget build(BuildContext context) {
    return Scrollbar( //进度条
      // 监听滚动通知
      child: NotificationListener<ScrollNotification>(
        // ignore: missing_return
        onNotification: (ScrollNotification notification) {
          double progress = notification.metrics.pixels /
              notification.metrics.maxScrollExtent;
          //重新构建
          setState(() {
            _progress = "${(progress * 100).toInt()}%";
          });
          print("BottomEdge: ${notification.metrics.extentAfter == 0}");
          //return true; //放开此行注释后，进度条将失效
        },
        child: Stack(
          alignment: Alignment.center,
          children: <Widget>[
            ListView.builder(
                itemCount: 100,
                itemExtent: 50.0,
                itemBuilder: (context, index) {
                  return ListTile(title: Text("监听滚动通知"+"$index"));
                }
            ),
            CircleAvatar(  //显示进度百分比
              radius: 30.0,
              child: Text(_progress),
              backgroundColor: Colors.black54,
            )
          ],
        ),
      ),
    );
  }
}