package com.yc.tcp

import com.yc.tcp.ext.toTcpType
import com.yc.tcp.data.TcpDataBean
import com.yc.tcp.inter.DataState

/**
 *
 *    0 bytes   1        2         3         4                           length+3
 *      +---------+---------+---------+---------+---------+ +---------+---------+
 *      |           length*           | version | type    | | Payload ***       |
 *      +---------+---------+---------+---------+---------+ +---------+---------+
 *
 *
 *  数据包长度: 报文的前3个字节用于放置数据包的长度，该长度不包含自己这3个字节，是从第4个字节算起；3个字节，可支持最大16M的数据包；
 *  版本: 第4个字节放置当前协议的版本，版本从1开始记；
 *  类型:  用于标记该数据包的类型，方便前后端拆包处理，详情参考附录；1个字节可支持255种操作类型；
 *  数据(Payload): 实际的业务数据，为JSON格式
 *  包头+包体
 */
data class TcpPacket(
    //TcpPackage的唯一标识
    var privateTag: String,
    val length: Int = 0,
    val version: Byte = TcpConfig.TCPVersion,
    val type: Int,
    val payload: ByteArray
) {
    /**
     * 将TcpPacket结构 转换成可以发送到TcpCore的ByteArray结构
     */
    private fun toBytes(): ByteArray {
        val type: Int = type
        val bytes: ByteArray = payload

        val header = ByteArray(5)

        val length = bytes.size + 2
        header[0] = (length shr 16 and 0xff).toByte()
        header[1] = (length shr 8 and 0xff).toByte()
        header[2] = (length and 0xff).toByte()
        header[3] = version
        header[4] = type.toByte()

        val result = ByteArray(length + 3)
        System.arraycopy(header, 0, result, 0, header.size)
        System.arraycopy(bytes, 0, result, header.size, bytes.size)
        return result
    }

    /**
     * 将TcpPacket转化成bean
     */
    fun toTcpData(): TcpDataBean {
        return TcpDataBean(privateTag, DataState.Created, toBytes())
    }

    fun subType(): Int {
        return if (payload.isNotEmpty()) {
            payload[0].toInt()
        } else {
            -1
        }
    }

    override fun toString(): String {
        val stringBuffer = StringBuffer()
        stringBuffer.append(
            "TcpPacket(privateTag = ${privateTag},length = ${payload.size},version = ${version},type = ${type.toTcpType()},data:${
                String(
                    payload
                )
            })"
        )
        return stringBuffer.toString()
    }


    /**
     * 将bean转化成TcpPacket
     */
    companion object {
        fun convertFromTcpData(tcpData: TcpDataBean): TcpPacket {
            val bytes = tcpData.bytes
            val head0 = bytes[0].toInt()
            val head1 = bytes[1].toInt()
            val head2 = bytes[2].toInt()
            val version = bytes[3].toInt()
            val type = bytes[4].toInt()
            var length = 0

            length += (head0 and 0xff) shl 16
            length += (head1 and 0xff) shl 8
            length += (head2 and 0xff)

            val payload = ByteArray(bytes.size - 5)
            System.arraycopy(bytes, 5, payload, 0, payload.size)
            return TcpPacket(
                privateTag = tcpData.tag,
                version = version.toByte(),
                type = type,
                payload = payload
            )
        }

        fun convertFromBytes(bytes: ByteArray): TcpPacket {

            val head0 = bytes[0].toInt()
            val head1 = bytes[1].toInt()
            val head2 = bytes[2].toInt()
            val version = bytes[3].toInt()
            val type = bytes[4].toInt()
            var length = 0

            length += (head0 and 0xff) shl 16
            length += (head1 and 0xff) shl 8
            length += (head2 and 0xff)

            val data = ByteArray(bytes.size - 5)
            System.arraycopy(bytes, 5, data, 0, data.size)
            val tag = "${type}_${System.currentTimeMillis()}"
            return TcpPacket(
                privateTag = tag,
                length = length,
                version = version.toByte(),
                type = type,
                payload = data
            )
        }
    }

}