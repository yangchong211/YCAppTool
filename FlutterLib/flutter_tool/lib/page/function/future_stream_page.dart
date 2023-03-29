


import 'package:flutter/material.dart';

class FutureStreamPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new FutureStreamState();
  }

}

class FutureStreamState extends State<FutureStreamPage>{

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("异步UI更新-FutureBuilder-StreamBuilder"),
      ),
      body: new Center(
        child: new ListView(
          children: [
            new Text(
              "这个是一个 FutureBuilder",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            new Text("FutureBuilder会依赖一个Future，它会根据所依赖的Future的状态来动态构建自身。"),
            Container(
              height: 200,
              color: Colors.blue[50],
              child: getFuture(context),
            ),

            new Text(
              "这个是一个 StreamBuilder",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            new Text("在Dart中Stream 也是用于接收异步事件数据，和Future 不同的是，"
                "它可以接收多个异步操作的结果，它常用于会多次读取数据的异步任务场景，"
                "如网络内容下载、文件读写等。StreamBuilder正是用于配合Stream来"
                "展示流上事件（数据）变化的UI组件。"),
            Container(
              height: 100,
              color: Colors.blue[50],
              child: getStream(context),
            ),


          ],
        ),
      ),
    );
  }

  Future<String> mockNetworkData() async {
    return Future.delayed(Duration(seconds: 4), () => "我是从互联网上获取的数据");
  }


  Widget getFuture(BuildContext context) {
    return Center(
      child: FutureBuilder<String>(
        future: mockNetworkData(),
        builder: (BuildContext context, AsyncSnapshot snapshot) {
          // 请求已结束
          if (snapshot.connectionState == ConnectionState.done) {
            if (snapshot.hasError) {
              // 请求失败，显示错误
              return Text("Error: ${snapshot.error}");
            } else {
              // 请求成功，显示数据
              return Text("Contents: ${snapshot.data}");
            }
          } else {
            // 请求未结束，显示loading
            return CircularProgressIndicator();
          }
        },
      ),
    );
  }


  Stream<int> counter() {
    return Stream.periodic(Duration(seconds: 3), (i) {
      return i;
    });
  }

  Widget getStream(BuildContext context) {
    return StreamBuilder<int>(
      stream: counter(), //
      //initialData: ,// a Stream<int> or null
      builder: (BuildContext context, AsyncSnapshot<int> snapshot) {
        if (snapshot.hasError)
          return Text('Error: ${snapshot.error}');
        switch (snapshot.connectionState) {
          case ConnectionState.none:
            return Text('没有Stream');
          case ConnectionState.waiting:
            return Text('等待数据...');
          case ConnectionState.active:
            return Text('active: ${snapshot.data}');
          case ConnectionState.done:
            return Text('Stream已关闭');
        }
        return null; // unreachable
      },
    );
  }


}
