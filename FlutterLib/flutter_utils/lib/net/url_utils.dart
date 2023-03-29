
import 'package:yc_flutter_utils/flutter_utils.dart';

class UrlUtils{

  /// 返回输入是否匹配url的正则表达式。
  static bool isURL(String url) {
    if(TextUtils.isEmpty(url)){
      return false;
    }
    return RegexUtils.matches(RegexConstants.REGEX_URL, url);
  }

  /// 获取url链接中host
  static String getUrlHost(String url){
    if(TextUtils.isEmpty(url)){
      return null;
    }
    String host ;
    try{
      host = Uri.parse(url).host;
    } catch(e){
      host = null;
    }
    return host;
  }

  /// 获取url链接中scheme
  static String getUrlScheme(String url){
    if(TextUtils.isEmpty(url)){
      return null;
    }
    String host ;
    try{
      host = Uri.parse(url).scheme;
    } catch(e){
      host = null;
    }
    return host;
  }

  /// 判断url链接是否包含参数
  static bool containsTarget(String url, String target) {
    if (TextUtils.isNotEmpty(url)) {
      Uri uri = Uri.parse(url);
      for (int i = 0; i < uri.pathSegments.length; i++) {
        if (uri.pathSegments[i] == target) {
          return true;
        }
      }
    }
    return false;
  }

  /// 获取url中第一个参数
  static String getFirstPath(String url) {
    if(TextUtils.isEmpty(url)){
      return null;
    } 
    Uri uri = Uri.tryParse(url);
    if(ObjectUtils.isEmpty(uri)){
      return null;
    }
    if (uri.queryParameters.length > 0) {
      uri = uri.replace(queryParameters: Map());
    }
    return uri.pathSegments[0];
  }

}