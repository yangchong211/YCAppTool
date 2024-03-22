package com.yc.tcp.socket

import com.yc.fileiohelper.BaseIoUtils
import com.yc.logclient.LogUtils
import com.yc.tcp.TcpConfig
import com.yc.tcp.data.TcpDataBean
import com.yc.tcp.inter.IReceive
import com.yc.tcp.inter.ReadCallBack
import com.yc.tcp.inter.SocketAdapter
import java.io.DataInputStream

class NormalSocketAdapter : SocketAdapter {

    companion object {
        val TAG: String = NormalSocketAdapter::class.java.simpleName
    }

    override fun read(mInput: DataInputStream?, receive: IReceive, readCallback: ReadCallBack) {
        LogUtils.e(TAG, TcpConfig.THREAD_RUN)
        /*// 步骤2：创建输入流读取器对象 并传入输入流对象
        // 该对象作用：获取服务器返回的数据
        val isr = InputStreamReader(mInput)
        val br = BufferedReader(isr)
        // 步骤3：通过输入流读取器对象 接收服务器发送过来的数据
        val readLine = br.readLine()*/

        val readBytes = BaseIoUtils.readBytes(mInput)
        //组装tcp数据
        val tcpData = TcpDataBean(bytes = readBytes)
        //接收数据
        receive.receive(tcpData)
        //读取数据成功
        readCallback.readSuccess(tcpData)
    }

}