package com.yc.monitorinterceptor;

import android.os.SystemClock;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Sink;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/01/30
 *     desc  : RequestBody限制处理
 *     revise:
 * </pre>
 */
public class SpeedLimitRequestBody extends RequestBody {

    private final long mSpeedByte;
    private final RequestBody mRequestBody;
    private BufferedSink mBufferedSink;

    SpeedLimitRequestBody(long speed, RequestBody source) {
        this.mRequestBody = source;
        //转成字节
        this.mSpeedByte = speed * 1024;
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (mBufferedSink == null) {
            //mBufferedSink = Okio.buffer(sink(sink));
            //默认8K 精确到1K
            mBufferedSink = new ByteCountBufferedSink(sink(sink), 1024L);
        }
        mRequestBody.writeTo(mBufferedSink);
        mBufferedSink.close();
    }

    private Sink sink(final BufferedSink sink) {
        return new MyForwardingSink(sink);
    }

    private class MyForwardingSink extends ForwardingSink {

        public MyForwardingSink(@NotNull Sink delegate) {
            super(delegate);
        }

        private long cacheTotalBytesWritten;
        private long cacheStartTime;

        @Override
        public void write(@NotNull Buffer source, long byteCount) throws IOException {
            if (cacheStartTime == 0) {
                cacheStartTime = SystemClock.uptimeMillis();
            }

            super.write(source, byteCount);
            cacheTotalBytesWritten += byteCount;

            long endTime = SystemClock.uptimeMillis() - cacheStartTime;
            //如果在一秒内
            if (endTime <= 1000L) {
                //大小就超出了限制
                if (cacheTotalBytesWritten >= mSpeedByte) {
                    long sleep = 1000L - endTime;
                    SystemClock.sleep(sleep);

                    //重置计算
                    cacheStartTime = 0L;
                    cacheTotalBytesWritten = 0L;
                }
            }
        }
    }

}
