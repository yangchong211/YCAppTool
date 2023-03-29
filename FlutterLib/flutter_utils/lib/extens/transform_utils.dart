

import 'package:yc_flutter_utils/extens/validator_utils.dart';
import 'package:yc_flutter_utils/num/num_utils.dart';
import 'package:yc_flutter_utils/object/object_utils.dart';

/// 转化工具类
class TransformUtils {

  /// Transform int value to binary
  /// 转换int值为二进制
  /// Example: 15 => 1111
  static String toBinary(int i) => i.toRadixString(2);

  /// Transform int value to binary
  /// 转换int值为二进制
  /// Example: 15 => 1111
  static int toBinaryInt(int i) => int.parse(i.toRadixString(2));

  /// Transform binary to int value
  /// 转换二进制为int值
  /// Example: 1111 => 15
  static int fromBinary(String binaryStr) => int.tryParse(binaryStr, radix: 2);

  /// Capitalize each word inside string
  /// 字符串内的每个单词都要大写
  /// Example: your name => Your Name, your name => Your name
  /// If First Only is `true`, the only letter get uppercase is the first letter
  static String capitalize(String s, {bool firstOnly = false}) {
    if (ObjectUtils.isNullOrBlank(s)) return null;
    if (firstOnly) return capitalizeFirst(s);

    List lst = s.split(' ');
    String newStr = '';
    for (var s in lst) newStr += capitalizeFirst(s);
    return newStr;
  }

  /// Uppercase first letter inside string and let the others lowercase
  /// 字符串的首字母大写，其他字母小写
  /// Example: your name => Your name
  static String capitalizeFirst(String s) {
    if (ObjectUtils.isNullOrBlank(s)) return null;
    return s[0].toUpperCase() + s.substring(1).toLowerCase();
  }

  /// Remove all whitespace inside string
  /// 删除字符串内的所有空格
  /// Example: your name => yourname
  static String removeAllWhitespace(String s) {
    if (ObjectUtils.isNullOrBlank(s)) {
      return null;
    }
    return s.replaceAll(' ', '');
  }

  /// Camelcase string
  /// Example: your name => yourName
  static String camelCase(String s) {
    if (ObjectUtils.isNullOrBlank(s)) {
      return null;
    }
    return s[0].toLowerCase() + removeAllWhitespace(capitalize(s)).substring(1);
  }

  /// Extract numeric value of string
  /// 提取字符串的数值
  /// Example: OTP 12312 27/04/2020 => 1231227042020ß
  /// If firstword only is true, then the example return is "12312" (first found numeric word)
  static String numericOnly(String s, {bool firstWordOnly = false}) {
    String numericOnlyStr = '';
    for (var i = 0; i < s.length; i++) {
      if (ValidatorUtils.isNumericOnly(s[i])) numericOnlyStr += s[i];
      if (firstWordOnly && numericOnlyStr.isNotEmpty && s[i] == " ") break;
    }
    return numericOnlyStr;
  }

}
