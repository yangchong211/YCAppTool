import 'package:flutter/material.dart';

class ScaffoldPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => new PageState();
}

class PageState extends State<ScaffoldPage>
    with SingleTickerProviderStateMixin {
  int _currentBottomIndex = 0; //底部tab索引
  //顶部Tab
  TabController _tabController;
  List<String> topTabLists = ["Tab 1", "Tab 2", "Tab 3"];

  @override
  void initState() {
    super.initState();
    //初始化顶部TabController
    _tabController = TabController(length: topTabLists.length, vsync: this);
//    _tabController.addListener(() {
//      switch (_tabController.index) {
//        case 0:
//          print("----11");
//          break;
//        case 1:
//          print("-----2211");
//          break;
//        case 2:
//          print("----333");
//          break;
//      }
//    }
//    );
  }

  void _onBottomTabChange(int index) {
    setState(() {
      _currentBottomIndex = index;
    });
  }

  void _onFabClick() {
    print("------------ FloatingActionButton Click ------------");
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
//          leading: Icon(Icons.account_balance),  //添加leading之后需要重写点击事件唤起抽屉菜单
          title: Text("Scaffold 脚手架"),
          centerTitle: true,
          actions: <Widget>[
            IconButton(icon: Icon(Icons.message), onPressed: () {}),
            PopupMenuButton(
                onSelected: (String value) {
                  print('-----------------$value');
                },
                itemBuilder: (BuildContext context) => [
                      new PopupMenuItem(
                          value: "选项一的内容", child: new Text("选项一")),
                      new PopupMenuItem(
                          value: "选项二的内容", child: new Text("选项二")),
                      new PopupMenuItem(
                          value: "选项三的内容", child: new Text("选项三")),
                    ])
          ],
          bottom: TabBar(
            controller: _tabController,
            tabs: topTabLists
                .map((element) => Tab(
                      text: element,
                      icon: Icon(Icons.print),
                    ))
                .toList(),
//            onTap: (index) => {},
          )),
      drawer: MyDrawer(),
      body: TabBarView(
          controller: _tabController,
          children: topTabLists.map((item) {
            return Container(
              alignment: Alignment.center,
              child: Text(item),
            );
          }).toList()),
      // bottomNavigationBar: BottomNavigationBar(
      //   //不设置该属性多于三个不显示颜色
      //   type: BottomNavigationBarType.fixed,
      //   items: [
      //     BottomNavigationBarItem(icon: Icon(Icons.home), title: Text("首页")),
      //     BottomNavigationBarItem(icon: Icon(Icons.message), title: Text("消息")),
      //     BottomNavigationBarItem(icon: Icon(Icons.add_a_photo), title: Text("动态")),
      //     BottomNavigationBarItem(icon: Icon(Icons.person), title: Text("我的"))
      //   ],
      //   currentIndex: _currentBottomIndex,
      //   fixedColor: Colors.blue,
      //   onTap: (index) => _onBottomTabChange(index),
      // ),

//      //与FloatingActionButton配合实现"打洞"效果
     bottomNavigationBar: BottomAppBar(
       color: Colors.white,
       // 底部导航栏打一个圆形的洞
       shape: CircularNotchedRectangle(),
       child: Row(
         children: [
           Tab(text: "首页", icon: Icon(Icons.home)),
           Tab(text: "消息", icon: Icon(Icons.message)),
           Tab(text: "动态", icon: Icon(Icons.add_a_photo)),
           Tab(text: "我的", icon: Icon(Icons.person)),
         ],
         mainAxisAlignment: MainAxisAlignment.spaceAround, //均分底部导航栏横向空间
       ),
     ),
      floatingActionButton: new FloatingActionButton(
        //点击
        onPressed: _onFabClick,
        tooltip: 'Increment',
        child: new Icon(Icons.add),
      ), // Th
      floatingActionButtonLocation:
          FloatingActionButtonLocation.centerDocked, //设置FloatingActionButton的位置
    );
  }
}

class MyDrawer extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Drawer(
        child: Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        Padding(
          padding: const EdgeInsets.only(top: 88.0, bottom: 30.0),
          child: Row(
            children: <Widget>[
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 16.0),
                child: ClipOval(
                  child: Image.network(
                    "https://avatar.csdn.net/6/0/6/3_xieluoxixi.jpg",
                    width: 60,
                  ),
                ),
              ),
              Text(
                "杨充逗比",
                style: TextStyle(fontWeight: FontWeight.bold),
              )
            ],
          ),
        ),
        Expanded(
          child: ListView(
            children: <Widget>[
              ListTile(
                leading: const Icon(Icons.settings),
                title: const Text('个人设置'),
              ),
              ListTile(
                leading: const Icon(Icons.live_help),
                title: const Text('帮助说明'),
              ),
              ListTile(
                leading: const Icon(Icons.settings),
                title: const Text('个人设置'),
              ),
              ListTile(
                leading: const Icon(Icons.live_help),
                title: const Text('帮助说明'),
              ),
            ],
          ),
        )
      ],
    ));
  }
}
