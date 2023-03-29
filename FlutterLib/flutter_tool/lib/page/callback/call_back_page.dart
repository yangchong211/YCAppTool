import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/widget/common_widget.dart';

class CallBackPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => PageState();
}

class PageState extends State<CallBackPage> {
  var _stateInfo = "初始值";

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("方法回调"),
        centerTitle: true,
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: <Widget>[Text(_stateInfo), ButtonWithCallBack("跳转下一页", updataInfo)],
      ),
    );
  }


  /**
   * 利用callback的思想类似方法回调，要处理一件事的时机是另外一件事处理完成之后触发。
   */
  void updataInfo() {
    setState(() {
      _stateInfo="被修改之后的值";
    });
    print("------------");
  }
}

class ButtonWithCallBack extends StatelessWidget {
  Function _funCallBack;
  var _title;

  ButtonWithCallBack(this._title, this._funCallBack);

  @override
  Widget build(BuildContext context) {
    return FlatButton(
      child: Text(_title),
      onPressed: () {
        Navigator.of(context)
            .push(new MaterialPageRoute(builder: (context) => CallBackSecondPage()))
            .then((res) {
          _funCallBack();
        });
      },
      color: Colors.black12,
    );
  }
}


class CallBackSecondPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("方法回调2"),
        centerTitle: true,
      ),
      body: CommonWidget(Center(
        child: Text("dddd"),
      )).onClick(onTap: () {
        print("------------click-------");
      }, onLongPress: () {
        print("long click---------------");
      }).build(),
    );
  }
}

class MyText extends Text {
  MyText(String data) : super(data, style: TextStyle(color: Colors.redAccent));
}
