


import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:yc_flutter_utils/state/page_change_notifier.dart';
import 'package:yc_flutter_utils/state/page_status_enum.dart';

abstract class AbsStatusPageState<T extends StatefulWidget> extends State<T> {

  @override
  void initState() {
    super.initState();
    Future(() {
      Provider.of<PageChangeNotifier>(context, listen: false);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Selector<PageChangeNotifier, PageStateEnum>(
        selector: (context, model) => model.viewState,
        builder: (context, value, child) {
          switch (value) {
            case PageStateEnum.LOADING:
              return showLoadingWidget();
            case PageStateEnum.EMPTY:
              return showEmptyView();
            case PageStateEnum.DATA_ERROR:
              return showDataErrorView();
            case PageStateEnum.NET_ERROR:
              return showErrorView();
            case PageStateEnum.SUCCESS:
              return showComponent();
            default:
              return Container();
          }
        },
      ),
    );
  }

  Widget showLoadingWidget();

  Widget showEmptyView();

  Widget showDataErrorView();

  Widget showErrorView();

  Widget showComponent();

}