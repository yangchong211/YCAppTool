package com.yc.toolutils.stream;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 高效字节流 & 字符流
 *     revise: 读数据 & 写数据
 * </pre>
 */
public final class BufferIoUtils {


    /**
     * 高效字节流写入字符串内容到文件中
     *
     * @param content  内容
     * @param fileName 文件名称
     * @return
     */
    public static boolean writeString2File1(String content, String fileName) {
        BufferedOutputStream bof = null;
        boolean isSuccess;
        try {
            bof = new BufferedOutputStream(new FileOutputStream(fileName)) ;
            // 写数据
            bof.write(content.getBytes());
            bof.flush();
            isSuccess = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            isSuccess = false;
        } catch (IOException e) {
            e.printStackTrace();
            isSuccess = false;
        } finally {
            // 释放资源
            try {
                if (bof != null) {
                    bof.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    /**
     * 高效字符流写入字符串内容到文件中
     *
     * @param content  内容
     * @param fileName 文件名称
     * @return
     */
    public static boolean writeString2File2(String content, String fileName) {
        BufferedWriter bw = null;
        boolean isSuccess;
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            bw = new BufferedWriter(fileWriter);
            // 写数据
            bw.write(content);
            isSuccess = true;
        } catch (Exception e) {
            Log.d("AppFileIoUtils", "异常: " + e.getMessage());
            isSuccess = false;
        } finally {
            // 释放资源
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    /*--------------------------------------------------------------------------------------------*/

    /**
     * 高效字节流读取file文件，转化成字符串
     *
     * @param fileName 文件名称
     * @return 文件内容
     */
    public static String readFile2String1(String fileName) {
        String res = "";
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(fileName)) ;
            // byte 表示一个字节。占4位
            // 一次读取一个字节数组
            byte[] bytes = new byte[1024] ;
            int len = 0 ;
            StringBuilder sb = new StringBuilder();
            while((len = bis.read(bytes)) != -1){
                String string = new String(bytes, 0, len);
                sb.append(string);
            }
            res = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    /**
     * 高效字符流读取file文件，转化成字符串
     *
     * @param fileName 文件名称
     * @return 文件内容
     */
    public static String readFile2String2(String fileName) {
        String res = "";
        BufferedReader br = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            // 创建高效的字符输入流对象
            br = new BufferedReader(inputStreamReader);

            //char[] chs = new char[1024];
            //int len = 0;
            //while ((len = br.read(chs)) != -1) {
            //    res = new String(chs, 0, len);
            //    sb.append(res);
            //}

            // 作用: 用来记录读取到的行数据
            StringBuilder sb = new StringBuilder();
            String line;
            // 一次读取一行
            while((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            res = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    /*--------------------------------------------------------------------------------------------*/

    /**
     * 使用高效流复制，根据文件路径拷贝文件。
     *
     * @param oldPath 源文件路径
     * @param newPath 目标文件路径
     * @return boolean 成功true、失败false
     */
    public static boolean copyFile1(String oldPath, String newPath) {
        BufferedReader br = null;
        BufferedWriter bw = null;
        boolean isSuccess;
        try {
            File oldFile = new File(oldPath);
            // 判断目录是否存在
            File newFile = new File(newPath);
            // 创建新文件
            File newFileDir = new File(newFile.getPath().replace(newFile.getName(), ""));
            if (!newFileDir.exists()) {
                //创建一个File对象所对应的目录，成功返回true，否则false。
                //且File对象必须为路径而不是文件。只会创建最后一级目录，如果上级目录不存在就抛异常。
                newFileDir.mkdirs();
            }
            // 文件存在时
            if (oldFile.exists()) {
                // 创建高效的字符输入流对象
                br = new BufferedReader(new FileReader(oldPath));
                // 创建高效的字符输出流对象
                bw = new BufferedWriter(new FileWriter(newFile));


                // 一次读取一个字符数组复制文件
                //char[] chs = new char[1024];
                //int len = 0;
                //while ((len = br.read(chs)) != -1) {
                //    bw.write(chs, 0, len);
                //}

                // 一次读取一行复制文件
                String line = null ;
                while((line = br.readLine()) != null) {
                    bw.write(line) ;
                    bw.newLine() ;
                    bw.flush() ;
                }

                isSuccess = true;
            } else {
                isSuccess = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        } finally {
            // 关闭流对象
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }

    /**
     * 根据文件路径拷贝文件。
     *
     * @param oldPath 源文件路径
     * @param newPath 目标文件路径
     * @return boolean 成功true、失败false
     */
    public static boolean copyFile2(String oldPath, String newPath) {
        BufferedInputStream isr = null;
        BufferedOutputStream osw = null;
        boolean isSuccess;
        try {
            File oldFile = new File(oldPath);
            // 判断目录是否存在
            File newFile = new File(newPath);
            // 创建新文件
            File newFileDir = new File(newFile.getPath().replace(newFile.getName(), ""));
            if (!newFileDir.exists()) {
                //创建一个File对象所对应的目录，成功返回true，否则false。
                //且File对象必须为路径而不是文件。只会创建最后一级目录，如果上级目录不存在就抛异常。
                newFileDir.mkdirs();
            }
            // 文件存在时
            if (oldFile.exists()) {
                // 创建转换输入流对象
                isr = new BufferedInputStream(new FileInputStream(oldPath)) ;
                // 创建转换输出流对象
                osw = new BufferedOutputStream(new FileOutputStream(newPath)) ;
                // 一次读取一个字节数组
                byte[] bytes = new byte[1024] ;
                int len;
                while((len = isr.read(bytes)) != -1){
                    osw.write(bytes, 0, len) ;
                }
                isSuccess = true;
            } else {
                isSuccess = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        } finally {
            // 关闭流对象
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }


}
