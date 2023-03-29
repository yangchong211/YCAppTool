package com.zuoyebang.iot.mid.tcp.bean.receive

import com.zuoyebang.iot.mid.tcp.bean.base.TcpReceiveBean

data class TcpPong(val msg: String = "TcpPong") : TcpReceiveBean