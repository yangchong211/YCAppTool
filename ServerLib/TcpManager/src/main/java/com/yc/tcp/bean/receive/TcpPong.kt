package com.yc.tcp.bean.receive

import com.yc.tcp.bean.base.TcpReceiveBean

data class TcpPong(val msg: String = "TcpPong") : TcpReceiveBean