import 'dart:typed_data';
import 'package:flutter/material.dart';
import 'byte.dart';

class ByteWord {

  Byte _high = Byte(0);
  Byte _low = Byte(0);

  Byte get high => _high;
  Byte get low => _low;

  ByteWord({@required Byte high, @required Byte low}) {
    _high = high;
    _low = low;
  }

  ByteWord.fromInt(int value) {
    _high = Byte(value >> 8);
    _low = Byte(value);
  }

  /// low at first, high at second.
  /// little-endian
  Uint8List get bytes => Uint8List.fromList([_low.value, _high.value]);

  @override
  String toString() {
    return _high.toString() + ',' + _low.toString();
  }
}
