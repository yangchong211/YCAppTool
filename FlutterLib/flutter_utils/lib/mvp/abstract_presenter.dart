
import 'package:yc_flutter_utils/mvp/i_model.dart';
import 'package:yc_flutter_utils/mvp/i_presenter.dart';
import 'package:yc_flutter_utils/mvp/i_view.dart';

abstract class AbstractPresenter<V extends IView, M extends IModel> implements IPresenter {

  M _model;
  V _view;

  @override
  void attachView(IView view) {
    this._model = createModel();
    this._view = view;
  }

  @override
  void detachView() {
    if (_view != null) {
      _view = null;
    }
    if (_model != null) {
      _model.dispose();
      _model = null;
    }
  }

  V get view {
    return _view;
  }

  //V get view => _view;

  M get model => _model;

  IModel createModel();
}