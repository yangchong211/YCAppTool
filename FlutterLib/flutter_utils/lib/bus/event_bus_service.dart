
import 'dart:async';

typedef void EventCallback(arg);

/// 消息结构体
class EventMessage {

  final String eventName;

  final dynamic arguments;

  EventMessage(this.eventName, {this.arguments});

}

/// 消息通知服务
class EventBusService {

  factory EventBusService() => _getInstance();

  static EventBusService get instance => _getInstance();

  static EventBusService _instance;

  EventBusService._internal();

  static EventBusService _getInstance() {
    if (_instance == null) {
      _instance = EventBusService._internal();
    }
    return _instance;
  }

  EventBus _eBus = EventBus();

  EventBus get eventBus {
    return _eBus;
  }
}


class EventBus {

  StreamController _streamController;

  StreamController get streamController => _streamController;

  EventBus({bool sync = false})
      : _streamController = StreamController.broadcast(sync: sync);

  EventBus.customController(StreamController controller)
      : _streamController = controller;

  ///监听事件
  Stream<T> on<T>() {
    if (T == dynamic) {
      return streamController.stream;
    } else {
      return streamController.stream.where((event) => event is T).cast<T>();
    }
  }

  ///添加事件
  void fire(event) {
    streamController.add(event);
  }

  ///移除事件
  void destroy() {
    _streamController.close();
  }
}

