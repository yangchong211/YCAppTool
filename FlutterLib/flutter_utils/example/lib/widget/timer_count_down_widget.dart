import 'dart:async';

import 'package:flutter/material.dart';


class TimerCountDownWidget extends StatefulWidget {
  Function onTimerFinish;

  TimerCountDownWidget({this.onTimerFinish}) : super();

  @override
  State<StatefulWidget> createState() => TimerCountDownWidgetState();
}

class TimerCountDownWidgetState extends State<TimerCountDownWidget> {
  Timer _timer;
  int _countdownTime = 0;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        if (_countdownTime == 0) {
          setState(() {
            _countdownTime = 60;
          });
          //开始倒计时
          startCountdownTimer();
        }
      },
      child: RaisedButton(
        color: Colors.black12,
        child: Text(
          _countdownTime > 0 ? '$_countdownTime后重新获取' : '获取验证码',
          style: TextStyle(
            fontSize: 14,
            color: _countdownTime > 0
                ? Colors.white
                : Color.fromARGB(255, 17, 132, 255),
          ),
        ),
      ),
    );
  }

  void startCountdownTimer() {
//    const oneSec = const Duration(seconds: 1);
//    var callback = (timer) => {
//      setState(() {
//        if (_countdownTime < 1) {
//          widget.onTimerFinish();
//          _timer.cancel();
//        } else {
//          _countdownTime = _countdownTime - 1;
//        }
//      })
//    };
//
//    _timer = Timer.periodic(oneSec, callback);

    _timer = Timer.periodic(
        Duration(seconds: 1),
        (Timer timer) => {
              setState(() {
                if (_countdownTime < 1) {
                  widget.onTimerFinish();
                  _timer.cancel();
                } else {
                  _countdownTime = _countdownTime - 1;
                }
              })
            });
  }

  @override
  void dispose() {
    super.dispose();
    if (_timer != null) {
      _timer.cancel();
    }
  }
}
