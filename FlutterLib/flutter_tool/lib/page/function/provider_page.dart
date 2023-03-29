



import 'package:flutter/material.dart';

class ProviderPage extends StatefulWidget{

  @override
  State<StatefulWidget> createState() {
    return new ProviderState();
  }

}

class ProviderState extends State<ProviderPage>{
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("跨组件状态共享-Provider"),
      ),
      body: new Center(
        child: new ListView(
          children: [

          ],
        ),
      ),
    );
  }

}