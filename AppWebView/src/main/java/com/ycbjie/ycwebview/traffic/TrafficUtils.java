package com.ycbjie.ycwebview.traffic;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class TrafficUtils implements Serializable {

    /**
     * https://blog.csdn.net/chchaooo_cc/article/details/80685853
     *
     * getMobileRxBytes()//获取通过Mobile连接收到的字节总数，但不包含WiFi static long
     * getMobileRxPackets()//获取Mobile连接收到的数据包总数 static long
     * getMobileTxBytes()//Mobile发送的总字节数 static long
     * getMobileTxPackets()//Mobile发送的总数据包数 static long
     * getTotalRxBytes()//获取总的接受字节数，包含Mobile和WiFi等 static long
     * getTotalRxPackets()//总的接受数据包数，包含Mobile和WiFi等 static long
     * getTotalTxBytes()//总的发送字节数，包含Mobile和WiFi等 static long
     * getTotalTxPackets()//发送的总数据包数，包含Mobile和WiFi等 static long
     * getUidRxBytes(int uid)//获取某个网络UID的接受字节数
     * static long getUidTxBytes(int uid) //获取某个网络UID的发送字节数
     */

    /**
     * 不支持状态【标识变量】
     */
    private static final int UNSUPPORT = -1;
    /**
     * 打印信息标志
     */
    private static final String TAG = "TrafficBean";
    /**
     * 当前对象实例
     */
    private static TrafficUtils instance;
    /**
     * 当前应用的uid
     */
    private static int UID;
    /**
     * 上一次记录网络字节流
     */
    private long preRxBytes = 0;
    /**
     * timer
     */
    private Timer mTimer = null;
    /**
     * 消息处理器
     */
    private Handler handler;
    /**
     * 更新频率
     */
    private final int UPDATE_FREQUENCY = 1;
    private int times = 1;


    public TrafficUtils(Context context, Handler handler) {
        this.handler = handler;
        UID = getUid(context);
    }

    /**
     * 获取实例对象
     *
     * @param context
     * @param handler
     * @return
     */
    public static TrafficUtils getInstance(Context context, Handler handler) {
        if (instance == null) {
            instance = new TrafficUtils(context, handler);
        }
        return instance;
    }

    /**
     * 获取总流量
     *
     * @return
     */
    public long getTrafficInfo() {
        long recTraffic = UNSUPPORT;//下载流量
        long sendTraffic = UNSUPPORT;//上传流量
        recTraffic = getRecTraffic();
        sendTraffic = getSendTraffic();

        if (recTraffic == UNSUPPORT || sendTraffic == UNSUPPORT) {
            return UNSUPPORT;
        } else {
            return recTraffic + sendTraffic;
        }
    }

    /**
     * 获取上传流量
     *
     * @return
     */
    private long getSendTraffic() {
        long sendTraffic = UNSUPPORT;
        sendTraffic = TrafficStats.getUidTxBytes(UID);
        if (sendTraffic == UNSUPPORT) {
            return UNSUPPORT;
        }
        RandomAccessFile rafSend = null;
        String sndPath = "/proc/uid_stat/" + UID + "/tcp_snd";
        try {
            rafSend = new RandomAccessFile(sndPath, "r");
            sendTraffic = Long.parseLong(rafSend.readLine());
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException: " + e.getMessage());
            sendTraffic = UNSUPPORT;
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rafSend != null)
                    rafSend.close();
            } catch (IOException e) {
                Log.w(TAG, "Close RandomAccessFile exception: " + e.getMessage());
            }
        }
        return sendTraffic;
    }

    /**
     * 获取下载流量
     * 某个应用的网络流量数据保存在系统的
     * /proc/uid_stat/$UID/tcp_rcv | tcp_snd文件中
     *
     * @return
     */
    private long getRecTraffic() {
        long recTraffic = UNSUPPORT;
        recTraffic = TrafficStats.getUidRxBytes(UID);
        if (recTraffic == UNSUPPORT) {
            return UNSUPPORT;
        }
        Log.i(TAG, recTraffic + " ---1");
        //访问数据文件
        RandomAccessFile rafRec = null;
        String rcvPath = "/proc/uid_stat/" + UID + "/tcp_rcv";
        try {
            rafRec = new RandomAccessFile(rcvPath, "r");
            recTraffic = Long.parseLong(rafRec.readLine()); // 读取流量统计
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException: " + e.getMessage());
            recTraffic = UNSUPPORT;
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rafRec != null)
                    rafRec.close();
            } catch (IOException e) {
                Log.w(TAG, "Close RandomAccessFile exception: " + e.getMessage());
            }
        }
        Log.i("test", recTraffic + "--2");
        return recTraffic;
    }


    /**
     * 获取当前下载流量总和
     *
     * @return
     */
    public static long getNetworkRxBytes() {
        return TrafficStats.getTotalRxBytes();
    }

    /**
     * 获取当前上传流量总和
     *
     * @return
     */
    public static long getNetworkTxBytes() {
        return TrafficStats.getTotalTxBytes();
    }

    /**
     * 获取当前网速
     *
     * @return
     */
    public double getNetSpeed() {
        long curRxBytes = getNetworkRxBytes();
        if (preRxBytes == 0)
            preRxBytes = curRxBytes;
        long bytes = curRxBytes - preRxBytes;
        preRxBytes = curRxBytes;
        //int kb = (int) Math.floor(bytes / 1024 + 0.5);
        double kb = (double) bytes / (double) 1024;
        BigDecimal bd = new BigDecimal(kb);
        return bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 开启流量监控
     */
    public void startCalculateNetSpeed() {
        preRxBytes = getNetworkRxBytes();
        stopCalculateNetSpeed();
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (times == UPDATE_FREQUENCY) {
                        Message msg = new Message();
                        msg.what = 1;
                        //msg.arg1 = getNetSpeed();
                        msg.obj = getNetSpeed();
                        handler.sendMessage(msg);
                        times = 1;
                    } else {
                        times++;
                    }
                }
            }, 1000, 1000);
        }
    }

    /**
     * 停止网速监听计算
     */
    public void stopCalculateNetSpeed() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    /**
     * 获取当前应用uid
     *
     * @return
     */
    public int getUid(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            //修改
            ApplicationInfo ai = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return ai.uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //定义GB的计算常量
    private static final int GB = 1024 * 1024 *1024;
    //定义MB的计算常量
    private static final int MB = 1024 * 1024;
    //定义KB的计算常量
    private static final int KB = 1024;
    public static String bytes2kb(long bytes){
        //格式化小数
        DecimalFormat format = new DecimalFormat("###.0");
        if (bytes / GB >= 1){
            return format.format(bytes / GB) + "GB";
        }
        else if (bytes / MB >= 1){
            return format.format(bytes / MB) + "MB";
        }
        else if (bytes / KB >= 1){
            return format.format(bytes / KB) + "KB";
        }else {
            return bytes + "B";
        }
    }

}
