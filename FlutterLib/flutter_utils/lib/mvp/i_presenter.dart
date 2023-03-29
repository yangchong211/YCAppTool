
import 'package:yc_flutter_utils/mvp/i_view.dart';

abstract class IPresenter<V extends IView> {

  ///将视图设置或附加到此Presenter
  void attachView(V view);

  ///将在视图被销毁时被调用
  void detachView();

}
