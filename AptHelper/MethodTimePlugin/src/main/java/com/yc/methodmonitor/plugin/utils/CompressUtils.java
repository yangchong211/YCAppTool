package com.yc.methodmonitor.plugin.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class CompressUtils {

    private final static int MAX_BUFFER_SIZE = 2048;

    public static void deCompress(String path, String dest) throws IOException {
        JarInputStream jarIn = new JarInputStream(new BufferedInputStream(new FileInputStream(path)));
        byte[] bytes = new byte[MAX_BUFFER_SIZE];

        while (true) {
            JarEntry entry = jarIn.getNextJarEntry();
            if (entry == null) break;

            File desTemp = new File(dest + File.separator + entry.getName());
            if (!desTemp.getParentFile().exists()) desTemp.getParentFile().mkdirs();

            if (entry.isDirectory()) {    //jar条目是空目录
                if (!desTemp.exists()) desTemp.mkdirs();
            } else {    //jar条目是文件
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(desTemp));
                int len = jarIn.read(bytes, 0, bytes.length);
                while (len != -1) {
                    out.write(bytes, 0, len);
                    len = jarIn.read(bytes, 0, bytes.length);
                }

                out.flush();
                out.close();
            }
            jarIn.closeEntry();
        }

        //解压Manifest文件
        Manifest manifest = jarIn.getManifest();
        if (manifest != null) {
            File manifestFile = new File(dest + File.separator + JarFile.MANIFEST_NAME);
            if (!manifestFile.getParentFile().exists()) manifestFile.getParentFile().mkdirs();
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(manifestFile));
            manifest.write(out);
            out.close();
        }

        //关闭JarInputStream
        jarIn.close();
    }

    /**
     * 重新打包jar
     *
     * @param srcPath
     * @param destPath
     */
    public static void compress(String srcPath, String destPath) throws IOException {
        File file = new File(srcPath);
        File destFile = new File(destPath);
        if (!destFile.exists()) {
            destFile.getParentFile().mkdirs();
        }
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(destFile));
        compressJarFolder(jarOutputStream, file, "");
        jarOutputStream.close();
    }

    /**
     * 递归打包jar
     *
     * @param outputStream
     * @param file
     * @param basePath
     * @throws IOException
     */
    private static void compressJarFolder(JarOutputStream outputStream, File file, String
            basePath) throws IOException {
        if (file.isFile()) {
            compressJarFile(outputStream, file, basePath);
        } else if (file.isDirectory()) {
            compressDirEntry(outputStream, file, basePath);

            String[] fileNameList = file.list();
            for (String fileName : fileNameList) {
                String newSource = file.getAbsolutePath() + File.separator + fileName;
                File newFile = new File(newSource);
                String newBasePath;
                if (basePath.equals("")) {
                    newBasePath = newFile.getName();
                } else {
                    newBasePath = basePath + "/" + newFile.getName();
                }
                compressJarFolder(outputStream, newFile, newBasePath);
            }
        }
    }


    /**
     * 压缩单个文件
     *
     * @param outputStream
     * @param file
     * @param basePath
     * @throws IOException
     */
    private static void compressJarFile(JarOutputStream outputStream, File file, String basePath)
            throws IOException {
        outputStream.putNextEntry(new JarEntry(basePath));
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));

        byte[] buffer = new byte[MAX_BUFFER_SIZE];
        int length = -1;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.closeEntry();
    }

    /**
     * 创建文件目录
     *
     * @param outputStream
     * @param file
     * @param basePath
     * @throws IOException
     */
    private static void compressDirEntry(JarOutputStream outputStream, File file, String basePath)
            throws IOException {
        if (basePath != null && !basePath.equals("")) {
            outputStream.putNextEntry(new JarEntry(basePath + "/"));
            outputStream.closeEntry();
        }
    }

}
