package com.yc.ycupdatelib;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.yc.eventuploadlib.LoggerReporter;
import com.yc.toolutils.file.AppFileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.MessageDigest;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/05/29
 *     desc  : 版本更新弹窗
 *     revise:
 * </pre>
 */
public final class AppUpdateUtils {

    /**
     * 清除文件
     *
     * @param context 上下文
     * @param apkName apk名称
     * @return true表示清除文件成功
     */
    public static boolean clearDownload(Context context, String apkName) {
        File localApkDownSaveFile = getLocalApkDownSaveFile(context, apkName);
        return AppFileUtils.deleteDirectory(localApkDownSaveFile);
    }

    /**
     * 根据apk名称生成一个文件路径
     *
     * @param context 上下文
     * @param apkName apk文件名称
     * @return file文件路径
     */
    public static String getLocalApkDownSavePath(Context context, String apkName) {
        File localApkDownSaveFile = getLocalApkDownSaveFile(context, apkName);
        return localApkDownSaveFile.getAbsolutePath();
    }

    /**
     * 根据apk名称生成一个文件
     *
     * @param context 上下文
     * @param apkName apk文件名称
     * @return file文件
     */
    public static File getLocalApkDownSaveFile(Context context, String apkName) {
        String saveApkDirs = AppFileUtils.getExternalFilePath(context, "downloadApk");
        File file = new File(saveApkDirs);
        //判断文件夹是否存在，如果不存在就创建，否则不创建
        if (!file.exists()) {
            //通过file的mkdirs()方法创建目录中包含却不存在的文件夹
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }

        String apkPath = file.getAbsolutePath() + File.separator + apkName;
        File apkFile = new File(apkPath);
        //判断文件夹是否存在，如果不存在就创建，否则不创建
        if (!file.exists()) {
            //通过file的mkdirs()方法创建目录中包含却不存在的文件夹
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }
        return apkFile;
    }

    /**
     * @param file:要加密的文件
     * @return MD5摘要码
     * @funcion 对文件全文生成MD5摘要
     */
    public static String getMD5(File file) {
        FileInputStream fis = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[2048];
            int length = -1;
            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }
            byte[] b = md.digest();
            return byteToHexString(b);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * @param tmp 要转换的byte[]
     * @return 十六进制字符串表示形式
     * @function 把byte[]数组转换成十六进制字符串表示形式
     */

    private static String byteToHexString(byte[] tmp) {
        char[] hexdigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] str = new char[16 * 2];
        String s;
        // 用字节表示就是 16 个字节
        // 每个字节用 16 进制表示的话，使用两个字符，所以表示成 16 进制需要 32 个字符
        // 比如一个字节为01011011，用十六进制字符来表示就是“5b”
        int k = 0; // 表示转换结果中对应的字符位置
        for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节转换成 16 进制字符的转换
            byte byte0 = tmp[i]; // 取第 i 个字节
            str[k++] = hexdigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
            str[k++] = hexdigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
        }
        s = new String(str); // 换后的结果转换为字符串
        return s;
    }



    /**
     * 关于在代码中安装 APK 文件，在 Android N 以后，为了安卓系统为了安全考虑，不能直接访问软件
     * 需要使用 fileProvider 机制来访问、打开 APK 文件。
     * 普通安装
     *
     * @param context 上下文
     * @param apkPath path，文件路径
     */
    protected static boolean installNormal(Context context, String apkPath, String applicationId) {
        if (apkPath == null) {
            return false;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            boolean canRequestPackageInstalls = context.getPackageManager().canRequestPackageInstalls();
            LoggerReporter.report(UpdateFragment.TAG,
                    "install apk canRequestPackageInstalls "+canRequestPackageInstalls);
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            File apkFile = new File(apkPath);
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //版本在7.0以上是不能直接通过uri访问的
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                Uri apkUri = FileProvider.getUriForFile(context, applicationId + ".provider", apkFile);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                Uri uri = Uri.fromFile(apkFile);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
            LoggerReporter.report(UpdateFragment.TAG, "install apk success ");
            return true;
        } catch (Exception e) {
            LoggerReporter.report(UpdateFragment.TAG, "install apk exception " + e.getMessage());
            return false;
        }
    }

    /**
     * 下载状态
     * START            开始下载
     * UPLOADING.       下载中
     * FINISH           下载完成，可以安装
     * ERROR            下载错误
     * PAUSED           下载暂停中，继续
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface DownloadStatus {
        int START = 6;
        int UPLOADING = 7;
        int FINISH = 8;
        int ERROR = 9;
        int PAUSED = 10;
    }


}
