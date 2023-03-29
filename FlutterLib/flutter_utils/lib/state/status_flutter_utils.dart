

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:yc_flutter_utils/state/page_change_notifier.dart';
import 'package:yc_flutter_utils/state/page_status_enum.dart';
import 'package:yc_flutter_utils/state/status_loading_dialog.dart';

class StatusFlutterUtils {

  static showLoadingDialog(BuildContext context){
    showDialog(
        context: context,
        builder: (_) {
          return StatusLoadingDialog();
        });
  }

  static showLoadingWidget(BuildContext context){
    PageChangeNotifier changeNotifier = Provider.of<PageChangeNotifier>(context, listen: false);
    changeNotifier.viewState = PageStateEnum.LOADING;
  }


  static showEmptyWidget(BuildContext context) {
    PageChangeNotifier changeNotifier = Provider.of<PageChangeNotifier>(context, listen: false);
    changeNotifier.viewState = PageStateEnum.EMPTY;
  }

  static showSuccessWidget(BuildContext context) {
    PageChangeNotifier changeNotifier = Provider.of<PageChangeNotifier>(context, listen: false);
    changeNotifier.viewState = PageStateEnum.SUCCESS;
  }

  static showNetErrorWidget(BuildContext context) {
    PageChangeNotifier changeNotifier = Provider.of<PageChangeNotifier>(context, listen: false);
    changeNotifier.viewState = PageStateEnum.NET_ERROR;
  }

  static showDataErrorWidget(BuildContext context) {
    PageChangeNotifier changeNotifier = Provider.of<PageChangeNotifier>(context, listen: false);
    changeNotifier.viewState = PageStateEnum.DATA_ERROR;
  }



}