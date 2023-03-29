

import 'package:yc_flutter_utils/mvp/abstract_presenter.dart';
import 'package:yc_flutter_utils/mvp/i_view.dart';
import 'package:yc_flutter_utils_example/utils/mvp/login_contract.dart';
import 'package:yc_flutter_utils_example/utils/mvp/login_model.dart';

class LoginPresenter extends AbstractPresenter<IView, LoginModel> implements ContractPresenter {

  @override
  LoginModel createModel() {
    return LoginModel();
  }

  @override
  void login() {

  }

  @override
  void logout() {

  }

}