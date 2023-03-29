import 'package:flutter/widgets.dart';

import 'notification_custom.dart';

abstract class NotificationChangeNotifier extends ChangeNotifier {

  RiderNotification _notification;
  NotificationType _type;
  NotificationChangeEvent _changeEvent;

  NotificationChangeNotifier({NotificationType type}) {
    this._type = type;
  }

  void update(RiderNotification notification
      /*, NotificationChangeEvent changeEvent*/) {
    this._notification = notification;
    // this._changeEvent = changeEvent;
    notifyListeners();
  }

  RiderNotification get notification => _notification;

  NotificationChangeEvent get changeEvent => _changeEvent;

  NotificationType get type => _type;
}

class TopNotification extends NotificationChangeNotifier {
  TopNotification() : super(type: NotificationType.top);
}

class XPanelNotification extends NotificationChangeNotifier {
  XPanelNotification() : super(type: NotificationType.xPanel);
}

class PopupNotification extends NotificationChangeNotifier {
  PopupNotification() : super(type: NotificationType.popup);
}

enum NotificationChangeEvent {
  added,
  removed,
}
