package com.zuoyebang.iot.mid.tcp.bean.receive

import com.google.gson.annotations.SerializedName
import com.zuoyebang.iot.mid.tcp.bean.base.TcpRBean
import java.io.Serializable

/**
 * 手表定位
 *
 * @author 裴云飞
 * @date 2022/6/8
 */
data class TcpGeo(
    @SerializedName("device_id") val deviceId: Long?,
    @SerializedName("ctime") val ctime: Long?,
    @SerializedName("server_time") val serverTime: Long?,
    @SerializedName("ext") val ext: String?,
    // 定位类型，0:没有得到定位结果;1:gps 定位结果;2:wifi定位结果;3:混合定位 结果;4:基站定位结果;5:其他--
    @SerializedName("geo_type") var geoType: Int?,
    // 0室外定位，1室内定位
    @SerializedName("indoor") var indoor: Int?,
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("radius") val radius: Double?,
    @SerializedName("correct") val correct: Deviat?,
    @SerializedName("display_indoor") val displayIndoor: Int?
): Serializable, TcpRBean

data class Deviat(
    @SerializedName("ext") val ext: String,
) : Serializable
