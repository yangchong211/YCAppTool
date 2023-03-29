import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_widget/notification/notification_callback.dart';
import 'package:flutter_widget/notification/notification_change_notifier.dart';
import 'package:flutter_widget/notification/notification_custom.dart';
import 'package:provider/provider.dart';



/// 顶部通知栏 Widget
class TopNotificationWidget extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _TopNotificationState();
  }
}

class _TopNotificationState extends State<TopNotificationWidget> {
  RiderNotification _curNotification;

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<TopNotification>(
      builder: (context, notification, child) {
        _curNotification = notification?.notification;
        return _buildWidget(context, _curNotification, child);
      },
    );
  }

  Widget _buildWidget(BuildContext context, RiderNotification notification, Widget child) {
    return Offstage(
      offstage: notification == null,
      child: _TopNotificationBannerWidget(
        notification,
        _handleClick,
        _handleButtonClick,
      ),
    );
  }

  void _handleClick(BuildContext context, RiderNotification notification) {

    if (notification.onClick != null) {
      notification.onClick(context, notification);
    }

    _curNotification.cancel();
  }

  void _handleButtonClick(BuildContext context, RiderNotification notification) {

    if (notification.onButtonClick != null) {
      notification.onButtonClick(context, notification);
    }

    _curNotification.cancel();
  }
}

class _TopNotificationBannerWidget extends StatelessWidget {
  final RiderNotification _notification;
  final NotificationClickCallback<RiderNotification> handleClick;
  final NotificationClickCallback<RiderNotification> handleButtonClick;

  final _containerPadding = EdgeInsets.symmetric(vertical: 20.0, horizontal: 16.0);
  final _buttonMargin = EdgeInsets.only(left: 5.0);
  final _textMargin = EdgeInsets.only(left: 10.0);

  _TopNotificationBannerWidget(this._notification, this.handleClick, this.handleButtonClick) {

  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
        onTap: () => handleClick(context, _notification),
        child: Container(
          padding: _containerPadding,
          color: _notification.backgroundColor,
          child: _buildWidget(context),
        ));
  }

  Widget _buildWidget(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        Image.asset(_notification.icon, width: 44, height: 44, cacheWidth: 44, cacheHeight: 44),
        Expanded(
          child: _createText(context, _notification),
        ),
        Container(
          margin: _buttonMargin,
          child: _createButton(context, _notification),
        ),
      ],
    );
  }

  Widget _createText(BuildContext context, RiderNotification notification) {
    return Container(
      alignment: Alignment.centerLeft,
      margin: _textMargin,
      child: Text(
        notification.text,
        maxLines: 2,
        style: TextStyle(fontSize: 16, color: notification.textColor),
      ),
    );
  }

  Widget _createButton(BuildContext context, RiderNotification notification) {
    if (notification.buttonText == null || notification.buttonText.isEmpty) {
      return Container();
    } else {
      return FlatButton(
        child: new Text(notification.buttonText),
        onPressed: (){
          handleButtonClick(context, notification);
        },
      );
    }
  }
}
