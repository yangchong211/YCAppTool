package com.ns.yc.lifehelper.utils.localFile;

import android.os.Environment;

import com.ns.yc.lifehelper.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileToolUtils {

    /**文档类型*/
    public static final int TYPE_DOC = 0;
    /**apk类型*/
    public static final int TYPE_APK = 1;
    /**压缩包类型*/
    public static final int TYPE_ZIP = 2;


    /**
     * 判断文件是否存在
     * @param path 文件的路径
     * @return
     */
    public static boolean isExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static int getFileType(String path) {
        path = path.toLowerCase();
        if (path.endsWith(".doc") || path.endsWith(".docx") || path.endsWith(".xls") || path.endsWith(".xlsx")
                || path.endsWith(".ppt") || path.endsWith(".pptx")) {
            return TYPE_DOC;
        }else if (path.endsWith(".apk")) {
            return TYPE_APK;
        }else if (path.endsWith(".zip") || path.endsWith(".rar") || path.endsWith(".tar") || path.endsWith(".gz")) {
            return TYPE_ZIP;
        }else{
            return -1;
        }
    }


    /**通过文件名获取文件图标*/
    public static int getFileIconByPath(String path){
        path = path.toLowerCase();
        int iconId = R.drawable.ic_icon_star;
        if (path.endsWith(".txt")){
            iconId = R.drawable.ic_icon_star;
        }else if(path.endsWith(".doc") || path.endsWith(".docx")){
            iconId = R.drawable.ic_icon_star;
        }else if(path.endsWith(".xls") || path.endsWith(".xlsx")){
            iconId = R.drawable.ic_icon_star;
        }else if(path.endsWith(".ppt") || path.endsWith(".pptx")){
            iconId = R.drawable.ic_icon_star;
        }else if(path.endsWith(".xml")){
            iconId = R.drawable.ic_icon_star;
        }else if(path.endsWith(".htm") || path.endsWith(".html")){
            iconId = R.drawable.ic_icon_star;
        }
        return iconId;
    }

    /**是否是图片文件*/
    public static boolean isPicFile(String path){
        path = path.toLowerCase();
        if (path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png")){
            return true;
        }
        return false;
    }


    /** 判断SD卡是否挂载 */
    public static boolean isSDCardAvailable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 从文件的全名得到文件的拓展名
     *
     * @param filename
     * @return
     */
    public static String getExtFromFilename(String filename) {
        int dotPosition = filename.lastIndexOf('.');
        if (dotPosition != -1) {
            return filename.substring(dotPosition + 1, filename.length());
        }
        return "";
    }
    /**
     * 读取文件的修改时间
     *
     * @param f
     * @return
     */
    public static String getModifiedTime(File f) {
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cal.setTimeInMillis(time);
        // System.out.println("修改时间[2] " + formatter.format(cal.getTime()));
        // 输出：修改时间[2] 2009-08-17 10:32:38
        return formatter.format(cal.getTime());
    }


}
