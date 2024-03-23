package com.yc.zxingserver.tools;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.yc.toolutils.system.AppLogUtils;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 解析摄像头二进制数据
 * 扫码场景
 */
public final class ZxingDecodeHelper {

    private static volatile ZxingDecodeHelper instance;
    private static final String TAG = "ZxingDecodeHelper: ";
    private DecodeListener decodeListener;
    private static final int DECODE_YUV = 1;
    private DecodeThread decodeThread;
    private int width;
    private int height;
    private MultiFormatReader multiFormatReader;
    private long lastFeedTime = 0;
    private final AtomicBoolean isInit = new AtomicBoolean(false);
    private final AtomicBoolean isAvailable = new AtomicBoolean(true);
    private final AtomicInteger countLong = new AtomicInteger(0);

    public static ZxingDecodeHelper getInstance() {
        if (instance == null) {
            synchronized (ZxingDecodeHelper.class) {
                if (instance == null) {
                    instance = new ZxingDecodeHelper();
                }
            }
        }
        return instance;
    }

    public void setDecodeListener(DecodeListener decodeListener) {
        this.decodeListener = decodeListener;
    }

    private ZxingDecodeHelper() {
        if (isInit.get()) {
            AppLogUtils.d(TAG + "已经初始化了");
        } else {
            init();
        }
    }

    private void init() {
        HandlerThread thread = new HandlerThread("qr_decode_thread");
        thread.start();
        decodeThread = new DecodeThread(thread.getLooper());
        //扫码初始化
        multiFormatReader = new MultiFormatReader();
        Collection<BarcodeFormat> decodeFormats = EnumSet.of(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_128);
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
        String characterSet = "utf-8";
        hints.put(DecodeHintType.CHARACTER_SET, characterSet);
        hints.put(DecodeHintType.TRY_HARDER, true);
        multiFormatReader.setHints(hints);
        AppLogUtils.d(TAG + "初始化完成");
        isInit.set(true);
    }

    public void release() {
        isAvailable.set(false);
        decodeListener = null;
        decodeThread.getLooper().quit();
        multiFormatReader = null;
        isInit.set(false);
        AppLogUtils.d(TAG + "销毁");
    }

    public void pushYuv(byte[] yuv, int width, int height) {
        if (isAvailable.get()) {
            isAvailable.set(false);
            this.width = width;
            this.height = height;
            decodeThread.obtainMessage(DECODE_YUV, yuv).sendToTarget();
            //PalmSdkUtils.d(TAG + "pushYuv " + yuv.length);
            countLong.getAndIncrement();
        }
    }

    private class DecodeThread extends Handler {
        DecodeThread(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null && msg.what == DECODE_YUV) {
                decode((byte[]) msg.obj, width, height);
                isAvailable.set(true);
            }
        }
    }

    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        return new PlanarYUVLuminanceSource(data, width, height, 0, 0,
                width, height, false);
    }

    /**
     * 刷码解析
     *
     * @param data   data数据
     * @param width  宽
     * @param height 高
     */
    public void decode(byte[] data, int width, int height) {
        long startTime = System.currentTimeMillis();
        //Java中的nanoTime是一个用于获取当前系统时间的方法。与其他获取时间的方法相比，nanoTime的精确度更高，可用于需要高精度计时的场景。
        //这个时间戳往往会在计算时间差、比较时间等场景下被使用。
        //long startTime = System.nanoTime();
        if (startTime - lastFeedTime > 3000) {
            AppLogUtils.d(TAG + "decode start 3秒回调 " + countLong.get());
            lastFeedTime = startTime;
            countLong.set(0);
        }
        PlanarYUVLuminanceSource source = buildLuminanceSource(data, width, height);
        Result rawResult = null;
        boolean isReDecode;
        try {
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            rawResult = multiFormatReader.decodeWithState(bitmap);
            isReDecode = false;
        } catch (ReaderException re) {
            isReDecode = true;
            //PalmSdkUtils.d(TAG + "decode exception " + re);
        }
        if (isReDecode) {
            try {
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source.invert()));
                rawResult = multiFormatReader.decodeWithState(bitmap);
                isReDecode = false;
            } catch (Exception e) {
                isReDecode = true;
            }
        }
        if (isReDecode) {
            try {
                BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
                rawResult = multiFormatReader.decodeWithState(bitmap);
                isReDecode = false;
            } catch (Exception e) {
                isReDecode = true;
            }
        }
        /*if (isReDecode) {
            try {
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                rawResult = multiFormatReader.decodeWithState(bitmap);
                isReDecode = false;
            } catch (ReaderException re) {
                //PalmSdkUtils.d(TAG + "decode exception " + re);
                isReDecode = true;
            }
        }*/
        if (isReDecode && source.isRotateSupported()) {
            try {
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source.rotateCounterClockwise()));
                rawResult = multiFormatReader.decodeWithState(bitmap);
            } catch (Exception e) {
                AppLogUtils.d(TAG + "decode exception " + e);
            }
        }
        long endTime = System.currentTimeMillis();
        AppLogUtils.d(TAG + "decode 耗时" + (endTime - startTime));
        multiFormatReader.reset();
        if (rawResult != null) {
            BarcodeFormat format = rawResult.getBarcodeFormat();
            //类型
            String type = format.toString();
            String content = rawResult.getText().trim();
            AppLogUtils.d(TAG + "decode result type " + type + " ; content" + content);
            if (!TextUtils.isEmpty(content)) {
                if (decodeListener != null) {
                    decodeListener.onDecodeSuccess(content);
                }
            } else {
                //空数据
                if (decodeListener != null) {
                    decodeListener.onDecodeFailed(-1);
                }
            }
        } else {
            AppLogUtils.d(TAG + "decode result null");
            //失败
            if (decodeListener != null) {
                decodeListener.onDecodeFailed(-2);
            }
        }
    }

    public interface DecodeListener {
        /**
         * 解析二维码
         *
         * @param content 二维码数据
         */
        void onDecodeSuccess(final String content);

        /**
         * 解析失败
         *
         * @param error 异常
         */
        void onDecodeFailed(final int error);
    }
}