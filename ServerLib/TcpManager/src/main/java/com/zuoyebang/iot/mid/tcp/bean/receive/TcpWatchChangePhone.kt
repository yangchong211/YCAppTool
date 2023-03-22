package com.zuoyebang.iot.mid.tcp.bean.receive

import com.google.gson.annotations.SerializedName
import com.zuoyebang.iot.mid.tcp.bean.base.TcpRBean

/**
 * 名称：
 * Created by niudong on 2022/7/29.
 */
data class TcpWatchChangePhone(
    @SerializedName("is_get_phone_no")
    val iszGetPhone_no: Int,
    @SerializedName("phone_no")
    val phoneNo: String,
    @SerializedName("device_id")
    val deviceId: Long,
) : TcpRBean