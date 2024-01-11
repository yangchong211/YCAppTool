package com.yc.compress.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2017/5/11
 *     desc   : 图片压缩
 *     revise :
 *     GitHub :
 * </pre>
 */
public final class AppCompress {

    private static volatile AppCompress INSTANCE;
    private CompressConfig compressConfig;

    private AppCompress() {

    }

    public static AppCompress getInstance() {
        if (INSTANCE == null) {
            synchronized (AppCompress.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppCompress();
                }
            }
        }
        return INSTANCE;
    }

    public void setCompressConfig(CompressConfig config){
        compressConfig = config;
    }

    public CompressConfig getCompressConfig() {
        if (compressConfig == null){
            compressConfig = CompressConfig.getDefault();
        }
        return compressConfig;
    }

    /**
     * 质量压缩
     */
    public Bitmap compressQuality(String path) {
        Bitmap inputBitmap = BitmapFactory.decodeFile(path);
        return CompressUtils.compressImage(inputBitmap);
    }

    /**
     * 质量压缩
     */
    public Bitmap compressSize(String path) {
        Bitmap inputBitmap = BitmapFactory.decodeFile(path);
        return CompressUtils.compressImage(inputBitmap);
    }

    /**
     * 采样率压缩 & 质量压缩
     */
    public Bitmap compressSizePath(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置options.inJustDecodeBounds会返回整张图片或者图片的size
        //计算采样率只需要宽高
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = getCompressConfig().getPixelConfig();
        //此处在option中已取得宽高
        BitmapFactory.decodeFile(path,options);
        //设置采样率
        options.inSampleSize = CompressUtils.calculateSampleSize(options);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path,options);
        Bitmap newBitmap = CompressUtils.compressByQuality(bitmap,100,options);
        return newBitmap;
    }

}
