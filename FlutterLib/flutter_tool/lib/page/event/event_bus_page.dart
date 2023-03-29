
import 'package:flutter/material.dart';

class EventBusPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new EventBusState();
  }

}

class EventBusState extends State<EventBusPage>{
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
            new Text("介绍"),

            new Text(
              "这个是一个 StreamBuilder",
              style:new TextStyle(
                color: Colors.red,
                fontSize: 20,
              ),
            ),
            new Text("介绍"),

          ],
        ),
      ),
    );
  }
}

