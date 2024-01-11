package com.yc.compress.utils;

import android.graphics.Bitmap;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2017/5/11
 *     desc   : 图片压缩配置类
 *     revise :
 *     GitHub :
 * </pre>
 */
public final class CompressConfig {

    /**
     * 图片压缩格式
     */
    private Bitmap.CompressFormat comPressFormat= Bitmap.CompressFormat.JPEG;
    /**
     * 图片格式
     */
    private String fileSuffix=".jpg";
    /**
     * 图片压缩像素模式
     */
    private Bitmap.Config pixelConfig= Bitmap.Config.ARGB_8888;
    /**
     * 无论宽高，目标允许的最大像素,启用像素压缩时生效
     */
    private int maxPixel = 1280;
    /**
     * 图片压缩的目标大小，单位B（最终图片的大小会小于这个值），启用质量压缩时生效
     */
    private int targetSize = 200 * 1024;
    /**
     * 默认压缩质量为80
     */
    private int quality = 80;
    /**
     * 质量压缩最大值
     */
    private int maxSize;
    /**
     * 获取默认的配置
     */
    public static CompressConfig getDefault() {
        return new CompressConfig();
    }

    public int getTargetSize() {
        return targetSize;
    }

    public int getMaxPixel() {
        return maxPixel;
    }

    public Bitmap.Config getPixelConfig() {
        return pixelConfig;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public Bitmap.CompressFormat getComPressFormat() {
        return comPressFormat;
    }

    public int getQuality() {
        return quality;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final CompressConfig config;

        private Builder() {
            config = new CompressConfig();
        }

        public Builder setTargetSize(int size) {
            config.targetSize = size;
            return this;
        }

        public Builder setMaxPixel(int pixel) {
            config.maxPixel = pixel;
            return this;
        }

        public Builder setQuality(int quality) {
            config.quality = quality;
            return this;
        }

        /**
         * 设置质量压缩最大值
         * @param maxSize   最大值
         */
        public Builder setMaxSize(int maxSize) {
            config.maxSize = maxSize;
            return this;
        }

        public Builder setBitmapConfig(Bitmap.Config bitmapConfig) {
            config.pixelConfig = bitmapConfig;
            return this;
        }

        public Builder setCompressFormat(Bitmap.CompressFormat format){
            config.comPressFormat = format;
            if (format== Bitmap.CompressFormat.JPEG){
                config.fileSuffix = ".jpg";
            }else if (format== Bitmap.CompressFormat.PNG){
                config.fileSuffix = ".png";
            }else {
                config.fileSuffix = ".webp";
            }
            return this;
        }

        public CompressConfig build() {
            return config;
        }
    }


}
