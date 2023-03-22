package com.zuoyebang.iot.mid.tcp.bean

import com.google.gson.annotations.SerializedName
import com.zuoyebang.iot.mod.tcp.inter.TcpMessage

/**
 * @author ChenXiang
 */

object CallConstants {
    //主叫方发起通话请求
    const val TYPE_CALLING_PARTY_DIAL_REQ = "apply"
    //主叫方取消通话请求
    const val TYPE_CALLING_PARTY_CONTROL_REQ = "apply_control"
    //被叫方操作请求
    const val TYPE_CALLED_PARTY_CONTROL_REQ = "call_resp"
    //被叫方收到通话响应
    const val TYPE_CALLED_PARTY_CALL_INCOMING_RESP = "call_incoming_resp"

    //主叫方发起通话响应
    const val TYPE_CALLING_PARTY_DIAL_RESP = "apply_resp"
    //主叫方收到对方操作响应
    const val TYPE_CALLING_PARTY_RECEIVE_CONTROL_RESP = "call_resp"
    //被叫方收到对方通话响应
    const val TYPE_CALLED_PARTY_RECEIVE_DIAL_RESP = "call_incoming"
    //被叫方收到对方取消响应
    const val TYPE_CALLED_PARTY_RECEIVE_CONTROL_RESP = "apply_control"

    const val CALL_TYPE_AUDIO = "audio"
    const val CALL_TYPE_VIDEO = "video"
//    const val CALL_TYPE_VIDEO = "audio"

    const val STATUS_ONLINE = "online"
    const val STATUS_OFFLINE = "offline"

    const val OPERATION_CALL_INCOMING = "call_incoming"
    const val OPERATION_ACCEPT = "accept"
    const val OPERATION_BUSY = "busy"
    const val OPERATION_HANGUP = "hangup"
    const val OPERATION_TIMEOUT = "timeout"
}

data class Profile(
    @SerializedName("name") val name: String?,
    @SerializedName("photo") val photo: String?,
    @SerializedName("device_name") val deviceName: String?,
)

interface TcpCallMessage : TcpMessage

/**
 * 主叫方发起通话请求
 */
data class CallingPartyDialReq(
    @SerializedName("type") val type: String = CallConstants.TYPE_CALLING_PARTY_DIAL_REQ,
    @SerializedName("session") val session: String,
    @SerializedName("from_id") val fromId: Long,
    @SerializedName("to_id") val toId: Long,
    @SerializedName("stamp") val stamp: Long = System.currentTimeMillis(),
    @SerializedName("call_type") val callType: String = CallConstants.CALL_TYPE_AUDIO,
) : TcpCallMessage

/**
 * 主叫方取消通话请求
 */
data class CallingPartyControlReq(
    @SerializedName("type") val type: String = CallConstants.TYPE_CALLING_PARTY_CONTROL_REQ,
    @SerializedName("session") val session: String,
    @SerializedName("from_id") val fromId: Long,
    @SerializedName("to_id") val toId: Long,
    @SerializedName("stamp") val stamp: Long = System.currentTimeMillis(),
    @SerializedName("call_type") val callType: String = CallConstants.CALL_TYPE_AUDIO,
) : TcpCallMessage

/**
 * 被叫方操作请求
 */
data class CalledPartyControlReq(
    @SerializedName("type") val type: String = CallConstants.TYPE_CALLED_PARTY_CONTROL_REQ,
    @SerializedName("session") val session: String,
    @SerializedName("from_id") val fromId: Long,
    @SerializedName("to_id") val toId: Long,
    @SerializedName("stamp") val stamp: Long = System.currentTimeMillis(),
    @SerializedName("operation") val operation: String,
    @SerializedName("call_type") val callType: String = CallConstants.CALL_TYPE_AUDIO,
) : TcpCallMessage

/**
 * 被叫方收到通话响应
 */
data class CalledPartyCallIncomingResp(
    @SerializedName("type") val type: String = CallConstants.TYPE_CALLED_PARTY_CALL_INCOMING_RESP,
    @SerializedName("session") val session: String,
    @SerializedName("from_id") val fromId: Long,
    @SerializedName("to_id") val toId: Long,
    @SerializedName("stamp") val stamp: Long = System.currentTimeMillis(),
    @SerializedName("call_type") val callType: String = CallConstants.CALL_TYPE_AUDIO,
) : TcpCallMessage


/*------------------------------------------------------------------------------*/
/*---------------------------收到tcp消息-------------------------------*/
/*------------------------------------------------------------------------------*/


/**
 * 主叫方发起通话响应
 */
data class CallingPartyDialResp(
    @SerializedName("type") val type: String = CallConstants.TYPE_CALLING_PARTY_DIAL_RESP,
    @SerializedName("session") val session: String,
    @SerializedName("status") val status: String,
    @SerializedName("stamp") val stamp: Long,
    @SerializedName("channel_name") val channelName: String,
    @SerializedName("agora_token") val agoraToken: String,
    @SerializedName("call_type") val callType: String = CallConstants.CALL_TYPE_AUDIO,
) : TcpCallMessage

/**
 * 主叫方收到对方操作响应
 */
data class CallingPartyReceiveControlResp(
    @SerializedName("type") val type: String = CallConstants.TYPE_CALLING_PARTY_RECEIVE_CONTROL_RESP,
    @SerializedName("session") val session: String,
    @SerializedName("from_id") val fromId: Long,
    @SerializedName("to_id") val toId: Long,
    @SerializedName("stamp") val stamp: Long,
    @SerializedName("operation") val operation: String,
    @SerializedName("call_type") val callType: String = CallConstants.CALL_TYPE_AUDIO,
) : TcpCallMessage

/**
 * 被叫方收到对方通话响应
 */
data class CalledPartyReceiveDialResp(
    @SerializedName("type") val type: String = CallConstants.TYPE_CALLED_PARTY_RECEIVE_DIAL_RESP,
    @SerializedName("session") val session: String,
    @SerializedName("from_id") val fromId: Long,
    @SerializedName("from_profile") val fromProfile: Profile,
    @SerializedName("to_id") val toId: Long,
    @SerializedName("stamp") val stamp: Long,
    @SerializedName("channel_name") val channelName: String,
    @SerializedName("agora_token") val agoraToken: String,
    @SerializedName("is_offline_msg") val isOfflineMsg: Int,
    @SerializedName("call_type") val callType: String = CallConstants.CALL_TYPE_AUDIO,
) : TcpCallMessage

/**
 * 被叫方收到对方取消响应
 */
data class CalledPartyReceiveControlResp(
    @SerializedName("type") val type: String = CallConstants.TYPE_CALLED_PARTY_RECEIVE_CONTROL_RESP,
    @SerializedName("session") val session: String,
    @SerializedName("from_id") val fromId: Long,
    @SerializedName("to_id") val toId: Long,
    @SerializedName("stamp") val stamp: Long,
    @SerializedName("call_type") val callType: String = CallConstants.CALL_TYPE_AUDIO,
) : TcpCallMessage