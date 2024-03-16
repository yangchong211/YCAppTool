package com.yc.compress.image;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2020/07/22
 *     desc   : 图片存储工具类
 *     revise :
 * </pre>
 */
public final class ImageSaveUtils {


    /**
     * 将图片保存为png格式图片
     * @param bmp           图片bitmap
     * @param path             png图片file
     */
    public static boolean saveBitmapAsPng(Bitmap bmp, String path) {
        File newFile = new File(path);
        if (!newFile.exists()) {
            //创建一个File对象所对应的目录，成功返回true，否则false。且File对象必须为路径而不是文件。
            //创建多级目录，创建路径中所有不存在的目录
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return saveBitmapAsPng(bmp, newFile);
    }

    /**
     * 将图片保存为png格式图片
     * @param bmp           图片bitmap
     * @param f             png图片file
     */
    public static boolean saveBitmapAsPng(Bitmap bmp, File f) {
        boolean success;
        try {
            FileOutputStream out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            success = false;
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    /**
     * 将图片保存为png格式图片
     * @param bmp           图片bitmap
     * @param path             png图片file
     */
    public static boolean saveBitmapAsJpg(Bitmap bmp, String path) {
        File newFile = new File(path);
        if (!newFile.exists()) {
            //创建一个File对象所对应的目录，成功返回true，否则false。且File对象必须为路径而不是文件。
            //创建多级目录，创建路径中所有不存在的目录
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return saveBitmapAsJpg(bmp, newFile);
    }

    /**
     * 将图片保存为png格式图片
     * @param bmp           图片bitmap
     * @param f             png图片file
     */
    public static boolean saveBitmapAsJpg(Bitmap bmp, File f) {
        boolean success;
        try {
            FileOutputStream out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            success = false;
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }



    public static boolean saveBitmap(Context context, Bitmap bitmap, String filePath) {
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            insertImage(context,file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 把图片加入到系统图库中
     * @param context               上下文
     * @param image                 图片file
     */
    public static void insertImage(Context context,String image){
        File newFile = new File(image);
        if (!newFile.exists()) {
            //创建一个File对象所对应的目录，成功返回true，否则false。且File对象必须为路径而不是文件。
            //创建多级目录，创建路径中所有不存在的目录
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        insertImage(context,newFile);
    }

    /**
     * 把图片加入到系统图库中
     * @param context               上下文
     * @param image                 图片file
     */
    public static void insertImage(Context context,File image){
        //把文件插入到系统图库
        try {
            ContentResolver contentResolver = context.getContentResolver();
            String absolutePath = image.getAbsolutePath();
            String name = image.getName();
            MediaStore.Images.Media.insertImage(contentResolver, absolutePath, name, null);

            // 最后通知图库更新
            Uri uri = Uri.parse("file://" + image);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 最后通知图库更新
     * @param context               上下文
     * @param image                 图片file
     */
    public static void refreshImage(Context context,File image){
        String absolutePath = image.getAbsolutePath();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            final Uri contentUri = Uri.parse(absolutePath);
            scanIntent.setData(contentUri);
            context.sendBroadcast(scanIntent);
        } else {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse(absolutePath)));
        }
    }

    /**
     * 扫描路径图片显示在多媒体相册中
     * @param context               上下文
     * @param filePath              文件路径
     */
    public static void scannerImage(Context context, String filePath) {
        // 通知系统多媒体扫描该文件，否则会导致拍摄出来的图片或者视频没有及时显示到相册中，而需要通过重启手机才能看到
        MediaScannerConnection.scanFile(context, new String[]{filePath},
        null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String path, Uri uri) {
                Log.i("ExternalStorage", "Scanned " + path + ":");
                Log.i("ExternalStorage", "-> uri=" + uri);
            }
        });
    }

}
