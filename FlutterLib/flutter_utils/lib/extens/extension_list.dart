


import 'dart:convert';

import 'package:yc_flutter_utils/extens/validator_utils.dart';
import 'package:yc_flutter_utils/object/object_utils.dart';

extension ExtensionList on List {

  /// Transform list to json
  /// 将list转化为json字符串
  String toJsonString() {
    return jsonEncode(this);
  }

  /// 将list转化为json字符串，换行
  String getJsonPretty() {
    return JsonEncoder.withIndent('\t').convert(this);
  }

  /// Get total value of list of num (int/double)
  /// 获取num列表的总值(int/double)
  /// Example: [1,2,3,4] => 10
  num valueTotal() {
    num total = 0;
    if (this == null || this.isEmpty) return total;
    if (this[0] is num) for (var v in this) total += v;
    throw FormatException('Can only accepting list of num (int/double)');
  }

  /// Checks if data is null.
  /// 判断对象是否为null
  bool isNull() => ObjectUtils.isNull(this);

  /// Checks if data is null or Blank (Empty or only contains whitespace).
  /// 检查数据是否为空或空(空或只包含空格)
  bool isNullOrBlank() => ObjectUtils.isNullOrBlank(this);

  /// Checks if length of list is LOWER OR EQUAL to maxLength.
  /// 检查数据长度是否小于或等于maxLength
  bool isLengthLowerOrEqual(int maxLength) =>
      ValidatorUtils.isLengthLowerOrEqual(this, maxLength);

  /// Checks if length of list is GREATER than maxLength.
  /// 检查数据长度是否大于maxLength
  bool isLengthGreaterThan(int maxLength) =>
      ValidatorUtils.isLengthGreaterThan(this, maxLength);

  /// Checks if length of list is GREATER OR EQUAL to maxLength.
  /// 检查数据长度是否大于或等于maxLength
  bool isLengthGreaterOrEqual(int maxLength) =>
      ValidatorUtils.isLengthGreaterOrEqual(this, maxLength);

  /// Checks if length of list is EQUAL than maxLength.
  /// 检查数据长度是否等于maxLength
  bool isLengthEqualTo(int maxLength) =>
      ValidatorUtils.isLengthEqualTo(this, maxLength);

  /// Checks if length of list is BETWEEN minLength to maxLength.
  /// 检查数据长度是否在minLength到maxLength之间
  bool isLengthBetween(int minLength, int maxLength) =>
      ValidatorUtils.isLengthBetween(this, minLength, maxLength);

}
