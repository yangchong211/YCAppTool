package com.yc.appcompress;

import android.content.Context;
import android.graphics.Bitmap;


public final class Compress {

    private static volatile Compress INSTANCE;
    private Context context;
    /**
     * 最大宽度
     */
    private float maxWidth = 720.0f;
    /**
     * 最大高度
     */
    private float maxHeight = 960.0f;
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


    public static Compress getDefault(Context context) {
        if (INSTANCE == null) {
            synchronized (Compress.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Compress(context);
                }
            }
        }
        return INSTANCE;
    }

    private Compress(Context context) {
        this.context = context;
    }



    /**
     * 采用建造者模式，设置Builder
     */
    public static class Builder {

        private final Compress mCompress;

        public Builder(Context context) {
            mCompress = new Compress(context);
        }

        /**
         * 设置图片最大宽度
         * @param maxWidth  最大宽度
         */
        public Builder setMaxWidth(float maxWidth) {
            mCompress.maxWidth = maxWidth;
            return this;
        }

        /**
         * 设置图片最大高度
         * @param maxHeight 最大高度
         */
        public Builder setMaxHeight(float maxHeight) {
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

        public Compress build() {
            return mCompress;
        }
    }
    
}
