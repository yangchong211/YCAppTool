package com.yc.toolutils.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : io流基础工具类
 *     revise:
 * </pre>
 */
public final class BaseIoUtils {

    /**
     * 将流对象转化为byte字节
     * @param inputStream           is流
     * @return                      byte字节
     */
    public static byte[] toByteArray(InputStream inputStream) {
        if (inputStream == null) {
            return new byte[0];
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int read;
        byte[] data = new byte[4096];
        try {
            while ((read = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, read);
            }
        } catch (Exception ignored) {
            return new byte[0];
        } finally {
            try {
                buffer.close();
            } catch (IOException ignored) {
            }
        }
        byte[] bytes = buffer.toByteArray();
        return bytes;
    }

    /**
     * 将流对象转化为byte字节
     * @param inputStream           is流
     * @return                      byte字节
     * @throws IOException
     */
    public static byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    /**
     * 将流对象转化为byte字节
     * @param inputStream           is流
     * @return                      byte字节
     * @throws IOException
     */
    public static byte[] readBytes(InputStream inputStream , int bufferSize) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        if (bufferSize < 0 || bufferSize> 1024 * 1024){
            bufferSize = 1024;
        }
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}
