import 'package:flutter/material.dart';
import 'package:yc_flutter_tool/res/image/images.dart';
import 'package:yc_flutter_utils/log/log_utils.dart';
import 'package:yc_flutter_utils/state/status_error_widget.dart';

class StatusLayoutPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('状态布局'),
      ),
      body: new Container(
        child: StatusErrorWidget(
          topWidget: null,
          imagePath: Images.image1,
          //imageType: ImageType.netImage,
          title: "no network service",
          content: "Please try again later",
          btnText: "重试",
          tryAgainHandler: tryAgainHandler,
          bottomWidget: Expanded(
            child: Column(
              children: [
                Expanded(child: SizedBox()),
                Column(
                  children: [
                    Text("文本1"),
                    Text("文本2"),
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  void tryAgainHandler() {
    LogUtils.i("tryAgainHandler  重试");
  }

}
