
import 'package:yc_flutter_utils/mvp/i_model.dart';

abstract class AbstractModel implements IModel {

  String _tag;

  String get tag => _tag;

  ///构造方法中生成唯一的tag 用于取消网络请求
  AbstractModel() {
    _tag = '${DateTime.now().millisecondsSinceEpoch}';
  }

}