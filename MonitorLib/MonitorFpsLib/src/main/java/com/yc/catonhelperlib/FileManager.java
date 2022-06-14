package com.yc.catonhelperlib;

import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;

public class FileManager {

    private static final String TAG = "FileManager";

    public FileManager() {
    }

    public static void writeTxtToFile(String strcontent, String filePath, String fileName) {
        makeFilePath(filePath, fileName);
        String strFilePath = filePath + fileName;
        String strContent = strcontent + "\r\n";

        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d(TAG, "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception var7) {
            Log.e(TAG, "Error on write File:" + var7);
        }

    }

    private static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);

        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return file;
    }

    private static void makeRootDirectory(String filePath) {
        File file = null;

        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception var3) {
            Log.i(TAG, var3 + "");
        }

    }
}
