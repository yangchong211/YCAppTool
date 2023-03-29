


import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:yc_flutter_tool/page/channel/flutter_calc_plugin.dart';

class CalcPluginPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return PageState();
  }

}

class PageState extends State<CalcPluginPage> {
  String _platformVersion = 'Unknown';
  String addResult = '';
  TextEditingController _addNumber1Controller,_addNumber2Controller;

  @override
  void initState() {
    super.initState();
    _addNumber1Controller = TextEditingController();
    _addNumber2Controller = TextEditingController();
  }

  Future<void> getAddResult() async {
    int addNumber1= int.parse(_addNumber1Controller.value.text);
    int addNumber2=int.parse(_addNumber2Controller.value.text);

    String result = '';
    try {
      result = await FlutterCalcPlugin.getResult(addNumber2, addNumber1);
    } on PlatformException {
      result = '未知错误';
    }
    setState(() {
      addResult = result;
    });
  }

  Future<void> initPlatformState() async {
    String platformVersion;

    try {
      platformVersion = await FlutterCalcPlugin.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('插件示例'),
        ),
        body: Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                MaterialButton(
                  color: Colors.amber,
                  child: Text("获取系统版本"),
                  onPressed: () {
                    initPlatformState();
                  },
                ),
                Text('当前系统版本 : $_platformVersion\n'),
                SizedBox(height: 30),
                Text("加法计算器"),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,

                  children: <Widget>[
                    SizedBox(
                      width: 80,
                      child: TextField(
                        controller: _addNumber1Controller,
                        keyboardType: TextInputType.number,
                      ),
                    ),
                    Text("  +  ",style: TextStyle(fontSize: 26),),
                    SizedBox(
                      width: 80,
                      child: TextField(
                        controller: _addNumber2Controller,
                        keyboardType: TextInputType.number,
                      ),
                    ),
                    Text("  = ",style: TextStyle(fontSize: 26),),
                  ],
                ),
                SizedBox(height: 30),
                MaterialButton(
                  color: Colors.amber,
                  child: Text("结果等于"),
                  onPressed: () {
                    getAddResult();
                  },
                ),
                Text(addResult),
              ],
            )),
      ),
    );
  }

}


