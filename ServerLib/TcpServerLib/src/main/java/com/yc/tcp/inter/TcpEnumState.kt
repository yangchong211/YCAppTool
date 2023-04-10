package com.yc.tcp.inter



enum class DataState {
    Created,  //DataWrapper 刚创建,未放到待发送队列中
    WaitSend,  //DataWrapper在待发送队列中,等待发送
    Sending,  //DataWrapper 从队列中取出,正在发送
    Sent,  //DataWrapper 发送成功
    Canceled;
}

enum class TcpState {
    UNKNOWN,
    NOT_CONNECT,
    CONNECTING,
    CONNECTED
}

enum class TcpError() {
    ERROR_READ,//读数据错误
    ERROR_WRITE,//写数据错误
    ERROR_CONNECT,//连接socket异常
    ERROR_READ_END,//socket.read()读到文件尾部
    ERROR_PONG_TIMEOUT//服务端Pong心跳超时异常
}
