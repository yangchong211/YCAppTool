import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/res/flutter_colors.dart';
import 'package:yc_flutter_utils/res/flutter_dimens.dart';





///加载弹窗动画
class StatusLoadingDialog extends StatefulWidget {

  /// 加载中的文字
  final String loadingText;
  /// 是否点击控件外部弹窗消失
  final bool outsideDismiss;
  /// 监听弹窗消失
  final Function dismissListener;

  StatusLoadingDialog(
      {Key key,
      this.loadingText = "loading...",
      this.outsideDismiss = false,
      this.dismissListener})
      : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return _LoadingDialogState();
  }
}

class _LoadingDialogState extends State<StatusLoadingDialog> {

  _dismissDialog() {
    Navigator.of(context).pop();
    widget.dismissListener();
  }

  @override
  Widget build(BuildContext context) {
    return new GestureDetector(
      onTap: widget.outsideDismiss ? _dismissDialog() : null,
      child: Material(
        type: MaterialType.transparency,
        child: GestureDetector(
          onTap: () {

          },
          child: new Center(
            child:new Container(
                width: FlutterDimens.flutter_dimen_100,
                height: FlutterDimens.flutter_dimen_100,
                decoration: ShapeDecoration(
                  color: Color(0xffffffff),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.all(
                      Radius.circular(FlutterDimens.flutter_dimen_8),
                    ),
                  ),
                ),
                child: new Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: <Widget>[
                    SizedBox(
                      width: FlutterDimens.flutter_dimen_35,
                      height: FlutterDimens.flutter_dimen_35,
                      child: new CircularProgressIndicator(
                        strokeWidth: FlutterDimens.flutter_dimen_3,
                        valueColor: AlwaysStoppedAnimation<Color>(
                            Color.fromARGB(255, 51, 51, 51)),
                      ),
                    ),
                    new Padding(
                      padding: EdgeInsets.only(
                        top: FlutterDimens.flutter_dimen_20,
                      ),
                      child: new Text(
                        widget.loadingText,
                        style: new TextStyle(
                          fontSize: FlutterDimens.flutter_dimen_font_14,
                          color: FlutterColors.color_00,
                        ),
                      ),
                    ),
                  ],
                ),
            ),
          ),
        ),
      ),
    );
  }
}
