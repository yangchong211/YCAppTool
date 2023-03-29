
import 'package:yc_flutter_utils/extens/transform_utils.dart';
import 'package:yc_flutter_utils/object/object_utils.dart';
import 'package:yc_flutter_utils/regex/regex_constants.dart';
import 'package:yc_flutter_utils/regex/regex_utils.dart';

/// 验证器工具类
class ValidatorUtils {

  /// 检查字符串是否只包含数字
  /// 只有数值不接受double数据类型的
  static bool isNumericOnly(String s) =>
      RegexUtils.hasMatch(s, RegexConstants.numericOnly);

  /// 检查字符串是否只包含字母。(没有空格)
  static bool isAlphabetOnly(String s) =>
      RegexUtils.hasMatch(s, RegexConstants.alphabetOnly);

  /// 检查字符串是否为布尔值
  static bool isBool(String s) {
    if (ObjectUtils.isNull(s)) {
      return false;
    }
    return (s == 'true' || s == 'false');
  }

  /// 检查string是否为vector文件
  static bool isVector(String s) =>
      RegexUtils.hasMatch(s, RegexConstants.vector);

  /// Checks if string is an image file.
  /// 检查字符串是否为图像文件
  static bool isImage(String s) =>
      RegexUtils.hasMatch(s, RegexConstants.image);

  /// Checks if string is an audio file.
  /// 检查字符串是否为音频文件
  static bool isAudio(String s) =>
      RegexUtils.hasMatch(s, RegexConstants.audio);

  /// Checks if string is an video file.
  /// 检查字符串是否为视频文件
  static bool isVideo(String s) =>
      RegexUtils.hasMatch(s, RegexConstants.video);

  /// Checks if string is an txt file.
  /// 检查字符串是否为txt文本文件
  static bool isTxt(String s) =>
      RegexUtils.hasMatch(s, RegexConstants.txt);

  /// Checks if string is an Doc file.
  /// 检查字符串是否为doc文件
  static bool isDocument(String s) =>
      RegexUtils.hasMatch(s, RegexConstants.doc);

  /// Checks if string is an Excel file.
  /// 检查字符串是否为excel文件
  static bool isExcel(String s) =>
      RegexUtils.hasMatch(s, RegexConstants.excel);

  /// Checks if string is an PPT file.
  /// 检查字符串是否为ppt文件
  static bool isPPT(String s) =>
      RegexUtils.hasMatch(s, RegexConstants.ppt);

  /// Checks if string is an APK file.
  /// 检查字符串是否为apk文件
  static bool isAPK(String s) =>
      RegexUtils.hasMatch(s, RegexConstants.apk);

  /// Checks if string is an pdf file.
  /// 检查字符串是否为pdf文件
  static bool isPDF(String s) =>
      RegexUtils.hasMatch(s, RegexConstants.pdf);

  /// Checks if string is an HTML file.
  /// 检查字符串是否为html文件
  static bool isHTML(String s) =>
      RegexUtils.hasMatch(s, RegexConstants.html);

  /// Checks if string is URL.
  /// 检查字符串是否为url文件
  static bool isURL(String s) =>
      RegexUtils.hasMatch(s, RegexConstants.url);

  /// Checks if string is email.
  /// 检查字符串是否为email文件
  static bool isEmail(String s) =>
      RegexUtils.hasMatch(s, RegexConstants.email);

  /// Checks if string is DateTime (UTC or Iso8601).
  /// 检查字符串是否为时间
  static bool isDateTime(String s) =>
      RegexUtils.hasMatch(s, RegexConstants.basicDateTime);

  /// Checks if string is MD5 hash.
  /// 检查字符串是否为md5
  static bool isMD5(String s) =>
      RegexUtils.hasMatch(s, RegexConstants.md5);

  /// Checks if string is SHA1 hash.
  /// 检查字符串是否为sha1
  static bool isSHA1(String s) =>
      RegexUtils.hasMatch(s, RegexConstants.sha1);

  /// Checks if string is SHA256 hash.
  /// 检查字符串是否为sha256
  static bool isSHA256(String s) =>
      RegexUtils.hasMatch(s, RegexConstants.sha256);

  /// Checks if string is IPv4.
  /// 检查字符串是否为ipv4
  static bool isIPv4(String s) =>
      RegexUtils.hasMatch(s, RegexConstants.ipv4);

  /// Checks if string is IPv6.
  /// 检查字符串是否为ipv6
  static bool isIPv6(String s) =>
      RegexUtils.hasMatch(s, RegexConstants.ipv6);

  /// Checks if string is Palindrome.
  /// 检查字符串是否为回文
  static bool isPalindrome(String s) {
    bool isPalindrome = true;
    for (var i = 0; i < s.length; i++) {
      if (s[i] != s[s.length - i - 1]){
        isPalindrome = false;
      }
    }
    return isPalindrome;
  }

  /// Checks if all data have same value.
  /// 检查所有数据是否具有相同的值
  /// Example: 111111 -> true, wwwww -> true, [1,1,1,1] -> true
  static bool isOneAKind(dynamic s) {
    if ((s is String || s is List) && !ObjectUtils.isNullOrBlank(s)) {
      var first = s[0];
      var isOneAKind = true;
      for (var i = 0; i < s.length; i++) {
        if (s[i] != first) isOneAKind = false;
      }
      return isOneAKind;
    }
    if (s is int) {
      String value = s.toString();
      var first = value[0];
      var isOneAKind = true;
      for (var i = 0; i < value.length; i++) {
        if (value[i] != first) isOneAKind = false;
      }
      return isOneAKind;
    }
    return false;
  }

  /// Checks if length of data is LOWER than maxLength.
  /// 检查数据长度是否小于maxLength
  static bool isLengthLowerThan(dynamic s, int maxLength) {
    if (ObjectUtils.isNull(s)) return (maxLength <= 0) ? true : false;
    switch (s.runtimeType) {
      case String:
      case List:
      case Map:
      case Set:
      case Iterable:
        return s.length < maxLength;
        break;
      case int:
        return s.toString().length < maxLength;
        break;
      case double:
        return s.toString().replaceAll('.', '').length < maxLength;
        break;
      default:
        return false;
    }
  }

  /// Checks if length of data is GREATER than maxLength.
  /// 检查数据长度是否大于maxLength
  static bool isLengthGreaterThan(dynamic s, int maxLength) {
    if (ObjectUtils.isNull(s)) return false;
    switch (s.runtimeType) {
      case String:
      case List:
      case Map:
      case Set:
      case Iterable:
        return s.length > maxLength;
        break;
      case int:
        return s.toString().length > maxLength;
        break;
      case double:
        return s.toString().replaceAll('.', '').length > maxLength;
        break;
      default:
        return false;
    }
  }

  /// Checks if length of data is GREATER OR EQUAL to maxLength.
  /// 检查数据长度是否大于或等于maxLength。
  static bool isLengthGreaterOrEqual(dynamic s, int maxLength) {
    if (ObjectUtils.isNull(s)) return false;
    switch (s.runtimeType) {
      case String:
      case List:
      case Map:
      case Set:
      case Iterable:
        return s.length >= maxLength;
        break;
      case int:
        return s.toString().length >= maxLength;
        break;
      case double:
        return s.toString().replaceAll('.', '').length >= maxLength;
        break;
      default:
        return false;
    }
  }

  /// Checks if length of data is LOWER OR EQUAL to maxLength.
  /// 检查数据长度是否小于或等于maxLength
  static bool isLengthLowerOrEqual(dynamic s, int maxLength) {
    if (ObjectUtils.isNull(s)) return false;
    switch (s.runtimeType) {
      case String:
      case List:
      case Map:
      case Set:
      case Iterable:
        return s.length <= maxLength;
        break;
      case int:
        return s.toString().length <= maxLength;
        break;
      case double:
        return s.toString().replaceAll('.', '').length <= maxLength;
      default:
        return false;
    }
  }

  /// Checks if length of data is EQUAL to maxLength.
  /// 检查数据长度是否等于maxLength。
  static bool isLengthEqualTo(dynamic s, int maxLength) {
    if (ObjectUtils.isNull(s)) return false;
    switch (s.runtimeType) {
      case String:
      case List:
      case Map:
      case Set:
      case Iterable:
        return s.length == maxLength;
        break;
      case int:
        return s.toString().length == maxLength;
        break;
      case double:
        return s.toString().replaceAll('.', '').length == maxLength;
        break;
      default:
        return false;
    }
  }

  /// Checks if length of data is BETWEEN minLength to maxLength.
  /// 检查数据长度是否在minLength到maxLength之间。
  static bool isLengthBetween(dynamic s, int minLength, int maxLength) {
    if (ObjectUtils.isNull(s)) {
      return false;
    }
    return isLengthGreaterOrEqual(s, minLength) &&
        isLengthLowerOrEqual(s, maxLength);
  }

  /// Checks if a contains b (Treating or interpreting upper- and lowercase letters as being the same).
  /// 检查a是否包含b(将大小写字母视为相同或解释)。
  static bool isCaseInsensitiveContains(String a, String b) =>
      a.toLowerCase().contains(b.toLowerCase());

  /// Checks if a contains b or b contains a (Treating or interpreting upper- and lowercase letters as being the same).
  /// 检查a中是否包含b或b中是否包含a(将大小写字母视为相同)。
  static bool isCaseInsensitiveContainsAny(String a, String b) {
    String lowA = a.toLowerCase();
    String lowB = b.toLowerCase();
    return lowA.contains(lowB) || lowB.contains(lowA);
  }

  /// Checks if string value is camelcase.
  /// 检查字符串值是否驼峰大小写。
  static bool isCamelCase(String s) =>
      s == TransformUtils.camelCase(s);

  /// Checks if string value is capitalize.
  /// 检查字符串值是否大写
  static bool isCapitalize(String s, {bool firstOnly = false}) =>
      s == TransformUtils.capitalize(s, firstOnly: firstOnly);
}
