

import 'dart:async';

import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/bus/event_bus_helper.dart';
import 'package:yc_flutter_utils/bus/event_bus_service.dart';


class BusPage extends StatefulWidget {

  BusPage();

  @override
  State<StatefulWidget> createState() {
    return new _DatePageState();
  }
}

class _DatePageState extends State<BusPage> {

  String message1 = "124321423";
  String message2 = "1243.21423";
  StreamSubscription _subscription;

  @override
  void initState() {
    super.initState();
    registerBus();
    registerBus2();
  }

  @override
  void dispose() {
    super.dispose();
    unRegisterBus();
    unRegisterBus2();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: new AppBar(
        title: new Text("DateUtils工具类"),
        centerTitle: true,
      ),
      body: new Column(
        children: <Widget>[
          new Text("接收bus消息1 ：$message1"),
          MaterialButton(
            onPressed: sendBus1,
            child: new Text("发送bus消息1"),
            color: Colors.cyan,
          ),
          new Text("接收bus消息2 ：$message2"),
          MaterialButton(
            onPressed: sendBus2,
            child: new Text("发送bus消息2"),
            color: Colors.cyan,
          ),
        ],
      ),
    );
  }

  void sendBus1() {
    EventBusService.instance.eventBus.fire(EventMessage(
      "eventBus1",
      arguments: {"busMessage": "发送bus消息1"},
    ));
  }

  void registerBus() {
    _subscription = EventBusService.instance.eventBus.on<EventMessage>().listen((event) {
          String name = event.eventName;
          //前后台切换发生了变化
          if (name == "eventBus1") {
            var busMessage = event.arguments["busMessage"];
            setState(() {
              message1 = busMessage;
            });
          }
        });
  }

  unRegisterBus(){
    if (_subscription != null) {
      _subscription.cancel();
      _subscription = null;
    }
  }

  ///------------------------------------------------------------------

  void sendBus2() {
    var arg = "发送bus消息1";
    bus.emit("eventBus2", arg);
  }

  void registerBus2() {
    bus.on("eventBus2", (arg) {
      var busMessage = arg;
      setState(() {
        message2 = "接收消息：" + busMessage;
      });
    });
  }

  unRegisterBus2(){
    bus.off("eventBus2", (arg) {

    });
  }


}