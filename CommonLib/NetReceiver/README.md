# 网络广播监听库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念说明
#### 1.1 网络状态监听
- Android 5.0 以下版本
    - 对于5.0以下版本，可以使用基类 activity 中注册网络状态接受者的方式来监听网络状态变化，这样所有继承基类的 activity 都可以监听网络状态变化。
- Android 5.0 以上版本
    - Android 5.0 提供了新的多网络 API，允许您的应用动态扫描具有特定能力的可用网络，它也可以监听到网络状态变化。



#### 1.2 全局网络监听
- AndroidAndroid针对网络状态变化的监听，在应用内我们通用需要监听设备网络状态的变化，作出相应的业务处理，需要一个方便的、全局的监听实现。
- 针对不同设备的系统版本，使用不同的API方法实现；注意使用广播监听网络状态在高版本的适配问题；
    - 1、Build.VERSION.SDK_INT >= Build.VERSION_CODES.N，使用connectivityManager.registerDefaultNetworkCallback()方法；
    - 2、Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP，使用connectivityManager.registerNetworkCallback(networkRequest, networkCallback);方法；
    - 3、其它系统版本使用广播监听；这个目前是使用比较多的！
    - 4、可以添加多个页面的监听，当某个页面需要监听网络时，就可以添加到监听者集合中，页面关闭时移除这个监听者；





### 02.常见思路和做法
#### 2.2 广播监听网络




#### 2.3 定制化监听
- 通过ConnectivityManager.registerNetworkCallback()方式注册网络变化监听器
    - 第一步：获取ConnectivityManager对象
    - 第二步：创建NetworkRequest对象，定制化监听
    - 第三步：创建网路变化监听器
    - 第四步：注册网络监听起
- 创建定制化监听
    - NetworkRequest用来定制化监听，只有符合约束条件的网络变化才会回调NetworkCallback中的方法。
    - addTransportType()用来添加网络类型约束，多个TransportType之间是“或”的关系，即满足其中之一即可。
    - removeTransportType()可以删除某个约束，假如它存在的话。
- Capability约束
    - addCapability()用来添加一些网络属性，比如没有使用vpn，非漫游网络等。
    - 多个Capability之间是“与”的关系，即必须同时满足。上面的代码表示必须没有使用vpn且不是漫游网络。
    ```
    new NetworkRequest.Builder()
            //没有使用vpn
            .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)
            //非漫游网络
            .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_ROAMING)
            .build();
    ```
- 什么时候终止监听？只有程序进程退出(注意是进程退出)或解除注册时终止监听。
    - connectivityManager.unregisterNetworkCallback(networkCallback);






### 03.Api调用说明




### 04.遇到的坑分析
#### 4.1 Android7.0优化
- 后台对网络广播优化背景
    - Android 7.0 移除了三项隐式广播，以帮助优化内存使用和电量消耗。此项变更很有必要，因为隐式广播会在后台频繁启动已注册侦听这些广播的应用。
    - 移动设备会经历频繁的连接变更，例如在 WLAN 和移动数据之间切换时。目前，可以通过在应用清单中注册一个接收器来侦听隐式 CONNECTIVITY_ACTION 广播，让应用能够监控这些变更。
    - 由于很多应用会注册接收此广播，因此单次网络切换即会导致所有应用被唤醒并同时处理此广播。
- 为缓解这些问题，Android 7.0 应用了以下优化措施：
    - 面向 Android 7.0 开发的应用不会收到 CONNECTIVITY_ACTION 广播，即使它们已有清单条目来请求接受这些事件的通知。
    - 在前台运行的应用如果使用 BroadcastReceiver 请求接收通知，则仍可以在主线程中侦听 CONNECTIVITY_CHANGE。
- 若你的目标版本若为7.0 及以上
    - 但是你仍采用注册 CONNECTIVITY_ACTION 广播接收器的方式来监听网络，那么你应该应该在代码中动态注册该网络状态广播接收器，而不应该静态注册在清单文件中。








### 05.其他问题说明




