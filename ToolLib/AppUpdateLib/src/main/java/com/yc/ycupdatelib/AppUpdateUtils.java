package com.yc.ycupdatelib;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.MessageDigest;
import java.util.ArrayList;

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

    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static String APP_UPDATE_DOWN_APK_PATH = "apk" + File.separator + "downApk";
    private static String mApkName;

    public static String getLocalApkDownSavePath(String apkName){
        mApkName = apkName;
        String saveApkPath= APP_UPDATE_DOWN_APK_PATH+ File.separator;
        String sdPath = getInnerSDCardPath();
        if (!isExistSDCard() || TextUtils.isEmpty(sdPath)) {
            ArrayList<String> sdPathList = getExtSDCardPath();
            if (sdPathList != null && sdPathList.size() > 0 && !TextUtils.isEmpty(sdPathList.get(0))) {
                sdPath = sdPathList.get(0);
            }
        }
        String saveApkDirs = sdPath+File.separator+saveApkPath;
        File file = new File(saveApkDirs);
        //判断文件夹是否存在，如果不存在就创建，否则不创建
        if (!file.exists()) {
            //通过file的mkdirs()方法创建目录中包含却不存在的文件夹
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }
        saveApkPath = saveApkDirs + apkName+".apk";
        return saveApkPath;
    }

    public static String getUserApkDownSavePath(Context context,String apkName){
        mApkName = apkName;
        String saveApkPath= APP_UPDATE_DOWN_APK_PATH+ File.separator;
        String sdPath = getRootDirPath(context);
        String saveApkDirs = sdPath+File.separator+saveApkPath;
        File file = new File(saveApkDirs);
        //判断文件夹是否存在，如果不存在就创建，否则不创建
        if (!file.exists()) {
            //通过file的mkdirs()方法创建目录中包含却不存在的文件夹
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }
        saveApkPath = saveApkDirs + apkName+".apk";
        return saveApkPath;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean clearDownload(Context context){
        if (mApkName!=null && mApkName.length()>0){
            String localApkDownSavePath = getUserApkDownSavePath(context,mApkName);
            return deleteFile(localApkDownSavePath);
        }
        return false;
    }

    /**
     * 删除单个文件
     * @param fileName          要删除的文件的文件名
     * @return                  单个文件删除成功返回true，否则返回false
     */
    private static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 判断是否有sd卡
     * @return                      是否有sd
     */
    private static boolean isExistSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    /**
     * 获取内置SD卡路径
     * @return                      路径
     */
    private static String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 获取外置SD卡路径
     * @return 应该就一条记录或空
     */
    private static ArrayList<String> getExtSDCardPath() {
        ArrayList<String> lResult = new ArrayList<>();
        try {
            Runtime rt = Runtime.getRuntime();
            Process process = rt.exec("mount");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("extSdCard")) {
                    String[] arr = line.split(" ");
                    String path = arr[1];
                    File file = new File(path);
                    if (file.isDirectory()) {
                        lResult.add(path);
                    }
                }
            }
            isr.close();
        } catch (Exception ignored) {
        }
        return lResult;
    }


    /**
     * 获取存储根目录路径
     *
     * @param context
     * @return
     */
    public static String getRootDirPath(Context context) {
        String rootPath = "";
        if (hasExternalStoragePermission(context)
                && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = context.getExternalFilesDir("");
            if (file != null && file.exists()) {
                rootPath = file.getAbsolutePath();
            } else {
                File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
                File filesDir = new File(new File(dataDir, context.getPackageName()), "files");
                if (!filesDir.exists()) {
                    if (filesDir.mkdirs()) {
                        rootPath = filesDir.getAbsolutePath();
                    }
                } else {
                    rootPath = filesDir.getAbsolutePath();
                }
            }
        }
        if (TextUtils.isEmpty(rootPath)) {
            rootPath = context.getFilesDir().getAbsolutePath();
        }
        return rootPath;
    }

    public static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
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


    /*
     注意配置清单文件
     <provider
     android:name="android.support.v4.content.FileProvider"
     android:authorities="你的包名.fileprovider"
     android:exported="false"
     android:grantUriPermissions="true">
     <meta-data
     android:name="android.support.FILE_PROVIDER_PATHS"
     android:resource="@xml/file_paths" />
     </provider>
     */
    /**
     * 关于在代码中安装 APK 文件，在 Android N 以后，为了安卓系统为了安全考虑，不能直接访问软件
     * 需要使用 fileProvider 机制来访问、打开 APK 文件。
     * 普通安装
     * @param context                   上下文
     * @param apkPath                    path，文件路径
     */
    protected static boolean installNormal(Context context, String apkPath , String application_id) {
        if(apkPath==null){
            return false;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            File apkFile = new File(apkPath);
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //版本在7.0以上是不能直接通过uri访问的
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                Uri apkUri = FileProvider.getUriForFile(context, application_id+".provider", apkFile);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                Uri uri = Uri.fromFile(apkFile);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
            return true;
        }catch (Exception e){
            e.printStackTrace();
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
