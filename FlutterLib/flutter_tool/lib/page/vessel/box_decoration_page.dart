import 'package:flutter/material.dart';


class BoxDecorationPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('装饰容器DecoratedBox'),
      ),
      body: new Column(
        children: [
          new Text(
            "这个是一个 SizedBox",
            style:new TextStyle(
              color: Colors.red,
              fontSize: 20,
            ),
          ),
          new SizedBox(
            width: 100.0,
            height: 100.0,
            child: new Container(
              decoration: new BoxDecoration(color: Colors.red),
            ),
          ),
          new Text(
            "这个是一个 DecoratedBox",
            style:new TextStyle(
              color: Colors.red,
              fontSize: 20,
            ),
          ),
          DecoratedBox(
              decoration: BoxDecoration(
                  gradient: LinearGradient(colors:[Colors.red,Colors.orange[700]]), //背景渐变
                  borderRadius: BorderRadius.circular(3.0), //3像素圆角
                  boxShadow: [ //阴影
                    BoxShadow(
                        color:Colors.black54,
                        offset: Offset(2.0,2.0),
                        blurRadius: 4.0
                    )
                  ]
              ),
              child: Padding(padding: EdgeInsets.symmetric(horizontal: 80.0, vertical: 18.0),
                child: Text("Login", style: TextStyle(color: Colors.white),),
              )
          ),
          
        ],
      )
    );
  }
}
