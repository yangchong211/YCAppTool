

import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/state/abs_status_page_state.dart';
import 'package:yc_flutter_utils/state/base_page_state.dart';
import 'package:yc_flutter_utils/state/status_flutter_utils.dart';

class StatePage extends StatefulWidget {

  @override
  State<StatefulWidget> createState() {
    return new _StatePageState();
  }
}

class _StatePageState extends State<StatePage>{


  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: new AppBar(
        title: new Text("状态页面切换管理"),
        centerTitle: true,
      ),
      body: new Column(
        children: <Widget>[
          Row(
            children: [
              Padding(padding: EdgeInsets.only(right: 5),),
              MaterialButton(
                minWidth: 40,
                onPressed: click1,
                child: new Text("loading"),
                color: Colors.cyan,
              ),
              Padding(padding: EdgeInsets.only(right: 5),),
              MaterialButton(
                minWidth: 30,
                onPressed: click2,
                child: new Text("空数据"),
                color: Colors.cyan,
              ),
              Padding(padding: EdgeInsets.only(right: 5),),
              MaterialButton(
                minWidth: 30,
                onPressed: click3,
                child: new Text("异常"),
                color: Colors.cyan,
              ),
            ],
          ),
          Row(
            children: [
              Padding(padding: EdgeInsets.only(right: 5),),
              MaterialButton(
                minWidth: 40,
                onPressed: click5,
                child: new Text("网络异常"),
                color: Colors.cyan,
              ),
              Padding(padding: EdgeInsets.only(right: 5),),
              MaterialButton(
                minWidth: 20,
                onPressed: click4,
                child: new Text("成功"),
                color: Colors.cyan,
              ),
            ],
          ),
          new StateChangeWidget(),
        ],
      ),
    );
  }

  void click1() {
    StatusFlutterUtils.showLoadingWidget(context);
  }

  void click2() {
    StatusFlutterUtils.showEmptyWidget(context);
  }

  void click3() {
    StatusFlutterUtils.showDataErrorWidget(context);
  }

  void click4() {
    StatusFlutterUtils.showSuccessWidget(context);
  }

  void click5() {
    StatusFlutterUtils.showNetErrorWidget(context);
  }
}

class StateChangeWidget extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return new MyChangeState();
  }
}

class ChangeState extends AbsStatusPageState<StateChangeWidget>{

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget showComponent() {
    return new Text("这个是正常布局");
  }

  @override
  Widget showEmptyView() {
    return new Text("这个是空数据布局");
  }

  @override
  Widget showErrorView() {
    return new Text("这个是异常布局");
  }

  @override
  Widget showLoadingWidget() {
    return new Text("这个是loading布局");
  }

  @override
  Widget showDataErrorView() {
    return new Text("这个是数据异常布局");
  }

}

class MyChangeState extends BasePageState<StateChangeWidget>{

  @override
  Widget showComponent() {
    return new Text("这个是正常布局");
  }

}

