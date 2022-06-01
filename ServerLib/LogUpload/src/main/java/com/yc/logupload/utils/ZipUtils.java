package com.yc.logupload.utils;


import android.text.TextUtils;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * @author yangchong
 * email  : yangchong211@163.com
 * time   : 2022/5/12
 * desc   : 将多个文件压缩成zip包工具类
 *          实践文档：https://wiki.zuoyebang.cc/pages/viewpage.action?pageId=356760839
 * revise :
 */
public final class ZipUtils {

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

    public static boolean writeToZip(List<EntrySet> entrySets, File zipfile) {
        if (entrySets == null || entrySets.isEmpty()) {
            return false;
        }
        if (zipfile.exists()) {
            zipfile.delete();
        }
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
            return false;
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
                // nothing
            }
        }
        return true;
    }

    public static void writeToZip(File baseDir, List<File> writeFiles, File zipfile) {
        if (zipfile.exists()) {
            zipfile.delete();
        }
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
