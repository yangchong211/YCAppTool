package com.zuoyebang.iot.mid.tcp.bean.send.sub

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.zuoyebang.iot.mid.tcp.bean.receive.TcpLampControlResp
import com.zuoyebang.iot.mod.tcp.inter.TcpMessage

/**
 * 创建者:baixuefei
 * 创建日期:2021/5/12 8:49 PM
 */

data class TcpLampControl(
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
    //@Transient //是否在线
    @SerializedName("online")
    var online: Int? = 0,
    @SerializedName("ota")
    var autoOta: Int? = null,
) : TcpMessage {
    fun copy(): TcpLampControl {
        return TcpLampControl(
            device_id = device_id, brightness = brightness,
            colour = colour, lightOn = lightOn, screenLocked = screenLocked
        )
    }

    companion object {
        fun fromTcpLampControlResp(resp: TcpLampControlResp): TcpLampControl {
            return TcpLampControl(
                device_id = resp.device_id,
                brightness = resp.brightness,
                colour = resp.colour,
                lightOn = resp.lightOn,
                screenLocked = resp.screenLocked
            )
        }

        fun fromTcpLampControlResp2(resp: TcpLampControlResp): TcpLampControl {
            return TcpLampControl(
                device_id = resp.device_id,
                brightness = resp.brightness,
                colour = resp.colour,
                volume = resp.volume,
                lightOn = resp.lightOn,
                nightLight = resp.nightLight,
                screenLocked = resp.screenLocked,
                online = resp.online,
                autoOta = resp.autoOta,
            )
        }
    }



}