


import 'package:dio/dio.dart';

class HttpLogInterceptor extends InterceptorsWrapper {

  @override
  Future onRequest(RequestOptions options) {
    print("HttpLogInterceptor--请求url: =>${options.path}");
    if (options.data != null) {
      print('HttpLogInterceptor--请求参数: =>' + options.data.toString());
    }
    return super.onRequest(options);
  }

  @override
  Future onResponse(Response response) {
    if (response != null) {
      print('HttpLogInterceptor--返回结果: =>' + response.toString());
    }
    return super.onResponse(response);
  }

  @override
  Future onError(DioError err) {
    print('HttpLogInterceptor--请求异常: =>' + err.toString());
    print('HttpLogInterceptor--请求异常信息: =>' + err.response?.toString() ?? "");
    return super.onError(err);
  }

}
