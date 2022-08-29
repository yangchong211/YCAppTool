package com.yc.appfilelib;

import android.os.Build;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : io流读写库
 *     revise: 读数据 & 写数据
 * </pre>
 */
public final class AppFileIoUtils {

    /**
     * 写入内容到文件中
     * @param content   内容
     * @param fileName 文件名称
     * @return
     */
    public static boolean writeString2File(String content,String fileName){
        BufferedWriter bw = null;
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            bw = new BufferedWriter(fileWriter) ;
            // 写数据
            bw.write(content) ;
        } catch (Exception e){
            Log.d("AppFileIoUtils", "异常: " + e.getMessage());
        } finally {
            // 释放资源
            try {
                if (bw != null) {
                    bw.close() ;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 读取file文件，转化成字符串
     *
     * @param fileName 文件名称
     * @return 文件内容
     */
    public static String readFile2String(String fileName) {
        String res = "";
        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            InputStreamReader inputStreamReader;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            } else {
                inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            }
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            res = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 读取io流到新的file文件中
     * @param newFile
     * @param is
     * @return
     */
    public static boolean writeFileFromIS(final File newFile, final InputStream is) {
        if (is == null || newFile==null) {
            Log.e("FileIOUtils", "create file <" + newFile + "> failed.");
            return false;
        }
        OutputStream os = null;
        int sBufferSize = 1024 * 100;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
            os = new BufferedOutputStream(fileOutputStream, sBufferSize);
            byte[] data = new byte[sBufferSize];
            for (int len; (len = is.read(data)) != -1; ) {
                os.write(data, 0, len);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 根据文件File拷贝文件
     *
     * @param src  源文件
     * @param dest 目标文件
     * @return boolean 成功true、失败false
     */
    public static boolean copyFile(File src, File dest) {
        if ((src == null) || (dest == null)) {
            return false;
        }
        if (dest.exists()) {
            // delete file
            dest.delete();
        }
        if (!AppFileUtils.createOrExistsDir(dest.getParentFile())) {
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (srcChannel != null) {
                    srcChannel.close();
                }
                if (dstChannel != null) {
                    dstChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 根据文件路径拷贝文件
     *
     * @param oldPath  源文件路径
     * @param newPath 目标文件路径
     * @return boolean 成功true、失败false
     */
    public static boolean copyFile(String oldPath, String newPath) {
        InputStream inStream = null;
        FileOutputStream fs = null;
        boolean result;
        try {
            int bytesum = 0;
            int byteread;
            File oldFile = new File(oldPath);
            // 判断目录是否存在
            File newFile = new File(newPath);
            File newFileDir = new File(newFile.getPath().replace(newFile.getName(), ""));
            if (!newFileDir.exists()) {
                newFileDir.mkdirs();
            }
            // 文件存在时
            if (oldFile.exists()) {
                // 读入原文件
                inStream = new FileInputStream(oldPath);
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    // 字节数 文件大小
                    bytesum += byteread;
                    fs.write(buffer, 0, byteread);
                }
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


}
