package com.yc.usbsocket;

import com.yc.toolutils.AppLogUtils;

import java.net.ServerSocket;
import java.net.Socket;

public class USBThread extends Thread {

    private final UsbSocket usbSocket;
    //端口号
    private static final int PORT = 8004;
    private static final String TAG = UsbSocket.class.getSimpleName();
    //USB通信服务
    private ServerSocket serverSocket = null;
    //是否让线程保持运行状态
    protected volatile boolean isRun = true;
    //当前连接的socket线程
    private PCThread pcThread;

    public USBThread(UsbSocket socket){
        usbSocket = socket;
        AppLogUtils.d(TAG, "USBThread Create"+ " --Thread Name:"+getName());
    }

    @Override
    public void run() {
        //如果出现端口占用  请参考解决方案：https://www.cnblogs.com/mantti/p/5262902.html
        try {
            //pc和手机的端口转发命令，可参考：https://blog.csdn.net/u013553529/article/details/80036227
            //不执行转发的话会出现：Connection refused 异常，需要root权限，pc或者手机端任意一端执行都可以
            //exec("adb forward tcp:8004 tcp:8004");
            //建立socket服务器，监听手机8004端口，这的端口与pc端的端口不属于同个端口，需要adb forward指令去关联转换才能通信
            serverSocket = new ServerSocket(PORT);
            while (isRun) {
                //接受客户端连接
                Socket socket = serverSocket.accept();
                //每次PC连接都要开辟一条线程处理数据
                if(socket != null){
                    if(pcThread != null && pcThread.isRun){
                        pcThread.release();
                        //多个连接线断开以前的连接
                        usbSocket.onConnectNext(false);
                    }
                    pcThread = new PCThread(socket);
                    pcThread.start();
                    //重新连接新的
                    usbSocket.onConnectNext(true);
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            //当serverSocket断开的时候这里会抛出异常
            AppLogUtils.d(TAG, "USBThread Exception:"+e.toString()+ " --Thread Name:"+getName());
            //异常退出，表示断开连接
            usbSocket.onConnectNext(false);
        }
    }

    /**
     * 发送数据
     */
    public boolean send(byte[] data){
        if(pcThread != null && pcThread.isRun){
            return pcThread.send(data);
        }
        return false;
    }

    /**
     * 释放USB通信的线程
     */
    public void release(){
        try {
            //关闭pc线程
            if(pcThread != null && pcThread.isRun){
                pcThread.release();
            }
            pcThread = null;
            //关闭单次连接的socket
            if(serverSocket != null){
                serverSocket.close();
                AppLogUtils.d(TAG, "serverSocket close"+ " --Thread Name:"+getName());
            }
            serverSocket = null;
        } catch (Exception e) {
            e.printStackTrace();
            AppLogUtils.d(TAG, "USBThread.release() Exception:"+ e.toString() +" --Thread Name:"+getName());
        } finally {
            isRun = false;
            AppLogUtils.d(TAG, "USBThread stop"+" --Thread Name:"+getName());
        }
    }

}
