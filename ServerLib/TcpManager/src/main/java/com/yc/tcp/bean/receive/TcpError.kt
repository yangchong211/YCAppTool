package com.yc.tcp.bean.receive

import com.yc.tcp.bean.base.TcpReceiveBean

/**
 * 创建者:baixuefei
 * 创建日期:2021/3/31 5:55 PM
 */
data class TcpError(val error_code: Int, val error_msg: String) : TcpReceiveBean