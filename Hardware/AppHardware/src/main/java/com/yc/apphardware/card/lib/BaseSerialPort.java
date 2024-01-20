package com.yc.apphardware.card.lib;


import android.util.Log;

import com.aill.androidserialport.SerialPort;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BaseSerialPort implements ISerialPort {

    protected SerialPort mSerialPort;
    protected OutputStream mOutputStream;
    protected InputStream mInputStream;

    @Override
    public void open(String port, int baudrate) {
        try {
            if (mSerialPort == null) {
                //mSerialPort.setSuPath("/system/xbin/su");
                mSerialPort = new SerialPort(new File(port), baudrate, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mSerialPort != null) {
            //从串口对象中获取输出流
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();
        }
    }

    @Override
    public void writeByte(byte[] data) {
        try {
            //写入数据
            mOutputStream.write(data);
            mOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String readData() throws IOException {
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[512];
        while ((mInputStream.available()) != 0) {
            int len = mInputStream.read(buffer);
            byte[] dataByte = new byte[len];
            System.arraycopy(buffer, 0, dataByte, 0, len);
            String data = MyConverterTool.ByteArrToHex(dataByte);
            sb.append(data);
        }
        return sb.toString();
    }

    @Override
    public void close() throws IOException {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
        if (mInputStream!= null) {
            mInputStream.close();
        }
        if (mOutputStream != null) {
            mOutputStream.close();
        }
    }


}
