
import 'package:yc_flutter_utils/screen/screen_adaptation_utils.dart';

/// 扩展类型
extension ScreenAdaptationExt on num {

  double get dp => ScreenAdaptationUtils().pt2dp(this);

  double get sp => ScreenAdaptationUtils().pt2sp(this);

}
