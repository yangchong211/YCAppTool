# App防止抓包工具库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念说明
#### 1.1 为何不能抓Https
- Android 7.0及以上为何不能轻易抓取到Https请求的明文数据？
    - 其实Charles上显示确实抓到了包，但是当看抓包的详细数据时会发现报错 You may need to configure your browser or application to trust the Charles Root Certificate. See SSL Proxying in the Help menu。
    - Charles说手机端没有信任Charles的根证书，但是我们手机上已经安装了Charles根证书了，为什么会这样？
    - 原来在Android 7.0(API 24 ) ，有一个名为“Network Security Configuration”的新安全功能。这个新功能的目标是允许开发人员在不修改应用程序代码的情况下自定义他们的网络安全设置。
    - 如果应用程序运行的系统版本高于或等于24，并且targetSdkVersion>=24，则只有系统(system)证书才会被信任。用户(user)导入的Charles根证书是不被信任的。
- Android 7.0 (api 24 ) 和 targetSdkVersion 对抓包的影响
    - 1.抓自己开发的app的网络包；2.抓第三方app的网络包，比如微博客户端
    - 这两种情况有什么区别的，第一种app是我们自己开发的，我们手里有源码，能够修改，能够做到像官方文档里面说的一样进行配置。第二种我们没有源码，要想做到像官方文档里面配置的话，只能反编译后，把配置文件添加进去然后重新打包，但是重新打包就会遇到很多坑，并不一定能成功，所以需要使用其他方式达到抓包目的。
    - 引用官方文档一句话：默认情况下，来自所有应用的安全连接（使用 TLS 和 HTTPS 之类的协议）均信任预装的系统 CA，而面向 Android 6.0（API 级别 23）及更低版本的应用默认情况下还会信任用户添加的 CA 存储。应用可以使用 base-config（应用范围的自定义）或 domain-config（按域自定义）自定义自己的连接。



#### 1.2 业务背景说明
- https确保数据安全
    - 现在几乎所有的api接口都使用https进行数据传输，客户端本身会对https证书的合法性进行校验，以确保数据传输的安全性。
- 存在抓包数据问题
    - 第三方还是可以通过信任代理证书（charles）、安装插件绕过证书检测流程等方式，破解https证书安全校验，解密获取到传输数据。



#### 1.3 防止中间人拦截
- 怎么保证服务端在传输公钥给客户端时不被中间人代理拦截呢？
    - 这里就要引入第三方认证服务：数字证书认证机构（Certificate Authority，简称CA）。
    - CA机构会根据申请方提供的公司信息生成一个数字证书，同时生成一对公钥和私钥。私钥由服务端保存，不可泄露。公钥则附带到数字证书的信息里，可以通过解密获取。



#### 1.4 信任的凭证
- 信任的凭证分为两种类型：系统和用户。
    - 其中系统一列是随设备出厂内置的，并随系统版本更新同步更新，用户一列则是由用户自己安装并信任的证书。
- 证书的类型
    - JKS：数字证书库。JKS里有KeyEntry和CertEntry，在库里的每个Entry都是靠别名（alias）来识别的。
    - P12：是PKCS12的缩写。同样是一个存储私钥的证书库，由.jks文件导出的，用户在PC平台安装，用于标示用户的身份。
    - CER：俗称数字证书，目的就是用于存储公钥证书，任何人都可以获取这个文件 。
    - BKS：由于Android平台不识别.keystore和.jks格式的证书库文件，因此Android平台引入一种的证书库格式，BKS。



#### 1.7 证书校验API
- SSLSocketFactory 或 SSLSocket
    - Android 使用的是 Java 的 API。那么 HTTPS 使用的 Socket 必然都是通过SSLSocketFactory 创建的 SSLSocket，当然自己实现了 TLS 协议除外。
- 此时使用的是默认的SSLSocketFactory（没有加载自己的证书），与下段代码使用的SSLContext是一致的：
    ```
    private synchronized SSLSocketFactory getDefaultSSLSocketFactory() {
      try {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, null, null);
        return defaultSslSocketFactory = sslContext.getSocketFactory();
      } catch (GeneralSecurityException e) {
        throw new AssertionError(); // The system has no TLS. Just give up.
      }
    }
    ```




### 02.常见思路和做法
#### 2.1 App安全配置
- 添加配置文件
    - android:networkSecurityConfig="@xml/network_security_config"
- 在官方文档找到了答案：
    - 网络安全性配置特性让应用可以在一个安全的声明性配置文件中自定义其网络安全设置，而无需修改应用代码。可以针对特定域和特定应用配置这些设置。此特性的主要功能如下所示：
    - 自定义信任锚：针对应用的安全连接自定义哪些证书颁发机构 (CA) 值得信任。例如，信任特定的自签署证书或限制应用信任的公共 CA 集。
    - 仅调试重写：在应用中以安全方式调试安全连接，而不会增加已安装用户的风险。
    - 明文通信选择退出：防止应用意外使用明文通信。
    - 证书固定：将应用的安全连接限制为特定的证书。



#### 2.5 网络数据加解密
- 网络数据加密的需求
    - 为了项目数据安全性，对请求体和响应体加密，那肯定要知道请求体或响应体在哪里，然后才能加密，其实都一样不论是加密url里面的query内容还是加密body体里面的都一样。
- 对数据哪里进行加密和解密
    - 目前对数据返回的data进行加解密。
- 加密和解密使用效率分析
    - 待完善


### 03.Api调用说明



### 04.遇到的坑分析
#### 4.1 配置正式和测试
- 背景说明：
    - 日常开发中，如果需要正式服和测试服过滤明文传输的话，可以这样设置。
- 具体操作步骤
    - 步骤一：在res资源文件夹下创建xml文件夹，在xml文件中创建 network_security_config_debug.xml 和 network_security_config_release.xml。
    - 步骤二：在build.gradle 中配置如下代码：
    ``` groovy
    buildTypes {
        release {
             manifestPlaceholders = [
                 NETWORK_SECURITY_CONFIG: "@xml/network_security_config_release"
             ]
        }
        debug {
            manifestPlaceholders = [
                    NETWORK_SECURITY_CONFIG: "@xml/network_security_config_debug"
            ]
        }
    }
    ```
    - 步骤三：在AndroidManifest 中的 application 中设置我们的网络安全配置。
    ```
    android:networkSecurityConfig="${NETWORK_SECURITY_CONFIG}"
    ```


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
- Okhttp如何添加HTTPS自签名证书信任
    - https://www.cnblogs.com/jiechao-zhang/p/15207246.html
- 深度抓包好文
    - https://zhuanlan.zhihu.com/p/465441201
- 关于HTTPS、TLS/SSL认证以及客户端证书导入方法
    - http://t.zoukankan.com/blogs-of-lxl-p-10136582.html
- HTTPS单向认证、双向认证、抓包原理
    - https://juejin.cn/post/6844903809068564493
- 参考博客
    - https://zhuanlan.zhihu.com/p/465441201


    
    