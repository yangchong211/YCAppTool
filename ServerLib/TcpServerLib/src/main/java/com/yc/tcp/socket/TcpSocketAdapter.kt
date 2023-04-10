package com.yc.tcp.socket

import com.yc.logclient.LogUtils
import com.yc.tcp.TcpConfig
import com.yc.tcp.data.TcpDataBean
import com.yc.tcp.inter.IReceive
import com.yc.tcp.inter.ReadCallBack
import com.yc.tcp.inter.SocketAdapter
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
                // 将数据拷贝到临时缓冲区
                System.arraycopy(oneByteBuffer, 0, lengthBuffer, byteCount, len)
                byteCount++
                if (byteCount == 3) {
                    frameLength =
                        lengthBuffer[2].toInt() and 0xff or (lengthBuffer[1].toInt() and 0xff shl 8) or (lengthBuffer[0].toInt() and 0xff shl 16)
                }
            }

            if (frameLength > 0) {
                val leftBytes = ByteArray(frameLength)
                mInput.readFully(leftBytes, 0, leftBytes.size)
                val frameBytes = ByteArray(lengthBuffer.size + leftBytes.size)
                System.arraycopy(lengthBuffer, 0, frameBytes, 0, lengthBuffer.size)
                System.arraycopy(leftBytes, 0, frameBytes, lengthBuffer.size, leftBytes.size)
                //组装tcp数据
                val tcpData = TcpDataBean(bytes = frameBytes)
                //接收数据
                receive.receive(tcpData)
                //读取数据成功
                readCallback.readSuccess(tcpData)
                byteCount = 0
                lengthBuffer = ByteArray(3)
                oneByteBuffer = ByteArray(1)
            }
        }
        LogUtils.d(TAG, "${TcpConfig.THREAD_OVER},Tcp InutStream Exit,Len:${len}")
    }

}