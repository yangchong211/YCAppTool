package com.yc.logclient.client;

import android.util.Log;

import com.yc.logclient.bean.AppLogBean;
import com.yc.logclient.inter.OnEnqueueListener;


/**
 * 职责描述: 负责处理 跨进程通信时，单次传输的日志太大(900K)左右,将日志进行切分，防止IPC 失败。
 */


public class IPCLargeProcessor {

    public static final String TAG = IPCLargeProcessor.class.getSimpleName();

    /**
     * 跨进程通信最大传输字节数
     * 跨进程通信允许的传递数据的最大值 900K
     */
    private final long TransanctionTooLargeSize = 900 * 1024;
    /**
     * MsgBean/String 占用内存设定的比例,用于调整打印字符串的单条字符串的大小。
     */
    public float ratio = 1.2F;
    /**
     * 缓存队列的最大长度
     */
    public int maxCacheSize;

    public IPCLargeProcessor(int maxCacheSize, float ratio) {
        this.maxCacheSize = maxCacheSize;
        this.ratio = ratio;
    }

    public void enqueue(AppLogBean bean, OnEnqueueListener listener) {
        double maxMsgBeanSize = TransanctionTooLargeSize * 1.0 / maxCacheSize;
        double maxPerByteString = maxMsgBeanSize / ratio;
        double msgByteLength = bean.getMsg().getBytes().length;

        int count = (int) Math.ceil(msgByteLength / maxPerByteString);//查分个数
        //Log.d(TAG,"addWriteLogCheckingSize maxMsgBeanSize:"+maxMsgBeanSize+",msgByteLength:"+msgByteLength+",count:"+count+",wholeMsg:"+bean.getMsg());
        String divideSession = bean.getMsg().hashCode() + "";
        if (count > 1) {
            int piecelength = (int) Math.ceil(bean.getMsg().length() * 1.0 / count);
            for (int i = 0; i < count; i++) {
                int startIndex = i * piecelength;
                int endIndex = Math.min((i + 1) * piecelength, bean.getMsg().length());
                String subMsg = "";
                if (endIndex >= startIndex) {
                    subMsg = "[-" + divideSession + "-]" + bean.getMsg().substring(startIndex, endIndex);
                    Log.d(TAG, "addWriteLogCheckingSize maxPerByteString:" + maxPerByteString + ",msgByteLength:" + msgByteLength + ",分片个数:" + count + ",单片长度:" + piecelength + "msglength:" + bean.getMsg().length() + "index:" + i + ",startIndex:" + startIndex + ",endIndex:" + endIndex + ",subMsg:" + subMsg);
                } else {
                    subMsg = "---- endIndex:" + endIndex + ",startIndex:" + startIndex + ",endIndex<= sartIndex -- please pay attetion";
                    Log.e(TAG, "addWriteLogCheckingSize maxPerByteString:" + maxPerByteString + ",msgByteLength:" + msgByteLength + ",分片个数:" + count + ",单片长度:" + piecelength + "msglength:" + bean.getMsg().length() + "index:" + i + ",startIndex:" + startIndex + ",endIndex:" + endIndex + ",subMsg:" + subMsg);
                }

                AppLogBean b = new AppLogBean(bean.getType(), bean.getLeve(), bean.getKey(), bean.getTag(), subMsg);
                listener.doQueue(b);
            }
        } else {
            listener.doQueue(bean);
        }
    }

}
