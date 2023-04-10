package com.yc.tcp.data

import com.yc.tcp.inter.DataState

/**
 * bytes:代表一个完整的TcpPacket
 */
data class TcpDataBean(
    val tag: String = "",
    var state: DataState = DataState.Created,
    var bytes: ByteArray,
    var retryCount: Int = 0 //失败重试次数
) {

    fun increaseRetry() {
        retryCount++
    }

    fun shouldBeDrop(): Boolean {
        return retryCount > MAX_RETRY_TIME
    }

    override fun hashCode(): Int {
        return tag.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return if (other is TcpDataBean) {
            hashCode() == other.hashCode()
        } else {
            false
        }
    }

    override fun toString(): String {
        val strBuffer = StringBuffer()
        strBuffer.append("TcpData(tag = ${tag},state:${state},bytes:${bytes.printByteArray()}")
        return strBuffer.toString()
    }

    private fun ByteArray?.printByteArray(): String {
        val buffer = StringBuffer()
        this?.forEachIndexed { index, byte ->
            run {
                buffer.append(byte.toHex())
                if ((index + 1) % 4 == 0) {
                    buffer.append(" ")
                }
            }
        }
        return buffer.toString()
    }

    private fun Byte.toHex(): String {
        var hex = Integer.toHexString(this.toInt() and 0xFF)
        if (hex.length == 1) {
            hex = "0$hex"
        }
        return hex
    }

    companion object {
        const val MAX_RETRY_TIME = 2 //消息发送失败后,可以进行重试的的最大次数 - 2次失败重试
    }
}


