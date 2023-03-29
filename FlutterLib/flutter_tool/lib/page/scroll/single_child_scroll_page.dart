
import 'package:flutter/material.dart';

class SingleChildScrollPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new SingleChildScrollState();
  }

}

class SingleChildScrollState extends State<SingleChildScrollPage>{
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("SingleChildScrollView"),
      ),
      body: new Center(
        child: new ListView(
          children: [
            new Text("SingleChildScrollView类似于Android中的ScrollView，它只能接收一个子组件。"),
            new Text(
              "这个是一个 SingleChildScrollView",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            new Center(
              child: new SingleChildScrollViewTestRoute(),
            )
          ],
        ),
      ),
    );
  }


}

class SingleChildScrollViewTestRoute extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    return Scrollbar( // 显示进度条
      child: SingleChildScrollView(
        physics: NeverScrollableScrollPhysics(),
        padding: EdgeInsets.all(16.0),
        // reverse: false,
        child: Center(
          child: Column(
            //动态创建一个List<Widget>
            children: str.split("")
            //每一个字母都用一个Text显示,字体为原来的两倍
                .map((c) => Text(c, textScaleFactor: 2.0,))
                .toList(),
          ),
        ),
      ),
    );
  }

  // @override
  // Widget build(BuildContext context) {
  //   String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  //   return Scrollbar( // 显示进度条
  //     child: SingleChildScrollView(
  //       physics: NeverScrollableScrollPhysics(),
  //       padding: EdgeInsets.all(16.0),
  //       // reverse: false,
  //       child: Center(
  //         //这样不行
  //         child: Row(
  //           //动态创建一个List<Widget>
  //           children: str.split("")
  //           //每一个字母都用一个Text显示,字体为原来的两倍
  //               .map((c) => Text(c, textScaleFactor: 2.0,))
  //               .toList(),
  //         ),
  //       ),
  //     ),
  //   );
  // }

}