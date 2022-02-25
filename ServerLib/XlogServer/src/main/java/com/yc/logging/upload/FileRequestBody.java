package com.yc.logging.upload;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


public class FileRequestBody extends RequestBody {

    private static final MediaType DEFAULT_CONTENT_TYPE = MediaType.parse("multipart/form-data");
    private final MediaType contentType;
    private final File file;
    private final long offset;
    private final long contentLength;

    public FileRequestBody(MediaType contentType, File file, long offset, long contentLength) {
        this.contentType = contentType;
        this.file = file;
        this.offset = offset;
        this.contentLength = contentLength;
    }

    @Override
    public MediaType contentType() {
        return contentType;
    }

    @Override
    public long contentLength() {
        return contentLength;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Buffer source = new Buffer();
        RandomAccessFile accessFile =null;
        try {
            accessFile = new RandomAccessFile(file, "r");
            accessFile.seek(offset);
            byte[] buffer = new byte[(int) contentLength];
            accessFile.read(buffer);
            source.write(buffer);
            sink.write(source, contentLength);
        } finally {
            Util.closeQuietly(accessFile);
            Util.closeQuietly(source);
        }
    }

    public static FileRequestBody create(final MediaType contentType, final File file,
            long offset, long contentLength) {
        if (file == null) throw new NullPointerException("content == null");
        return new FileRequestBody(contentType, file, offset, contentLength);
    }

    public static FileRequestBody create(final File file, long offset, long contentLength) {
        if (file == null) throw new NullPointerException("content == null");
        return new FileRequestBody(DEFAULT_CONTENT_TYPE, file, offset, contentLength);
    }

    public static FileRequestBody create(final File file) {
        if (file == null) throw new NullPointerException("content == null");
        return new FileRequestBody(DEFAULT_CONTENT_TYPE, file, 0, file.length());
    }
}
