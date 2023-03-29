


import 'package:yc_flutter_utils/mvp/i_model.dart';
import 'package:yc_flutter_utils/mvp/i_presenter.dart';
import 'package:yc_flutter_utils/mvp/i_view.dart';


///定义了登录页面的view接口、model接口和presenter 接口。

abstract class ContractView implements IView {
  ///登录成功
  void loginSuccess();
  void logoutSuccess();
}


abstract class ContractPresenter implements IPresenter {
  ///登录
  void login();
  void logout();
}


abstract class ContractModel implements IModel {
  ///登录
  void login(String phoneNo, String password, Function callback);
}


