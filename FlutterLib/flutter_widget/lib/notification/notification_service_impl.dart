import 'dart:collection';
import 'package:collection/collection.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_widget/notification/notification_change_notifier.dart';
import 'package:flutter_widget/notification/notification_service.dart';
import 'package:flutter_widget/notification/notification_custom.dart';
import 'package:provider/provider.dart';
import 'package:yc_flutter_utils/log/log_utils.dart';



/**
 * 消息通知组件服务：负责处理多类型消息的优先级，分发当前最高优先级的通知给 UI 进行展示
 */
class NotificationServiceImpl implements NotificationService<RiderNotification> {

  final _notificationMap = HashMap<NotificationType, PriorityQueue<RiderNotification>>();

  final BuildContext _context;

  NotificationServiceImpl(this._context);

  BuildContext get context => _context;

  RiderNotification _peekNotification(NotificationType type) {
    final PriorityQueue<RiderNotification> queue = _getQueueOf(type);

    if (queue.isNotEmpty) {
      return queue.first;
    } else {
      return null;
    }
  }

  void _insertNotification(RiderNotification notification) {
    final PriorityQueue<RiderNotification> queue = _getQueueOf(notification.templateType);
    queue.add(notification);
  }

  RiderNotification _removeNotification(RiderNotification notification) {
    final PriorityQueue<RiderNotification> queue = _getQueueOf(notification.templateType);
    final removed = queue.remove(notification);

    if (removed) {
      return notification;
    } else {
      return null;
    }
  }

  RiderNotification _findNotification(String id) {
    // 解析 type
    final type = RiderNotification.parseType(id);
    if (id == null || type == null) {
      LogUtils.d("_findNotification error: id or type is null!");
      return null;
    }

    // 查找 Notification
    final queue = _getQueueOf(type);
    final notification = queue.toList().firstWhere((element) => (id == element.id));
    return notification;
  }

  PriorityQueue<RiderNotification> _getQueueOf(NotificationType type) {
    PriorityQueue<RiderNotification> queue = _notificationMap[type];
    if (queue == null) {
      queue = _createNewQueue();
      _notificationMap[type] = queue;
    }

    return queue;
  }

  PriorityQueue<RiderNotification> _createNewQueue() {
    return HeapPriorityQueue<RiderNotification>((RiderNotification a, RiderNotification b) {
      return a.compareTo(b);
    });
  }

  void _notifyNotificationChangedByType(RiderNotification notification, NotificationType type) {
    NotificationChangeNotifier changeNotifier;
    switch (type) {
      case NotificationType.top:
        changeNotifier = Provider.of<TopNotification>(context, listen: false);
        break;
      case NotificationType.xPanel:
        changeNotifier = Provider.of<XPanelNotification>(context, listen: false);
        break;
      case NotificationType.popup:
        changeNotifier = Provider.of<PopupNotification>(context, listen: false);
        break;
    }

    changeNotifier?.update(notification);
  }

  @override
  void show(RiderNotification notification) {
    // 插入队列中进行排序
    _insertNotification(notification);

    final type = notification.templateType;
    // 取出队首元素
    final RiderNotification first = _peekNotification(type);

    if (null == first) {
      LogUtils.d("show() error: first notification is null!");
      return;
    }

    // 通知外部监听者 Notification 变更
    _notifyNotificationChangedByType(notification, type);
  }

  @override
  void cancel(RiderNotification notification) {
    final type = notification.templateType;
    // 记录队首元素
    final first = _peekNotification(type);
    // 从队列中移除通知
    final RiderNotification removed = _removeNotification(notification);

    // xpanel 类型 notification 取消时无须向业务侧更新，其他类型向业务侧更新
    if (type == NotificationType.top || type == NotificationType.popup) {
      if (null == removed) {
        LogUtils.d("cancel() error: removed notification is null!");
        return;
      }

      LogUtils.d("cancel first: $first removed: $removed");
      // 移除的是非队首元素，则不需要通知外部
      if (first != null && first != removed) {
        return;
      }

      // 否则，则是队首元素被移除
      // 从队列中取出新的元素
      final RiderNotification newFirst = _peekNotification(type);

      // 通知外部监听者 Notification 变更
      _notifyNotificationChangedByType(newFirst, type);
    }
  }

  @override
  void cancelById(String id) {
    RiderNotification notification = _findNotification(id);
    notification?.cancel();
  }
}
