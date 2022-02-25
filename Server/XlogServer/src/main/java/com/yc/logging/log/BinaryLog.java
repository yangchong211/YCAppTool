package com.yc.logging.log;

import androidx.annotation.RestrictTo;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class BinaryLog extends AbstractLog {

    private byte[] mData;

    @Override
    public String getContent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getTag() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getMsg() {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] getData() {
        return mData;
    }

    public BinaryLog(byte[] data) {
        mData = data;
    }
}
