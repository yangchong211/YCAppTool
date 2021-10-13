package com.yc.zxingserver.scan;

import android.graphics.Bitmap;

import com.google.zxing.Result;

public interface OnCaptureListener {


    /**
     * 接收解码后的扫码结果
     * @param result
     * @param barcode
     * @param scaleFactor
     */
    void onHandleDecode(Result result, Bitmap barcode, float scaleFactor);


}
