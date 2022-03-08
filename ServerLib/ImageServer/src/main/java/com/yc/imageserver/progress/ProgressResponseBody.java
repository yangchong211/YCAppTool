package com.yc.imageserver.progress;

import androidx.annotation.Nullable;
import android.util.Log;

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
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/10/24
 *     desc  : glide加载进度工具
 *     revise: 自定义ResponseBody，处理加载进度逻辑，比较复杂
 *             新建一个ProgressResponseBody类，并让它继承自OkHttp的ResponseBody，
 *             然后在这个类当中去编写具体的监听下载进度的逻辑
 * </pre>
 */
public class ProgressResponseBody extends ResponseBody {

    private BufferedSource bufferedSource;
    private final ResponseBody responseBody;
    private ProgressListener listener;

    /**
     * 该构造方法中要求传入一个url参数和一个ResponseBody参数
     * @param url                               url参数就是图片的url地址
     * @param responseBody                      ResponseBody参数则是OkHttp拦截到的原始的ResponseBody对象
     */
    public ProgressResponseBody(String url, ResponseBody responseBody) {
        this.responseBody = responseBody;
        //调用了ProgressInterceptor中的LISTENER_MAP来去获取该url对应的监听器回调对象，
        //有了这个对象，待会就可以回调计算出来的下载进度
        listener = ProgressInterceptor.LISTENER_MAP.get(url);
    }

    /**
     * 在contentType()和contentLength()方法中直接就调用传入的原始ResponseBody的contentType()
     * 和contentLength()方法即可，这相当于一种委托模式。
     * @return                                  MediaType
     */
    @Nullable
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    /**
     * 在contentType()和contentLength()方法中直接就调用传入的原始ResponseBody的contentType()
     * 和contentLength()方法即可，这相当于一种委托模式。
     * @return                                  long内容长度
     */
    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            //调用了原始ResponseBody的source()方法来去获取Source对象
            BufferedSource source = responseBody.source();
            //接下来将这个Source对象封装到了一个ProgressSource对象当中
            ProgressSource progressSource = new ProgressSource(source);
            //用Okio的buffer()方法封装成BufferedSource对象返回
            bufferedSource = Okio.buffer(progressSource);
        }
        return bufferedSource;
    }

    /**
     * 自定义的继承自ForwardingSource的实现类
     * ForwardingSource也是一个使用委托模式的工具，它不处理任何具体的逻辑，只是负责将传入的原始Source对象进行中转
     */
    private class ProgressSource extends ForwardingSource {

        long totalBytesRead = 0;
        /**
         * 当前进度
         */
        int currentProgress;

        ProgressSource(Source source) {
            super(source);
        }

        /**
         * 在ProgressSource中我们重写了read()方法，
         * 然后在read()方法中获取该次读取到的字节数以及下载文件的总字节数，
         * 并进行一些简单的数学计算就能算出当前的下载进度了。
         * @param sink                              buffer
         * @param byteCount                         字节数
         * @return
         * @throws IOException
         */
        @Override
        public long read(@NotNull Buffer sink, long byteCount) throws IOException {
            long bytesRead = super.read(sink, byteCount);
            //读取相应body实体对象中的数据长度
            long fullLength = responseBody.contentLength();
            if (bytesRead == -1) {
                totalBytesRead = fullLength;
            } else {
                totalBytesRead += bytesRead;
            }
            int progress = (int) (100f * totalBytesRead / fullLength);
            Log.d("加载图片进度值--", "download progress is " + progress);
            if (listener != null && progress != currentProgress) {
                listener.onProgress(progress);
            }
            if (listener != null && totalBytesRead == fullLength) {
                listener = null;
            }
            currentProgress = progress;
            return bytesRead;
        }
    }
}

