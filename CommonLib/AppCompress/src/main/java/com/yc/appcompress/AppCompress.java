package com.yc.appcompress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;


public final class AppCompress {

    private static volatile AppCompress INSTANCE;
    private Context context;
    /**
     * 最大宽度
     */
    private int maxWidth = 720;
    /**
     * 最大高度
     */
    private int maxHeight = 960;
    /**
     * 默认压缩后的方式为JPEG
     */
    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
    /**
     * 默认压缩质量为80
     */
    private int quality = 80;
    /**
     * 默认的图片处理方式是ARGB_8888
     */
    private Bitmap.Config bitmapConfig = Bitmap.Config.ARGB_8888;


    public static AppCompress getDefault(Context context) {
        if (INSTANCE == null) {
            synchronized (AppCompress.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppCompress(context);
                }
            }
        }
        return INSTANCE;
    }

    private AppCompress(Context context) {
        this.context = context;
    }

    public Bitmap compressInputStream(InputStream is){
        Bitmap image = BitmapFactory.decodeStream(is);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, os);
        if (os.toByteArray().length / 1024 > 1024) {
            //判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            os.reset();
            //重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, quality, os);
            //这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = bitmapConfig;
        Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = CompressUtils.calculateInSampleSize(newOpts, maxWidth, maxHeight);
        //设置缩放比例
        newOpts.inSampleSize = be;
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        is = new ByteArrayInputStream(os.toByteArray());
        bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        int desWidth = w / be;
        int desHeight = h / be;
        bitmap = Bitmap.createScaledBitmap(bitmap, desWidth, desHeight, true);
        //压缩好比例大小后再进行质量压缩
        //return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;
    }


    /**
     * 采用建造者模式，设置Builder
     */
    public static class Builder {

        private final AppCompress mCompress;

        public Builder(Context context) {
            mCompress = new AppCompress(context);
        }

        /**
         * 设置图片最大宽度
         * @param maxWidth  最大宽度
         */
        public Builder setMaxWidth(int maxWidth) {
            mCompress.maxWidth = maxWidth;
            return this;
        }

        /**
         * 设置图片最大高度
         * @param maxHeight 最大高度
         */
        public Builder setMaxHeight(int maxHeight) {
            mCompress.maxHeight = maxHeight;
            return this;
        }

        /**
         * 设置压缩的后缀格式
         */
        public Builder setCompressFormat(Bitmap.CompressFormat compressFormat) {
            mCompress.compressFormat = compressFormat;
            return this;
        }

        /**
         * 设置Bitmap的参数
         */
        public Builder setBitmapConfig(Bitmap.Config bitmapConfig) {
            mCompress.bitmapConfig = bitmapConfig;
            return this;
        }

        /**
         * 设置压缩质量，建议80
         * @param quality   压缩质量，[0,100]
         */
        public Builder setQuality(int quality) {
            mCompress.quality = quality;
            return this;
        }

        public AppCompress build() {
            return mCompress;
        }
    }
    
}
