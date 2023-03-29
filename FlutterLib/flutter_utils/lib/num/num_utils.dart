


import 'package:yc_flutter_utils/num/decimal.dart';
import 'package:yc_flutter_utils/object/object_utils.dart';

/// num工具类
class NumUtils {

  /// Checks if string is int or double.
  /// 检查字符串是int还是double
  static bool isNum(String s) {
    if (ObjectUtils.isNull(s)){
      return false;
    }
    return num.tryParse(s) is num ?? false;
  }

  /// 将数字字符串转num，数字保留x位小数
  static num getNumByValueString(String valueStr, {int fractionDigits}) {
    double value = double.tryParse(valueStr);
    return fractionDigits == null ? value
        : getNumByValueDouble(value, fractionDigits);
  }

  /// 浮点数字保留x位小数
  static num getNumByValueDouble(double value, int fractionDigits) {
    if (value == null) return null;
    String valueStr = value.toStringAsFixed(fractionDigits);
    return fractionDigits == 0
        ? int.tryParse(valueStr)
        : double.tryParse(valueStr);
  }

  /// get int by value string
  /// 将数字字符串转int
  static int getIntByValueString(String valueStr, {int defValue = 0}) {
    return int.tryParse(valueStr) ?? defValue;
  }

  /// get double by value str.
  /// 数字字符串转double
  static double getDoubleByValueString(String valueStr, {double defValue = 0}) {
    return double.tryParse(valueStr) ?? defValue;
  }

  /// isZero
  /// 判断是否是否是0
  static bool isZero(num value) {
    return value == null || value == 0;
  }


  /// add (without loosing precision).
  /// 两个数相加（防止精度丢失）
  static double addNum(num a, num b) {
    return addDec(a, b).toDouble();
  }

  /// subtract (without loosing precision).
  /// 两个数相减（防止精度丢失）
  static double subtractNum(num a, num b) {
    return subtractDec(a, b).toDouble();
  }

  /// multiply (without loosing precision).
  /// 两个数相乘（防止精度丢失）
  static double multiplyNum(num a, num b) {
    return multiplyDec(a, b).toDouble();
  }

  /// divide (without loosing precision).
  /// 两个数相除（防止精度丢失）
  static double divideNum(num a, num b) {
    return divideDec(a, b).toDouble();
  }

  /// 加 (精确相加,防止精度丢失).
  /// add (without loosing precision).
  static Decimal addDec(num a, num b) {
    return addDecString(a.toString(), b.toString());
  }

  /// 减 (精确相减,防止精度丢失).
  /// subtract (without loosing precision).
  static Decimal subtractDec(num a, num b) {
    return subtractDecString(a.toString(), b.toString());
  }

  /// 乘 (精确相乘,防止精度丢失).
  /// multiply (without loosing precision).
  static Decimal multiplyDec(num a, num b) {
    return multiplyDecString(a.toString(), b.toString());
  }

  /// 除 (精确相除,防止精度丢失).
  /// divide (without loosing precision).
  static Decimal divideDec(num a, num b) {
    return divideDecString(a.toString(), b.toString());
  }

  /// 余数
  static Decimal remainder(num a, num b) {
    return remainderDecString(a.toString(), b.toString());
  }

  /// Relational less than operator.
  /// 关系小于运算符。判断a是否小于b
  static bool lessThan(num a, num b) {
    return lessThanDecString(a.toString(), b.toString());
  }

  /// Relational less than or equal operator.
  /// 关系小于或等于运算符。判断a是否小于或者等于b
  static bool thanOrEqual(num a, num b) {
    return thanOrEqualDecString(a.toString(), b.toString());
  }

  /// Relational greater than operator.
  /// 关系大于运算符。判断a是否大于b
  static bool greaterThan(num a, num b) {
    return greaterThanDecString(a.toString(), b.toString());
  }

  /// Relational greater than or equal operator.
  static bool greaterOrEqual(num a, num b) {
    return greaterOrEqualDecString(a.toString(), b.toString());
  }

  /// 两个数相加（防止精度丢失）
  static Decimal addDecString(String a, String b) {
    return Decimal.parse(a) + Decimal.parse(b);
  }

  /// 减
  static Decimal subtractDecString(String a, String b) {
    return Decimal.parse(a) - Decimal.parse(b);
  }

  /// 乘
  static Decimal multiplyDecString(String a, String b) {
    return Decimal.parse(a) * Decimal.parse(b);
  }

  /// 除
  static Decimal divideDecString(String a, String b) {
    return Decimal.parse(a) / Decimal.parse(b);
  }

  /// 余数
  static Decimal remainderDecString(String a, String b) {
    return Decimal.parse(a) % Decimal.parse(b);
  }

  /// Relational less than operator.
  /// 判断a是否小于b
  static bool lessThanDecString(String a, String b) {
    return Decimal.parse(a) < Decimal.parse(b);
  }

  /// Relational less than or equal operator.
  /// 判断a是否小于或者等于b
  static bool thanOrEqualDecString(String a, String b) {
    return Decimal.parse(a) <= Decimal.parse(b);
  }

  /// Relational greater than operator.
  /// 判断a是否大于b
  static bool greaterThanDecString(String a, String b) {
    return Decimal.parse(a) > Decimal.parse(b);
  }

  /// Relational greater than or equal operator.
  static bool greaterOrEqualDecString(String a, String b) {
    return Decimal.parse(a) >= Decimal.parse(b);
  }

  /// Checks if num a LOWER than num b.
  /// 检查num a是否小于num b。
  static bool isLowerThan(num a, num b) => a < b;

  /// Checks if num a GREATER than num b.
  /// 检查num a是否大于num b。
  static bool isGreaterThan(num a, num b) => a > b;

  /// Checks if num a EQUAL than num b.
  /// 检查num a是否等于num b。
  static bool isEqual(num a, num b) => a == b;

}
