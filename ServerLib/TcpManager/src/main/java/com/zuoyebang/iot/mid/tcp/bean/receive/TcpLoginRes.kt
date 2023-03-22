package com.zuoyebang.iot.mid.tcp.bean.receive

import com.zuoyebang.iot.mid.tcp.bean.base.TcpRBean

/**
 * 创建者:baixuefei
 * 创建日期:2021/3/31 10:13 AM
 */
data class TcpLoginRes(
    val status: Int,
    val user_id: String,
    val error_code: Int,
    val msg: String,
    val stamp: Long
) : TcpRBean
