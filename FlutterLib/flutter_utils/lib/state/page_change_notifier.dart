import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/state/page_status_enum.dart';


class PageChangeNotifier extends ChangeNotifier {

  PageStateEnum _viewState = PageStateEnum.IDLE;

  bool get isLoading => _viewState == PageStateEnum.LOADING;

  bool get isError => _viewState == PageStateEnum.NET_ERROR;

  bool get isDataError => _viewState == PageStateEnum.DATA_ERROR;

  bool get isEmpty => _viewState == PageStateEnum.EMPTY;

  bool get isSuccess => _viewState == PageStateEnum.SUCCESS;

  PageStateEnum get viewState => _viewState;

  bool disposed = false;

  @override
  void dispose() {
    disposed = true;
    super.dispose();
  }

  set viewState(PageStateEnum value) {
    if (disposed) {
      return;
    }
    _viewState = value;
    notifyListeners();
  }

}
