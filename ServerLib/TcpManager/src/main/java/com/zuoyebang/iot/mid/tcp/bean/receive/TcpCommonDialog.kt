package com.zuoyebang.iot.mid.tcp.bean.receive

import com.google.gson.annotations.SerializedName
import com.zuoyebang.iot.mid.tcp.bean.base.TcpRBean

/**
 * 创建者:baixuefei
 * 创建日期:2021/8/20 11:32 上午
 */
data class TcpCommonDialog(

    //对话框标题,可以为空
    @SerializedName("title")
    val title: String?,

    //对话框内容
    @SerializedName("content")
    var content: String?,

    //跳转url,可以为空
    @SerializedName("url")
    val url: String?,

    //对话框按钮文案:force = true,为中间按钮文案,force = false,为右侧按钮文案.
    @SerializedName("okButton")
    val okButton: String?,

    /**
     * 对话框类型,根据业务需要而定,暂时未用到
     */
    @SerializedName("type")
    val type: Int?,

    /**
     * force 对话框强制处理,不允许忽略
     */
    @SerializedName("force")
    val force: Boolean,

    //消息是否活跃，false-消息未被消费,true-消息已被处理,需要被消费
    var active: Boolean = true

) : TcpRBean {

    companion object {

        const val TYPE_DEFAULT = 1

        /**
         * 管理员同意申请者的申请（申请者收到此消息） 扫码 会到消息---同意
         */
        const val TYPE_SHARE_APPLIED = 2

        /**
         * 管理员解除其他人的授权（被解除授权者收到此消息）
         */
        const val TYPE_SHARE_REVOKED = 3

        /**
         * 转让管理员（被授权者收到此消息）
         */
        const val TYPE_SHARE_TRANSFERRED = 4

        /**
         * 通知消息
         */
        const val TYPE_NOTIFY_PUSH = 5
    }

}