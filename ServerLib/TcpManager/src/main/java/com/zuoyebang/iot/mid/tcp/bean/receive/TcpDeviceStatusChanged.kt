package com.zuoyebang.iot.mid.tcp.bean.receive

import com.google.gson.annotations.SerializedName
import com.zuoyebang.iot.mid.tcp.bean.base.TcpRBean

/**
 * @author ChenXiang
 */
data class TcpDeviceStatusChanged(
    //status = 1 设备tcp在线,0 设别tcp不在线
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("device")
    val device: Long? = null
) : TcpRBean