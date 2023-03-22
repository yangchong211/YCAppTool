package com.zuoyebang.iot.mid.tcp.bean.receive

import com.google.gson.annotations.SerializedName
import com.zuoyebang.iot.mid.tcp.bean.base.TcpRBean

/**
 * 创建者:baixuefei
 * 创建日期:2021/4/22 4:12 PM
 */



data class TcpChildNewBatch(
    @SerializedName("child_id")
    val child_id: Long,
    @SerializedName("stamp")
    val stamp: Long
) : TcpRBean

//{"volume":67,"colour":20,"brightness":20,"nightlight":1,"online":1,"locked":1,"ota":1,"on":1}
data class TcpLampControlResp(
    @SerializedName("device_id")
    var device_id: Long? = null,
    @SerializedName("brightness")
    var brightness: Int? = null,
    @SerializedName("colour")
    var colour: Int? = null,
    @SerializedName("volume")
    var volume: Int? = null,
    //打开或者关闭灯
    @SerializedName("on")
    var lightOn: Int? = null,
    //小夜灯
    @SerializedName("nightlight")
    var nightLight: Int? = null,
    //屏幕关闭
    @SerializedName("locked")
    var screenLocked: Int? = null,
    //是否在线
    @SerializedName("online")
    var online: Int? = null,
    @SerializedName("ota")
    var autoOta: Int? = null,
) : TcpRBean


/**
 * 台灯 ota 升级 tcp通知
 */
data class TcpLampOtaState(
    @SerializedName("device_id")
    var deviceId: Long? = null,
    //-1:升级失败，0:未升级，1:下载中，2:升级中，3:升级成功
    @SerializedName("status")
    var status: Int? = null,
    @SerializedName("download_percent")
    var downloadPercent: Int? = null,
    @SerializedName("message")
    var message: String? = null,
) : TcpRBean