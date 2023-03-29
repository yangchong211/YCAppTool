

import 'package:yc_flutter_utils/extens/transform_utils.dart';
import 'package:yc_flutter_utils/extens/validator_utils.dart';
import 'package:yc_flutter_utils/object/object_utils.dart';

/// Num扩展也适用于int
extension ExtensionInt on int {

  /// Checks if int value is Palindrome.
  /// 检查int是否为回文
  bool isPalindrome() {
    var numericOnly = TransformUtils.numericOnly(this.toString());
    return ValidatorUtils.isPalindrome(numericOnly);
  }

  /// Checks if all int value are same.
  /// 检查所有数据是否具有相同的值
  /// Example: 111111 -> true
  bool isOneAKind() => ValidatorUtils.isOneAKind(this);

  /// Transform int value to binary string
  /// 转换int值为二进制
  String toBinary() => TransformUtils.toBinary(this);

  /// Transform int value to binary int
  /// 转换int值为二进制int
  int toBinaryInt() => int.parse(TransformUtils.toBinary(this));

  /// Transform int value to binary string
  /// 转换int值为二进制字符串
  /// Example: 1111 => 15
  int fromBinary() => TransformUtils.fromBinary(this.toString());

}
