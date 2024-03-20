package com.yc.toolutils.stream;

import android.util.Log;


import com.yc.toolutils.file.AppFileUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 使用字节流io流读写库
 *     revise: 读数据 & 写数据
 * </pre>
 */
public final class FileIoUtils {

    /**
     * 使用字节流，写入字符串内容到文件中
     *
     * @param content  内容
     * @param fileName 文件名称
     * @return
     */
    public static boolean writeString2File1(String content, String fileName) {
        boolean isSuccess;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
            byte[] bytes = content.getBytes();
            fos.write(bytes);
            isSuccess = true;
        } catch (Exception e) {
            isSuccess = false;
            Log.d("AppFileIoUtils", "异常: " + e.getMessage());
        } finally {
            // 释放资源
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }


    /**
     * 使用字符流，写入字符串内容到文件中
     *
     * @param content  内容
     * @param fileName 文件名称
     * @return
     */
    public static boolean writeString2File2(String content, String fileName) {
        boolean isSuccess;
        BufferedWriter bw = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream);
            bw = new BufferedWriter(osw);
            // 写数据
            bw.write(content);
            bw.flush();
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
     * 字节流读取file文件，转化成字符串
     *
     * @param fileName 文件名称
     * @return 文件内容
     */
    public static String readFile2String1(String fileName) {
        String res = "";
        FileInputStream fis;
        try {
            fis = new FileInputStream(fileName);
            byte[] chs = new byte[1024];
            int len = 0;
            StringBuilder sb = new StringBuilder();
            while ((len = fis.read(chs)) != -1) {
                res = new String(chs, 0, len);
                sb.append(res);
            }
            res = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 字符流读取file文件，转化成字符串
     *
     * @param fileName 文件名称
     * @return 文件内容
     */
    public static String readFile2String2(String fileName) {
        String res = "";
        InputStreamReader isr;
        try {
            isr = new InputStreamReader(new FileInputStream(fileName));
            char[] chs = new char[1024];
            int len = 0;
            StringBuilder sb = new StringBuilder();
            while ((len = isr.read(chs)) != -1) {
                res = new String(chs, 0, len);
                sb.append(res);
            }
            res = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /*--------------------------------------------------------------------------------------------*/

    /**
     * 使用字符流读取流数据到新的file文件中
     *
     * @param is      io流
     * @param newFile 文件
     * @return
     */
    public static boolean writeFileFromIS1(final InputStream is, final File newFile) {
        boolean isSuccess;
        if (is == null || newFile == null) {
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
            isSuccess = true;
        } catch (IOException e) {
            e.printStackTrace();
            isSuccess = false;
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
        return isSuccess;
    }


    /**
     * 使用字节流将流数据写到文件中
     *
     * @param inStream io流
     * @param newPath  目标文件路径
     * @return boolean 成功true、失败false
     */
    public static boolean writeFileFromIS2(InputStream inStream, String newPath) {
        boolean isSuccess;
        FileOutputStream fs = null;
        try {
            // 判断目录是否存在
            File newFile = new File(newPath);
            // 创建新文件
            File newFileDir = new File(newFile.getPath().replace(newFile.getName(), ""));
            if (!newFileDir.exists()) {
                //创建一个File对象所对应的目录，成功返回true，否则false。
                //且File对象必须为路径而不是文件。只会创建最后一级目录，如果上级目录不存在就抛异常。
                newFileDir.mkdirs();
            }
            // 输出文件
            fs = new FileOutputStream(newPath);
            // 一次读取一个字节数组复制文件
            byte[] buffer = new byte[4 * 1024];
            // 作用: 记录读取到的有效的字节个数
            int len = 0;
            // 循环遍历读取数据
            while ((len = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, len);
            }
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        } finally {
            // 关闭流对象
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }

    /*--------------------------------------------------------------------------------------------*/

    /**
     * 根据文件File拷贝文件
     *
     * @param src  源文件
     * @param dest 目标文件
     * @return boolean 成功true、失败false
     */
    public static boolean copyFileChannel(File src, File dest) {
        boolean isSuccess;
        if ((src == null) || (dest == null)) {
            return false;
        }
        if (dest.exists()) {
            // delete file
            dest.delete();
        }
        //判断文件是否创建
        if (!AppFileUtils.createOrExistsDir(dest.getParentFile())) {
            return false;
        }
        try {
            //如果文件存在返回false，否则返回true并且创建文件
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
            isSuccess = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            isSuccess = false;
        } catch (IOException e) {
            e.printStackTrace();
            isSuccess = false;
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
        return isSuccess;
    }

    /**
     * 使用字节流复制文件，根据文件路径拷贝文件。
     *
     * @param oldPath 源文件路径
     * @param newPath 目标文件路径
     * @return boolean 成功true、失败false
     */
    public static boolean copyFile1(String oldPath, String newPath) {
        InputStream inStream = null;
        FileOutputStream fs = null;
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
                // 读入原文件
                inStream = new FileInputStream(oldPath);
                // 输出文件
                fs = new FileOutputStream(newPath);
                // 一次读取一个字节数组复制文件
                byte[] buffer = new byte[1024];
                // 作用: 记录读取到的有效的字节个数
                int len = 0;
                // 循环遍历读取数据
                while ((len = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, len);
                }

                //int bytesum = 0;
                //while ((byteread = inStream.read(buffer)) != -1) {
                // 字节数 文件大小
                //    bytesum += byteread;
                //    fs.write(buffer, 0, byteread);
                //}

                isSuccess = true;
            } else {
                isSuccess = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        } finally {
            // 关闭流对象
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
        return isSuccess;
    }


    /**
     * 使用字符流复制文件，根据文件路径拷贝文件。
     *
     * @param oldPath 源文件路径
     * @param newPath 目标文件路径
     * @return boolean 成功true、失败false
     */
    public static boolean copyFile2(String oldPath, String newPath) {
        InputStreamReader isr = null;
        OutputStreamWriter osw = null;
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
                isr = new InputStreamReader(new FileInputStream(oldPath));
                // 创建转换输出流对象
                osw = new OutputStreamWriter(new FileOutputStream(newPath));
                // 一次读取一个字符数组复制文件
                char[] chs = new char[1024];
                int len;
                while ((len = isr.read(chs)) != -1) {
                    osw.write(chs, 0, len);
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
