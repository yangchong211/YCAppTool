# 简单的网络封装库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念说明



#### 1.3 Post提交数据四种方式
- Post提交数据四种方式如下
    - 第一种：Map 通过 GSON 转为 JSON
    - 第二种：使用MultipartBody提交表单 form-data
    - 第三种：JSONObject
    - 第四种：使用FormBody提交表单数据，x-www-form-urlencoded
- 第一种方式核心代码【Map 通过 GSON 转为 JSON】
    ```
    RequestBody requestBody = FormBody.create(mediaType, gson.toJson(map));
    Request.Builder builder = new Request.Builder().url(url);
    Request request = builder.post(requestBody).build();
    Call call = client.newCall(request);
    Response resp = call.execute();
    ```
- 第二种方式核心代码【表单 form-data】
    ```
    MultipartBody.Builder bodyBuilder = new MultipartBody
        .Builder()
        .setType(MultipartBody.FORM);
    bodyBuilder.addFormDataPart("word", "西红柿炒鸡蛋");
    Request.Builder builder = new Request.Builder().url(url);
    builder.post(bodyBuilder.build());
    Call call = client.newCall(builder.build());
    Response resp = call.execute();
    ```
- 第三种方式核心代码【JSONObject方式】
    ```
    JSONObject json = new JSONObject();
    json.put("word", "西红柿炒鸡蛋");
    json.put("page", "1");
    RequestBody bodyBuilder = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
    Request.Builder builder = new Request.Builder().url(url);
    builder.post(bodyBuilder).build();
    Call call = client.newCall(builder.build());
    Response resp = call.execute();
    ```


##### 1.8 Cookie应用场景
- 遇到的问题：登陆成功后，发送请求获取消息，老是提示我没有登陆。
    - Request经常都要携带Cookie，上面说过request创建时可以通过header设置参数，Cookie也是参数之一。
    ```
    Request request = new Request.Builder()
        .url(url)
        .header("Cookie", "xxx")
        .build();
    ```
- 从返回的response里得到新的Cookie，得想办法把Cookie保存起来。但是OkHttp可以不用我们管理Cookie，自动携带，保存和更新Cookie。
    - 方法是在创建OkHttpClient设置管理Cookie的CookieJar，然后在saveFromResponse保存数据，在loadForRequest获取数据。
- 如何实现免密登陆
    - 当我们想要实现免密登录，我们只需要将Cookie存储在文件中或者数据库中即可。




### 02.常见思路和做法



#### 2.4 如何Cookie的持久化
- Android中如何使用Cookie的持久化
    - 第一步：通过响应拦截器从response取出cookie并保存到本地，通过请求拦截器从本地取出cookie并添加到请求中
    - 第二步：自定义CookieJar，在saveFromResponse()中保存cookie到本地，在loadForRequest()从本地取出cookie
    - 第三步：注意在Android中，建议使用sp存储cookie，轻量级存储到本地


### 03.Api调用说明
- OkHttp持久化Cookie操作
    - Okhttp3默认是不持久化Cookie的，想要持久化Cookie就要实现CookieJar接口。
    ```
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    builder.cookieJar(new CustomCookieJar());
    ```



### 04.遇到的坑分析


### 05.其他问题说明




