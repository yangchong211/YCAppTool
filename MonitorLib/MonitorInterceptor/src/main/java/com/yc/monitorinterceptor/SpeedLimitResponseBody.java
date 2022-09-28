package com.yc.monitorinterceptor;

import android.os.SystemClock;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/01/30
 *     desc  : ResponseBody限制处理
 *     revise:
 * </pre>
 */
public class SpeedLimitResponseBody extends ResponseBody {

    private static String TAG = "SpeedLimitResponseBody";
    /**
     * 限速字节
     */
    private final long mSpeedByte;
    private final ResponseBody mResponseBody;
    private BufferedSource mBufferedSource;

    SpeedLimitResponseBody(long speed, ResponseBody source) {
        this.mResponseBody = source;
        //转成字节
        this.mSpeedByte = speed * 1024L;
    }

    @Override
    public MediaType contentType() {
        //ResponseBody的类型
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        //ResponseBody的内容长度
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            BufferedSource source = mResponseBody.source();
            MyForwardingSource myForwardingSource = new MyForwardingSource(source);
            mBufferedSource = Okio.buffer(myForwardingSource);
        }
        return mBufferedSource;
    }

    private class MyForwardingSource extends ForwardingSource {

        public MyForwardingSource(@NotNull Source delegate) {
            super(delegate);
        }

        /**
         * 如果小于1s 会重置
         */
        private long cacheTotalBytesRead;
        /**
         * 分片读取1024个字节开始时间 小于1s会重置
         */
        private long cacheStartTime;

        @Override
        public long read(Buffer sink, long byteCount) throws IOException {
            if (cacheStartTime == 0) {
                cacheStartTime = SystemClock.uptimeMillis();
            }

            //默认8K 精确到1K -1代表已经读取完毕
            long bytesRead = super.read(sink.buffer(), 1024L);
            if (bytesRead == -1) {
                return bytesRead;
            }
            //一般为1024
            cacheTotalBytesRead = cacheTotalBytesRead + bytesRead;

            /*
             * 判断当前请求累计消耗的时间 即相当于读取1024个字节所需要的时间
             */
            long costTime = SystemClock.uptimeMillis() - cacheStartTime;

            //如果每次分片读取时间小于ls sleep 延迟时间
            if (costTime <= 1000L) {
                if (cacheTotalBytesRead >= mSpeedByte) {
                    long sleep = 1000L - costTime;
                    SystemClock.sleep(sleep);
                    //重置计算
                    cacheStartTime = 0L;
                    cacheTotalBytesRead = 0L;
                }
            }
            return bytesRead;
        }
    }

}
