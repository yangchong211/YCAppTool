package com.yc.toolutils.store;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

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
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                //通过扩展名找到mimeType
                String mimeType = FileMimeType.getMimeType(file.getAbsolutePath());
                shareIntent.setType(mimeType);
                Uri uri = AppFileUriUtils.file2Uri(context, file);
                if (uri == null){
                    return false;
                }
                Log.d("","share file uri : " + uri);
                String encodedPath = uri.getEncodedPath();
                //external_path/fileShare.txt
                //如此构造后，第三方应用收到此Uri后，并不能从路径看出我们传递的真实路径，这就解决了第一个问题：
                //发送方传递的文件路径接收方完全知晓，一目了然，没有安全保障。
                Log.d("","share file uri encode path : " + encodedPath);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //赋予读写权限
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Intent intent = Intent.createChooser(shareIntent, "分享文件");
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
