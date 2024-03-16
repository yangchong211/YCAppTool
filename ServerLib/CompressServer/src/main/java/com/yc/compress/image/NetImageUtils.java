package com.yc.compress.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2020/07/22
 *     desc   : 网络图片请求工具
 *     revise :
 * </pre>
 */
public final class NetImageUtils {


    /**
     * 请求网络图片转化成bitmap
     * @param url                       url
     * @return                          将url图片转化成bitmap对象
     */
    public static Bitmap returnBitMap(String url) {
        long l1 = System.currentTimeMillis();
        URL myFileUrl = null;
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (myFileUrl==null){
            return null;
        }
        try {
            conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(5000);
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    conn.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            long l2 = System.currentTimeMillis();
            long time = (l2-l1) ;
            Log.e("毫秒值",time+"");
        }
        return bitmap;
    }



}
