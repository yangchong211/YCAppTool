

import 'package:flutter/material.dart';
import 'package:flutter_widget/notification/notification_callback.dart';
import 'package:flutter_widget/notification/notification_custom.dart';
import 'package:flutter_widget/res/color/flutter_colors.dart';

/// 消息通知组件工厂类，封装了 App 内消息通知的使用方式。
abstract class NotificationFactory {
  ///展示顶部普通样式通知
  static RiderNotification showTopNotification(BuildContext context,String text,
      {String btnText, String btnUrl, String icon, int displayTimeInSecs,
        NotificationClickCallback<RiderNotification> onClick,
        NotificationClickCallback<RiderNotification> onButtonClick}) {
    final builder = RiderNotificationBuilder()
        .setText(text)
        .setButtonText(btnText)
        .setButtonUrl(btnUrl)
        // .setIcon(icon ?? Images.top_notification_icon_warning_black)
        .setOnClick(onClick)
        .setOnButtonClick(onButtonClick)
        .setBackgroundColor(FlutterColors.color_63676E)
        .setPriority(NotificationPriority.normal)
        .setTemplateType(NotificationType.top);

    // 普通通知展示时间规则：无按钮则表示为即时消息，需要自动隐藏
    if (btnText == null || btnText.isEmpty) {
      builder.setDisplayTimeInSecs(5); // 设置5s自动隐藏
    }

    return builder.build(context).show();
  }

  /**
   * 展示顶部警告样式通知
   */
  static RiderNotification showTopWarningNotification(BuildContext context,String text,
      {String btnText, String btnUrl,
        NotificationClickCallback<RiderNotification> onClick,
        NotificationClickCallback<RiderNotification> onButtonClick}) {
    return RiderNotificationBuilder()
        .setText(text)
        .setButtonText(btnText)
        .setButtonUrl(btnUrl)
        // .setIcon(Images.top_notification_icon_warning)
        .setOnClick(onClick)
        .setOnButtonClick(onButtonClick)
        .setBackgroundColor(FlutterColors.color_63676E)
        .setPriority(NotificationPriority.high)
        .setTemplateType(NotificationType.top)
        .build(context)
        .show();
  }

  /**
   * 展示 XPanel 普通样式通知
   */
  static RiderNotification showXPanelNotification(BuildContext context,String text,
      {String btnText, String btnUrl, int btnAction,
        NotificationClickCallback<RiderNotification> onClick,
        NotificationClickCallback<RiderNotification> onButtonClick}) {
    return RiderNotificationBuilder()
        .setText(text)
        .setButtonText(btnText)
        .setButtonUrl(btnUrl)
        .setButtonAction(btnAction)
        .setOnClick(onClick)
        .setOnButtonClick(onButtonClick)
        .setBackgroundColor(FlutterColors.color_63676E)
        .setPriority(NotificationPriority.normal)
        .setTemplateType(NotificationType.xPanel)
        .build(context)
        .show();
  }

  /**
   * 展示 XPanel 5s倒计时通知
   */
  static RiderNotification showXPanelCountdownNotification(BuildContext context,String text,
      {String btnText, String btnUrl, int btnAction,
        NotificationClickCallback<RiderNotification> onClick,
        NotificationClickCallback<RiderNotification> onButtonClick}) {
    return RiderNotificationBuilder()
        .setText(text)
        .setButtonText(btnText)
        .setButtonUrl(btnUrl)
        .setButtonAction(btnAction)
        .setOnClick(onClick)
        .setOnButtonClick(onButtonClick)
        .setBackgroundColor(FlutterColors.color_63676E)
        .setPriority(NotificationPriority.medium)
        .setTemplateType(NotificationType.xPanel)
        .setDisplayTimeInSecs(5)
        .build(context)
        .show();
  }

  /**
   * 展示 XPanel 警告样式通知
   */
  static RiderNotification showXPanelWarningNotification(BuildContext context,String text,
      {String btnText, String btnUrl, int btnAction,
        NotificationClickCallback<RiderNotification> onClick,
        NotificationClickCallback<RiderNotification> onButtonClick}) {
    return RiderNotificationBuilder()
        .setText(text)
        .setButtonText(btnText)
        .setButtonUrl(btnUrl)
        .setButtonAction(btnAction)
        .setOnClick(onClick)
        .setOnButtonClick(onButtonClick)
        .setBackgroundColor(FlutterColors.color_63676E)
        .setPriority(NotificationPriority.high)
        .setTemplateType(NotificationType.xPanel)
        .build(context)
        .show();
  }

  /**
   * 展示中央弹窗通知
   */
  static RiderNotification showPopupNotification(BuildContext context,String text,
      {String imageUrl, String btnText, String btnUrl,
        NotificationClickCallback<RiderNotification> onClick,
        NotificationClickCallback<RiderNotification> onButtonClick}) {
    return RiderNotificationBuilder()
        .setText(text)
        .setButtonText(btnText)
        .setButtonUrl(btnUrl)
        .setOnClick(onClick)
        .setOnButtonClick(onButtonClick)
        .setBackgroundColor(FlutterColors.color_63676E)
        .setPriority(NotificationPriority.normal)
        .setTemplateType(NotificationType.popup)
        .build(context)
        .show();
  }
}
