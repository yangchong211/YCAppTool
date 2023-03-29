import 'package:flutter/cupertino.dart';
import 'package:yc_flutter_utils/log/log_utils.dart';

enum PatternState {
  none,   //无模式
  normal, //正常模式
  small,  //小屏模式
  overview, //全屏模式
}

class BusinessPattern extends ChangeNotifier {

  PatternState currentState = PatternState.none;

  void updateBusinessPatternState(PatternState state ,{bool refresh}) {
    if (currentState.index != state.index || refresh) {
      LogUtils.d('当前模式:$currentState');
      LogUtils.d('更新模式:$state');
      currentState = state;
      notifyListeners();
    }
  }
}
