library flutter_widget;

import 'package:yc_flutter_utils/screen/screen_adaptation_utils.dart';
import 'package:yc_flutter_utils/screen/screen_adaption.dart';

/// A Calculator.
class FlutterWidget {

  static void init() {
    // 初始化屏幕适配
    ScreenAdaptationUtils.init(ScreenAdaptation.none());
  }

}


