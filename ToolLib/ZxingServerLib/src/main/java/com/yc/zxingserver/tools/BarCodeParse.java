package com.yc.zxingserver.tools;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public final class BarCodeParse {

    /**
     * 解析一维码条形码
     *
     * @param bitmap bitmap
     * @return 解析数据
     */
    public static String parseCode(Bitmap bitmap) {
        MultiFormatReader reader = getMultiFormatReader();
        // 将 Bitmap 转换为 RGBLuminanceSource 对象
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        RGBLuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), pixels);
        // 创建 BinaryBitmap 对象
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            // 解码条形码
            Result result = reader.decode(binaryBitmap);
            String barcodeValue = result.getText();
            // 处理解析到的条形码值
            return barcodeValue;
        } catch (Exception e) {
            // 处理解码失败的情况
            return "";
        }
    }

    /**
     * 解析一维码条形码
     * 使用 YUV 数据创建 RGBLuminanceSource 对象
     * 第一步: 获取 YUV 数据：首先，你需要获取到 YUV 数据。
     * 第二步：创建 YUV 数据源：使用 YUV 数据创建一个 PlanarYUVLuminanceSource 对象，它是 ZXing 库中的一个类，用于处理 YUV 数据。
     * 第三步：创建 RGBLuminanceSource 对象：使用 source 对象创建一个 RGBLuminanceSource 对象，它是 RGBLuminanceSource 的子类。
     *
     * @param yuv yuv数据
     * @return 解析数据
     */
    public static String parseCode(byte[] yuv, int width, int height) {
        MultiFormatReader reader = getMultiFormatReader();
        // 将 Bitmap 转换为 RGBLuminanceSource 对象
        RGBLuminanceSource rgbSource = buildRGBLuminanceSource(yuv, width, height);
        // 创建 BinaryBitmap 对象
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbSource));
        try {
            // 解码条形码
            Result result = reader.decode(binaryBitmap);
            String barcodeValue = result.getText();
            // 处理解析到的条形码值
            return barcodeValue;
        } catch (Exception e) {
            // 处理解码失败的情况
            return "";
        }
    }

    public static PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        //public PlanarYUVLuminanceSource(byte[] yuvData, //传入的帧数据
        //                                  int dataWidth, //数据源图片宽度
        //                                  int dataHeight, //数据源图片高度
        //                                  int left, //识别区域的左边距
        //                                  int top, //识别区域的上边距
        //                                  int width, //识别区域的宽度
        //                                  int height, //识别区域的高度
        //                                  boolean reverseHorizontal //是否需要旋转图片
        //                                  ）
        return new PlanarYUVLuminanceSource(data, width, height, 0, 0,
                width, height, false);
    }

    /**
     * 解析一维码条形码
     * 使用 YUV 数据创建 RGBLuminanceSource 对象
     * 第一步: 获取 YUV 数据：首先，你需要获取到 YUV 数据。
     * 第二步：创建 YUV 数据源：使用 YUV 数据创建一个 PlanarYUVLuminanceSource 对象，它是 ZXing 库中的一个类，用于处理 YUV 数据。
     * 第三步：创建 RGBLuminanceSource 对象：使用 source 对象创建一个 RGBLuminanceSource 对象，它是 RGBLuminanceSource 的子类。
     *
     * @param yuv    data数据，yuv格式
     * @param width  宽度
     * @param height 高度
     * @return RGBLuminanceSource
     */
    public static RGBLuminanceSource buildRGBLuminanceSource(byte[] yuv, int width, int height) {
        PlanarYUVLuminanceSource source = buildLuminanceSource(yuv, width, height);
//        PalmSdkUtils.d(TAG + "buildRGBLuminanceSource 1 " +
//                yuv.length + " , width " + width + " , height " + height);
        //注意：renderThumbnail这个数据，跟原始数据yuv不一样，不能用这个
        int[] ints = source.renderThumbnail();
        byte[] matrix = source.getMatrix();
        int width1 = source.getWidth();
        int height1 = source.getHeight();
//        PalmSdkUtils.d(TAG + "buildRGBLuminanceSource 2 " +
//                matrix.length + " , width " + width1 + " , height " + height1);
        // 将 Bitmap 转换为 RGBLuminanceSource 对象
        RGBLuminanceSource rgbSource = new RGBLuminanceSource(
                source.getThumbnailWidth(),
                source.getThumbnailHeight(),
                source.renderThumbnail()
        );
//        PalmSdkUtils.d(TAG + "buildRGBLuminanceSource 3 " +
//                source.renderThumbnail().length + " , width " + source.getThumbnailWidth() +
//                " , height " + source.getThumbnailHeight());
        return rgbSource;
    }

    public static MultiFormatReader getMultiFormatReader() {
        MultiFormatReader reader = new MultiFormatReader();
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
        reader.setHints(hints);
        return reader;
    }
}
