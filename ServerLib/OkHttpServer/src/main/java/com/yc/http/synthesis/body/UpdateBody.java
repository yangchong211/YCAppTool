package com.yc.http.synthesis.body;

import com.yc.http.synthesis.EasyUtils;
import com.yc.http.synthesis.model.ContentType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/12/14
 *    desc   : 上传文件流
 */
public class UpdateBody extends RequestBody {

    /** 上传源 */
    private final Source mSource;

    /** 内容类型 */
    private final MediaType mMediaType;

    /** 内容名称 */
    private final String mKeyName;

    /** 内容大小 */
    private final long mLength;

    public UpdateBody(File file) throws FileNotFoundException {
        this(Okio.source(file), ContentType.guessMimeType(file.getName()), file.getName(), file.length());
    }

    public UpdateBody(InputStream inputStream, String name) throws IOException {
        this(Okio.source(inputStream), ContentType.STREAM, name, inputStream.available());
    }

    public UpdateBody(Source source, MediaType type, String name, long length) {
        mSource = source;
        mMediaType = type;
        mKeyName = name;
        mLength = length;
    }

    @Override
    public MediaType contentType() {
        return mMediaType;
    }

    @Override
    public long contentLength() {
        if (mLength == 0) {
            // 如果不能获取到大小，则返回 -1，参考 RequestBody.contentLength 方法实现
            return -1;
        }
        return mLength;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        try {
            sink.writeAll(mSource);
        } finally {
            EasyUtils.closeStream(mSource);
        }
    }

    public String getKeyName() {
        return mKeyName;
    }
}