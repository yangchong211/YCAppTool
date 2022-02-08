package com.yc.monitorfilelib;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Locale;

public final class FileExplorerUtils {

    public static final String DB = "db";
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String XML = ".xml";
    public static final String JSON = ".json";
    public static final boolean IS_DEBUG = true;

    private FileExplorerUtils() {

    }

    /**
     * 日志
     * @param log                       日志
     */
    public static void logInfo(String log){
        if (IS_DEBUG){
            Log.i("FileExplorer",log);
        }
    }

    /**
     * 日志
     * @param log                       日志
     */
    public static void logError(String log){
        if (IS_DEBUG){
            Log.e("FileExplorer",log);
        }
    }

    /**
     * 这个是获取文件的后缀名
     * @param file                      file文件
     * @return                          后缀名
     */
    public static String getSuffix(File file) {
        return file != null && file.exists() ?
                file.getName().substring(file.getName().lastIndexOf(".") + 1)
                        .toLowerCase(Locale.getDefault()) : "";
    }

    /**
     * 系统分享文件
     * 需要使用Provider
     * @param context               上下文
     * @param file                  文件
     */
    public static void shareFile(Context context, File file) {
        try {
            if (null != file && file.exists()) {
                Intent share = new Intent(Intent.ACTION_SEND);
                Uri uri;
                //判断7.0以上
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(context, context.getPackageName() + ".crashFileProvider", file);
                } else {
                    uri = Uri.fromFile(file);
                }
                share.putExtra(Intent.EXTRA_STREAM, uri);
                //此处可发送多种文件
                share.setType(getMimeType(file.getAbsolutePath()));
                share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(Intent.createChooser(share, "分享文件"));
            } else {
                Toast.makeText(context, "分享文件不存在", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "分享失败：" + e.toString(), Toast.LENGTH_SHORT).show();
        }
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
     * 复制内容到剪切板
     * @param context                   上下文
     * @param content                   内容
     */
    public static void copyToClipBoard(Context context , String content){
        if (!TextUtils.isEmpty(content)){
            //获取剪贴版
            ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
            //创建ClipData对象
            //第一个参数只是一个标记，随便传入。
            //第二个参数是要复制到剪贴版的内容
            ClipData clip = ClipData.newPlainText("", content);
            //传入clipdata对象.
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "复制成功：" + content , Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 是否是db文件
     * @param file                      文件
     * @return
     */
    public static boolean isDB(File file) {
        if (file == null) {
            return false;
        } else {
            String suffix = getSuffix(file);
            return DB.equals(suffix);
        }
    }

    /**
     * 是否是sp文件
     * @param file                      文件
     * @return
     */
    public static boolean isSp(File file) {
        File parentFile = file.getParentFile();
        return parentFile != null && parentFile.getName().equals(SHARED_PREFS)
                && file.getName().contains(XML);
    }

    /**
     * 将文件大小转化为具体的kb单位
     * @param size                          大小，字节
     * @return
     */
    public static SpannableString getPrintSizeForSpannable(long size) {
        SpannableString spannableString;
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.5f);
        if (size < 1024) {
            spannableString = new SpannableString(String.valueOf(size) + "B");
            spannableString.setSpan(sizeSpan, spannableString.length() - 1, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            return spannableString;
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            spannableString = new SpannableString(String.valueOf(size) + "KB");
            spannableString.setSpan(sizeSpan, spannableString.length() - 2, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            return spannableString;
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            size = size * 100;
            String string = String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
            spannableString = new SpannableString(string);
            spannableString.setSpan(sizeSpan, spannableString.length() - 2, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            return spannableString;
        } else {
            size = size * 100 / 1024;
            String string = String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
            spannableString = new SpannableString(string);
            spannableString.setSpan(sizeSpan, spannableString.length() - 2, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            return spannableString;
        }
    }

    /**
     * 删除文件
     * @param file                      文件
     */
    public static boolean deleteDirectory(File file) {
        if (file!=null){
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                int length = listFiles.length;
                for(int i = 0; i < length; ++i) {
                    File f = listFiles[i];
                    deleteDirectory(f);
                }
            }
            file.delete();
        }
        // 如果删除的文件路径所对应的文件存在，并且是一个文件，则表示删除失败
        if (file!=null && file.exists() && file.isFile()) {
            return false;
        } else {
            //删除成功
            return true;
        }
    }

    /**
     * 获取文件大小
     * @param directory                 文件
     * @return
     */
    public static long getDirectorySize(File directory) {
        long size = 0L;
        File[] listFiles = directory.listFiles();
        if (listFiles != null) {
            File[] files = listFiles;
            int length = listFiles.length;
            for (int i = 0; i < length; ++i) {
                File file = files[i];
                if (file.isDirectory()) {
                    size += getDirectorySize(file);
                } else {
                    size += file.length();
                }
            }
        } else {
            //如果不是文件目录，则获取单个文件大小
            size = directory.length();
        }
        return size;
    }

    /**
     * 获取分享路径地址
     * @return                              路径
     */
    public static String getFileSharePath() {
        String path = Environment.getExternalStorageDirectory() + "";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }


    /**
     * 根据文件路径拷贝文件
     *
     * @param src                                   源文件
     * @param dest                                  目标文件
     * @return                                      boolean 成功true、失败false
     */
    public static boolean copyFile(File src, File dest) {
        boolean result = false;
        if ((src == null) || (dest == null)) {
            return false;
        }
        if (dest.exists()) {
            // delete file
            dest.delete();
        }
        if (!createOrExistsDir(dest.getParentFile())) {
            return false;
        }
        try {
            dest.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileChannel srcChannel = null;
        FileChannel dstChannel = null;

        try {
            srcChannel = new FileInputStream(src).getChannel();
            dstChannel = new FileOutputStream(dest).getChannel();
            srcChannel.transferTo(0, srcChannel.size(), dstChannel);
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
        try {
            srcChannel.close();
            dstChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 判断文件是否创建，如果没有创建，则新建
     * @param file                                  file文件
     * @return
     */
    public static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

}
