import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/toast/snack_utils.dart';


class TextFieldPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new TextFieldState();
  }
}

class TextFieldState extends State<TextFieldPage> {

  TextEditingController _userPhoneController = new TextEditingController();
  TextEditingController _userPasswordController = new TextEditingController();

  //清空输入框内容
  void onTextClear() {
    setState(() {
      _userPhoneController.text = "";
      _userPasswordController.text = "";
    });
  }


  @override
  Widget build(BuildContext context) {
    return new Scaffold(
        appBar: new AppBar(
          title: new Text("登录"),
        ),
        body: new Column(
          children: <Widget>[
            new TextField(
              controller: _userPhoneController,
              keyboardType: TextInputType.name,
              style: new TextStyle(
                fontSize: 14,
                color: Colors.brown
              ),
              decoration: new InputDecoration(
                contentPadding: const EdgeInsets.all(10.0),
                hintText: "这个是hint文案内容",
              ),
              cursorColor: Colors.red,
            ),

            new TextField(
              controller: _userPhoneController,
              keyboardType: TextInputType.number,
              decoration: new InputDecoration(
                  contentPadding: const EdgeInsets.all(10.0),
                  icon: new Icon(Icons.phone),
                  labelText: "请输入手机号",
                  hintText: "需要输入11位的手机号",
                  helperText: "注册时填写的手机号"),
              onChanged: (String str) {
                //onChanged是每次输入框内每次文字变更触发的回调
                print('手机号为：$str----------');
              },
              onSubmitted: (String str) {
                //onSubmitted是用户提交而触发的回调{当用户点击提交按钮（输入法回车键）}
                print('最终手机号为:$str---------------');
              },
            ),
            new TextField(
              controller: _userPasswordController,
              keyboardType: TextInputType.text,
              decoration: new InputDecoration(
                  contentPadding: const EdgeInsets.all(10.0),
                  icon: new Icon(Icons.nature_people),
                  labelText: "请输入名字",
//                  hintText: "fdsfdss",
                  helperText: "注册名字"),
            ),
            new Builder(builder: (BuildContext context) {
              return new RaisedButton(
                onPressed: () {
                  if (_userPasswordController.text.toString() == "10086" &&
                      _userPhoneController.text.toString() == "10086") {
                    SnackUtils.toast(context, "校验通过");
                  } else {
                    SnackUtils.toast(context, "校验有问题，请重新输入");
                    onTextClear(); //情况输入内容，让用户重新输入
                  }
                },
                color: Colors.blue,
                highlightColor: Colors.deepPurple,
                disabledColor: Colors.cyan,
                child: new Text(
                  "登录",
                  style: new TextStyle(color: Colors.white),
                ),
              );
            })
          ],
        ));
  }
}