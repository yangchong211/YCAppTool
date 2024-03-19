# 简单的网络封装库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念说明
#### 1.1 项目背景
- 需求分析背景
    - 实现一个网络请求的封装库：主要包括get,post，form表单请求，文件上传和下载等一些基础网络功能的实现。
- 进行技术选型
    - 在HttpUrlConnection，Volley和OkHttp中选择我们我们的封装基础库。
    - OkHttp也是在HttpUrlConnection上做的封装，其继承了Volley的优势，且在Volley上构建了自己的有优点，包括：连接池复用，重试重定向机制，拦截器模式等


#### 1.2 常见网络请求
- 同步请求和异步请求。
    ```
    //同步
    Response response = okHttpClient.newCall(request).execute();
    //异步
    okHttpClient.newCall(request).enqueue(callback);
    ```


#### 1.4 Post提交数据四种方式
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
#### 2.4 网络日志打印
- 如何打印日志
    - OkHttp提供了一个拦截器接口，使得在打印日志时极为方便，并且对接口代码无入侵性。
- 适当地添加日志拦截器
    - 打印日志一般都是在debug的时候才需要，对于release即发布出去的版本，是不需要打印请求的日志的。



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
#### 4.6 HTTPS证书校验
- 背景说明
    - 如何在 Android 端下和服务端实现自制证书的连接？
- App如何证书校验。目前okHttp已经提供了Api来处理证书校验的功能
    ```
    //不建议使用这个，方法过时，使用反射机制寻找X509信任管理类，消耗了不必要的性能。
    okHttpBuilder.sslSocketFactory(sslSocketFactory);
    //会传入信任管理类数组中的第一条
    okHttpBuilder.sslSocketFactory(sslSocketFactory,x509TrustManager);
    okHttpBuilder.hostnameVerifier(hostnameVerifier);
    ```
- 校验过程：
    - 实际上，在 HTTPS 握手开始后，服务器会把整个证书链发送到客户端，给客户端做校验。校验的过程是要找到这样一条证书链，链中每个相邻节点，上级的公钥可以校验通过下级的证书，链的根节点是设备信任的锚点或者根节点可以被锚点校验。
    - 那么锚点对于浏览器而言就是内置的根证书啦（注：根节点并不一定是根证书）。校验通过后，视情况校验客户端，以及确定加密套件和用非对称密钥来交换对称密钥。从而建立了一条安全的信道。


### 05.其他问题说明




