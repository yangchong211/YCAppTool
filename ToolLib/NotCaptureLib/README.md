# App防止抓包工具库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念说明
- Android 7.0及以上为何不能轻易抓取到Https请求的明文数据？
    - 其实Charles上显示确实抓到了包，但是当看抓包的详细数据时会发现报错 You may need to configure your browser or application to trust the Charles Root Certificate. See SSL Proxying in the Help menu。
    - Charles说手机端没有信任Charles的根证书，但是我们手机上已经安装了Charles根证书了，为什么会这样？
    - 原来在Android 7.0(API 24 ) ，有一个名为“Network Security Configuration”的新安全功能。这个新功能的目标是允许开发人员在不修改应用程序代码的情况下自定义他们的网络安全设置。
    - 如果应用程序运行的系统版本高于或等于24，并且targetSdkVersion>=24，则只有系统(system)证书才会被信任。用户(user)导入的Charles根证书是不被信任的。
- Android 7.0 (api 24 ) 和 targetSdkVersion 对抓包的影响
    - 1.抓自己开发的app的网络包；2.抓第三方app的网络包，比如微博客户端
    - 这两种情况有什么区别的，第一种app是我们自己开发的，我们手里有源码，能够修改，能够做到像官方文档里面说的一样进行配置。第二种我们没有源码，要想做到像官方文档里面配置的话，只能反编译后，把配置文件添加进去然后重新打包，但是重新打包就会遇到很多坑，并不一定能成功，所以需要使用其他方式达到抓包目的。
    - 引用官方文档一句话：默认情况下，来自所有应用的安全连接（使用 TLS 和 HTTPS 之类的协议）均信任预装的系统 CA，而面向 Android 6.0（API 级别 23）及更低版本的应用默认情况下还会信任用户添加的 CA 存储。应用可以使用 base-config（应用范围的自定义）或 domain-config（按域自定义）自定义自己的连接。



### 02.常见思路和做法


#### 2.5 网络数据加解密
- 网络数据加密的需求
    - 为了项目数据安全性，对请求体和响应体加密，那肯定要知道请求体或响应体在哪里，然后才能加密，其实都一样不论是加密url里面的query内容还是加密body体里面的都一样。
- 对数据哪里进行加密和解密
    - 目前对数据返回的data进行加解密。



### 03.Api调用说明



### 04.遇到的坑分析



### 05.其他问题说明



### 参考文章
- 全面总结 Android HTTPS 抓包
    - https://mp.weixin.qq.com/s/M82R5YelhMAbdsjJ29gH9A
- Android 高版本 HTTPS 抓包解决方案及问题分析！
    - https://mp.weixin.qq.com/s/lnv4-vvz4W8u5Qp_epEkkw
- 还不懂 HTTP 代理吗？
    - https://mp.weixin.qq.com/s/H5H0LixgRY6CoRunBaLBAw
- App 安全的HTTPS 通信
    - https://developer.aliyun.com/article/64810?spm=a2c6h.13813017.content3.4.6f0b2fe20aa1S0
- Android 系统各个版本上https的抓包
    - https://mp.weixin.qq.com/s?__biz=MzIxNzU1Nzk3OQ==&mid=2247486834&idx=1&sn=91850a5d1ac13953fcb869bf1f232aab&chksm=97f6b3c6a0813ad0bd3df0b09ff0cdbcbd8c85021592febed13f5f265b97cd3a8bbb32e5ca55&scene=38#wechat_redirect
- 解决APP抓包问题「网络安全」
    - https://baijiahao.baidu.com/s?id=1749021972862503189&wfr=spider&for=pc

