package com.zuoyebang.iot.mid.tcp.bean.receive

import com.google.gson.annotations.SerializedName
import com.zuoyebang.iot.mid.tcp.bean.base.TcpRBean

/**
 * @author yangchong
 * email  : yangchong211@163.com
 * time   : 2022/6/20
 * desc   : tcp触发日志主动上报。目前服务端返回空data数据
 * revise :
 */
data class TcpUploadLog(

    //日期，预留字段
    @SerializedName("time")
    val time: Long?,
    //预留字段
    @SerializedName("msg")
    val msg: String?,

) : TcpRBean