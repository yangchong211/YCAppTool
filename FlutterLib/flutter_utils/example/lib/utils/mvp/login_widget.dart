
import 'package:flutter/material.dart';
import 'package:flutter/src/widgets/framework.dart';
import 'package:yc_flutter_utils/mvp/abstract_view.dart';
import 'package:yc_flutter_utils/mvp/abstract_view_state.dart';
import 'package:yc_flutter_utils/mvp/i_presenter.dart';
import 'package:yc_flutter_utils/mvp/i_view.dart';
import 'package:yc_flutter_utils_example/utils/mvp/login_contract.dart';
import 'package:yc_flutter_utils_example/utils/mvp/login_presenter.dart';

class LoginWidget extends AbstractView {

  @override
  AbstractViewState<IPresenter<IView>, AbstractView> getState() {
    return _LoginState();
  }

}

class _LoginState extends AbstractViewState<LoginPresenter, LoginWidget> implements ContractView{

  String status = "未开始";

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('MVP 加购'),
      ),
      body: new ListView(
        children: <Widget>[
          new Text("开始请求网络并且处理数据"),
          MaterialButton(
            onPressed: click1,
            child: new Text("存储json文件"),
            color: Colors.cyan,
          ),
          new Text("开始推出登陆并销毁"),
          MaterialButton(
            onPressed: click2,
            child: new Text("获取json文件"),
            color: Colors.cyan,
          ),
          new Text("登陆的状态：$status"),
        ],
      ),
    );
  }

  @override
  LoginPresenter createPresenter() {
    return LoginPresenter();
  }


  void click1() {
    presenter.login();
  }

  void click2() {
    presenter.logout();
  }

  @override
  void loginSuccess() {
    setState(() {
      status = "登陆成功";
    });
  }

  @override
  void logoutSuccess() {
    setState(() {
      status = "推出登陆";
    });
  }
}