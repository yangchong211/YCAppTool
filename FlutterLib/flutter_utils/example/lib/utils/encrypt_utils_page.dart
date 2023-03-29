

import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/date/data_formats.dart';
import 'package:yc_flutter_utils/date/date_utils.dart';
import 'package:yc_flutter_utils/encrypt/encrypt_utils.dart';

class EncryptPage extends StatefulWidget {

  EncryptPage();

  @override
  State<StatefulWidget> createState() {
    return new _PageState();
  }
}

class _PageState extends State<EncryptPage> {

  String string = "yangchong";
  String key = '11, 22, 33, 44, 55, 66';


  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    var encodeBase64 = EncryptUtils.encodeBase64(string);
    var encodeBase642 = EncryptUtils.decodeBase64(encodeBase64);


    String encode = EncryptUtils.xorBase64Encode(string, key);
    String decode = EncryptUtils.xorBase64Decode(encode, key);

    return Scaffold(
      appBar: new AppBar(
        title: new Text("EncryptUtils 加解密工具类"),
        centerTitle: true,
      ),
      body: new Column(
        children: <Widget>[
          new Text("md5 加密：" + EncryptUtils.encodeMd5(string)),
          new Text("Base64加密：" + encodeBase64),
          new Text("Base64解密：" + encodeBase642),
          new Text("异或对称 Base64 加密：" + encode),
          new Text("异或对称 Base64 解密：" + decode),
        ],
      ),
    );
  }
}