
import 'package:flutter/material.dart';

class SwitchPage extends StatefulWidget {

  @override
  State<StatefulWidget> createState() {
    return new SwitchState();
  }

}

class SwitchState extends State<SwitchPage>{

  bool _switchSelected=true; //维护单选开关状态
  bool _checkboxSelected=true;//维护复选框状态
  int _radioValue = 0;

  double _currentIndex = 0.0;


  @override
  Widget build(BuildContext context) {
    return new Scaffold(
        appBar: new AppBar(title: new Text("单选开关和复选框")),
        body: ListView(
          children: <Widget>[
            new Text("单选开关Switch和复选框Checkbox，虽然它们都是继承自StatefulWidget。"
                "它们本身不会保存当前选中状态，选中状态都是由父组件来管理的"),
            new Text("单选开关Switch和复选框Checkbox，虽然它们都是继承自StatefulWidget。"
                "它们本身不会保存当前选中状态，选中状态都是由父组件来管理的"),
            new Center(
              child: new Switch(
                  value: _switchSelected,
                  onChanged: (value){
                    //重新构建页面
                    setState(() {
                      _switchSelected=value;
                    });
                  }
                ),
            ),
            Text(
              "Checkbox",
              textAlign: TextAlign.center,
            ),
            new Checkbox(
              value: _checkboxSelected,
              //选中时的颜色
              activeColor: Colors.red,
              onChanged:(value){
                setState(() {
                  _checkboxSelected=value;
                });
              } ,
            ),
            Text(
              "Radio",
              textAlign: TextAlign.center,
            ),
            new Column(
              children: <Widget>[
                new Radio(
                    value: 0,
                    groupValue: _radioValue,
                    onChanged: handleRadioValueChanged),
                new Radio(
                    value: 1,
                    groupValue: _radioValue,
                    activeColor: Colors.red,
                    onChanged: handleRadioValueChanged),
                new Radio(
                    value: 2,
                    groupValue: _radioValue,
                    onChanged: handleRadioValueChanged),
              ],
            ),


            Text("Slider"),
            new Slider(
                value: _currentIndex,
                min: 0.0,
                max: 7.0,
                label: "星期$_currentIndex",
                activeColor: Colors.green,
                onChanged: _onSliderStateChanged
            ),



          ],
        ));
  }



  void handleRadioValueChanged(int value) {
    setState(() {
      _radioValue = value;
      print(_radioValue.toString() + '-------------------');
    });
  }

  void _onSliderStateChanged(double value) {
    setState(() {
      _currentIndex = value;
      print(_currentIndex.toString() + '-------------------');
    });
  }


}