import 'package:flutter/material.dart';
import 'package:yc_flutter_utils/extens/extension_string.dart';
import 'package:yc_flutter_utils/res/flutter_dimens.dart';

/// 图片类型
enum ImageType {
  netImage, //网络图片
  assetImage, //本地资源图片，目前来看都是本地资源
}

/// 点击重试时候的回调
typedef TryAgainHandler = void Function();

/// 加载失败缺省页，统一封装
/// 目前看主要是【相同类似】：图片 + 粗标题 + 内容 + 按钮
/// 灵活处理，比如有的没有内容，有的没有按钮，包括可以动态设置各组件间距
class StatusErrorWidget extends StatelessWidget {
  /// 图片地址
  final String imagePath;

  /// 图片的间距。不同场景对应到top不一样，可以通过这个设置
  final EdgeInsetsGeometry imagePadding;

  /// 粗标题，没有则不展示
  final String title;

  /// 粗标题的间距。不同场景对应到top不一样，可以通过这个设置
  final EdgeInsetsGeometry titlePadding;

  /// 二级文本内容，没有则不展示
  final String content;

  /// 二级文本的间距。不同场景对应到top不一样，可以通过这个设置
  final EdgeInsetsGeometry contentPadding;

  /// 设置图片类型
  final ImageType imageType;

  /// 点击重试时候的回调
  final TryAgainHandler tryAgainHandler;

  /// 按钮的内容
  final String btnText;

  /// top头部组件，一般没有
  final Widget topWidget;

  /// bottom底部组件，一般没有
  final Widget bottomWidget;

  StatusErrorWidget({
    Key key,
    //图片地址
    this.imagePath,
    //图片的间距
    this.imagePadding,
    //粗标题，没有则不展示
    this.title,
    //粗标题的间距
    this.titlePadding,
    //二级文本内容，没有则不展示
    this.content,
    //二级文本的间距
    this.contentPadding,
    //图片类型
    this.imageType = ImageType.assetImage,
    //callback回调
    this.tryAgainHandler,
    //按钮的内容
    this.btnText,
    //top头部组件
    this.topWidget,
    //bottom底部组件
    this.bottomWidget,
  })  : assert(imagePath != null),
        super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      //背景颜色是白色
      // color: Colors.cyan,
      // width: double.infinity,
      // height: double.infinity,
      //   height: 500,
      // alignment: AlignmentDirectional.center,
      child: Center(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisAlignment: MainAxisAlignment.center,
          // mainAxisSize: MainAxisSize.max,
          children: [

            //top组件
            if (topWidget != null) topWidget,

            //图片
            if (imagePath.isNotNullOrEmpty())
              Container(
                padding: imagePadding == null
                    ? EdgeInsets.only(
                  top: 80,
                )
                    : imagePadding,
                child: _getImage(),
              ),

            //粗标题
            //如果粗标题有，则显示，反之隐藏
            if (title.isNotNullOrEmpty())
              Container(
                padding: titlePadding == null
                    ? EdgeInsets.only(
                  top: 20,
                  left: 24,
                  right: 24,
                )
                    : titlePadding,
                child: Text(
                  title ?? "",
                  maxLines: 2,
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    fontSize: 20,
                    color: Colors.black,
                    fontWeight: FontWeight.w500,
                    letterSpacing: 0,
                  ),
                ),
              ),

            //内容
            if (content.isNotNullOrEmpty())
              Container(
                padding: contentPadding == null
                    ? EdgeInsets.only(
                  top: 8,
                  left: 80,
                  right: 80,
                )
                    : contentPadding,
                child: Text(
                  content ?? "",
                  maxLines: 2,
                  textAlign: TextAlign.center,
                  overflow: TextOverflow.ellipsis,
                  style: TextStyle(
                    fontSize: 16,
                    color: Color(0xFF999999),
                    fontWeight: FontWeight.normal,
                    letterSpacing: 0,
                  ),
                ),
              ),

            //这个是重试按钮
            if (btnText.isNotNullOrEmpty())
              Padding(
                padding: EdgeInsets.only(
                  top: 32,
                ),
                child: OutlineButton(
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(4),
                  ),
                  borderSide: BorderSide(
                    color: Colors.red,
                    width: 1,
                  ),
                  child: new Text(
                    (btnText != null && btnText.isNotEmpty)
                        ? btnText
                        : "Try again",
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      color: Colors.red,
                      fontSize: 14,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                  onPressed: () {
                    if (tryAgainHandler != null) {
                      tryAgainHandler.call();
                    }
                  },
                ),
              ),

            //bottom组件
            if (bottomWidget != null) bottomWidget,
          ],
        ),
      ),
    );
  }

  Widget _getImage() {
    if (imageType != null && imageType == ImageType.netImage) {
      return Image.network(
        //读取网络资源
        imagePath,
        width: FlutterDimens.flutter_dimen_140,
        height: FlutterDimens.flutter_dimen_140,
      );
    } else {
      return Image.asset(
        //默认读取本地资源
        imagePath,
        width: FlutterDimens.flutter_dimen_140,
        height: FlutterDimens.flutter_dimen_140,
      );
    }
  }
}
