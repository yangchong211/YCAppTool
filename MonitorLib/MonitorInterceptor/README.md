#### 目录介绍
- 01.基础概念
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析



### 01.基础概念


### 02.常见思路和做法


### 03.Api调用说明
- api调用如下所示，直接拿来用即可
    ``` java
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            //添加拦截器
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


### 04.遇到的坑分析






