package com.yc.monitorapplib.log;


import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import com.yc.monitorapplib.AppConst;

public class FileLogManager {

    private static final String ALARM_LOG = "alarm.log";
    private static final String LOG_PATH = Environment.getExternalStorageDirectory() + File.separator + AppConst.LOG_DIR;
    private static final String LOG_FILE = LOG_PATH + File.separator + ALARM_LOG;
    private static FileLogManager mInstance = new FileLogManager();

    private FileLogManager() {
    }

    public static void init() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                mInstance.doPrepare();
            }
        }).run();
    }

    public static FileLogManager getInstance() {
        return mInstance;
    }

    private void doPrepare() {
        File d = new File(LOG_PATH);
        boolean m = true;
        if (!d.exists()) {
            m = d.mkdirs();
        }
        if (m) {
            File f = new File(LOG_FILE);
            if (!f.exists()) {
                try {
                    boolean c = f.createNewFile();
                    if (!c) {
                        System.err.println("create file error");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void log(String message) {

        doPrepare();

        if (!message.endsWith("\n")) {
            message += "\n";
        }

        FileOutputStream outputStream = null;
        PrintWriter writer = null;
        try {
            outputStream = new FileOutputStream(LOG_FILE, true);
            writer = new PrintWriter(outputStream);
            writer.write(message);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
