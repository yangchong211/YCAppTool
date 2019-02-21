package com.ycbjie.library.utils.image;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.blankj.utilcode.util.LogUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
public class BitmapUtils {



    /**
     * 请求网络图片转化成bitmap
     * @param url                       url
     * @return                          将url图片转化成bitmap对象
     */
    private static long time = 0;
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
            time = (l2-l1) + time;
            LogUtils.e("毫秒值"+time);
            //请求8张图片，耗时毫秒值174
        }
        return bitmap;
    }

    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     * @return          Bitmap
     */
    public static Bitmap getSmallBitmap(String filePath, int newWidth, int newHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        //设置只解析图片的边界参数，即宽高
        options.inJustDecodeBounds = true;
        //options.inSampleSize = 2;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        //科学计算图片所需的采样比例
        options.inSampleSize = calculateInSampleSize(options, newWidth, newHeight);
        //设置图片加载的渲染模式为Config.RGB_565，能降低一半内存，但是会影响图片质量
        //options.inPreferredConfig = Bitmap.Config.RGB_565;
        // Decode bitmap with inSampleSize set
        //关闭标记，解析真实的图片
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        //质量压缩
        Bitmap newBitmap = compressImage(bitmap, 500);
        if (bitmap != null){
            bitmap.recycle();
        }
        return newBitmap;
    }


    /**
     * 计算图片的缩放值
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 质量压缩
     * @param image
     * @param maxSize
     */
    private static Bitmap compressImage(Bitmap image, int maxSize){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale
        int options = 80;
        // Store the bitmap into output stream(no compress)
        image.compress(Bitmap.CompressFormat.JPEG, options, os);
        // Compress by loop
        while ( os.toByteArray().length / 1024 > maxSize) {
            // Clean up os
            os.reset();
            // interval 10
            options -= 10;
            image.compress(Bitmap.CompressFormat.JPEG, options, os);
        }
        Bitmap bitmap = null;
        byte[] b = os.toByteArray();
        if (b.length != 0) {
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return bitmap;
    }

}
