package com.yc.compress.luban;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.yc.compress.image.AppBitmapUtils;
import com.yc.compress.image.PicCalculateUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Responsible for starting compress and managing active and cached resources.
 */
class Engine {
    private final InputStreamProvider srcImg;
    private final File tagImg;
    private int srcWidth;
    private int srcHeight;
    private final boolean focusAlpha;

    Engine(InputStreamProvider srcImg, File tagImg, boolean focusAlpha) throws IOException {
        this.tagImg = tagImg;
        this.srcImg = srcImg;
        this.focusAlpha = focusAlpha;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;

        BitmapFactory.decodeStream(srcImg.open(), null, options);
        this.srcWidth = options.outWidth;
        this.srcHeight = options.outHeight;
    }

    File compress() throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //计算采样率
        options.inSampleSize = PicCalculateUtils.computeSize(srcWidth,srcHeight);

        Bitmap tagBitmap = BitmapFactory.decodeStream(srcImg.open(), null, options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        if (Checker.SINGLE.isJPG(srcImg.open())) {
            //旋转Bitmap的角度，获取一张新的图片
            tagBitmap = AppBitmapUtils.rotatingImage(tagBitmap, Checker.SINGLE.getOrientation(srcImg.open()));
        }
        tagBitmap.compress(focusAlpha ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 60, stream);
        tagBitmap.recycle();

        FileOutputStream fos = new FileOutputStream(tagImg);
        fos.write(stream.toByteArray());
        fos.flush();
        fos.close();
        stream.close();

        return tagImg;
    }
}