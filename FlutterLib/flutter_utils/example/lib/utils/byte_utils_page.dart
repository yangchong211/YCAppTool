

import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/byte/byte.dart';
import 'package:yc_flutter_utils/byte/byte_array.dart';
import 'package:yc_flutter_utils/byte/byte_double_word.dart';
import 'package:yc_flutter_utils/byte/byte_utils.dart';
import 'package:yc_flutter_utils/byte/byte_word.dart';

class BytePage extends StatefulWidget {

  @override
  State<StatefulWidget> createState() {
    return new _PageState();
  }
}

class _PageState extends State<BytePage> {

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: new AppBar(
        title: new Text("ByteUtils 字节工具类"),
        centerTitle: true,
      ),
      body: Center(child: WidgetExample()),
    );
  }
}


class WidgetExample extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new ListView(
      children: [
        MaterialButton(
          onPressed: testFromReadable,
          child: new Text("转换int值为二进制"),
          color: Colors.cyan,
        ),
        MaterialButton(
          onPressed: testToReadable,
          child: new Text("将字节数组转换为可读字符串"),
          color: Colors.cyan,
        ),
        MaterialButton(
          onPressed: testToBase64,
          child: new Text("将字节数组转换为base64字符串"),
          color: Colors.cyan,
        ),
        MaterialButton(
          onPressed: testFromBase64,
          child: new Text("转换base64字符串到字节数组"),
          color: Colors.cyan,
        ),
        MaterialButton(
          onPressed: testClone,
          child: new Text("克隆字节数组"),
          color: Colors.cyan,
        ),
        MaterialButton(
          onPressed: testSame,
          child: new Text("判断两个字节是否相同"),
          color: Colors.cyan,
        ),
        MaterialButton(
          onPressed: testExtract,
          child: new Text("从字节序列中提取数据"),
          color: Colors.cyan,
        ),
        MaterialButton(
          onPressed: testByteArrayContructor,
          child: new Text("字节数组处理"),
          color: Colors.cyan,
        ),
        MaterialButton(
          onPressed: testByteClass,
          child: new Text("ByteClass"),
          color: Colors.cyan,
        ),
      ],
    );
  }

  void testFromReadable() {
    const str1 = '01 02, ff 0x10,0xfa , 90 76 AF a0';
    Uint8List bytes1 = ByteUtils.fromReadable(str1);
    // [1, 2, 255, 16, 250, 144, 118, 175, 160]
    print(bytes1.toString());

    const str2 = '101 02 90 01,33 90 76 102, 901';
    final bytes2 = ByteUtils.fromReadable(str2, radix: Radix.dec);
    // [101, 2, 90, 1, 33, 90, 76, 102, 133]
    print(bytes2);

    Uint8List bytes3 = ByteUtils.fromReadable("a8");
    print(bytes3.toString());

  }

  void testToReadable() {
    final bytes = Uint8List.fromList([0x80, 01, 02, 0xff, 0xA1, 30, 10, 20, 77]);
    final str1 = ByteUtils.toReadable(bytes);
    // 0x80 0x1 0x2 0xFF 0xA1 0x1E 0xA 0x14 0x4D
    print(str1);
    final str2 = ByteUtils.toReadable(bytes, radix: Radix.dec);
    // 128 1 2 255 161 30 10 20 77
    print(str2);

    final bytes3 = Uint8List.fromList([0x80]);
    final str3 = ByteUtils.toReadable(bytes3);
    print(str3);
  }

  void testToBase64() {
    final bytes = Uint8List.fromList([0x80, 01, 02, 0xff, 0xA1, 30, 10, 32]);
    final base64 = ByteUtils.toBase64(bytes);
    // gAEC/6EeCiA=
    print(base64);
  }

  void testFromBase64() {
    final base64 = 'gAEC/6EeCiA=';
    final bytes = ByteUtils.fromBase64(base64);
    // [128, 1, 2, 255, 161, 30, 10, 32]
    print(bytes);
  }

  void testClone() {
    final bytes = Uint8List.fromList([0x80, 01, 02, 0xff, 0xA1, 30, 10, 32]);
    final clone = ByteUtils.clone(bytes);
    // [128, 1, 2, 255, 161, 30, 10, 32]
    print(clone);
  }

  void testSame() {
    final bytes1 = Uint8List.fromList([0x80, 01, 02, 0xff, 0xA1, 30, 10, 32]);
    final bytes2 = Uint8List.fromList([0xA1, 30, 10, 32]);
    final same = ByteUtils.same(bytes1, bytes2);
    // false
    print(same);
  }

  void testExtract() {
    final bytes = Uint8List.fromList([0x80, 01, 02, 0xff, 0xA1, 30, 10, 32]);

    // 0x1 0x2 0xFF
    print(ByteUtils.toReadable(
        ByteUtils.extract(origin: bytes, indexStart: 1, length: 3)));

    // null
    print(ByteUtils.toReadable(
        ByteUtils.extract(origin: bytes, indexStart: 0, length: 0)));

    // 0x80 0x1 0x2 0xFF 0xA1 0x1E 0xA 0x20
    print(ByteUtils.toReadable(
        ByteUtils.extract(origin: bytes, indexStart: 0, length: 100)));

    // null
    print(ByteUtils.toReadable(
        ByteUtils.extract(origin: bytes, indexStart: 10, length: 8)));

    // 0x80 0x1 0x2 0xFF 0xA1 0x1E 0xA 0x20
    print(ByteUtils.toReadable(
        ByteUtils.extract(origin: bytes, indexStart: 0, length: 8)));

    // null
    print(ByteUtils.toReadable(
        ByteUtils.extract(origin: bytes, indexStart: 8, length: 1)));

    // 0x20
    print(ByteUtils.toReadable(
        ByteUtils.extract(origin: bytes, indexStart: 7, length: 1)));
  }

  void testByteArrayContructor() {
    // [1, 2, 3]
    final arr1 = ByteArray(Uint8List.fromList([1, 2, 3]));
    print(arr1.bytes);

    // [3]
    final arr2 = ByteArray.fromByte(3);
    print(arr2.bytes);

    // [1, 2, 3, 4, 5, 6]
    final arr3 = ByteArray.combineArrays(
        Uint8List.fromList([1, 2, 3]), Uint8List.fromList([4, 5, 6]));
    print(arr3.bytes);

    // [1, 2, 3, 7]
    final arr4 = ByteArray.combine1(Uint8List.fromList([1, 2, 3]), 7);
    print(arr4.bytes);

    // [8, 1, 2, 3]
    final arr5 = ByteArray.combine2(8, Uint8List.fromList([1, 2, 3]));
    print(arr5.bytes);

    // [8, 1, 2, 3, 10]
    arr5.append(10);
    print(arr5.bytes);

    // [8, 1, 2, 3, 10, 9, 9]
    arr5.appendArray(Uint8List.fromList([9, 9]));
    print(arr5.bytes);

    // [8, 12, 1, 2, 3, 10, 9, 9]
    arr5.insert(indexStart: 1, value: 12);
    print(arr5.bytes);

    // [8, 12, 1, 2, 3, 10, 9, 9, 13]
    arr5.insert(indexStart: 100, value: 13);
    print(arr5.bytes);

    // [8, 12, 1, 23, 23, 2, 3, 10, 9, 9, 13]
    arr5.insertArray(indexStart: 3, arrayInsert: Uint8List.fromList([23, 23]));
    print(arr5.bytes);

    // [12, 1, 23, 23, 2, 3, 10, 9, 9, 13]
    arr5.remove(indexStart: 0, lengthRemove: 1);
    print(arr5.bytes);

    // [12, 1, 23]
    arr5.remove(indexStart: 3, lengthRemove: 9);
    print(arr5.bytes);
  }

  void testByteClass() {
    final byte1 = Byte(3);
    final byte2 = Byte(0xA1);
    final byte3 = Byte(12);
    final byte4 = Byte(65);

    // 03
    print(byte1);

    final word = ByteWord(high: byte2, low: byte1);
    // A1,03
    print(word);

    final doubleWord =
    ByteDoubleWord(one: byte1, two: byte2, three: byte3, four: byte4);
    // 41,0C,A1,03
    print(doubleWord);

    final dw = ByteDoubleWord.fromInt(184384451);
    // 0x0A,0xFD,0x7B,0xC3
    print(dw);
  }
}
