package com.yc.http.synthesis.exception;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/11/16
 *    desc   : MD5 校验异常
 */
public final class MD5Exception extends NetHttpException {

    private final String mMD5;

    public MD5Exception(String message, String md5) {
        super(message);
        mMD5 = md5;
    }

    public String getMD5() {
        return mMD5;
    }
}