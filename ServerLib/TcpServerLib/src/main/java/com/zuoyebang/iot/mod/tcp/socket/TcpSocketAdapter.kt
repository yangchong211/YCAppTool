package com.zuoyebang.iot.mod.tcp.socket

import com.yc.logclient.LogUtils
import com.zuoyebang.iot.mod.tcp.TcpConfig
import com.zuoyebang.iot.mod.tcp.data.TcpDataBean
import com.zuoyebang.iot.mod.tcp.inter.IReceive
import com.zuoyebang.iot.mod.tcp.inter.ReadCallBack
import com.zuoyebang.iot.mod.tcp.inter.SocketAdapter
import java.io.DataInputStream

class TcpSocketAdapter : SocketAdapter {

    companion object {
        val TAG: String = TcpSocketAdapter::class.java.simpleName
    }

    override fun read(mInput: DataInputStream?, receive: IReceive, readCallback: ReadCallBack) {
        LogUtils.e(TAG, TcpConfig.THREAD_RUN)
        var len = 0 // 读取长度
        var byteCount = 0
        var oneByteBuffer = ByteArray(1) // 先读前三个字节算出整个包长度
        var lengthBuffer = ByteArray(3) // 创建临时缓冲区
        while (true) {
            if (mInput == null) {
                LogUtils.d(TAG, "mInput == null,just break")
                break
            }
            len = mInput.read(oneByteBuffer)
            if (len < 0) {
                LogUtils.d(TAG, "Tcp read Len:${len} =======the end of the stream has been reached")
                break
            }

            //LogUtils.d(TAG, "Tcp calculate oneByteBuffer:${oneByteBuffer[0]},byteCount:${byteCount}")

            var frameLength = 0
            //取出前三个byte,计算数据帧长度
            if (byteCount < 3) {
                System.arraycopy(oneByteBuffer, 0, lengthBuffer, byteCount, len) // 将数据拷贝到临时缓冲区
                byteCount++
                if (byteCount == 3) {
                    frameLength =
                        lengthBuffer[2].toInt() and 0xff or (lengthBuffer[1].toInt() and 0xff shl 8) or (lengthBuffer[0].toInt() and 0xff shl 16)
                }
            }

            if (frameLength > 0) {
                val leftbytes = ByteArray(frameLength)
                mInput.readFully(leftbytes, 0, leftbytes.size)
                val frameBytes = ByteArray(lengthBuffer.size + leftbytes.size)
                System.arraycopy(lengthBuffer, 0, frameBytes, 0, lengthBuffer.size)
                System.arraycopy(leftbytes, 0, frameBytes, lengthBuffer.size, leftbytes.size)
                val tcpData = TcpDataBean(bytes = frameBytes)

                receive.receive(tcpData)
                readCallback.readSuccess(tcpData)
                byteCount = 0
                lengthBuffer = ByteArray(3)
                oneByteBuffer = ByteArray(1)
            }
        }
        LogUtils.d(TAG, "${TcpConfig.THREAD_OVER},Tcp InutStream Exit,Len:${len}")
    }

}