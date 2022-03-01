package com.ycbjie.webviewlib.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

public final class BitmapUtils {

    public static Bitmap base64ToBitmap(String base64Data) {
        BitmapFactory.Options bitmapOption = new BitmapFactory.Options();
        bitmapOption.inPreferredConfig = Bitmap.Config.ARGB_4444;
        String[] split = base64Data.split(",");
        byte[] decodedString = Base64.decode(split[1], Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length, bitmapOption);
        return decodedByte;
    }

    public static boolean isBase64Img(String imgurl) {
        if (!TextUtils.isEmpty(imgurl) && (
                imgurl.startsWith("data:image/png;base64,")
                        || imgurl.startsWith("data:image/*;base64,")
                        || imgurl.startsWith("data:image/jpg;base64,")
                        || imgurl.startsWith("data:image/jpeg;base64,"))) {
            return true;
        }
        return false;
    }

}
