import 'dart:async';
import 'dart:collection';

import 'package:yc_flutter_utils/log/log_utils.dart';

/// 队列task工具类
class TaskQueueUtils {
  //队列任务
  Queue<_TaskInfo> _taskList = Queue();
  //是否有正在进行的task
  _TaskInfo _currentTask;

  /// 添加任务，并指定最多任务数量
  void addTask(Future Function() performSelector , int max) {
    //最多max个任务
    if (_taskList.length >= max) {
      //大于max个后，从头开始删除、保持队列任务是最新的。
      LogUtils.d("TripQueueUtils - 移除队列");
      _taskList.removeFirst();
    }
    _TaskInfo taskInfo = _TaskInfo(performSelector);

    /// 添加到任务队列
    LogUtils.d("TripQueueUtils - 加入队列");
    _taskList.add(taskInfo);

    LogUtils.d("TripQueueUtils - 加入队列后长度 ${_taskList.length}");
    /// 触发任务
    _doTask();
  }

  /// 取消执行任务
  void cancelTasks() {
    _taskList.clear();
    _currentTask = null;
  }

  _doTask() async {
    /// 判断正在进行的task是否不为空
    if (_currentTask != null) {
      return;
    }
    /// 队列任务为空
    if (_taskList.isEmpty){
      return;
    }

    LogUtils.d("TripQueueUtils -出队列取任务 ${_taskList.length}");
    _currentTask = _taskList.removeFirst();

    /// 执行任务
    LogUtils.d("TripQueueUtils - 执行任务");
    try {
      await _currentTask.performSelector();
    } catch (e) {
      LogUtils.e("TripQueueUtils - ${e.toString()}");
    } finally{
      _currentTask = null;
    }

    LogUtils.d("TripQueueUtils - 出队列取任务后长度 ${_taskList.length}");
    /// 递归执行任务
    _doTask();
  }
}

class _TaskInfo {
  Future Function() performSelector;
  _TaskInfo(this.performSelector);
}
