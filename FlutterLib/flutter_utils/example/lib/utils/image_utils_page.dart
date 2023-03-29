

import 'dart:io';

import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/date/data_formats.dart';
import 'package:yc_flutter_utils/date/date_utils.dart';
import 'package:yc_flutter_utils/encrypt/encrypt_utils.dart';
import 'package:yc_flutter_utils/image/image_utils.dart';
import 'package:yc_flutter_utils/object/object_utils.dart';

class ImagePage extends StatefulWidget {

  ImagePage();

  @override
  State<StatefulWidget> createState() {
    return new _PageState();
  }
}

class _PageState extends State<ImagePage> {

  String string = "yangchong";
  List list1 = new List();
  List list2 = null;
  Map map1 = new Map();
  Map map2 ;


  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    list1.add("yangchong");
    map1["name"] = "yangchong";
    return Scaffold(
      appBar: new AppBar(
        title: new Text("EncryptUtils 加解密工具类"),
        centerTitle: true,
      ),
      body: new Column(
        children: <Widget>[
          ImageUtils.showNetImageWh("https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/586766ce5091479d9a10215141c51d13~tplv-k3u1fbpfcp-zoom-in-crop-mark:652:0:0:0.awebp",100,100),
          ImageUtils.showNetImageWh("https://img-blog.csdnimg.cn/20201013094115174.png",200,200),
          ImageUtils.showNetImageWhClip("https://img-blog.csdnimg.cn/20201013094115174.png",200,200,30),
          ImageUtils.showNetImageCircle("https://img-blog.csdnimg.cn/20201013094115174.png",100),
        ],
      ),
    );
  }
}



class Base64Page extends StatefulWidget {

  final String title = "base64";

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<Base64Page> {
  // final picker = ImagePicker();
  TextEditingController base64;

  @override
  void initState() {
    base64 = TextEditingController(text: '');
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Container(
        child: Column(
          children: [
            MaterialButton(
                onPressed: () async {
                  var pickedFile;
                  // await picker.getImage(source: ImageSource.gallery);
                  setState(() {
                    if (pickedFile != null) {
                      base64.text =
                          ImageUtils.fileToBase64(File(pickedFile.path));
                    } else {
                      print('No image selected.');
                    }
                  });
                },
                child: Text("Select Image")),
            SizedBox(height: 10),
            Text("Base64 String of selected image:${base64.text}"),
            SizedBox(height: 30),
            Text("Base64 to Image:"),
            CircleAvatar(
              radius: 25,
              backgroundImage: ImageUtils.base64ToImage(base64.text),
            ),
          ],
        ),
      ),
    );
  }
}
