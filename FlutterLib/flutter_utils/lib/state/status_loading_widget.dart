
import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/res/flutter_colors.dart';
import 'package:yc_flutter_utils/res/flutter_dimens.dart';

class StatusLoadingWidget extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return _buildLoadingWidget();
  }

  Widget _buildLoadingWidget() {
    return Container(
      color: FlutterColors.color_FFEEEEEE,
      height: double.infinity,
      child: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            Container(
              width: FlutterDimens.flutter_dimen_50,
              height: FlutterDimens.flutter_dimen_50,
              child: CircularProgressIndicator(
                strokeWidth: FlutterDimens.flutter_dimen_2,
              ),
            ),
            Padding(
              padding: EdgeInsets.all(FlutterDimens.flutter_dimen_20),
              child: Text(
                "加载中...",
                style: TextStyle(
                  color: FlutterColors.color_99,
                  fontSize: FlutterDimens.flutter_dimen_font_18,
                ),
              ),
            ),
            Opacity(opacity: 0.5,child: Text("哈哈"),),
          ],
        ),
      ),
    );
  }

}