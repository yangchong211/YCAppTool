package com.zuoyebang.iot.mid.tcp.convert

import android.text.TextUtils
import android.util.Log
import com.yc.logclient.LogUtils
import com.zuoyebang.iot.mid.tcp.TcpPacket
import com.zuoyebang.iot.mid.tcp.TcpSubType
import com.zuoyebang.iot.mid.tcp.TcpType
import com.zuoyebang.iot.mid.tcp.bean.*
import com.zuoyebang.iot.mid.tcp.bean.receive.*
import com.zuoyebang.iot.mid.tcp.utils.JsonUtils
import com.zuoyebang.iot.mod.tcp.TcpLog
import com.zuoyebang.iot.mod.tcp.inter.TcpMessage
import org.json.JSONException
import org.json.JSONObject

/**
 * 创建者:baixuefei
 * 创建日期:2021/8/24 3:07 下午
 */
class ReceiveTcpConvertor : TcpConverter {

    companion object {
        val TAG: String = ReceiveTcpConvertor::class.java.simpleName
    }

    override fun packet2TcpMessage(tcpPacket: TcpPacket): TcpMessage? {
        return try {
            tcpPacket.parseReceive()
        } catch (e: Exception) {
            LogUtils.d("ReceiveTcpConvertor exception:${e.localizedMessage}")
            null
        }
    }

    fun TcpPacket.parseReceive(): TcpMessage? {
        val jsonStr = String(this.payload)
        if (!TextUtils.isEmpty(jsonStr)) {
            LogUtils.i(TAG, "tcp数据，${jsonStr}")
        } else {
            LogUtils.i(TAG, "tcp数据为空")
        }
        return when (this.type) {
            TcpType.TCP_ERROR -> {  //错误处理
                JsonUtils.fromJson(jsonStr, TcpError::class.java)
            }

            TcpType.LOGIN -> { //登录
                JsonUtils.fromJson(jsonStr, TcpLoginRes::class.java)
            }

            TcpType.HEART_BEAT_PONG -> {//server端 心跳pong包
                TcpPong()
            }

            TcpType.KICK_OFF -> {//被踢
                TcpKickOff(code = 1000, "Kick Off")
            }

            TcpType.GENERAL_PROTOCOL -> {//通用业务协议
                val jsonObj = JSONObject(jsonStr)
                var subType: Int = -1
                var dataStr = ""
                if (jsonObj.has("sub_type")) {
                    subType = jsonObj.getInt("sub_type")
                }
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
            TcpSubType.BATCH_SEARCH_NEW_PICTURE -> {
                JsonUtils.fromJson(jsonStr, TcpChildNewBatch::class.java)
            }

            TcpSubType.LAMP_CHANGE_USER -> {
                //doDispatchMessage(JsonUtils.fromJson(jsonStr, TcpChildNewBatch::class.java))
                null
            }

            TcpSubType.LAMP_CONTROL -> {
                JsonUtils.fromJson(jsonStr, TcpLampControlResp::class.java)
            }

            TcpSubType.TCP_COMMON_DIALOG -> {
                JsonUtils.fromJson(jsonStr, TcpCommonDialog::class.java).apply { active = true }
            }

            TcpSubType.DEVICE_STATUS_CHANGED -> {
                JsonUtils.fromJson(jsonStr, TcpDeviceStatusChanged::class.java)
            }

            TcpSubType.CALL -> {
                val type = try {
                    JSONObject(jsonStr).getString("type")
                } catch (e: JSONException) {
                    null
                }
                //呼叫的类型 音频是：audio    视频是：video
                val callType = try {
                    JSONObject(jsonStr).getString("call_type")
                } catch (e: JSONException) {
                    null
                }
                LogUtils.d("ReceiveTcpConvertor call : $callType....$jsonStr")
                when (type) {
                    //主叫方发起通话响应
                    CallConstants.TYPE_CALLING_PARTY_DIAL_RESP -> {
                        JsonUtils.fromJson(jsonStr, CallingPartyDialResp::class.java)
                    }
                    //主叫方收到对方操作响应
                    CallConstants.TYPE_CALLING_PARTY_RECEIVE_CONTROL_RESP -> {
                        JsonUtils.fromJson(jsonStr, CallingPartyReceiveControlResp::class.java)
                    }
                    //被叫方收到对方通话响应
                    CallConstants.TYPE_CALLED_PARTY_RECEIVE_DIAL_RESP -> {
                        JsonUtils.fromJson(jsonStr, CalledPartyReceiveDialResp::class.java)
                    }
                    //被叫方收到对方取消响应
                    CallConstants.TYPE_CALLED_PARTY_RECEIVE_CONTROL_RESP -> {
                        JsonUtils.fromJson(jsonStr, CalledPartyReceiveControlResp::class.java)
                    }
                    else -> null
                }
            }
            TcpSubType.PAD -> {
                JsonUtils.fromJson(jsonStr, PadPushResp::class.java)
            }

            TcpSubType.LAMP_OTA -> {
                JsonUtils.fromJson(jsonStr, TcpLampOtaState::class.java)
            }
            //接收到上传日志的指令 sub_type = 2008
            TcpSubType.UPLOAD_LOG -> {
                //tcp信息
                //TcpPacket(privateTag = 123_1655690959146,length = 46,version = 1,type = GENERAL_PROTOCOL(123),data:{"device_id":280203,"data":{},"sub_type":2008})
                //todo 目前服务端这个data传递的是空对象数据
                JsonUtils.fromJson(jsonStr, TcpUploadLog::class.java)
            }
            //手表定位
            TcpSubType.WATCH_LOCATION -> {
                if (!TextUtils.isEmpty(jsonStr)) {
                    JsonUtils.fromJson(jsonStr, TcpGeo::class.java)
                } else {
                    null
                }
            }
            TcpSubType.WATCH_CHANGE_PHONE->{
                if (!TextUtils.isEmpty(jsonStr)) {
                    JsonUtils.fromJson(jsonStr, TcpWatchChangePhone::class.java)
                } else {
                    null
                }
            }
            else -> null
        }
    }
}