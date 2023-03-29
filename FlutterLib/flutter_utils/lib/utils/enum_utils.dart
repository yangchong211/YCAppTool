/// 枚举工具类
class EnumUtils {

  ///枚举格式化 String
  static String enumValueToString(Object o){
    var last = o.toString().split('.').last;
    return last;
  }

  ///String反显枚举
  static T enumValueFromString<T>(String key, List<T> values) =>
      values.firstWhere((v) => key == enumValueToString(v), orElse: () => null);
}
