# 通用TCP消息框架
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念说明
#### 1.1 Socket如何理解
- Socket 可以作插口或者插槽讲
    - 虽然我们是写软件程序，但是你可以想象为弄一根网线，一头插在客户端，一头插在服务端，然后进行通信。所以在通信之前，双方都要建立一个 Socket。
- 在建立 Socket 的时候，应该设置什么参数呢？
    - Socket 编程进行的是端到端的通信，往往意识不到中间经过多少局域网，多少路由器，因而能够设置的参数，也只能是端到端协议之上网络层和传输层的。
    - 在网络层，Socket 函数需要指定到底是 IPv4 还是 IPv6，分别对应设置为 AF_INET 和 AF_INET6。


#### 1.2 Socket建立连接
- 建立Socket连接至少需要一对套接字，其中一个运行与客户端--ClientSocket，一个运行于服务端--ServiceSocket
    - 1、服务器监听：服务器端套接字并不定位具体的客户端套接字，而是处于等待连接的状态，实时监控网络状态，等待客户端的连接请求。
    - 2、客户端请求：指客户端的套接字提出连接请求，要连接的目标是服务器端的套接字。注意：客户端的套接字必须描述他要连接的服务器的套接字，指出服务器套接字的地址和端口号，然后就像服务器端套接字提出连接请求。
    - 3、连接确认：当服务器端套接字监听到客户端套接字的连接请求时，就响应客户端套接字的请求，建立一个新的线程，把服务器端套接字的描述发给客户端，一旦客户端确认了此描述，双方就正式建立连接。而服务端套接字则继续处于监听状态，继续接收其他客户端套接字的连接请求。



#### 1.3





### 02.常见思路和做法
#### 2.1 Socket编程实践


### 03.Api调用说明





### 04.通信实践设计
#### 4.1 设计Tcp初始化
- 设计Tcp初始化的代码如下所示
    ```
    TcpFacade.putExtra(TcpFacade.UDID, "E1274F3EF737603D08FE758455F67AF9|0")
        .setDynamicInfoDelegate { key ->
            when (key) {
                TcpFacade.TOKEN -> "token"
            }
        }
        .setDisableTcpOnBackGround(false)
        .setTrustManagerProvider(TrustManagerProviderUnSafe())
        .init(this)
    ```
- TCP注册干了啥
    - 传递了一些参数，比如用户token，uid，版本号，用户 secret。主要是客户端和服务端通信需要的一些关键参数。
- Tcp初始化的流程图如下所示
    - ![image](https://img-blog.csdnimg.cn/8a67646577144160bb94e2f5aa185f57.png)




#### 4.2 设计Tcp监听
- 设计Tcp监听的代码如下所示
    ```
    TcpFacade.registerTcpMessage(this) { tcpMessage ->
        AppLogUtils.d("registerTcpMessage:${it}")
        when (tcpMessage) {
            is TcpCommonDialog -> {
                //出事消息
            }
        }
    }
    ```
- TCP添加监听消息流程图
    - ![image](https://img-blog.csdnimg.cn/a288c472b5804e8c9207991961ab8c20.png)



#### 4.3 设计Tcp发送
- 设计Tcp监听的代码如下所示
    ```
    TcpFacade.getTcpManager().sendCall(
        TcpLogin(token = token,uid = uid)
    )
    ```




#### 4.4 断开重试的设计
- 长连接何时会断开
    - 正常而言，长连接是不会断开的。大家可以自己试一试，两个socket建立连接，只要网络不变、一切正常，那么这两个socket可以一直互相传送数据，不会断开。
    - 但是，在移动网络下，网络状态复杂多变，比如线路异常，网络线路被切断、服务器宕机等，都会导致长连接中断。
- 需要关注下面几个长连接断开原因：
    - 1.长连接所在进程被杀。这个很容易理解，如果App切换到后台，那么系统随时可能将我们的App杀掉，这时长连接自然也就随之断开。
    - 2.用户切换网络。比如手机网络断开，或者发生Wi-Fi和蜂窝数据切换，这时会导致手机IP地址变更。TCP连接是基于IP + Port的，一旦IP变更，TCP连接自然也就失效了，或者说长连接也就相当于断开了。
    - 3.系统休眠等导致NAT超时。运营商会给每台设备分配一个公网IP。随着连接网络的设备不断增多，网关负载也会不断加大，这时运营商会对一些不太活跃设备进行公网IP回收，如果下次这个设备需要连网，那就重新分配一个IP即可。
    - 4.DHCP 租期过期。如果没有及时续约，同样会导致IP地址失效。
    - 综合而言：长连接在正常情况下是不会断开的，但是，一旦手机的IP地址失效，这时就不得不重新建立连接了。
- 断开重连的方案设计
    - 创建Receiver，监控网络状态，如果网络发生切换则立即重连；
    - 监控服务端心跳包回包，如果连续5次没有收到回包，则认为长连接已经失效；
    - 设置心跳包超时限制，如果超过时间还没有收到心跳回包，则重连，这种方式比较耗电；
    - 等socket IO异常抛出，不过耗时太长，需要15s左右才能发现。





#### 4.5 数据的封包和解析
- 如何解析数据
    - 将二进制数据转化为实体packet数据。
- 如何封包数据
    - 组装packet数据体，然后将数据转化为io流二进制数据。




#### 4.6 如何设计长链接
- Socket 长连接的实现背景知识
    - Socket 长连接，指的是在客户和服务端之间保持一个 socket 连接长时间不断开。
- 先来思考一个问题
    - 假定现在有一对已经连接的 socket，在以下情况发生时候，socket 将不再可用。
    - 1.某一端关闭是 socket（这不是废话吗）。主动关闭的一方会发送 FIN，通知对方要关闭 TCP 连接。在这种情况下，另一端如果去读 socket，将会读到 EoF（End of File）。于是我们知道对方关闭了 socket。
    - 2.应用程序奔溃。此时 socket 会由内核关闭，结果跟情况1一样。
    - 3.系统奔溃。这时候系统是来不及发送 FIN 的，因为它已经跪了。此时对方无法得知这一情况。对方在尝试读取数据时，最后会返回 read time out。如果写数据，则是 host unreachable 之类的错误。
    - 4.电缆被挖断、网线被拔。跟情况3差不多，如果没有对 socket 进行读写，两边都不知道发生了事故。跟情况3不同的是，如果我们把网线接回去，socket 依旧可以正常使用。
- 如何设计长链接
    - 要实现一个socket长连接，需要做的就是不断地给对方写数据，然后读取对方的数据，也就是所谓的心跳。只要心还在跳，socket 就是活的。写数据的间隔，需要根据实际的应用需求来决定。
    - 首先会每隔1分钟，客户端就给服务端对方发送一个 ping 包，看看对面在不在，服务端则回执 pong 包表示回应。如果超过2分钟秒还没有回复我，那就说明对方掉线了，关闭这边的 TCP 端。
- 轮训发送ping作用
    - 客户端发送ping【无业务互动时】，而且还是一个轮训的，主要是保持长链接通畅。
- 服务端发送pong包
    - server端发送心跳pong包，主要是回应客户端信息【回复ping消息】。
- 客户端如何保证消息轮训处理且不会阻塞主线程【场景是：频繁处理消息】
    - 使用独享的Looper(HandlerThread)。处理异步任务的方式和 Thread + Looper + Handler 方式相同。内部实现了普通线程的 Looper 消息循环，不会阻塞UI县城。





#### 4.7 数据的封包和解析
- Tcp发送消息体【App发送tcp消息】
    - 将消息组装成 TcpPacket 结构体，然后发送出去。需要注意：发送的消息体需要转化为字符串，然后再转化为byte字节
    ```
    TcpPacket(privateTag = TcpLogin_1656059681564,length = 157,version = 1,type = LOGIN(80),data:{"app_version":"2.3.0","device_type":1,"sig":"085494bfd8586f3b22d6725bec51556b","stamp":1656059681551,"token":"","udid":"56A58B4C989DDCF083F200F078A324B5|0"})
    TcpPacket(privateTag = TcpPing_1656059681611,length = 2,version = 1,type = HEART_BEAT_PING(1),data:{})
    ```
- Tcp接收消息体【App接收tcp消息】
    ```
    TcpPacket(privateTag = 0_1656059681622,length = 58,version = 1,type = TCP_ERROR(0),data:{"type":80,"error_code":6,"error_msg":"缺少参数token"})
    TcpPacket(privateTag = 2_1656059681653,length = 0,version = 1,type = HEART_BEAT_PONG(2),data:)
    ```
- 设计的TcpPacket数据包体
    - privateTag，tcp唯一标示，主要是类型和时间戳拼接成的tag字符串
    - length，tcp长度，这个主要是指data的长度
    - version，版本
    - type，类型，用来区分一级消息类型
    - data，数据，一个json对象
    ``` kotlin
    data class TcpPacket(
        var privateTag: String,
        val length: Int = 0,
        val version: Byte = TcpConfig.TCPVersion,
        val type: Int,
        val payload: ByteArray
    ) 
    ```


#### 4.8 一些注意点
- 实际应用中，在发生异常时，需要关闭 socket，并根据实际业务做一些错误处理工作。




### 05.其他问题说明




