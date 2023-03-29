
import 'dart:convert';

import 'package:http/http.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:yc_flutter_utils/flutter_utils.dart';
import 'package:yc_flutter_utils/extens/extension_map.dart';
import 'package:yc_flutter_utils/extens/extension_list.dart';

/// sp存储工具类，适合存储轻量级数据，不建议存储json长字符串
class SpUtils {

  SpUtils._();

  static SharedPreferences _prefs;

  /// 初始化，必须要初始化
  static Future<SharedPreferences> init() async {
    if(ObjectUtils.isNull(_prefs)){
      _prefs = await SharedPreferences.getInstance();
    }
    return _prefs;
  }

  /// 判断是否存在key的数据
  static bool hasKey(String key) {
    if (_prefs == null){
      return false;
    }
    Set keys = getKeys();
    return keys.contains(key);
  }

  /// put object.
  /// 存储object类型数据
  static Future<bool> putObject(String key, Object value) {
    if (_prefs == null){
      return null;
      //return Future.value(false);
    }
    return _prefs.setString(key, value == null ? "" : json.encode(value));
  }

  /// 获取sp中key的map数据
  static Map getObject(String key) {
    if (_prefs == null){
      return null;
    }
    String _data = _prefs.getString(key);
    return (_data == null || _data.isEmpty) ? null : json.decode(_data);
  }

  /// put object list.
  /// 存储sp中key的list集合
  static Future<bool> putObjectList(String key, List<Object> list) {
    if (_prefs == null){
      return null;
      //return Future.value(false);
    }
    List<String> _dataList = list?.map((value) {
      return json.encode(value);
    })?.toList();
    return _prefs.setStringList(key, _dataList);
  }

  /// get object list.
  /// 获取sp中key的list集合
  static List<Map> getObjectList(String key) {
    if (_prefs == null){
      return null;
    }
    List<String> dataLis = _prefs.getStringList(key);
    return dataLis?.map((value) {
      Map _dataMap = json.decode(value);
      return _dataMap;
    })?.toList();
  }

  /// get string.
  /// 获取sp中key的字符串
  static String getString(String key, {String defValue: ''}) {
    if (_prefs == null) {
      return defValue;
    }
    return _prefs.getString(key) ?? defValue;
  }

  /// put string.
  /// 存储sp中key的字符串
  static Future<bool> putString(String key, String value) {
    if (_prefs == null){
      return null;
    }
    return _prefs.setString(key, value);
  }

  /// get bool.
  /// 获取sp中key的布尔值
  static bool getBool(String key, {bool defValue: false}) {
    if (_prefs == null) {
      return defValue;
    }
    return _prefs.getBool(key) ?? defValue;
  }

  /// put bool.
  /// 存储sp中key的布尔值
  static Future<bool> putBool(String key, bool value) {
    if (_prefs == null){
      return null;
      // return Future.value(false);
    }
    return _prefs.setBool(key, value);
  }

  /// get int.
  /// 获取sp中key的int值
  static int getInt(String key, {int defValue: 0}) {
    if (_prefs == null) {
      return defValue;
    }
    return _prefs.getInt(key) ?? defValue;
  }

  /// put int.
  /// 存储sp中key的int值
  static Future<bool> putInt(String key, int value) {
    if (_prefs == null){
      return null;
    }
    return _prefs.setInt(key, value);
  }

  /// get double.
  /// 获取sp中key的double值
  static double getDouble(String key, {double defValue: 0.0}) {
    if (_prefs == null) {
      return defValue;
    }
    return _prefs.getDouble(key) ?? defValue;
  }

  /// put double.
  /// 存储sp中key的double值
  static Future<bool> putDouble(String key, double value) {
    if (_prefs == null){
      return null;
    }
    return _prefs.setDouble(key, value);
  }

  /// get string list.
  /// 获取sp中key的list<String>值
  static List<String> getStringList(String key,
      {List<String> defValue: const []}) {
    if (_prefs == null) {
      return defValue;
    }
    return _prefs.getStringList(key) ?? defValue;
  }

  /// put string list.
  /// 存储sp中key的list<String>值
  static Future<bool> putStringList(String key, List<String> value) {
    if (_prefs == null){
      return null;
    }
    return _prefs.setStringList(key, value);
  }

  /// 获取sp中key的map值
  static Map getStringMap(String key) {
    if (_prefs == null){
      return null;
    }
    var jsonString = _prefs.getString(key);
    Map map = json.decode(jsonString);
    return map;
  }

  /// 存储sp中key的map值
  static Future<bool> putStringMap(String key, Map value) {
    if (_prefs == null){
      return null;
      // return Future.value(false);
    }
    var jsonMapString = value.toJsonString();
    return _prefs.setString(key, jsonMapString);
  }

  /// 存储sp中key的list值
  static Future<bool> putStringList2(String key, List value) {
    if (_prefs == null){
      return null;
    }
    var jsonMapString = value.toJsonString();
    return _prefs.setString(key, jsonMapString);
  }

  /// get dynamic.
  /// 获取sp中key的dynamic值
  static dynamic getDynamic(String key, {Object defValue}) {
    if (_prefs == null) {
      return defValue;
    }
    return _prefs.get(key) ?? defValue;
  }

  /// get keys.
  /// 获取sp中所有的key
  static Set<String> getKeys() {
    if (_prefs == null){
      return null;
    }
    return _prefs.getKeys();
  }

  /// remove.
  /// 移除sp中key的值
  static Future<bool> remove(String key) {
    if (_prefs == null){
      return null;
    }
    return _prefs.remove(key);
  }

  /// clear.
  /// 清除sp
  static Future<bool> clear() {
    if (_prefs == null){
      return null;
    }
    return _prefs.clear();
  }

  ///检查初始化
  static bool isInitialized() {
    return _prefs != null;
  }

  /// 遍历打印sp的key和value
  static void forEach(){
    Set<String> keys = getKeys();
    Iterator<String> iterator = keys.iterator;
    while(iterator.moveNext()){
      var key = iterator.current;
      var object = get(key);
      LogUtils.i("key : $key , value : $object",tag: "SpUtils");
    }
  }

}