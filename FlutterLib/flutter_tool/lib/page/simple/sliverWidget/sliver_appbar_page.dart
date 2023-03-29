import 'package:flutter/material.dart';

class SliverAppBarPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => new NestedScrollViewPageState();
}

class NestedScrollViewPageState extends State with TickerProviderStateMixin {
//  https://blog.csdn.net/yumi0629/article/details/83305627
//
//  https://blog.csdn.net/yumi0629/article/details/83305627
//
//  https://gitee.com/yumi0629/FlutterUI/tree/master/lib/sliver
  TabController _tabController;

  @override
  void initState() {
    super.initState();
    _tabController = new TabController(length: 3, vsync: this);
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
        body: NestedScrollView(
            headerSliverBuilder:
                (BuildContext context, bool innerBoxIsScrolled) {
              return <Widget>[
                SliverAppBar(
                  title: Text("dfasfdsafd"),
                  expandedHeight: 300.0,
                  floating: false,
                  //滑动到最上面，再滑动是否隐藏导航栏的文字和标题等的具体内容，为true是隐藏，为false是不隐藏
                  pinned: true,
                  //是否固定导航栏，为true是固定，为false是不固定，往上滑，导航栏可以隐藏
                  snap: false,
                  //只跟floating相对应，如果为true，floating必须为true，也就是向下滑动一点儿，整个大背景就会动画显示全部，往上滑动整个导航栏的内容就会消失
                  flexibleSpace: FlexibleSpaceBar(
                    centerTitle: true,
//                    title: Text("我是一个帅气的标题",
//                        style: TextStyle(
//                          color: Colors.white,
//                          fontSize: 16.0,
//                        )),
                    background: Image.network(
                      "https://avatar.csdn.net/6/0/6/3_xieluoxixi.jpg",
                      fit: BoxFit.fill,
                    ),
                    collapseMode: CollapseMode.pin,
                  ),
                  bottom: TabBar(tabs: [
                    Tab(
                      text: "Item",
                      icon: Icon(Icons.message),
                    ),
                    Text("ddd"),
                    Text("ddd"),
                  ], controller: _tabController),
                ),
              ];
            },
            body: new Center(
              child: ListView.builder(
                itemBuilder: (BuildContext context, int index) {
                  return ListTile(
                    leading: Icon(Icons.access_alarms),
                    title: Text("Item -- $index"),
                  );
                },
                itemCount: 20,
              ),
            )));
  }
}
