import 'dart:convert';

import 'package:yc_flutter_utils/log/log_utils.dart';


/// json 格式转化工具类
class JsonUtils {

  /// 单纯的Json格式输出打印
  static void printJson(Object object) {
    try {
      JsonEncoder encoder = JsonEncoder.withIndent('  ');
      var encoderString = encoder.convert(object);
      LogUtils.i(encoderString,tag: "json:");
    } catch (e) {
      LogUtils.e(e,tag: "json:");
    }
  }

  /// 单纯的Json格式输出打印
  static void printJsonEncode(Object object) {
    try {
      var encoderString = json.encode(object);
      LogUtils.i(encoderString,tag: "json:");
    } catch (e) {
      LogUtils.e(e,tag: "json:");
    }
  }

  /// 将对象[值]转换为JSON字符串
  /// Converts object [value] to a JSON string.
  static String encodeObj(dynamic value) {
    return value == null ? null : json.encode(value);
  }

  /// 转换JSON字符串到对象
  /// Converts JSON string [source] to object.
  static T getObj<T>(String source, T f(Map v)) {
    if (source == null || source.isEmpty) return null;
    try {
      Map map = json.decode(source);
      return f(map);
    } catch (e) {
      print('JsonUtils convert error, Exception：${e.toString()}');
    }
    return null;
  }

  /// 转换JSON字符串或JSON映射[源]到对象
  /// Converts JSON string or JSON map [source] to object.
  static T getObject<T>(dynamic source, T f(Map v)) {
    if (source == null || source.toString().isEmpty) return null;
    try {
      Map map;
      if (source is String) {
        map = json.decode(source);
      } else {
        map = source;
      }
      return f(map);
    } catch (e) {
      print('JsonUtils convert error, Exception：${e.toString()}');
    }
    return null;
  }

  /// 转换JSON字符串列表[源]到对象列表
  /// Converts JSON string list [source] to object list.
  static List<T> getObjList<T>(String source, T f(Map v)) {
    if (source == null || source.isEmpty) return null;
    try {
      List list = json.decode(source);
      return list.map((value) {
        if (value is String) {
          value = json.decode(value);
        }
        return f(value);
      }).toList();
    } catch (e) {
      print('JsonUtils convert error, Exception：${e.toString()}');
    }
    return null;
  }

  /// 转换JSON字符串或JSON映射列表[源]到对象列表
  /// Converts JSON string or JSON map list [source] to object list.
  static List<T> getObjectList<T>(dynamic source, T f(Map v)) {
    if (source == null || source.toString().isEmpty) return null;
    try {
      List list;
      if (source is String) {
        list = json.decode(source);
      } else {
        list = source;
      }
      return list.map((value) {
        if (value is String) {
          value = json.decode(value);
        }
        return f(value);
      }).toList();
    } catch (e) {
      print('JsonUtils convert error, Exception：${e.toString()}');
    }
    return null;
  }

  /// get List
  static List<T> getList<T>(dynamic source) {
    List list;
    if (source is String) {
      list = json.decode(source);
    } else {
      list = source;
    }
    return list?.map((v) {
      return v as T;
    })?.toList();
  }
}
