package com.yc.zxingserver.tools;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.yc.toolutils.system.AppLogUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/6/6
 *     desc  : 解析二维码工具类
 *     revise: 参考：https://blog.csdn.net/mountain_hua/article/details/80646089
 * </pre>
 */
public final class ZxingCodeParse {

    public static final int DEFAULT_REQ_WIDTH = 450;
    public static final int DEFAULT_REQ_HEIGHT = 800;


    /**
     * 解析一维码/二维码图片
     *
     * @param bitmapPath 路径
     * @return 内容
     */
    public static String parseCode(String bitmapPath) {
        Map<DecodeHintType, Object> hints = new HashMap<>();
        //添加可以解析的编码类型
        Vector<BarcodeFormat> decodeFormats = new Vector<>();
        decodeFormats.addAll(DecodeManager.ONE_D_FORMATS);
        decodeFormats.addAll(DecodeManager.QR_CODE_FORMATS);
        decodeFormats.addAll(DecodeManager.DATA_MATRIX_FORMATS);
        decodeFormats.addAll(DecodeManager.AZTEC_FORMATS);
        decodeFormats.addAll(DecodeManager.PDF417_FORMATS);

        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
        return parseCode(bitmapPath, hints);
    }

    /**
     * 解析一维码/二维码图片
     *
     * @param bitmapPath 路径
     * @param hints      解析编码类型
     * @return 码内容
     */
    public static String parseCode(String bitmapPath, Map<DecodeHintType, Object> hints) {
        Result result = parseCodeResult(bitmapPath, hints);
        if (result != null) {
            return result.getText();
        }
        return null;
    }

    /**
     * 解析一维码/二维码图片
     *
     * @param bitmapPath 路径
     * @param hints      解析编码类型
     * @return 码内容
     */
    public static Result parseCodeResult(String bitmapPath, Map<DecodeHintType, Object> hints) {
        return parseCodeResult(bitmapPath, DEFAULT_REQ_WIDTH, DEFAULT_REQ_HEIGHT, hints);
    }


    /**
     * 解析一维码/二维码图片
     *
     * @param bitmapPath 路径
     * @param reqWidth   宽
     * @param reqHeight  高
     * @param hints      解析编码类型
     * @return 码内容
     */
    public static Result parseCodeResult(String bitmapPath, int reqWidth,
                                         int reqHeight, Map<DecodeHintType, Object> hints) {
        Result result = null;
        try {
            MultiFormatReader reader = new MultiFormatReader();
            reader.setHints(hints);
            RGBLuminanceSource source = getRGBLuminanceSource(
                    ZxingImageUtils.compressBitmap(bitmapPath, reqWidth, reqHeight));
            if (source != null) {

                boolean isReDecode;
                try {
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                    result = reader.decodeWithState(bitmap);
                    isReDecode = false;
                } catch (Exception e) {
                    isReDecode = true;
                }

                if (isReDecode) {
                    try {
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source.invert()));
                        result = reader.decodeWithState(bitmap);
                        isReDecode = false;
                    } catch (Exception e) {
                        isReDecode = true;
                    }
                }

                if (isReDecode) {
                    try {
                        BinaryBitmap bitmap = new BinaryBitmap(
                                new GlobalHistogramBinarizer(source));
                        result = reader.decodeWithState(bitmap);
                        isReDecode = false;
                    } catch (Exception e) {
                        isReDecode = true;
                    }
                }

                if (isReDecode && source.isRotateSupported()) {
                    try {
                        BinaryBitmap bitmap = new BinaryBitmap(
                                new HybridBinarizer(source.rotateCounterClockwise()));
                        result = reader.decodeWithState(bitmap);
                    } catch (Exception e) {

                    }
                }
                reader.reset();
            }
        } catch (Exception e) {
            AppLogUtils.w(e.getMessage());
        }

        return result;
    }


    /**
     * 解析二维码图片
     *
     * @param bitmapPath 路径
     * @return 码内容
     */
    public static String parseQRCode(String bitmapPath) {
        Map<DecodeHintType, Object> hints = new HashMap<>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        return parseQRCode(bitmapPath, hints);
    }

    /**
     * 解析二维码图片
     *
     * @param bitmapPath 路径
     * @param hints      配置参数
     * @return 码内容
     */
    public static String parseQRCode(String bitmapPath, Map<DecodeHintType, ?> hints) {
        Result result = parseQRCodeResult(bitmapPath, hints);
        if (result != null) {
            return result.getText();
        }
        return null;
    }

    /**
     * 解析二维码图片
     *
     * @param bitmapPath 路径
     * @param hints      配置参数
     * @return 码内容
     */
    public static Result parseQRCodeResult(String bitmapPath, Map<DecodeHintType, ?> hints) {
        return parseQRCodeResult(bitmapPath, DEFAULT_REQ_WIDTH, DEFAULT_REQ_HEIGHT, hints);
    }

    /**
     * 解析二维码图片
     *
     * @param bitmapPath 路径
     * @param reqWidth   宽
     * @param reqHeight  高
     * @param hints      配置参数
     * @return 码内容
     */
    public static Result parseQRCodeResult(String bitmapPath, int reqWidth, int reqHeight, Map<DecodeHintType, ?> hints) {
        Result result = null;
        try {
            QRCodeReader reader = new QRCodeReader();
            RGBLuminanceSource source = getRGBLuminanceSource(ZxingImageUtils.compressBitmap(bitmapPath, reqWidth, reqHeight));
            if (source != null) {
                boolean isReDecode;
                try {
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                    result = reader.decode(bitmap, hints);
                    isReDecode = false;
                } catch (Exception e) {
                    isReDecode = true;
                }
                if (isReDecode) {
                    try {
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source.invert()));
                        result = reader.decode(bitmap, hints);
                        isReDecode = false;
                    } catch (Exception e) {
                        isReDecode = true;
                    }
                }
                if (isReDecode) {
                    try {
                        BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
                        result = reader.decode(bitmap, hints);
                        isReDecode = false;
                    } catch (Exception e) {
                        isReDecode = true;
                    }
                }
                if (isReDecode && source.isRotateSupported()) {
                    try {
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source.rotateCounterClockwise()));
                        result = reader.decode(bitmap, hints);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                reader.reset();
            }
        } catch (Exception e) {
            AppLogUtils.w(e.getMessage());
        }
        return result;
    }

    /**
     * 获取RGBLuminanceSource
     *
     * @param bitmap bitmap
     * @return 资源
     */
    private static RGBLuminanceSource getRGBLuminanceSource(@NonNull Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0,
                bitmap.getWidth(), bitmap.getHeight());
        return new RGBLuminanceSource(width, height, pixels);
    }


}
