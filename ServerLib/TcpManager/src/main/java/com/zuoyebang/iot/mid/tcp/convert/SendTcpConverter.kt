package com.zuoyebang.iot.mid.tcp.convert

import com.google.gson.reflect.TypeToken
import com.yc.logclient.LogUtils
import com.zuoyebang.iot.mid.tcp.TcpPacket
import com.zuoyebang.iot.mid.tcp.TcpSubType
import com.zuoyebang.iot.mid.tcp.TcpType
import com.zuoyebang.iot.mid.tcp.bean.*
import com.zuoyebang.iot.mid.tcp.bean.receive.TcpChildNewBatch
import com.zuoyebang.iot.mid.tcp.bean.receive.TcpLampControlResp
import com.zuoyebang.iot.mid.tcp.bean.send.*
import com.zuoyebang.iot.mid.tcp.bean.send.sub.TcpLampControl
import com.zuoyebang.iot.mid.tcp.utils.JsonUtils
import com.zuoyebang.iot.mod.tcp.TcpLog
import com.zuoyebang.iot.mod.tcp.inter.TcpMessage
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

/**
 * 创建者:baixuefei
 * 创建日期:2021/8/24 10:19 上午
 */
class SendTcpConverter : TcpConverter {
    override fun packet2TcpMessage(tcpPacket: TcpPacket): TcpMessage? {
        return try {
            tcpPacket.parseSend()
        } catch (e: Exception) {
            LogUtils.d("SendTcpConverter exception:${e.localizedMessage}")
            null
        }
    }

    fun TcpPacket.parseSend(): TcpMessage? {
        val jsonStr = String(this.payload)
        return when (this.type) {
            TcpType.LOGIN -> { //登录
                JsonUtils.fromJson(jsonStr, TcpLogin::class.java)
            }

            TcpType.HEART_BEAT_PING -> {//server端 心跳pong包
                TcpPing()
            }

            TcpType.GENERAL_PROTOCOL -> {//通用业务协议
                val jsonObj = JSONObject(jsonStr)
                var subType: Int = -1
                if (jsonObj.has("sub_type")) {
                    subType = jsonObj.getInt("sub_type")
                }
                var dataStr = ""
                if (jsonObj.has("data")) {
                    dataStr = jsonObj.getString("data")
                }
                if (subType != -1 && dataStr.isNotEmpty()) {
                    parseGeneralProtocol(subType, dataStr)
                } else {
                    null
                }
            }
            else -> {
                null
            }
        }
    }

    private fun parseGeneralProtocol(subType: Int, jsonStr: String): TcpMessage? {
        return when (subType) {

            TcpSubType.LAMP_CONTROL -> {
                JsonUtils.fromJson(jsonStr, TcpLampControl::class.java)
            }
            TcpSubType.CALL -> {
                val type = try {
                    JSONObject(jsonStr).getString("type")
                } catch (e: JSONException) {
                    null
                }
                when (type) {
                    //主叫方发起通话请求
                    CallConstants.TYPE_CALLING_PARTY_DIAL_REQ -> {
                        JsonUtils.fromJson(jsonStr, CallingPartyDialReq::class.java)
                    }
                    //主叫方取消通话请求
                    CallConstants.TYPE_CALLING_PARTY_CONTROL_REQ -> {
                        JsonUtils.fromJson(jsonStr, CallingPartyControlReq::class.java)
                    }
                    //被叫方操作请求
                    CallConstants.TYPE_CALLED_PARTY_CONTROL_REQ -> {
                        JsonUtils.fromJson(jsonStr, CalledPartyControlReq::class.java)
                    }
                    //被叫方收到通话响应
                    CallConstants.TYPE_CALLED_PARTY_CALL_INCOMING_RESP -> {
                        JsonUtils.fromJson(jsonStr, CalledPartyCallIncomingResp::class.java)
                    }
                    else -> null
                }
            }
            else -> null
        }
    }
}