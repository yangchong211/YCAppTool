
/// Object 工具类
class ObjectUtils {

  /// 判断对象是否为null
  static bool isNull(dynamic s) => s == null;

  /// Checks if data is null or blank (empty or only contains whitespace).
  /// 检查数据是否为空或空(空或只包含空格)
  static bool isNullOrBlank(dynamic s) {
    if (isNull(s)) return true;
    switch (s.runtimeType) {
      case String:
      case List:
      case Map:
      case Set:
      case Iterable:
        return s.isEmpty;
        break;
      default:
        return s.toString() == 'null' || s.toString().trim().isEmpty;
    }
  }

  /// Returns true if the string is null or 0-length.
  /// 判断字符串是否为空
  static bool isEmptyString(String str) {
    return str == null || str.isEmpty;
  }

  /// Returns true if the list is null or 0-length.
  /// 判断集合是否为空
  static bool isEmptyList(Iterable list) {
    return list == null || list.isEmpty;
  }

  /// Returns true if there is no key/value pair in the map.
  /// 判断字典是否为空
  static bool isEmptyMap(Map map) {
    return map == null || map.isEmpty;
  }

  /// Returns true  String or List or Map is empty.
  /// 判断object对象是否为空
  static bool isEmpty(Object object) {
    if (object == null) return true;
    if (object is String && object.isEmpty) {
      return true;
    } else if (object is Iterable && object.isEmpty) {
      return true;
    } else if (object is Map && object.isEmpty) {
      return true;
    }
    return false;
  }

  /// Returns true String or List or Map is not empty.
  /// 判断object是否不为空
  static bool isNotEmpty(Object object) {
    return !isEmpty(object);
  }

  /// Returns true Two List Is Equal.
  /// 比较两个集合是否相同
  static bool compareListIsEqual(List listA, List listB) {
    if (listA == listB) return true;
    if (listA == null || listB == null) return false;
    int length = listA.length;
    if (length != listB.length) return false;
    for (int i = 0; i < length; i++) {
      if (!listA.contains(listB[i])) {
        return false;
      }
    }
    return true;
  }

  /// get length.
  /// 获取object的长度
  static int getLength(Object value) {
    if (value == null) return 0;
    if (value is String) {
      return value.length;
    } else if (value is Iterable) {
      return value.length;
    } else if (value is Map) {
      return value.length;
    } else {
      return 0;
    }
  }
}
