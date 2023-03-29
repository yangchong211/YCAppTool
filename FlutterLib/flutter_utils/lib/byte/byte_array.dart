import 'dart:typed_data';
import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/byte/byte.dart';
import 'package:yc_flutter_utils/byte/byte_utils.dart';


/// 字节数组
class ByteArray {

  Uint8List _bytes = Uint8List(0);

  /// bytes in UInt8List
  Uint8List get bytes => _bytes;

  /// bytes in List<Byte>
  List<Byte> get array => _bytes.map((byte) => Byte(byte)).toList();

  ByteArray(Uint8List array) {
    _bytes = Uint8List.fromList(array);
  }

  /// 从字节数组中提取数值
  ByteArray.fromByte(int value) {
    _bytes = _toArray(value);
  }

  /// 合并两个字节
  ByteArray.combineArrays(Uint8List array1, Uint8List array2) {
    _bytes = ByteUtils.combine(arrayFirst: array1, arraySecond: array2);
  }

  /// 合并某个值到字节数组，value放到最后
  ByteArray.combine1(Uint8List array, int value) {
    _bytes = ByteUtils.combine(arrayFirst: array, arraySecond: _toArray(value));
  }

  /// 合并某个值到数据，value放到最前
  ByteArray.combine2(int value, Uint8List array) {
    _bytes = ByteUtils.combine(arrayFirst: _toArray(value), arraySecond: array);
  }

  /// 添加某个值到字节数组
  Uint8List append(int value) {
    _bytes = ByteUtils.combine(arrayFirst: _bytes, arraySecond: _toArray(value));
    return _bytes;
  }

  Uint8List appendArray(Uint8List array) {
    _bytes = ByteUtils.combine(arrayFirst: _bytes, arraySecond: array);
    return _bytes;
  }

  Uint8List insert({@required int indexStart, @required int value}) {
    _bytes = ByteUtils.insert(origin: _bytes,
        indexStart: indexStart, arrayInsert: Uint8List.fromList([value]));
    return _bytes;
  }

  Uint8List insertArray(
      {@required int indexStart, @required Uint8List arrayInsert}) {
    _bytes = ByteUtils.insert(origin: _bytes,
        indexStart: indexStart, arrayInsert: arrayInsert);
    return _bytes;
  }

  Uint8List remove({@required int indexStart, @required int lengthRemove}) {
    _bytes = ByteUtils.remove(origin: _bytes,
        indexStart: indexStart, lengthRemove: lengthRemove);
    return _bytes;
  }

  Uint8List _toArray(int value) {
    return Uint8List.fromList([value]);
  }

}
