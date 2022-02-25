package com.yc.appstatuslib.log;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

abstract class BaseFormatStrategy implements FormatStrategy {
    private static final String NEW_LINE = System.getProperty("line.separator");
    SimpleDateFormat sDateFormat;
    static final String SEPARATOR = ",";
    File mFile;

    BaseFormatStrategy(File file) {
        this.sDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.US);
        this.mFile = file;
    }

    void write(File file, String log) {
        FileWriter writer = null;

        try {
            writer = new FileWriter(file, true);
            writer.write(log);
            writer.write(NEW_LINE);
        } catch (IOException var13) {
            var13.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException var12) {
                    var12.printStackTrace();
                }
            }

        }

    }
}

