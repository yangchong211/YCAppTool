

import 'dart:async';

import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/locator/get_it.dart';
import 'package:yc_flutter_utils/log/log_utils.dart';
import 'package:yc_flutter_utils/timer/task_queue_utils.dart';

// GetIt serviceLocator = GetIt.instance;
PollingService Function() pollingService = () => GetIt.instance.get<PollingService>();


class PollingPage extends StatefulWidget {

  @override
  State<StatefulWidget> createState() {
    return new _PageState();
  }
}

class _PageState extends State<PollingPage> {

  String state = "无状态";
  PollingService _pollingService = pollingService();

  @override
  void initState() {
    super.initState();
    // PollingService _pollingService = serviceLocator<PollingService>();
    _pollingService.registerOrderChangedFunction(changedFunction);
  }

  @override
  void dispose() {
    super.dispose();
    // PollingService _pollingService = serviceLocator<PollingService>();
    _pollingService.unregisterOrderChangedFunction(changedFunction);
    //解绑操作
    // serviceLocator.resetLazySingleton<PollingService>();
    stop();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: new AppBar(
        title: new Text("TaskQueueUtils 请求队列"),
        centerTitle: true,
      ),
      body: new ListView(
        children: <Widget>[
          new Text("轮训的状态 : $state"),
          MaterialButton(
            onPressed: start,
            child: new Text("点击开始轮训"),
            color: Colors.cyan,
          ),
          MaterialButton(
            onPressed: stop,
            child: new Text("点击停止轮训"),
            color: Colors.cyan,
          ),
          MaterialButton(
            onPressed: changedFunction,
            child: new Text("点击刷新通知"),
            color: Colors.cyan,
          ),
        ],
      ),
    );
  }

  void start() {
    // PollingService _pollingService = serviceLocator<PollingService>();
    _pollingService.startPolling();
  }

  void stop() {
    // PollingService _pollingService = serviceLocator<PollingService>();
    _pollingService.stopPolling();
  }

  void changedFunction() {
    // PollingService _pollingService = serviceLocator<PollingService>();
    _pollingService.notify();
  }
}


class PollingServiceImpl extends PollingService {

  //创建请求队列
  TaskQueueUtils _tripQueue = TaskQueueUtils();
  String tag = "PollingServiceImpl";
  Timer _timer;
  List<Function()> _orderChangedFunctionList = List();

  @override
  Future fetchTrip() async{
    LogUtils.d("请求数据-开始",tag: tag);
    return await loadData();
  }

  @override
  void registerOrderChangedFunction(Function tripChangedFunction) {
    _orderChangedFunctionList.add(tripChangedFunction);
    LogUtils.d("注册监听变化事件",tag: tag);
  }

  @override
  void restartPolling() {
    LogUtils.d("重启轮询",tag: tag);
    stopPolling();
    startPolling();
    _addTripQueue();
  }

  @override
  void startPolling() {
    if (_timer == null) {
      LogUtils.d("开启轮询",tag: tag);
      Duration timeout = Duration(seconds: 3);
      _timer = Timer.periodic(timeout, (timer) {
        _addTripQueue();
      });
    }
  }

  @override
  void stopPolling() {
    if (_timer != null && _timer.isActive) {
      LogUtils.d("停止轮询",tag: tag);
      //取消队列
      _tripQueue.cancelTasks();
      _timer.cancel();
      _timer = null;
    }
  }

  @override
  void unregisterOrderChangedFunction(Function tripChangedFunction) {
    _orderChangedFunctionList.remove(tripChangedFunction);
    LogUtils.d("解绑监听变化事件",tag: tag);
  }

  void _addTripQueue() {
    LogUtils.d("TripQueueUtils - 加入队列",tag: tag);
    _tripQueue.addTask(() async {
      return await fetchTrip();
    },3);
  }

  Future loadData() async {
    LogUtils.d("请求数据-结束",tag: tag);
    return new Future.value(true);
  }

  /// 通知发生变化
  void notifyOrderChangedListener() {
    LogUtils.d("通知订单发生变化 ${_orderChangedFunctionList.length}",tag: tag);
    _orderChangedFunctionList.forEach((function) {
      function.call();
    });
  }

  @override
  void notify() {
    notifyOrderChangedListener();
  }

}

abstract class PollingService {

  /// 启动轮询
  void startPolling();

  /// 暂停轮询
  void stopPolling();

  /// 重启轮询，立马请求trip接口
  void restartPolling();

  /// 主动拉取行程
  Future fetchTrip();

  /// 注册订单发生变化监听器
  void registerOrderChangedFunction(Function tripChangedFunction);

  /// 注销订单发生变化监听器
  void unregisterOrderChangedFunction(Function tripChangedFunction);

  void notify();
}