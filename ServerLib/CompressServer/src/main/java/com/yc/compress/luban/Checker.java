package com.yc.compress.luban;

import android.graphics.BitmapFactory;

import com.yc.fileiohelper.BaseIoUtils;
import com.yc.compress.image.PicCalculateUtils;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

enum Checker {
    /**
     * 单例对象
     */
    SINGLE;

    private static final String JPG = ".jpg";

    private final byte[] JPEG_SIGNATURE = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};

    /**
     * Determine if it is JPG.
     *
     * @param is image file input stream
     */
    boolean isJPG(InputStream is) {
        return isJPG(BaseIoUtils.toByteArray(is));
    }

    /**
     * Returns the degrees in clockwise. Values are 0, 90, 180, or 270.
     */
    int getOrientation(InputStream is) {
        return PicCalculateUtils.getOrientation(BaseIoUtils.toByteArray(is));
    }

    private boolean isJPG(byte[] data) {
        if (data == null || data.length < 3) {
            return false;
        }
        byte[] signatureB = new byte[]{data[0], data[1], data[2]};
        return Arrays.equals(JPEG_SIGNATURE, signatureB);
    }


    String extSuffix(InputStreamProvider input) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input.open(), null, options);
            return options.outMimeType.replace("image/", ".");
        } catch (Exception e) {
            return JPG;
        }
    }

    boolean needCompress(int leastCompressSize, String path) {
        if (leastCompressSize > 0) {
            File source = new File(path);
            return source.exists() && source.length() > (leastCompressSize << 10);
        }
        return true;
    }
}
