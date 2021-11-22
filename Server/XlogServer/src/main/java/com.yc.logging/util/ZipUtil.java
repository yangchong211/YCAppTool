package com.yc.logging.util;


import android.support.annotation.RestrictTo;
import android.text.TextUtils;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@RestrictTo(RestrictTo.Scope.LIBRARY)
public class ZipUtil {
    public static class EntrySet {
        String prefixPath;
        File baseDir;
        List<File> files;

        public EntrySet(String prefix, File baseDir, List<File> files) {
            this.prefixPath = prefix;
            this.baseDir = baseDir;
            this.files = files;
        }
    }

    public static void writeToZip(List<EntrySet> entrySets, File zipfile) {
        if (entrySets == null || entrySets.isEmpty()) return;
        if (zipfile.exists()) zipfile.delete();
        ZipOutputStream zos = null;
        try {
            zipfile.createNewFile();
            zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipfile)));
            for (EntrySet entrySet : entrySets) {
                List<File> writeFiles = entrySet.files;
                File baseDir = entrySet.baseDir;
                String prefixPath = entrySet.prefixPath;
                if (writeFiles != null && writeFiles.size() > 0) {
                    byte[] buffer = new byte[1024];
                    for (int i = 0; i < writeFiles.size(); i++) {
                        InputStream in = null;
                        File file;
                        try {
                            file = writeFiles.get(i);
                            String relativePath = baseDir == null ?
                                    file.getName() :
                                    file.getAbsolutePath().replace(baseDir.getAbsolutePath() + "/", "");

                            String entryName =
                                    TextUtils.isEmpty(prefixPath) ? relativePath : prefixPath + "/" + relativePath;
                            zos.putNextEntry(new ZipEntry(entryName));
                            in = new FileInputStream(file);
                            int readNum;
                            while ((readNum = in.read(buffer)) != -1) {
                                zos.write(buffer, 0, readNum);
                            }
                            zos.closeEntry();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (in != null) {
                                    in.close();
                                }
                            } catch (Exception e) {
                                // nothing
                            }
                        }
                    }
                }
            }
        } catch (Throwable e) {
            // do nothing
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
                // nothing
            }
        }
    }

    public static void writeToZip(File baseDir, List<File> writeFiles, File zipfile) {
        if (zipfile.exists()) zipfile.delete();
        ZipOutputStream zos = null;
        try {
            zipfile.createNewFile();
            zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipfile)));

            if (writeFiles != null && writeFiles.size() > 0) {
                byte[] buffer = new byte[1024];
                for (int i = 0; i < writeFiles.size(); i++) {
                    InputStream in = null;
                    File file;
                    try {
                        file = writeFiles.get(i);
                        String relativePath = file.getAbsolutePath().replace(baseDir.getAbsolutePath(), "");
                        zos.putNextEntry(new ZipEntry(relativePath));
                        in = new FileInputStream(file);
                        int readNum;
                        while ((readNum = in.read(buffer)) != -1) {
                            zos.write(buffer, 0, readNum);
                        }
                        zos.closeEntry();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (in != null) {
                                in.close();
                            }
                        } catch (Exception e) {
                            // nothing
                        }
                    }
                }
            }
        } catch (Throwable e) {
            // do nothing
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
                // nothing
            }
        }
    }

}
