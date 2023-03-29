
import 'dart:convert';
import 'dart:io';
import 'package:http/http.dart' as http;
import 'dart:async';

import 'package:yc_flutter_utils/log/log_utils.dart';
import 'package:yc_flutter_utils/net/http_api_utils.dart';


///网络请求工具类
class HttpRequest{

  ///开始网络请求
  static Future startRequest(String url, Function callback, {String method,
    Map<String, String> headers, Map<String, String> params,
    Function errorCallback}) async {

    //暂时先用这个，后期看能否抽取成model
    String errorMsg;
    int errorCode;
    var data;

    try {
      //这个是请求头参数
      Map<String, String> headerMap = headers == null ? new Map() : headers;
      //这个是请求参数
      Map<String, String> paramMap = params == null ? new Map() : params;
      http.Response res;
      if (HttpApiUtils.POST == method) {
        LogUtils.i("POST:URL="+url);
        LogUtils.i("POST:BODY="+paramMap.toString());
        //post请求
        res = await http.post(url, headers: headerMap, body: paramMap);
      } else {
        LogUtils.i("GET:URL="+url);
        //get请求
        res = await http.get(url, headers: headerMap);
      }

      /*if(res.statusCode < 200 || res.statusCode >= 300) {
        errorMsg = "网络请求错误,状态码:" + res.statusCode.toString();
        handError(errorCallback, errorMsg);
        return;
      }*/

      if (res.statusCode != 200) {
        errorMsg = "网络请求错误,状态码:" + res.statusCode.toString();
        handError(errorCallback, errorMsg);
        return;
      }

      //以下部分可以根据自己业务需求封装,这里是errorCode>=0则为请求成功,data里的是数据部分
      //记得Map中的泛型为dynamic
      //这个是相应body
      var body = res.body;
      Map<String, dynamic> map = json.decode(body);

      errorCode = map['errorCode'];
      errorMsg = map['errorMsg'];
      data = map['data'];

      //callback返回data,数据类型为dynamic
      //errorCallback中为了方便我直接返回了String类型的errorMsg
      if (callback != null) {
        if (errorCode >= 0) {
          callback(data);
        } else {
          handError(errorCallback, errorMsg);
        }
      }
    } catch (exception) {
      handError(errorCallback, exception.toString());
    }
  }

  static void handError(Function errorCallback,String errorMsg){
    if (errorCallback != null) {
      errorCallback(errorMsg);
    }
    LogUtils.e("errorMsg :"+errorMsg);
  }



}

