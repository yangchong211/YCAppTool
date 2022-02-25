
package com.yc.catonhelperlib.canary;

import android.util.Log;

import com.yc.catonhelperlib.canary.monitor.BlockCanaryInternals;
import com.yc.catonhelperlib.canary.monitor.HandlerThreadFactory;
import com.yc.catonhelperlib.canary.monitor.LogWriter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


final class Uploader {

    private static final String TAG = "Uploader";
    private static final SimpleDateFormat FORMAT =
            new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);

    private Uploader() {
        throw new InstantiationError("Must not instantiate this class");
    }

    private static File zip() {
        String timeString = Long.toString(System.currentTimeMillis());
        try {
            timeString = FORMAT.format(new Date());
        } catch (Throwable e) {
            Log.e(TAG, "zip: ", e);
        }
        File zippedFile = LogWriter.generateTempZip("BlockCanary-" + timeString);
        BlockCanaryInternals.getContext().zip(BlockCanaryInternals.getLogFiles(), zippedFile);
        LogWriter.deleteAll();
        return zippedFile;
    }

    public static void zipAndUpload() {
        HandlerThreadFactory.getWriteLogThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                final File file = zip();
                if (file.exists()) {
                    BlockCanaryInternals.getContext().upload(file);
                }
            }
        });
    }
}
