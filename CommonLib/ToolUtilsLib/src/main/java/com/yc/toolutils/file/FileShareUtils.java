package com.yc.toolutils.file;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.yc.toolutils.AppLogUtils;

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
                //此处可发送多种文件
                String absolutePath = file.getAbsolutePath();
                //通过扩展名找到mimeType
                String mimeType = getMimeType(absolutePath);
                share.setType(mimeType);
                Uri uri;
                //判断7.0以上
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //第二个参数表示要用哪个ContentProvider，这个唯一值在AndroidManifest.xml里定义了
                    //若是没有定义MyFileProvider，可直接使用FileProvider替代
                    String authority = context.getPackageName() + ".fileExplorerProvider";
                    uri = FileProvider.getUriForFile(context,authority, file);
                } else {
                    uri = Uri.fromFile(file);
                }
                //content://com.yc.lifehelper.fileExplorerProvider/external_path/fileShare.txt
                //content 作为scheme；
                //com.yc.lifehelper.fileExplorerProvider 即为我们定义的 authorities，作为host；
                AppLogUtils.d("share file uri : " + uri);
                String encodedPath = uri.getEncodedPath();
                //external_path/fileShare.txt
                //如此构造后，第三方应用收到此Uri后，并不能从路径看出我们传递的真实路径，这就解决了第一个问题：
                //发送方传递的文件路径接收方完全知晓，一目了然，没有安全保障。
                AppLogUtils.d("share file uri encode path : " + encodedPath);
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //赋予读写权限
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Intent intent = Intent.createChooser(share, "分享文件");
                //交由系统处理
                context.startActivity(intent);
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
