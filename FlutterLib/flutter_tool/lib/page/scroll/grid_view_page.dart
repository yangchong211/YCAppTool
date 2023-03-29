


import 'package:flutter/material.dart';

class GridViewPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new GridViewState();
  }

}

class GridViewState extends State<GridViewPage>{

  int type = 1;

  @override
  Widget build(BuildContext context) {
    Widget body;
    if (type == 1){
      body = getView1();
    } else if(type == 2){
      body = getView2();
    } else if(type == 3){
      body = getView3();
    }  else if(type == 4){
      body = getView4();
    }  else if(type == 5){
      body = getView5();
    } else {
      body = getView1();
    }

    return new Scaffold(
      appBar: new AppBar(
        title: new Text("GridView"),
      ),
      body: body,
      floatingActionButton: new FloatingActionButton(
        //点击
        onPressed: _onFabClick,
        tooltip: 'Increment',
        child: new Text("切换"+type.toString()),
      ), //
    );
  }

  void _onFabClick(){
    if(type>5){
      type = 0;
    }
    setState(() {
      type = type + 1;
    });
  }

  Widget getView1(){
    var gridView = GridView(
        gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
            crossAxisCount: 3, //横轴三个子widget
            childAspectRatio: 1.0 //宽高比为1时，子widget
        ),
        children:<Widget>[
          Icon(Icons.ac_unit),
          Icon(Icons.airport_shuttle),
          Icon(Icons.all_inclusive),
          Icon(Icons.beach_access),
          Icon(Icons.cake),
          Icon(Icons.free_breakfast)
        ]
    );
    return gridView;
  }

  Widget getView2(){
    var gridView = GridView.count(
      crossAxisCount: 3,
      childAspectRatio: 1.0,
      children: <Widget>[
        Icon(Icons.ac_unit),
        Icon(Icons.airport_shuttle),
        Icon(Icons.all_inclusive),
        Icon(Icons.beach_access),
        Icon(Icons.cake),
        Icon(Icons.free_breakfast),
      ],
    );
    return gridView;
  }

  Widget getView3(){
    var gridView = GridView(
      padding: EdgeInsets.zero,
      gridDelegate: SliverGridDelegateWithMaxCrossAxisExtent(
          maxCrossAxisExtent: 120.0,
          childAspectRatio: 2.0 //宽高比为2
      ),
      children: <Widget>[
        Icon(Icons.ac_unit),
        Icon(Icons.airport_shuttle),
        Icon(Icons.all_inclusive),
        Icon(Icons.beach_access),
        Icon(Icons.cake),
        Icon(Icons.free_breakfast),
      ],
    );
    return gridView;
  }

  Widget getView4(){
    var gridView = GridView.extent(
      maxCrossAxisExtent: 120.0,
      childAspectRatio: 2.0,
      children: <Widget>[
        Icon(Icons.ac_unit),
        Icon(Icons.airport_shuttle),
        Icon(Icons.all_inclusive),
        Icon(Icons.beach_access),
        Icon(Icons.cake),
        Icon(Icons.free_breakfast),
      ],
    );
    return gridView;
  }

  Widget getView5(){
    var infiniteGridView = new InfiniteGridView();
    return infiniteGridView;
  }

}


class InfiniteGridView extends StatefulWidget {
  @override
  _InfiniteGridViewState createState() => new _InfiniteGridViewState();
}

class _InfiniteGridViewState extends State<InfiniteGridView> {

  List<IconData> _icons = []; //保存Icon数据

  @override
  void initState() {
    // 初始化数据
    _retrieveIcons();
  }

  @override
  Widget build(BuildContext context) {
    return GridView.builder(
        gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
            crossAxisCount: 3, //每行三列
            childAspectRatio: 1.0 //显示区域宽高相等
        ),
        itemCount: _icons.length,
        itemBuilder: (context, index) {
          //如果显示到最后一个并且Icon总数小于200时继续获取数据
          if (index == _icons.length - 1 && _icons.length < 200) {
            _retrieveIcons();
          }
          return Icon(_icons[index]);
        }
    );
  }

  //模拟异步获取数据
  void _retrieveIcons() {
    Future.delayed(Duration(milliseconds: 200)).then((e) {
      setState(() {
        _icons.addAll([
          Icons.ac_unit,
          Icons.airport_shuttle,
          Icons.all_inclusive,
          Icons.beach_access, Icons.cake,
          Icons.free_breakfast
        ]);
      });
    });
  }
}




