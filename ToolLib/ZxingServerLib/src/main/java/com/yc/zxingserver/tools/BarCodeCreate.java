package com.yc.zxingserver.tools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.ColorInt;
import android.text.TextPaint;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.yc.toolutils.system.AppLogUtils;

import java.util.Map;

public final class BarCodeCreate {


    /**
     * 生成条形码
     * @param content
     * @param desiredWidth
     * @param desiredHeight
     * @return
     */
    public static Bitmap createBarCode(String content, int desiredWidth, int desiredHeight) {
        return createBarCode(content, BarcodeFormat.CODE_128,desiredWidth,desiredHeight,null);
    }

    /**
     * 生成条形码
     * @param content
     * @param format
     * @param desiredWidth
     * @param desiredHeight
     * @return
     */
    public static Bitmap createBarCode(String content,BarcodeFormat format, int desiredWidth, int desiredHeight) {
        return createBarCode(content,format,desiredWidth,desiredHeight,null);
    }

    public static Bitmap createBarCode(String content, int desiredWidth, int desiredHeight, boolean isShowText) {
        return createBarCode(content,BarcodeFormat.CODE_128,desiredWidth,desiredHeight,null,isShowText,40, Color.BLACK);
    }

    /**
     * 生成条形码
     * @param content
     * @param desiredWidth
     * @param desiredHeight
     * @param isShowText
     * @param codeColor
     * @return
     */
    public static Bitmap createBarCode(String content, int desiredWidth, int desiredHeight, boolean isShowText,@ColorInt int codeColor) {
        return createBarCode(content,BarcodeFormat.CODE_128,desiredWidth,desiredHeight,null,isShowText,40,codeColor);
    }

    /**
     * 生成条形码
     * @param content
     * @param format
     * @param desiredWidth
     * @param desiredHeight
     * @param hints
     * @return
     */
    public static Bitmap createBarCode(String content, BarcodeFormat format, int desiredWidth, int desiredHeight, Map<EncodeHintType,?> hints) {
        return createBarCode(content,format,desiredWidth,desiredHeight,hints,false,40,Color.BLACK);
    }

    /**
     * 生成条形码
     * @param content
     * @param format
     * @param desiredWidth
     * @param desiredHeight
     * @param hints
     * @param isShowText
     * @return
     */
    public static Bitmap createBarCode(String content, BarcodeFormat format, int desiredWidth, int desiredHeight, Map<EncodeHintType,?> hints, boolean isShowText) {
        return createBarCode(content,format,desiredWidth,desiredHeight,hints,isShowText,40,Color.BLACK);
    }

    /**
     * 生成条形码
     * @param content
     * @param format
     * @param desiredWidth
     * @param desiredHeight
     * @param isShowText
     * @param codeColor
     * @return
     */
    public static Bitmap createBarCode(String content, BarcodeFormat format, int desiredWidth, int desiredHeight,  boolean isShowText,@ColorInt int codeColor) {
        return createBarCode(content,format,desiredWidth,desiredHeight,null,isShowText,40,codeColor);
    }

    /**
     * 生成条形码
     * @param content
     * @param format
     * @param desiredWidth
     * @param desiredHeight
     * @param hints
     * @param isShowText
     * @return
     */
    public static Bitmap createBarCode(String content, BarcodeFormat format, int desiredWidth, int desiredHeight, Map<EncodeHintType,?> hints, boolean isShowText,@ColorInt int codeColor) {
        return createBarCode(content,format,desiredWidth,desiredHeight,hints,isShowText,40,codeColor);
    }

    /**
     * 生成条形码
     * @param content
     * @param format
     * @param desiredWidth
     * @param desiredHeight
     * @param hints
     * @param isShowText
     * @param textSize
     * @param codeColor
     * @return
     */
    public static Bitmap createBarCode(String content,BarcodeFormat format, int desiredWidth, int desiredHeight,Map<EncodeHintType,?> hints,boolean isShowText,int textSize,@ColorInt int codeColor) {
        if(TextUtils.isEmpty(content)){
            return null;
        }
        final int WHITE = Color.WHITE;
        final int BLACK = codeColor;

        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix result = writer.encode(content, format, desiredWidth,
                    desiredHeight, hints);
            int width = result.getWidth();
            int height = result.getHeight();
            int[] pixels = new int[width * height];
            // All are 0, or black, by default
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            if(isShowText){
                return addCode(bitmap,content,textSize,codeColor,textSize/2);
            }
            return bitmap;
        } catch (WriterException e) {
            AppLogUtils.w(e.getMessage());
        }
        return null;
    }

    /**
     * 条形码下面添加文本信息
     * @param src
     * @param code
     * @param textSize
     * @param textColor
     * @return
     */
    private static Bitmap addCode(Bitmap src,String code,int textSize,@ColorInt int textColor,int offset) {
        if (src == null) {
            return null;
        }

        if (TextUtils.isEmpty(code)) {
            return src;
        }

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();

        if (srcWidth <= 0 || srcHeight <= 0) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight + textSize + offset * 2, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            TextPaint paint = new TextPaint();
            paint.setTextSize(textSize);
            paint.setColor(textColor);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(code,srcWidth/2,srcHeight + textSize /2 + offset,paint);
            canvas.save();
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            AppLogUtils.w(e.getMessage());
        }
        return bitmap;
    }



}
