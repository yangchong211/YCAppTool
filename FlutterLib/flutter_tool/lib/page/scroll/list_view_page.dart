import 'package:flutter/material.dart';


class ListViewLayoutPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new ListViewState();
  }

}

class ListViewState extends State<ListViewLayoutPage> {

  /// itemExtent：该参数如果不为null，则会强制children的“长度”为itemExtent的值；这里的“长度”是指滚动方向上子组件的长度，也就是说如果滚动方向是垂直方向，则itemExtent代表子组件的高度；如果滚动方向为水平方向，则itemExtent就代表子组件的宽度。在ListView中，指定itemExtent比让子组件自己决定自身长度会有更好的性能，这是因为指定itemExtent后，滚动系统可以提前知道列表的长度，而无需每次构建子组件时都去再计算一下，尤其是在滚动位置频繁变化时（滚动系统需要频繁去计算列表高度）。
  /// prototypeItem：如果我们知道列表中的所有列表项长度都相同但不知道具体是多少，这时我们可以指定一个列表项，该列表项被称为 prototypeItem（列表项原型）。指定 prototypeItem 后，可滚动组件会在 layout 时计算一次它延主轴方向的长度，这样也就预先知道了所有列表项的延主轴方向的长度，所以和指定 itemExtent 一样，指定 prototypeItem 会有更好的性能。注意，itemExtent 和prototypeItem 互斥，不能同时指定它们。如果
  /// shrinkWrap：该属性表示是否根据子组件的总长度来设置ListView的长度，默认值为false 。默认情况下，ListView的会在滚动方向尽可能多的占用空间。当ListView在一个无边界(滚动方向上)的容器中时，shrinkWrap必须为true。
  /// addAutomaticKeepAlives：该属性我们将在介绍 PageView 组件时详细解释。
  /// addRepaintBoundaries：该属性表示是否将列表项（子组件）包裹在RepaintBoundary组件中。RepaintBoundary 读者可以先简单理解为它是一个”绘制边界“，将列表项包裹在RepaintBoundary中可以避免列表项不必要的重绘，但是当列表项重绘的开销非常小（如一个颜色块，或者一个较短的文本）时，不添加RepaintBoundary反而会更高效（具体原因会在本书后面 Flutter 绘制原理相关章节中介绍）。如果列表项自身来维护是否需要添加绘制边界组件，则此参数应该指定为 false。

  int type = 1;

  @override
  Widget build(BuildContext context) {
    Widget body;
    if (type == 1){
      body = getListView1();
    } else if(type == 2){
      body = getListView2();
    } else if(type == 3){
      body = getListView3(context);
    }  else if(type == 4){
      body = getListView4(context);
    } else {
      body = getListView1();
    }
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('滚动布局'),
      ),
      body: body,
      floatingActionButton: new FloatingActionButton(
        //点击
        onPressed: _onFabClick,
        tooltip: 'Increment',
        child: new Text("切换"+type.toString()),
      ), // Th
    );
  }

  void _onFabClick(){
    if(type>4){
      type = 0;
    }
    setState(() {
      type = type + 1;
    });
  }

  Widget getListView1(){
    var listView = ListView(
      shrinkWrap: true,
      padding: const EdgeInsets.all(20.0),
      children: <Widget>[
        new Center(
          child: new Text(
            '\n大标题',
            style: new TextStyle(fontFamily: 'serif', fontSize: 20.0),
          ),
        ),
        new Center(
          child: new Text(
            '小标题',
            style: new TextStyle(
              fontFamily: 'serif',
              fontSize: 12.0,
            ),
          ),
        ),
        const Text('I\'m dedicating every day to you'),
        const Text('Domestic life was never quite my style'),
        const Text('When you smile, you knock me out, I fall apart'),
        const Text('And I thought I was so smart'),
      ],
    );
    return listView;
  }


  Widget getListView2(){
    var listView = ListView.builder(
        itemCount: 100,
        itemExtent: 20.0, //强制高度为20.0
        itemBuilder: (BuildContext context, int index) {
          return ListTile(title: Text("$index"));
        }
    );
    return listView;
  }

  Widget getListView3(BuildContext context) {
    //下划线widget预定义以供复用。
    Widget divider1=Divider(color: Colors.blue,);
    Widget divider2=Divider(color: Colors.green);
    return ListView.separated(
      itemCount: 100,
      //列表项构造器
      itemBuilder: (BuildContext context, int index) {
        return ListTile(title: Text("$index"));
      },
      //分割器构造器
      separatorBuilder: (BuildContext context, int index) {
        return index%2==0?divider1:divider2;
      },
    );
  }

  Widget getListView4(BuildContext context) {
    var infiniteListView = new InfiniteListView();
    return infiniteListView;
  }

}

class InfiniteListView extends StatefulWidget {
  @override
  _InfiniteListViewState createState() => new _InfiniteListViewState();
}

class _InfiniteListViewState extends State<InfiniteListView> {
  static const loadingTag = "##loading##"; //表尾标记
  var _words = <String>[loadingTag];

  @override
  void initState() {
    super.initState();
    _retrieveData();
  }

  @override
  Widget build(BuildContext context) {
    return ListView.separated(
      itemCount: _words.length,
      itemBuilder: (context, index) {
        //如果到了表尾
        if (_words[index] == loadingTag) {
          //不足100条，继续获取数据
          if (_words.length - 1 < 100) {
            //获取数据
            _retrieveData();
            //加载时显示loading
            return Container(
              padding: const EdgeInsets.all(16.0),
              alignment: Alignment.center,
              child: SizedBox(
                  width: 24.0,
                  height: 24.0,
                  child: CircularProgressIndicator(strokeWidth: 2.0)
              ),
            );
          } else {
            //已经加载了100条数据，不再获取数据。
            return Container(
                alignment: Alignment.center,
                padding: EdgeInsets.all(16.0),
                child: Text("没有更多了", style: TextStyle(color: Colors.grey),)
            );
          }
        }
        //显示单词列表项
        return ListTile(title: Text(_words[index]));
      },
      separatorBuilder: (context, index) => Divider(height: .0),
    );
  }

  void _retrieveData() {
    Future.delayed(Duration(seconds: 2)).then((e) {
      setState(() {
        for(int i=0 ; i<20 ; i++){
          _words.insert(i, "helloWorld"+i.toString());
        }


        //重新构建列表
        // _words.insertAll(_words.length - 1,
        //     //每次生成20个单词
        //     generateWordPairs().take(20).map((e) => e.asPascalCase).toList()
        // );
      });
    });
  }

}


