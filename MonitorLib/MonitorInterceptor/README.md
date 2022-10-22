# 弱网模拟工具库
#### 目录介绍
- 01.基础概念
- 02.常见思路和做法
- 03.Api调用说明
- 04.测试case罗列
- 05.遇到的坑分析



### 01.基础概念
- 先说下业务场景
    - 针对App的网络请求，有时候由于网络问题测试提出了一些bug，极难复现。还有真多弱网情况，也会出现一些难以预料的问题。网络的不稳定性带来的问题很隐晦和难以发现！
- 如何提高异常测试效率
    - 除了使用Charles工具mock数据和修改网络请求外，开发一个小工具模拟网络异常操作很有必要。方便开发和测试一键设置傻瓜式使用！
- 模拟网络异常工具作用
    - 测试和开发可以用它来看不同异常环境下，业务业务稳定性，数据稳定性。举一些简单的例子来说明：
    - 1.开发写了某个页面业务，那么没有网络，或者请求500(有网络)后，页面该怎么展示异常情况，吐司什么，方便业务稳定性测试
    - 2.测试使用该小工具，可以快速模拟各种有网络情况下的各种异常情况，无需你关闭Wi-Fi，比如设置重定向，超时，服务端异常等
    - 3.还有一种情况最难测试，弱网络或者网络环境反复切换，比如针对tcp打电话和接电话，这块非常有利测试项目稳定性



### 02.常见思路和做法
- 如何开发模拟网络异常工具
    - 整体设计思路是，给OkHttpClient添加拦截器，然后在拦截器中拿到request和respond数据做处理即可。
- 如何模拟网络超时
    - 网络超时分为两种：请求超时和响应超时。请求超时是指客户端发送request到服务端过程超时，响应超时是指客户端在接收服务端respond数据过程超时。
    - 模拟请求超时需要在chain.request()代码前面sleep时间，模拟响应超时需要在chain.proceed(request)代码之后sleep时间即可。
- 如何模拟重定向或服务端异常
    - 通过拦截器拿到Chain对象，先发送request，请求成功后会拿到真是respond数据。那么这个时候可以针对这个respond处理，重新设置数据和code码信息
- 如何模拟弱网环境
    - 模拟弱网环境相对复杂一点，发送request和接收respond数据都会有io流读写操作。其核心思路就是对io读写速度做出限制，比如每秒读取1k数据，那么读取10k的接口数据需要10秒



### 03.Api调用说明
- api调用如下所示，直接拿来用即可
    ``` java
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            //添加拦截器，用于模拟各种网络异常case操作，建议在debug下使用
            .addInterceptor(new WeakNetworkInterceptor())
            .build();
    ```
- 如何打开开关和设置网络异常Api
    ```
    //设置网络拦截模拟异常工具生效
    WeakNetworkManager.get().setActive(true);
    //模拟断网400
    WeakNetworkManager.get().setType(WeakNetworkManager.TYPE_OFF_NETWORK);
    //模拟服务端异常500
    WeakNetworkManager.get().setType(WeakNetworkManager.TYPE_SERVER_ERROR);
    //模拟重定向300
    WeakNetworkManager.get().setType(WeakNetworkManager.TYPE_REDIRECTED);
    //模拟网路超时
    WeakNetworkManager.get().setType(WeakNetworkManager.TYPE_TIMEOUT);
    //模拟弱网限速
    WeakNetworkManager.get().setType(WeakNetworkManager.TYPE_SPEED_LIMIT);
    ```



### 04.测试case罗列



### 05.遇到的坑分析






