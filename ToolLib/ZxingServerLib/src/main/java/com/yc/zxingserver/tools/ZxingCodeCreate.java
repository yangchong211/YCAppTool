package com.yc.zxingserver.tools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import androidx.annotation.FloatRange;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.yc.toolutils.system.AppLogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/6/6
 *     desc  : 生成二维码工具类
 *     revise: 参考博客
 *                  https://www.cnblogs.com/tfxz/p/12621626.html
 *                  https://www.jianshu.com/p/e5a45f9fbce3
 * </pre>
 */
public final class ZxingCodeCreate {

    /**
     * 不能直接new，否则抛个异常
     */
    private ZxingCodeCreate() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 生成二维码
     *
     * @param content   二维码的内容
     * @param heightPix 二维码的高
     * @return
     */
    public static Bitmap createQRCode(String content, int heightPix) {
        return createQRCode(content, heightPix, null);
    }

    /**
     * 生成二维码
     *
     * @param content   二维码的内容
     * @param heightPix 二维码的高
     * @param logo      logo大小默认占二维码的20%
     * @return
     */
    public static Bitmap createQRCode(String content, int heightPix, Bitmap logo) {
        return createQRCode(content, heightPix, logo, Color.BLACK);
    }

    /**
     * 生成我二维码
     *
     * @param content   二维码的内容
     * @param heightPix 二维码的高
     * @param logo      logo大小默认占二维码的20%
     * @param codeColor 二维码的颜色
     * @return
     */
    public static Bitmap createQRCode(String content, int heightPix, Bitmap logo, int codeColor) {
        return createQRCode(content, heightPix, logo, 0.2f, codeColor);
    }

    /**
     * 生成二维码
     *
     * @param content   二维码的内容
     * @param heightPix 二维码的高
     * @param logo      二维码中间的logo
     * @param ratio     logo所占比例 因为二维码的最大容错率为30%，所以建议ratio的范围小于0.3
     * @param codeColor 二维码的颜色
     * @return
     */
    public static Bitmap createQRCode(String content, int heightPix, Bitmap logo,
                                      @FloatRange(from = 0.0f, to = 1.0f) float ratio, int codeColor) {
        //配置参数
        Map<EncodeHintType, Object> hints = new HashMap<>();
        // 设置编码方式
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //容错级别
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //设置空白边距的宽度
        hints.put(EncodeHintType.MARGIN, 1); //default is 1
        return createQRCode(content, heightPix, logo, ratio, hints, codeColor);
    }

    /**
     * 生成二维码
     *
     * @param content   二维码的内容
     * @param heightPix 二维码的高
     * @param logo      二维码中间的logo
     * @param ratio     logo所占比例 因为二维码的最大容错率为30%，所以建议ratio的范围小于0.3
     * @param hints     配置参数
     * @param codeColor 二维码的颜色
     * @return
     */
    public static Bitmap createQRCode(String content, int heightPix, Bitmap logo,
                                      @FloatRange(from = 0.0f, to = 1.0f) float ratio,
                                      Map<EncodeHintType, ?> hints, int codeColor) {
        try {
            // 图像数据转换，使用了矩阵转换
            // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            BitMatrix bitMatrix = new QRCodeWriter().encode(content,
                    BarcodeFormat.QR_CODE, heightPix, heightPix, hints);
            int[] pixels = new int[heightPix * heightPix];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < heightPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * heightPix + x] = codeColor;
                    } else {
                        pixels[y * heightPix + x] = Color.WHITE;
                    }
                }
            }
            // 生成二维码图片的格式
            Bitmap bitmap = Bitmap.createBitmap(heightPix, heightPix, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, heightPix, 0, 0, heightPix, heightPix);
            if (logo != null) {
                //添加最中间的logo
                bitmap = addLogo(bitmap, logo, ratio);
            }
            return bitmap;
        } catch (WriterException e) {
            AppLogUtils.w(e.getMessage());
        }
        return null;
    }


    /**
     * 在二维码中间添加Logo图案
     *
     * @param src   二维码
     * @param logo  logo
     * @param ratio logo所占比例 因为二维码的最大容错率为30%，所以建议ratio的范围小于0.3
     * @return
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo, @FloatRange(from = 0.0f, to = 1.0f) float ratio) {
        if (src == null) {
            return null;
        }
        if (logo == null) {
            return src;
        }
        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }
        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }
        //logo大小为二维码整体大小
        float scaleFactor = srcWidth * ratio / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2.0f, srcHeight / 2.0f);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2.0f, (srcHeight - logoHeight) / 2.0f, null);
            canvas.save();
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            AppLogUtils.w(e.getMessage());
        }
        return bitmap;
    }

}
