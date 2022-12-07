# 简单的网络封装库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念说明



### 02.常见思路和做法
#### 2.3 Cookie应用场景
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





### 03.Api调用说明



### 04.遇到的坑分析


### 05.其他问题说明




