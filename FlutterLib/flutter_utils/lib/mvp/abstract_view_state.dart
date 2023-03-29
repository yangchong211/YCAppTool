
import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/mvp/abstract_view.dart';
import 'package:yc_flutter_utils/mvp/i_presenter.dart';
import 'package:yc_flutter_utils/mvp/i_view.dart';

abstract class AbstractViewState<P extends IPresenter, V extends AbstractView>
    extends State<V> implements IView {

  P presenter;

  @override
  void initState() {
    super.initState();
    presenter = createPresenter();
    if (presenter != null) {
      presenter.attachView(this);
    }
  }

  P createPresenter();

  P getPresenter() {
    return presenter;
  }

  @override
  void dispose() {
    super.dispose();
    if (presenter != null) {
      presenter.detachView();
      presenter = null;
    }
  }
}