package me.pqpo.librarylog4a.test;

import android.content.Context;

import java.io.File;

/**
 * Created by pqpo on 2017/11/21.
 */

public class FileUtils {

    public static File getLogDir(Context context) {
        File log = context.getExternalFilesDir("logs");
        if (log == null) {
            log = new File(context.getFilesDir(), "logs");
        }
        if (!log.exists()) {
            log.mkdir();
        }
        return log;
    }

}
