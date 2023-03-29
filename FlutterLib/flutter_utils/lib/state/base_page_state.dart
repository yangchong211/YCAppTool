

import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/state/abs_status_page_state.dart';
import 'package:yc_flutter_utils/state/status_error_widget.dart';
import 'package:yc_flutter_utils/state/status_loading_dialog.dart';
import 'package:yc_flutter_utils/state/status_loading_widget.dart';

class BasePageState<T extends StatefulWidget> extends AbsStatusPageState<T>{

  @override
  Widget showComponent() {
    return SizedBox();
  }

  @override
  Widget showDataErrorView() {
    return StatusErrorWidget(
      imagePath: "",
      title: "数据异常",
      content: "二级标题",
      btnText: "重试按钮",
      //点击重试
      tryAgainHandler: () {

      },
    );
  }

  @override
  Widget showEmptyView() {
    return StatusErrorWidget(
      imagePath: "",
      title: "空数据",
      content: "二级标题",
      btnText: "重试按钮",
      //点击重试
      tryAgainHandler: () {

      },
    );
  }

  @override
  Widget showErrorView() {
    return StatusErrorWidget(
      imagePath: "",
      title: "异常页面",
      content: "二级标题",
      btnText: "重试按钮",
      //点击重试
      tryAgainHandler: () {

      },
    );
  }

  @override
  Widget showLoadingWidget() {
    return StatusLoadingDialog();
    return StatusLoadingWidget();
  }

}