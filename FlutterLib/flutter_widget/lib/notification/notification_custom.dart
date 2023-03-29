import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter_widget/notification/notification_callback.dart';
import 'package:flutter_widget/notification/notification_service.dart';
import 'package:flutter_widget/notification/notification_service_impl.dart';
import 'package:flutter_widget/res/color/flutter_colors.dart';



/**
 * 消息通知栏结构定义
 * http://wiki.intra.xiaojukeji.com/pages/viewpage.action?pageId=592212887
 */
class RiderNotification implements Comparable<RiderNotification> {
  static final _ID_SEPARATOR = "_";

  BuildContext _context;
  /** 唯一标识 */
  String _id;

  /** 内容属性 */
  String _text;
  String _buttonText;
  String _buttonUrl;
  int _buttonAction;
  String _icon;

  /** 样式属性 */
  Color _textColor;
  Color _backgroundColor;

  /** 交互属性 */
  int _displayTimeInSecs; // 显示时长（单位：秒）
  int _timestampCreated; // Notification 创建时的时间戳
  NotificationClickCallback<RiderNotification> _onButtonClick; // 按钮点击回调
  NotificationClickCallback<RiderNotification> _onClick; // 通知栏点击回调

  /** 优先级 */
  NotificationPriority _priority;

  /** 模板类型 */
  NotificationType _templateType;

  BuildContext get context => _context;

  String get id => _id;

  String get text => _text;

  String get buttonText => _buttonText;

  String get buttonUrl => _buttonUrl;

  int get buttonAction => _buttonAction;

  String get icon => _icon;

  Color get textColor => _textColor;

  Color get backgroundColor => _backgroundColor;

  int get displayTimeInSecs => _displayTimeInSecs;

  int get timestampCreated => _timestampCreated;

  NotificationClickCallback<RiderNotification> get onButtonClick => _onButtonClick;

  NotificationClickCallback<RiderNotification> get onClick => _onClick;

  NotificationPriority get priority => _priority;

  NotificationType get templateType => _templateType;

  RiderNotification(RiderNotificationBuilder builder,BuildContext context) {
    this._context = context;
    this._id = _generateId(builder);

    this._text = builder.text;
    this._buttonText = builder.buttonText;
    this._buttonUrl = builder.buttonUrl;
    this._buttonAction = builder.buttonAction;
    this._icon = builder._icon;

    this._textColor = builder.textColor;
    this._backgroundColor = builder.backgroundColor;

    this._displayTimeInSecs = builder.displayTimeInSecs;
    this._timestampCreated = builder.timestampCreated;
    this._onButtonClick = builder.onButtonClick;
    this._onClick = builder.onClick;

    this._priority = builder.priority;
    this._templateType = builder.templateType;
  }

  @override
  int compareTo(other) {
    if (this.priority.index != other.priority.index) {
      // 先按优先级排序（高优先级在前）
      return other.priority.index - this.priority.index;
    } else {
      // 优先级相同时
      // 需要定时隐藏的排在前面
      if (this.needAutoDismiss() && !other.needAutoDismiss()) {
        return -1;
      } else if (!this.needAutoDismiss() && other.needAutoDismiss()) {
        return 1;
      } else {
        // 其他情况，按创建时间排序（先创建的排在前面）
        return this.timestampCreated - other.timestampCreated;
      }
    }
  }

  @override
  operator ==(Object other) {
    if (other == null || !(other is RiderNotification)) return false;

    RiderNotification o = other;
    return this.id == o.id;
  }

  bool needAutoDismiss() {
    return this.displayTimeInSecs > 0;
  }

  NotificationService<RiderNotification> _notificationService;

  RiderNotification show() {
    if(_notificationService==null){
      createService();
    }
    _notificationService.show(this);
    return this;
  }

  void cancel() {
    if(_notificationService==null){
      createService();
    }
    _notificationService.cancel(this);
  }

  void createService(){
    _notificationService = new NotificationServiceImpl(context);
  }

  static String _generateId(RiderNotificationBuilder builder) => ["${builder.templateType}", "${builder.priority}", "${builder.timestampCreated}"].join(_ID_SEPARATOR);

  /**
   * 通过 ID 解析 Notification 的 Type
   * 规则参见 _generateId(RiderNotificationBuilder) 方法实现
   */
  static NotificationType parseType(String id) {
    if (id == null || id.isEmpty) return null;

    final fields = id.split(_ID_SEPARATOR);
    final typeName = fields[0];
    return NotificationType.values.firstWhere((element) => element.toString() == typeName, orElse: null);
  }
}

enum NotificationPriority { normal, medium, high }

enum NotificationType { top, xPanel, popup }

class RiderNotificationBuilder {
  /** 内容属性 */
  String _text = "";
  String _buttonText = "";
  String _buttonUrl = "";
  int _buttonAction = 0;
  String _icon = "";

  /** 样式属性 */
  Color _textColor = FlutterColors.color_EE;
  Color _backgroundColor = FlutterColors.color_66;

  /** 交互属性 */
  int _displayTimeInSecs = -1; // 显示时长（单位：秒），默认常驻
  //TODO 初始化为当前时间戳，使用与服务器同步时间的统一 Clock 工具
  int _timestampCreated = DateTime.now().millisecondsSinceEpoch; // Notification 创建时的时间戳
  NotificationClickCallback<RiderNotification> _onButtonClick; // 按钮点击回调
  NotificationClickCallback<RiderNotification> _onClick; // 通知栏点击回调

  /** 优先级 */
  NotificationPriority _priority = NotificationPriority.normal;

  /** 模板类型 */
  NotificationType _templateType = NotificationType.top;

  String get text => _text;

  RiderNotificationBuilder setText(String value) {
    _text = value;
    return this;
  }

  String get buttonText => _buttonText;

  RiderNotificationBuilder setButtonText(String value) {
    _buttonText = value;
    return this;
  }

  String get buttonUrl => _buttonUrl;

  RiderNotificationBuilder setButtonUrl(String value) {
    _buttonUrl = value;
    return this;
  }

  int get buttonAction => _buttonAction;

  RiderNotificationBuilder setButtonAction(int value) {
    _buttonAction = value;
    return this;
  }

  String get icon => _icon;

  RiderNotificationBuilder setIcon(String value) {
    _icon = value;
    return this;
  }

  Color get textColor => _textColor;

  RiderNotificationBuilder setTextColor(Color value) {
    _textColor = value;
    return this;
  }

  Color get backgroundColor => _backgroundColor;

  RiderNotificationBuilder setBackgroundColor(Color value) {
    _backgroundColor = value;
    return this;
  }

  int get displayTimeInSecs => _displayTimeInSecs;

  RiderNotificationBuilder setDisplayTimeInSecs(int value) {
    _displayTimeInSecs = value;
    return this;
  }

  int get timestampCreated => _timestampCreated;

  RiderNotificationBuilder setTimestampCreated(int value) {
    _timestampCreated = value;
    return this;
  }

  NotificationClickCallback<RiderNotification> get onButtonClick => _onButtonClick;

  RiderNotificationBuilder setOnButtonClick(NotificationClickCallback<RiderNotification> value) {
    _onButtonClick = value;
    return this;
  }

  NotificationClickCallback<RiderNotification> get onClick => _onClick;

  RiderNotificationBuilder setOnClick(NotificationClickCallback<RiderNotification> value) {
    _onClick = value;
    return this;
  }

  NotificationPriority get priority => _priority;

  RiderNotificationBuilder setPriority(NotificationPriority value) {
    _priority = value;
    return this;
  }

  NotificationType get templateType => _templateType;

  RiderNotificationBuilder setTemplateType(NotificationType value) {
    _templateType = value;
    return this;
  }

  RiderNotification build(BuildContext context) {
    return RiderNotification(this,context);
  }
}
