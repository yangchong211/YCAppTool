package com.yc.tracesdk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class LogHelper {
    private static final String TAG = "tracesdk_log";
    private static final String TIME_FORMAT = "MM-dd HH:mm:ss.SSS";

    public static void log(String text) {
        if (!Config.DEBUG) {
            return;
        }
        android.util.Log.d(TAG, text);
//        writeToFile(text);
    }

    public static boolean writeToFile(String log) {
        if (!Config.DEBUG) {
            return false;
        }
        String logFilePath = "/sdcard/tracesdk.txt";
        File logFile = new File(logFilePath);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
        }

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(new File(logFilePath), true);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "unicode");
            osw.write(log);
            osw.write("\n");
            osw.flush();
            osw.close();
            return true;
        } catch (FileNotFoundException e1) {
            //e1.printStackTrace();
        } catch (UnsupportedEncodingException e2) {
            //e2.printStackTrace();
        } catch (Exception e2) {
            //e2.printStackTrace();
        }
        return false;
    }

    public static String formatTs2Str(long timestamp) {
    	SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT, Locale.CHINA);
    	return sdf.format(timestamp);
    }
}
