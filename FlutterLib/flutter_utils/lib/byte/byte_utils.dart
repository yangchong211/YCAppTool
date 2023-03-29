
import 'dart:convert';
import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/flutter_utils.dart';

/// Radix: hex&dec
enum Radix {
  hex,
  dec,
}

/// 字节工具类
class ByteUtils {

  ByteUtils._();

  /// 将可读字符串转换为字节数组，用空格或逗号分隔的字节。
  /// 默认的基数是十六进制，或者指定的基数
  /// 比如：'01 02, ff 0x10,0xfa , 90 76 AF a0'
  /// 输出：[1, 2, 255, 16, 250, 144, 118, 175, 160]
  static Uint8List fromReadable(String readable, {Radix radix = Radix.hex}) {
    if(TextUtils.isEmpty(readable)){
      return null;
    }
    final List<int> list = [];
    final split = RegExp(' +');
    final strArray = readable.replaceAll(',', ' ').split(split);

    for (String str in strArray) {
      var pure = str.trim();
      if (radix == Radix.hex && pure.startsWith('0x')) {
        pure = pure.substring(2);
      }

      final value = int.tryParse(pure, radix: radix == Radix.hex ? 16 : 10);
      if (value == null) return null;

      list.add(value);
    }

    if (list.length <= 0) return null;

    return Uint8List.fromList(list);
  }

  /// 将字节数组转换为可读字符串。
  /// 默认基数是十六进制，或指定的基数
  static String toReadable(Uint8List buffer, {Radix radix = Radix.hex}) {
    if (buffer == null || buffer.length <= 0) return '';

    final List<String> list = [];
    for (int data in buffer) {
      var str =
          data.toRadixString(radix == Radix.hex ? 16 : 10).padLeft(2, '0');
      if (radix == Radix.hex) str = ('0x' + str.toUpperCase());
      list.add(str);
    }
    final result = list.join(' ');
    return result;
  }

  /// 将字节数组转换为base64字符串
  static String toBase64(Uint8List buffer) {
    final str = base64Encode(buffer);
    return str;
  }

  /// 转换base64字符串到字节数组
  static Uint8List fromBase64(String base64) {
    final data = base64Decode(base64);
    return data;
  }

  /// 克隆字节数组
  static Uint8List clone(Uint8List origin) {
    return Uint8List.fromList(origin);
  }

  /// 判断两个字节是否相同
  static bool same(Uint8List bytes1, Uint8List bytes2) {
    if (bytes1.length != bytes2.length) return false;
    for (int i = 0; i < bytes1.length; i++) {
      if (bytes1[i] != bytes2[i]) return false;
    }
    return true;
  }

  /// 从字节序列中提取数据
  static Uint8List extract({@required Uint8List origin,
      @required int indexStart, @required int length}) {
    if (indexStart >= origin.length) return null;
    int end = indexStart + length;
    if (end >= origin.length) {
      end = origin.length;
    }
    final sub = origin.sublist(indexStart, end);
    return sub;
  }

  /// 将两个字节拼接
  static Uint8List combine({@required Uint8List arrayFirst,
    @required Uint8List arraySecond}) {
    return insert(origin: arrayFirst, indexStart: arrayFirst.length,
        arrayInsert: arraySecond);
  }

  /// 在字节某个索引处插入字节
  static Uint8List insert({@required Uint8List origin,
      @required int indexStart, @required Uint8List arrayInsert}) {
    if (indexStart < 0 || arrayInsert.length <= 0) return origin;

    if (origin.length == 0) {
      return Uint8List.fromList(arrayInsert);
    }

    final actIndex = indexStart > origin.length ? origin.length : indexStart;

    var list = List<int>.from(origin);
    list.insertAll(actIndex, arrayInsert);
    return Uint8List.fromList(list);
  }

  /// 在字节某个索引处移除字节
  static Uint8List remove({@required Uint8List origin,
    @required int indexStart, @required int lengthRemove}) {
    if (indexStart < 0 || lengthRemove <= 0) return origin;
    if (origin.length == 0) return origin;
    if (indexStart >= origin.length) return origin;
    final actEnd = (indexStart + lengthRemove > origin.length)
        ? origin.length
        : (indexStart + lengthRemove);
    var list = List<int>.from(origin);
    list.removeRange(indexStart, actEnd);
    return Uint8List.fromList(list);
  }
}
