package com.yc.usbsocket;
import java.io.Serializable;


public class UsbSocket implements Serializable {

    private static final String TAG = UsbSocket.class.getSimpleName();
    //volatile防止指令重排序，内存可见(缓存中的变化及时刷到主存，并且其他的线程副本内存失效，必须从主存获取)，但不保证原子性
    private static volatile UsbSocket singleton = null;
    //USB交互的服务
    private UsbThread usbThread;

    /**
     * 获取单例
     * @return 单例
     */
    public static UsbSocket getInstance(){
        //第一次判断，假设会有好多线程，如果singleton 没有被实例化，那么就会到下一步获取锁，只有一个能获取到，
        //如果已经实例化，那么直接返回了，减少除了初始化时之外的所有锁获取等待过程
        //如果没有第一次判断的话，在多线程高并发情况下每个线程都要获取synchronized锁，这是非常耗内存的事
        if(singleton == null){
            synchronized (UsbSocket.class){
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
                    singleton = new UsbSocket();
                }
            }
        }
        return singleton;
    }

    /**
     * 构造函数
     */
    private UsbSocket() {
        //如果已存在，直接抛出异常，保证只会被new 一次，解决反射破坏单例的方案
        if (singleton != null) {
            throw new RuntimeException("对象已存在不可重复创建");
        }
        usbThread = new UsbThread(this);
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
        usbThread = new UsbThread(this);
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


    /********************** 点餐机连接结果回调回调 *****************/

    private OnConnectListener onConnectListener;

    public void setOnConnectListener(OnConnectListener onConnectListener) {
        this.onConnectListener = onConnectListener;
    }

    void onConnectNext(final boolean result){
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

    void onRawDataNext(final byte[] buffer){
        if(this.onRawDataListener != null){
            onRawDataListener.onData(buffer);
        }
    }

    public interface OnRawDataListener {
        void onData(byte[] buff);
    }
}
