
import 'package:flutter/material.dart';

class IndicatorProgressPage extends StatefulWidget {

  @override
  State<StatefulWidget> createState() {
    return new IndicatorProgressState();
  }

}

class IndicatorProgressState extends State<IndicatorProgressPage>{

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
        appBar: new AppBar(title: new Text("进度指示器")),
        body: ListView(
          children: <Widget>[
            new Text("两种进度指示器：LinearProgressIndicator和CircularProgressIndicator"),

            Text("圆形进度条"),
            new Center(
              child:  new CircularProgressIndicator(
                backgroundColor: Colors.white,
                valueColor: AlwaysStoppedAnimation(Colors.deepOrange),
//            value: 0.5, //(在不指定value的情况下，进度条一直滚动)
                strokeWidth: 5.0,
              ),
            ),
            SizedBox(
              height: 20,
            ),
            Text("水平进度条"),
            new Center(
              child: new LinearProgressIndicator(
                backgroundColor: Colors.white,
                valueColor: AlwaysStoppedAnimation(Colors.deepOrange),
//                value: 0.5, //(在不指定value的情况下，进度条一直滚动))
              ),
            ),

            SizedBox(
              height: 20,
            ),

            Text("刷新进度条"),
            new Center(
              child: new RefreshProgressIndicator(
                backgroundColor: Colors.white,
                valueColor: AlwaysStoppedAnimation(Colors.deepOrange),
//                value: 0.5, //(在不指定value的情况下，进度条一直滚动))
              ),
            ),
          ],
        ));
  }

}