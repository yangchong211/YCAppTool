import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/file/file_utils.dart';
import 'package:yc_flutter_utils/timer/timer_utils.dart';

class TimerPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => StorageState();
}

class StorageState extends State<TimerPage> {

  bool play = false;
  String string1 = "null";

  @override
  void dispose() {
    super.dispose();
    stop();
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('TimerUtils 计时器'),
      ),
      body: new ListView(
        children: <Widget>[
          new Text("倒计时器 ：$string1"),
          MaterialButton(
            onPressed: start,
            child: new Text("开始倒计时"),
            color: Colors.cyan,
          ),
          new Text("开启timer倒计时：$string1"),
          MaterialButton(
            onPressed: stop,
            child: new Text("停止计时器"),
            color: Colors.cyan,
          ),
        ],
      ),
    );
  }

  TimerUtils timerUtils = null;
  void start() {
    stop();
    //timerUtils = new TimerUtils(mInterval: 1000,mTotalTime: 60);
    timerUtils = new TimerUtils();
    timerUtils.setTotalTime(60);
    timerUtils.setInterval(1000);
    timerUtils.startTimer();
    timerUtils.setOnTimerTickCallback((millisUntilFinished) {
      setState(() {
        string1 = millisUntilFinished.toString();
      });
    });
  }


  void stop() {
    if(timerUtils!=null){
      timerUtils.cancel();
    }
  }


}
