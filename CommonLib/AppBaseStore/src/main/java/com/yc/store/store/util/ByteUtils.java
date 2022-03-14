package com.yc.store.store.util;

import java.nio.ByteBuffer;

/**
 * Created by tom on 15/9/15.
 */
public class ByteUtils {
    private static ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE / Byte.SIZE);

    public static byte[] longToBytes(long x) {
        buffer.putLong(0, x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getLong();
    }
}
