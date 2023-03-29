library randomutils;

import 'dart:math';

import 'package:yc_flutter_utils/flutter_utils.dart';

Random _random = Random();

/// 随机工具类
class RandomUtils {

  /// Generates a random integer that represents a Hex color.
  /// 生成一个表示十六进制颜色的随机整数
  static int randomColor() {
    var hex = "0xFF";
    for (int i = 0; i < 3; i++) {
      hex += _random.nextInt(255).toRadixString(16).padLeft(2, '0');
    }
    return int.parse(hex);
  }

  /// Generates a random string of provided or random length.
  /// 生成指定长度或随机长度的随机字符串
  static String randomString({int length}) {
    var codeUnits =
        List.generate(length ?? _random.nextInt(pow(2, 10)), (index) {
      return _random.nextInt(33) + 89;
    });

    return String.fromCharCodes(codeUnits);
  }

  /// Cleans up provided string by removing extra whitespace.
  /// 通过删除额外的空格来清理提供的字符串
  static String condenseWhiteSpace(String str) {
    return str.replaceAll(RegExp(r"\s+"), " ").trim();
  }

  /// Removes all whitespace from provided string.
  /// 从提供的字符串中删除所有空格。
  static String removeWhiteSpace(String str) {
    return str.replaceAll(RegExp(r"\s+"), "");
  }

  /// Returns true for for all strings that are empty, null or only whitespace.
  /// 对于所有为空、空或只有空格的字符串返回true。
  static bool isWhiteSpaceOrEmptyOrNull(String str) {
    return removeWhiteSpace(str ?? "").isEmpty;
  }

  /// Extracts decimal numbers from the provided string.
  /// 从提供的字符串中提取十进制数。
  static String removeNonDigits(String str) {
    return str.replaceAll(RegExp(r"\D"), "");
  }

  /// Generate a random number between start and end inclusive.
  /// 在开始和结束之间生成一个随机数
  static int randInt(int end, {int start = 0}) {
    return _random.nextInt(end) + start;
  }

  /// 从列表中返回一个随机元素。
  static T randomElement<T>(List<T> items) {
    if(ObjectUtils.isEmpty(items)){
      return null;
    }
    return items[randInt(items.length)];
  }


}
