


import 'package:flutter/material.dart';

class CustomScrollViewPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new CustomScrollViewState();
  }

}

class CustomScrollViewState extends State<CustomScrollViewPage>{
  int type = 1;

  @override
  Widget build(BuildContext context) {
    Widget body;
    if (type == 1){
      body = getWidget1();
    } else if(type == 2){
      body = getWidget1();
    }else {
      body = getWidget1();
    }

    return new Scaffold(
      appBar: new AppBar(
        title: new Text("CustomScrollView"),
      ),
      body: body,
    );
  }

  void onFabClick(){
    if(type>2){
      type = 0;
    }
    setState(() {
      type = type + 1;
    });
  }

  Widget getWidget1(){
    var customScrollViewTestRoute = new CustomScrollViewTestRoute();
    return customScrollViewTestRoute;
  }

}

class CustomScrollViewTestRoute extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    //因为本路由没有使用Scaffold，为了让子级Widget(如Text)使用
    //Material Design 默认的样式风格,我们使用Material作为本路由的根。
    return Material(
      child: CustomScrollView(
        slivers: <Widget>[
          //AppBar，包含一个导航栏
          SliverAppBar(
            pinned: true,
            expandedHeight: 250.0,
            flexibleSpace: FlexibleSpaceBar(
              title: const Text('Demo'),
              background: Image.asset(
                "./images/avatar.png", fit: BoxFit.cover,),
            ),
          ),

          SliverPadding(
            padding: const EdgeInsets.all(8.0),
            sliver: new SliverGrid( //Grid
              gridDelegate: new SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 2, //Grid按两列显示
                mainAxisSpacing: 10.0,
                crossAxisSpacing: 10.0,
                childAspectRatio: 4.0,
              ),
              delegate: new SliverChildBuilderDelegate(
                    (BuildContext context, int index) {
                  //创建子widget
                  return new Container(
                    alignment: Alignment.center,
                    color: Colors.cyan[100 * (index % 9)],
                    child: new Text('grid item $index'),
                  );
                },
                childCount: 20,
              ),
            ),
          ),
          //List
          new SliverFixedExtentList(
            itemExtent: 50.0,
            delegate: new SliverChildBuilderDelegate(
                    (BuildContext context, int index) {
                  //创建列表项
                  return new Container(
                    alignment: Alignment.center,
                    color: Colors.lightBlue[100 * (index % 9)],
                    child: new Text('list item $index'),
                  );
                },
                childCount: 50 //50个列表项
            ),
          ),
        ],
      ),
    );
  }
}
