package com.yc.socketbusiness;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpSocketChat {

    /**
     * 分别启动发送方和接收方
     */
    public static void sendMessage(String host,int port){
        System.out.println("Sender Start...");

        //1.创建socket服务
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        //2.封装数据
        String str = "Did you recite words today";
        byte[] bytes = str.getBytes();
        //地址
        InetAddress address = null;
        try {
            //host : 192.168.31.137 , 找到ip地址
            address = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        //参数：数据、长度、地址、端口
        DatagramPacket dp = new DatagramPacket(bytes,bytes.length,address,port);

        //3.发送数据包
        try {
            if (ds != null) {
                ds.send(dp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //4.关闭socket服务
            if (ds != null) {
                ds.close();
            }
        }
    }

    public static void receiveMessage(int userPort){
        System.out.println("Receiver Start...");

        //1.创建udp的socket服务,并声明端口号
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(userPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        //2.创建接收数据的数据包
        byte[] bytes = new byte[1024];
        DatagramPacket dp = new DatagramPacket(bytes,bytes.length);

        //3.将数据接收到数据包中，为阻塞式方法
        try {
            if (ds != null) {
                ds.receive(dp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //4.解析数据
        //发送方IP
        InetAddress address = dp.getAddress();
        //发送方端口
        int port = dp.getPort();
        String content = new String(dp.getData(),0,dp.getLength());
        System.out.println("address:"+address+"---port:"+port+"---content:"+content);

        //关闭服务
        if (ds != null) {
            ds.close();
        }
    }

}
