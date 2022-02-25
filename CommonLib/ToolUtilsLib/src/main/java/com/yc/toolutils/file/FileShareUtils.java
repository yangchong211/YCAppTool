package com.yc.toolutils.file;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

public final class FileShareUtils {


    /**
     * 系统分享文件
     * 需要使用Provider
     *
     * @param context 上下文
     * @param file    文件
     */
    public static boolean shareFile(Context context, File file) {
        boolean isShareSuccess;
        try {
            if (null != file && file.exists()) {
                Intent share = new Intent(Intent.ACTION_SEND);
                Uri uri;
                //判断7.0以上
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(context,
                            context.getPackageName() + ".fileExplorerProvider", file);
                } else {
                    uri = Uri.fromFile(file);
                }
                share.putExtra(Intent.EXTRA_STREAM, uri);
                //此处可发送多种文件
                share.setType(getMimeType(file.getAbsolutePath()));
                share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(Intent.createChooser(share, "分享文件"));
                isShareSuccess = true;
            } else {
                isShareSuccess = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            isShareSuccess = false;
        }
        return isShareSuccess;
    }

    // 根据文件后缀名获得对应的MIME类型。
    private static String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime = "*/*";
        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (IllegalStateException e) {
                return mime;
            } catch (IllegalArgumentException e) {
                return mime;
            } catch (RuntimeException e) {
                return mime;
            }
        }
        return mime;
    }


    /**
     * 系统分享文件
     * 需要使用Provider
     *
     * @param context 上下文
     * @param file    文件
     */
    @Deprecated
    public static boolean systemShare(Context context, File file) {
        if (file != null && file.exists()) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                Uri uri = FileProvider.getUriForFile(context,
                        context.getPackageName() + ".fileExplorerProvider", file);
                ContentResolver contentResolver = context.getContentResolver();
                //InputStream inputStream = contentResolver.openInputStream(uri);
                String type = contentResolver.getType(uri);
                share.setDataAndType(uri, type);
                share.putExtra("android.intent.extra.STREAM", uri);
                if (share.resolveActivity(context.getPackageManager()) == null) {
                    share.setDataAndType(uri, "*/*");
                }
                context.startActivity(share);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }



}
