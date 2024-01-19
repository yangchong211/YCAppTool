package com.yc.apphardware.card.lib;

import android.os.Build;
import android.util.Log;

import com.aill.androidserialport.SerialPort;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MySerialPort extends BaseSerialPort {

    private String mPort;
    private int mIBaudRate;

    private static byte cSTX = 0x02;    //包头
    private static byte cFrame = 0x00;    //包序号
    private static byte[] cTotleLen;    //包长度
    private static byte cType;    //指令类型
    private static byte cCMD;    //指令命令  //01:打开M1卡 02:关闭M1卡 03:非接触式IC卡寻卡 04：CPU卡复位 05:CPU卡
    //ADPU指令11:认证M卡的密钥 12:读M卡块数据 13:读M卡块数据
    private static byte[] cBufLen;    //指令数据长度
    private static byte cLrc;    //包LRC校验值
    private static byte cETX = 0x40;    //包尾

    private static final long read_timeout = 1000;

    public static long write_time;
    public static long read_time;

    public MySerialPort() {
        setSerialPort();
        open(mPort, mIBaudRate);
    }

    private void setSerialPort() {
        String product_model = Build.MODEL;
        if (product_model.contains("SP306+/SP308+") || product_model.contains("SP306/SP308")) {
            mPort = "/dev/ttyMT1";
            mIBaudRate = 115200;
        } else if (product_model.contains("SP801") || product_model.contains("FR35")
                || product_model.contains("SP806") || product_model.contains("S225")) {
            mPort = "/dev/ttyS4";
            mIBaudRate = 115200;
        } else if (product_model.contains("SP805")) {
            mPort = "/dev/ttyS2";
            mIBaudRate = 115200;
        } else {
            mPort = "/dev/ttyMT2";
            mIBaudRate = 115200;
        }
    }

    public void setSerialPort(String port, int iBaudRate) {
        mPort = port;
        mIBaudRate = iBaudRate;
    }

    public String setCosOrder(byte type, byte cmd) throws Exception {

        byte[] send_data = new byte[10];  //数据组成：尾部校验码加包尾

        send_data[0] = cSTX;
        send_data[1] = cFrame;
        send_data[2] = 0x00;
        send_data[3] = 0x04;
        send_data[4] = type;
        send_data[5] = cmd;
        send_data[6] = 0x00;
        send_data[7] = 0x00;
        send_data[8] = MyConverterTool.CheckSum(send_data, 1, (send_data.length - 3));//计算校验码
        send_data[9] = cETX;

        return writeData(send_data);
    }

    public String setCosOrder(byte[] data, byte type, byte cmd) throws Exception {
        byte[] send_data = new byte[8 + data.length + 2];  //数据组成：尾部校验码加包尾
        send_data[0] = cSTX;
        send_data[1] = cFrame;
        send_data[2] = 0x00;
        send_data[3] = (byte) (data.length + 4);
        send_data[4] = type;
        send_data[5] = cmd;
        send_data[6] = 0x00;
        send_data[7] = (byte) data.length;

        for (int i = 0; i < data.length; i++) {
            send_data[i + 8] = data[i];
        }

        send_data[data.length + 8] = MyConverterTool.CheckSum(send_data, 1, (send_data.length - 3));//计算校验码
        send_data[data.length + 9] = cETX;

        return writeData(send_data);
    }

    private String writeData(byte[] bOutArray) throws Exception {
        String ss = MyConverterTool.ByteArrToHex(bOutArray);
        if (CardReadManager.getInstance().getDebugStatus()) {
            Log.i("CardComm", "send command = " + ss);
            write_time = System.currentTimeMillis();
        }

        mInputStream.skip(mInputStream.available());
        writeByte(bOutArray);
        return readData(mInputStream);
    }

    private String readData(InputStream is) throws Exception {
        long start_time = System.currentTimeMillis();
        while (true) {
            //超时退出
            if (is.available() == 0) {
                if ((System.currentTimeMillis() - start_time) >= read_timeout) {
                    Log.e("CardComm", "Read Timeout!");
                    return null;
                }
                Thread.sleep(10);
                continue;
            }

            StringBuilder sb = new StringBuilder();
            byte[] buffer = new byte[512];

            while ((is.available()) != 0) {
                int len = is.read(buffer);

                byte[] dataByte = new byte[len];
                System.arraycopy(buffer, 0, dataByte, 0, len);
                String data = MyConverterTool.ByteArrToHex(dataByte);

                sb.append(data);
            }

            String readData = sb.toString();
            if (CardReadManager.getInstance().getDebugStatus()) {
                Log.i("CardComm", "read data is : " + readData);
            }

            if (CardReadManager.getInstance().getDebugStatus())
                read_time = System.currentTimeMillis() - write_time;

            readData = readData.replace(" ", "");

            byte[] readDataByte = MyConverterTool.HexToByteArr(readData);
            int readDataLength = readDataByte.length;
            if (readDataLength < 11) {   // 有效数据长度不会少于11字节
                Log.e("CardComm", "read data length error");
                continue;
            }
            if (readDataByte[0] != 0x02 || readDataByte[readDataLength - 1] != 0x40) {   // 包头尾有误
                Log.e("CardComm", "data format error");
                continue;
            }
            if (MyConverterTool.CheckSum(readDataByte, 1, readDataLength - 3) != readDataByte[readDataLength - 2]) {
                Log.e("CardComm", "checksum mismatch");
                continue;
            }
            return readData;
        }
    }

}
