package com.yc.webviewlib.cache;

import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class WebFileUtils {


    public static void deleteDirs(String path, boolean isDeleteDir) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        File dir = new File(path);
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                deleteDirs(file.getAbsolutePath(), isDeleteDir);
            } else {
                file.delete();
            }
        }
        if (isDeleteDir) {
            dir.delete();
        }

    }


    public static void copy(InputStream inputStream, OutputStream out) throws IOException {
        byte[] buf =new byte[512];
        int len = -1;
        while ((len = inputStream.read(buf))!=-1){
            out.write(buf,0,len);
        }
        inputStream.close();
        out.close();
    }

}
