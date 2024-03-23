package com.yc.socket.easy;


import com.yc.toolutils.system.AppLogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Usb通信机制的Socket类封装,最原始的协议，这里不做业务相关的操作
 */
public class EasySocket implements Serializable {

    private static final String TAG = EasySocket.class.getSimpleName();
    //volatile防止指令重排序，内存可见(缓存中的变化及时刷到主存，并且其他的线程副本内存失效，必须从主存获取)，但不保证原子性
    private static volatile EasySocket singleton = null;
    //端口号
    private static final int PORT = 8004;
    //USB交互的服务
    private USBThread usbThread;

    /**
     * 获取单例
     * @return 单例
     */
    public static EasySocket getInstance(){
        //第一次判断，假设会有好多线程，如果singleton 没有被实例化，那么就会到下一步获取锁，只有一个能获取到，
        //如果已经实例化，那么直接返回了，减少除了初始化时之外的所有锁获取等待过程
        //如果没有第一次判断的话，在多线程高并发情况下每个线程都要获取synchronized锁，这是非常耗内存的事
        if(singleton == null){
            synchronized (EasySocket.class){
                //第二次判断是因为假设有两个线程A、B,两个同时通过了第一个if，然后A获取了锁，进入然后判断singleton是null，他就实例化了singleton，然后他出了锁，
                //这时候线程B经过等待A释放的锁，B获取锁了，如果没有第二个判断，那么他还是会去new Singleton()，再创建一个实例，所以为了防止这种情况，需要第二次判断
                if(singleton == null){
                    //下面这句代码其实分为三步：
                    //1.开辟内存分配给这个对象
                    //2.初始化对象
                    //3.将内存地址赋给虚拟机栈内存中的singleton变量
                    //注意上面这三步，如果没有volatile修饰变量singleton，第2步和第3步的顺序是随机的，这是计算机指令重排序的问题
                    //假设有两个线程，其中一个线程执行下面这行代码，如果第三步先执行了，就会把没有初始化的内存赋值给singleton
                    //然后恰好这时候有另一个线程执行了第一个判断if(singleton == null)，然后就会发现singleton指向了一个内存地址
                    //这另一个线程就直接返回了这个没有初始化的内存，就会出问题。
                    singleton = new EasySocket();
                }
            }
        }
        return singleton;
    }

    /**
     * 构造函数
     */
    private EasySocket() {
        //如果已存在，直接抛出异常，保证只会被new 一次，解决反射破坏单例的方案
        if (singleton != null) {
            throw new RuntimeException("对象已存在不可重复创建");
        }
        usbThread = new USBThread();
    }

    //解决被序列化破坏单例的问题
    private Object readResolve(){
        return singleton;
    }

    /**
     * 发送数据
     * @param data 字节码
     */
    public boolean send(byte[] data){
        if(usbThread != null && usbThread.isRun){
            return usbThread.send(data);
        }
        return false;
    }

    /**
     * 启动USB服务
     */
    public void start() {
        if(usbThread != null){
            usbThread.release();
        }
        usbThread = new USBThread();
        usbThread.start();
    }

    /**
     * 停止USB服务
     */
    public void stop() {
        if(usbThread != null){
            usbThread.release();
        }
        usbThread = null;
    }

    /**
     * 用来启动USB通信的总线程
     */
    private class USBThread extends Thread {

        //USB通信服务
        private ServerSocket serverSocket = null;
        //是否让线程保持运行状态
        private volatile boolean isRun = true;
        //当前连接的socket线程
        private PCThread pcThread;

        public USBThread(){
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
                            onConnectNext(false);
                        }
                        pcThread = new PCThread(socket);
                        pcThread.start();
                        //重新连接新的
                        onConnectNext(true);
                    }
                }
            } catch (Exception e) {
                //e.printStackTrace();
                //当serverSocket断开的时候这里会抛出异常
                AppLogUtils.d(TAG, "USBThread Exception:"+e.toString()+ " --Thread Name:"+getName());
                //异常退出，表示断开连接
                onConnectNext(false);
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

    /**
     * 用来与PC交互的线程
     */
    private class PCThread extends Thread{

        //与pc交互的socket通道
        private Socket socket;
        //是否让线程保持运行状态
        private volatile boolean isRun;

        private PCThread(Socket socket){
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
                        final String data = new String(rawBuffer, StandardCharsets.UTF_8);
                        AppLogUtils.d(TAG, "收到数据，大小: " + data.length() + " 原始数据:" + data +" --Thread Name:"+getName());
                        //对外抛出
                        onRawDataNext(rawBuffer);
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

    /********************** 点餐机连接结果回调回调 *****************/

    private OnConnectListener onConnectListener;

    public void setOnConnectListener(OnConnectListener onConnectListener) {
        this.onConnectListener = onConnectListener;
    }

    private void onConnectNext(final boolean result){
        if(this.onConnectListener != null){
            onConnectListener.onConnect(result);
        }
    }

    public interface OnConnectListener {
        void onConnect(boolean success);
    }

    /********************** 得到原始数据 *****************/

    private OnRawDataListener onRawDataListener;

    public void setOnRawDataListener(OnRawDataListener onRawDataListener) {
        this.onRawDataListener = onRawDataListener;
    }

    private void onRawDataNext(final byte[] buffer){
        if(this.onRawDataListener != null){
            onRawDataListener.onData(buffer);
        }
    }

    public interface OnRawDataListener {
        void onData(byte[] buff);
    }
}
