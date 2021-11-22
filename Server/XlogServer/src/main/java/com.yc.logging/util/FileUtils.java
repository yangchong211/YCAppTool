package com.yc.logging.util;

import android.annotation.TargetApi;
import android.os.Build;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class FileUtils {
    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException ex) {
            // Ignore the exception on close.
        }
    }

    public static void cleanDir(File file) {
        if (file == null || !file.exists() || !file.isDirectory()) {
            return;
        }
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        for (File f : files) {
            deleteFile(f);
        }
    }

    public static void deleteFile(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteFile(f);
                }
            }
        }
        file.delete();
    }

    /**
     * Get the size of an file
     *
     * @param file the file to check
     * @return the size of the file in byte
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static long sizeOf(final File file) {
        if (!file.exists()) {
            final String message = file + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!file.isDirectory()) {
            return file.length();
        }

        long len = 0;
        LinkedList<File> dirStack = new LinkedList<>();
        dirStack.push(file);

        while (!dirStack.isEmpty()) {
            File dir = dirStack.pop();
            File[] files = dir.listFiles();
            if (files == null) {
                continue;
            }
            for (File f : files) {
                if (!f.isDirectory()) {
                    len += f.length();
                } else {
                    dirStack.push(f);
                }
            }
        }
        return len;
    }

    public static List<File> collectAllFiles(final File file) {
        if (!file.exists()) {
            final String message = file + " does not exist";
            throw new IllegalArgumentException(message);
        }
        List<File> list = new ArrayList<>();
        if (!file.isDirectory()) {
            return list;
        }

        LinkedList<File> dirStack = new LinkedList<>();
        dirStack.push(file);

        while (!dirStack.isEmpty()) {
            File dir = dirStack.pop();
            File[] files = dir.listFiles();
            if (files == null) {
                continue;
            }
            for (File f : files) {
                if (!f.isDirectory()) {
                    list.add(f);
                } else {
                    dirStack.push(f);
                }
            }
        }
        return list;
    }


    public static File buildPath(File base, String... segments) {
        File cur = base;
        for (String segment : segments) {
            if (cur == null) {
                cur = new File(segment);
            } else {
                cur = new File(cur, segment);
            }
        }
        return cur;
    }

}
