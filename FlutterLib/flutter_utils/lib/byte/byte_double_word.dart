import 'dart:typed_data';

import 'package:flutter/material.dart';

import 'byte.dart';

class ByteDoubleWord {

  Byte _one = Byte(0);
  Byte _two = Byte(0);
  Byte _three = Byte(0);
  Byte _four = Byte(0);

  ByteDoubleWord({@required Byte one, @required Byte two,
    @required Byte three, @required Byte four}) {
    _one = one;
    _two = two;
    _three = three;
    _four = four;
  }

  ByteDoubleWord.fromInt(int value) {
    _four = Byte(value >> 24);
    _three = Byte(value >> 16);
    _two = Byte(value >> 8);
    _one = Byte(value);
  }

  Uint8List get bytes =>
      Uint8List.fromList([_one.value, _two.value, _three.value, _four.value]);

  @override
  String toString() {
    return _four.toString() +
        ',' + _three.toString() +
        ',' + _two.toString() +
        ',' + _one.toString();
  }
}
