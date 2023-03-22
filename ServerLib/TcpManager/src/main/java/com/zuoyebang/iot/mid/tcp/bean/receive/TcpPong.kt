package com.zuoyebang.iot.mid.tcp.bean.receive

import com.zuoyebang.iot.mid.tcp.bean.base.TcpRBean

/**
 * 创建者:baixuefei
 * 创建日期:2021/3/31 5:53 PM
 */
data class TcpPong(val msg: String = "TcpPong") : TcpRBean