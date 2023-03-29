



import 'package:flutter/material.dart';

class IconPage extends StatelessWidget {



  @override
  Widget build(BuildContext context) {
    return new Scaffold(
        appBar: new AppBar(title: new Text("IconPage")),
        body: ListView(
          children: <Widget>[
            new Text("iconfont即“字体图标”，它是将图标做成字体文件，然后通过指定不同的字符而显示不同的图片。"),
            Icon(Icons.accessible,color: Colors.green,),
            Icon(Icons.error,color: Colors.green,),
            Icon(Icons.fingerprint,color: Colors.green,),
            Icon(Icons.arrow_drop_up,color: Colors.green,),

            new Text("使用自定义字体图标"),
            Icon(MyIcons.book,color: Colors.purple,),
            Icon(MyIcons.wechat,color: Colors.green,),
            Icon(MyIcons.arrow_up_circle),
            Icon(MyIcons.arrow_right_circle),
            Icon(MyIcons.accessible),

          ],
        ));
  }

}

class MyIcons{
  static const String _family = 'iconfont';

  static const IconData accessible = IconData(0xe914, fontFamily: 'MaterialIcons');

  // book 图标
  static const IconData book = const IconData(
      0xe61,
      fontFamily: 'myIcon',
      matchTextDirection: true
  );
  // 微信图标
  static const IconData wechat = const IconData(
      0xec7d,
      fontFamily: 'myIcon',
      matchTextDirection: true
  );

  /// 如何使用自定义icon，https://www.jianshu.com/p/af2df7325e4e
  static const IconData arrow_up_circle = const IconData(
      0xe665,
      fontFamily: _family,
  );

  static const IconData arrow_right_circle= const IconData(
      0xe666,
      fontFamily: _family,
      matchTextDirection: true
  );



}