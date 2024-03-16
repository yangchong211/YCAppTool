package com.yc.toolutils.store;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     blog  :
 *     time  : 2017/6/9.
 *     desc  : 保存文件路径
 *     revise:
 * </pre>
 */
public final class FileSaveUtils {

    /**
     * 读取共享目录下图片文件
     * @param context  上下文
     * @param filename 文件名称（带后缀a.jpg），是MediaStore查找文件的条件之一
     * @return
     */
    public static List<InputStream> getImageFile(Context context, String filename)  {
        String[] projection = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Thumbnails.DATA
        };
        List<InputStream> insList = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        //根据日期降序查询
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " DESC";
        //查询条件 “显示名称为？”
        String selection = MediaStore.Images.Media.DISPLAY_NAME + "='" + filename + "'";
        Cursor cursor =  resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, selection, null, sortOrder);
        if (cursor != null && cursor.moveToFirst()) {
            //媒体数据库中查询到的文件id
            int columnId = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            do {
                //通过mediaId获取它的uri
                int mediaId = cursor.getInt(columnId);
                //获取图片路径
                //String tPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                Uri itemUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + mediaId );
                try {
                    //通过uri获取到inputStream
                    ContentResolver cr = context.getContentResolver();
                    InputStream ins=cr.openInputStream(itemUri);
                    insList.add(ins);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        return insList;
    }

}
