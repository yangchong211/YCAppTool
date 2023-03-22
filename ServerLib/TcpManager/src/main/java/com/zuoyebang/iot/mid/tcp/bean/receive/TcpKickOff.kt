package com.zuoyebang.iot.mid.tcp.bean.receive

import com.zuoyebang.iot.mid.tcp.bean.base.TcpRBean

/**
 * 创建者:baixuefei
 * 创建日期:2021/5/18 4:37 PM
 */
data class TcpKickOff(val code: Int = -1, val msg: String = "KickOff", val active: Boolean = true) :
    TcpRBean