# 网络抓包工具库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念说明


### 02.常见思路和做法
- callStart(Call call) 请求开始：
    - RealCall#execute()或者RealCall#enqueue()，当一个Call（代表一个请求）被同步执行或被添加异步队列中时，即会调用这个回调方法。需要说明这个方法是在dispatcher.executed/enqueue前执行的。
- callFailed/callEnd 请求异常和请求结束：
    - 在RealCall类中，callFailed在两种情况下被调用，第一种是在请求执行的过程中发生异常时。第二种是在请求结束后，关闭输入流时产生异常时。
    - 在StreamAllocation类中，callEnd也有两种调用场景。第一种也是在关闭流时。第二种是在释放连接时。
- dnsStart DNS解析开始：
    - 在RouteSelector类中，Dns#lookup(String hostname)，dnsStart/dnsEnd就是在lookup前后被调用的。DNS解析是请求DNS服务器，将域名解析成ip的过程。域名解析工作是由JDK中的InetAddress类完成的。
- connectStart 连接开始：
    - 在RealConnection类中，OkHttp是使用Socket接口建立Tcp连接的，所以这里的连接就是指Socket建立一个连接的过程。
    - 当连接被重用时，connectStart/connectEnd不会被调用。当请求被重定向到新的域名后，connectStart/connectEnd会被调用多次。
- connectEnd 连接结束：
    - 在RealConnection类中，因为创建的连接有两种类型（服务端直连和隧道代理），所以callEnd有两处调用位置。为了在基于代理的连接上使用SSL，需要单独发送CONECT请求。在连接过程中，无论是Socket连接失败，还是TSL/SSL握手失败，都会回调connectEnd。
- secureConnectStart/secureConnectEnd TLS连接开始和结束
    - 在RealConnection类中，在Socket建立连接后，会执行一个establishProtocol方法，这个方法的作用就是TSL/SSL握手。
    - 如果我们使用了HTTPS安全连接，在TCP连接成功后需要进行TLS安全协议通信，等TLS通讯结束后才能算是整个连接过程的结束，也就是说connectEnd在secureConnectEnd之后调用。
    - connectStart --->  secureConnectStart ---> secureConnectEnd ---> ConnectEnd
- connectionAcquired/connectionReleased 连接绑定和释放监听
    - 因为OkHttp是基于连接复用的，当一次请求结束后并不会马上关闭当前连接，而是放到连接池中。当有相同域名的请求时，会从连接池中取出对应的连接使用，减少了连接的频繁创建和销毁。
    - 当根据一个请求从连接池取连接时，并打开输入输出流就是acquired，用完释放流就是released。
- requestHeadersStart/requestHeadersEnd
    - 在CallServerInterceptor拦截器中，
- requestBodyStart/requestBodyEnd
- responseHeadersStart/responseHeadersEnd
- responseBodyStart/responseBodyEnd
- callEnd(Call call) 请求结束：
- callFailed 请求异常：


### 03.Api调用说明



### 04.遇到的坑分析


### 05.其他问题说明



### 参考博客


