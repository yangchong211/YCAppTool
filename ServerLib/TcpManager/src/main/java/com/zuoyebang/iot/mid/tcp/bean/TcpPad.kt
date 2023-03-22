package com.zuoyebang.iot.mid.tcp.bean

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import com.zuoyebang.iot.mod.tcp.inter.TcpMessage

/**
 * @author ChenXiang
 */

object PadConstants {
    /**
     * 查询锁屏状态
     * success,lock_state,lock_remain_time,lock_time
     * 查询一直锁屏状态
     * success,lock_state
     */
    const val PUSH_TYPE_GET_LOCK_STATE = "get_lock_state"

    /**
     * 设置有时间锁屏
     * lock_state,time,success
     * 设置一直锁屏
     * lock_state,is_lock,success
     */
    const val PUSH_TYPE_LOCK_SCREEN = "lock_screen"

    /**
     * 解锁
     * lock_state,success,time_control
     */
    const val PUSH_TYPE_UNLOCK_SCREEN = "unlock_screen"

    /**
     * 设置密码
     * password,success
     */
    const val PUSH_TYPE_PASSWORD = "password"

    /**
     * 通知绑定状态
     * success,status
     */
    const val PUSH_TYPE_BIND_STATUS_NOTIFY = "bindStatusNotify"

    /**
     * 应用管控设置
     * success,button,name
     */
    const val PUSH_TYPE_APP = "app"
}

interface TcpPadMessage : TcpMessage

data class PadPushResp(
    @SerializedName("sn") val sn: String? = null,
    @SerializedName("pushType") val pushType: String? = null,
    @SerializedName("sessionId") val sessionId: String? = null,
    @SerializedName("msg") val msgStr: String? = null,
) : TcpPadMessage {
    data class Message(
        @SerializedName("success") val success: Boolean? = null,
        @SerializedName("lock_state") val lockState: Boolean? = null,
        @SerializedName("lock_time") val lockTime: Int? = null,
        @SerializedName("lock_remain_time") val lockRemainTime: Int? = null,
        @SerializedName("is_lock") val isLock: Boolean? = null,
        @SerializedName("time") val time: Int? = null,
        @SerializedName("password") val password: String? = null,
        @SerializedName("status") val status: Int? = null,
        /**
         * true表示隐藏，false表示显示
         */
        @SerializedName("button") val button: Boolean? = null,
        @SerializedName("name") val name: String? = null,
        /**
         * 0代表没有时间控制，1代表有时间控制
         */
        @SerializedName("time_control") val timeControl: Int? = null,
    )

    @SerializedName("_msg")
    var msg: Message? = null
        get() {
            if (field == null) {
                field = try {
                    Gson().fromJson(msgStr, Message::class.java)
                } catch (e: JsonSyntaxException) {
                    null
                }
            }

            return field
        }

}