

import 'dart:convert';
import 'dart:io';
import 'dart:typed_data';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/services.dart';
import 'package:http/http.dart' as http;
import 'package:flutter/material.dart';

/// 图片工具类
class ImageUtils {

  ///读取本地资源文件
  static String getImagePath(String name,
      {String path = "assets/images/",String format = "png"}) {
    return "$path$name.$format";
  }

  ///将base64流转化为图片
  static MemoryImage base64ToImage(String base64String) {
    return MemoryImage(
      base64Decode(base64String),
    );
  }

  ///将base64流转化为Uint8List对象
  static Uint8List base64ToUnit8list(String base64String) {
    return base64Decode(base64String);
  }

  ///将图片file转化为base64
  static String fileToBase64(File imgFile) {
    return base64Encode(imgFile.readAsBytesSync());
  }

  ///将网络链接图片转化为base64
  static Future networkImageToBase64(String url) async {
    http.Response response = await http.get(url);
    return base64.encode(response.bodyBytes);
  }

  ///将asset图片转化为base64
  Future assetImageToBase64(String path) async {
    ByteData bytes = await rootBundle.load(path);
    return base64.encode(Uint8List.view(bytes.buffer));
  }

  ///加载网络图片，并且指定宽高大小。使用默认预加载loading和错误视图
  static CachedNetworkImage showNetImageWh(String url, double width, double height) {
    return CachedNetworkImage(
      width: width,
      height: height,
      imageUrl: url ?? '',
      imageBuilder: (context, imageProvider) => Container(
        decoration: BoxDecoration(
          image: DecorationImage(
              image: imageProvider,
              fit: BoxFit.cover,
              colorFilter:
              ColorFilter.mode(Colors.transparent, BlendMode.colorBurn)),
        ),
      ),
      placeholder: (context, url) => Center(
        child: Container(
          height: 40,
          width: 40,
          margin: EdgeInsets.all(5),
          child: CircularProgressIndicator(
            strokeWidth: 2.0,
            valueColor: AlwaysStoppedAnimation(Colors.blue),
          ),
        ),
      ),
      errorWidget: (context, url, error) => Container(
        child: Icon(
          Icons.terrain,
          size: 64,
        ),
        alignment: Alignment.center,
        color: Colors.black12,
      ),
    );
  }


  ///加载网络图片，并且指定宽高大小。切割圆角
  static CachedNetworkImage showNetImageWhClip(String url,
      double width, double height , double circular) {
    return CachedNetworkImage(
      width: width,
      height: height,
      imageUrl: url ?? '',
      imageBuilder: (context, imageProvider) => Container(
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(circular),
          image: DecorationImage(
              image: imageProvider,
              fit: BoxFit.cover,
              colorFilter:
              ColorFilter.mode(Colors.transparent, BlendMode.colorBurn)),
        ),
      ),
      placeholder: (context, url) => Center(
        child: Container(
          height: 40,
          width: 40,
          margin: EdgeInsets.all(5),
          child: CircularProgressIndicator(
            strokeWidth: 2.0,
            valueColor: AlwaysStoppedAnimation(Colors.blue),
          ),
        ),
      ),
      errorWidget: (context, url, error) => Container(
        child: Icon(
          Icons.terrain,
          size: 64,
        ),
        alignment: Alignment.center,
        color: Colors.black12,
      ),
    );
  }


  ///加载网络图片，切割圆形图片。
  static CachedNetworkImage showNetImageCircle(String url, double radius) {
    return CachedNetworkImage(
      width: radius * 2,
      height: radius * 2,
      imageUrl: url ?? '',
      imageBuilder: (context, imageProvider) => Container(
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(radius),
          image: DecorationImage(
              image: imageProvider,
              fit: BoxFit.cover,
              colorFilter:
              ColorFilter.mode(Colors.transparent, BlendMode.colorBurn)),
        ),
      ),
      placeholder: (context, url) => Center(
        child: Container(
          height: 40,
          width: 40,
          margin: EdgeInsets.all(5),
          child: CircularProgressIndicator(
            strokeWidth: 2.0,
            valueColor: AlwaysStoppedAnimation(Colors.blue),
          ),
        ),
      ),
      errorWidget: (context, url, error) => Container(
        child: Icon(
          Icons.terrain,
          size: 64,
        ),
        alignment: Alignment.center,
        color: Colors.black12,
      ),
    );
  }

  ///加载网络图片，并且指定宽高大小。传入错误视图
  static CachedNetworkImage showNetImageWhError(String url,
      double width, double height , LoadingErrorWidgetBuilder error) {
    return CachedNetworkImage(
      width: width,
      height: height,
      imageUrl: url ?? '',
      imageBuilder: (context, imageProvider) => Container(
        decoration: BoxDecoration(
          image: DecorationImage(
              image: imageProvider,
              fit: BoxFit.cover,
              colorFilter:
              ColorFilter.mode(Colors.transparent, BlendMode.colorBurn)),
        ),
      ),
      placeholder: (context, url) => Center(
        child: Container(
          height: 40,
          width: 40,
          margin: EdgeInsets.all(5),
          child: CircularProgressIndicator(
            strokeWidth: 2.0,
            valueColor: AlwaysStoppedAnimation(Colors.blue),
          ),
        ),
      ),
      errorWidget: error
    );
  }

  ///加载网络图片，并且指定宽高大小。传入预加载，错误视图
  static CachedNetworkImage showNetImageWhPlaceError(String url,
      double width, double height ,PlaceholderWidgetBuilder place,
      LoadingErrorWidgetBuilder error) {
    return CachedNetworkImage(
        width: width,
        height: height,
        imageUrl: url ?? '',
        imageBuilder: (context, imageProvider) => Container(
          decoration: BoxDecoration(
            image: DecorationImage(
                image: imageProvider,
                fit: BoxFit.cover,
                colorFilter:
                ColorFilter.mode(Colors.transparent, BlendMode.colorBurn)),
          ),
        ),
        placeholder: place,
        errorWidget: error
    );
  }


}