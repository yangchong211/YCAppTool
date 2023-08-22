package com.yc.usbsocket;

import com.yc.toolutils.AppLogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class PhoneThread extends Thread{

    private UsbSocket usbSocket;
    private static final String TAG = UsbSocket.class.getSimpleName();
    //与pc交互的socket通道
    private Socket socket;
    //是否让线程保持运行状态
    volatile boolean isRun;

    public PhoneThread(Socket socket){
        this.socket = socket;
        this.isRun = true;
        AppLogUtils.d(TAG, "PCThread Create"+ " --Thread Name:"+getName());
    }

    @Override
    public void run() {
        InputStream in;
        try {
            //读取连接的输入流，也就是pc端发来的数据
            in = socket.getInputStream();
            while (isRun) {
                //一次读取1K数据，最大限度是2K，但是越大的话每次读取花费的时间越大
                final int MLEN = 1024;
                byte[] buff = new byte[MLEN];
                //读取buffer
                int size = in.read(buff);
                //存在数据
                if (size >= 0) {
                    //有效的原始数据
                    byte[] rawBuffer = new byte[size];
                    //得到有效的原始数据
                    System.arraycopy(buff, 0, rawBuffer, 0, size);
                    //转成UTF-8的数据
                    final String data;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        data = new String(rawBuffer, StandardCharsets.UTF_8);
                    } else {
                        data = new String(rawBuffer);
                    }
                    AppLogUtils.d(TAG, "收到数据，大小: " + data.length() + " 原始数据:" + data +" --Thread Name:"+getName());
                    //对外抛出
                    usbSocket.onRawDataNext(rawBuffer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppLogUtils.d(TAG, "PCThread Exception:"+e.toString()+" --Thread Name:"+getName());
            //遇到异常直接断开连接，让客户端重新发起连接
            release();
        }
    }

    /**
     * 发送数据
     */
    public boolean send(byte[] data){
        if(this.socket != null){
            try {
                OutputStream outputStream = this.socket.getOutputStream();
                if(outputStream != null){
                    outputStream.write(data);
                    outputStream.flush();
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                AppLogUtils.d(TAG, "PCThread Exception:"+e.toString()+"send failed --Thread Name:"+getName());
            }
        }
        return false;
    }

    /**
     * 释放线程和socket
     */
    public void release(){
        try {
            //关闭单次连接的socket
            if(socket != null){
                socket.close();
                AppLogUtils.d(TAG, "PCThread socket.close"+" --Thread Name:"+getName());
            }
            socket = null;
        } catch (Exception e) {
            e.printStackTrace();
            AppLogUtils.d(TAG, "PCThread release Exception:"+e.toString()+" --Thread Name:"+getName());
        } finally {
            //停止线程
            isRun = false;
        }
    }

}
