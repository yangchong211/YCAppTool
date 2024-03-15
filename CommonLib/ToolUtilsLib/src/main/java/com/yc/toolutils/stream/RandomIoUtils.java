package com.yc.toolutils.stream;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : RandomAccessFile读写
 *     revise: 读数据 & 写数据
 * </pre>
 */
public final class RandomIoUtils {


    public static void writeFileR(String content, String path, String fileName, boolean isRewrite) {
        File file = new File(path + fileName);
        if (!file.exists()) {
            //先创建文件夹 保证文件创建成功
            File pathFile = new File(path);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            File newFile = new File(path + File.separator + fileName);
            if (!newFile.exists()) {
                try {
                    newFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        RandomAccessFile randomAccessFile;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            if (isRewrite) {
                randomAccessFile.setLength(content.length());
                randomAccessFile.seek(0);
            } else {
                randomAccessFile.seek(randomAccessFile.length());
            }
            randomAccessFile.write(content.getBytes());
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String readFileR(String path, String fileName) {
        File file = new File(path + fileName);
        if (!file.exists()) {
            return null;
        }
        StringBuilder buffer = new StringBuilder();
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(0);
            byte[] buf = new byte[(int) randomAccessFile.length()];
            if (randomAccessFile.read(buf) != -1) {
                buffer.append(new String(buf));
            }
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }


}
