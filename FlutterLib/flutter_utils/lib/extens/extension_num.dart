

import 'package:yc_flutter_utils/extens/validator_utils.dart';
import 'package:yc_flutter_utils/object/object_utils.dart';

extension ExtensionNum on num {

  /// Checks if data is null.
  bool isNull() => ObjectUtils.isNull(this);

  /// Checks if data is null or Blank (Empty or only contains whitespace).
  bool isNullOrBlank() => ObjectUtils.isNullOrBlank(this);

  /// Checks if num data is LOWER than comparedTo.
  bool isLowerThan(num compareTo) => this < compareTo;

  /// Checks if num data is GREATER than comparedTo.
  bool isGreaterThan(num compareTo) => this > compareTo;

  /// Checks if num data is EQUAL to compared one.
  bool isEqualTo(num compareTo) => this == compareTo;

  /// Checks if length of num is LOWER than maxLength.
  bool isLengthLowerThan(int maxLength) =>
      ValidatorUtils.isLengthLowerThan(this, maxLength);

  /// Checks if length of num is LOWER OR EQUAL to maxLength.
  bool isLengthLowerOrEqual(int maxLength) =>
      ValidatorUtils.isLengthLowerOrEqual(this, maxLength);

  /// Checks if length of num is GREATER than maxLength.
  bool isLengthGreaterThan(int maxLength) =>
      ValidatorUtils.isLengthGreaterThan(this, maxLength);

  /// Checks if length of num is GREATER OR EQUAL to maxLength.
  bool isLengthGreaterOrEqual(int maxLength) =>
      ValidatorUtils.isLengthGreaterOrEqual(this, maxLength);

  /// Checks if length of num is EQUAL than maxLength.
  bool isLengthEqualTo(int maxLength) =>
      ValidatorUtils.isLengthEqualTo(this, maxLength);

  /// Checks if length of num is BETWEEN minLength to maxLength.
  bool isLengthBetween(int minLength, int maxLength) =>
      ValidatorUtils.isLengthBetween(this, minLength, maxLength);
}
