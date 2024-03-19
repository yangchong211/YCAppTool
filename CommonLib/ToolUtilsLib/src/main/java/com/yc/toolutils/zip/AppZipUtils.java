package com.yc.toolutils.zip;

import android.text.TextUtils;

import com.yc.toolutils.context.AppToolUtils;
import com.yc.toolutils.file.AppFileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * <pre>
 *     author: yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2020/07/08
 *     desc  : 压缩相关工具类
 * </pre>
 */
public final class AppZipUtils {

    private static final int BUFFER_LEN = 8192;

    private AppZipUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 批量压缩文件
     *
     * @param resFiles    待压缩文件路径集合
     * @param zipFilePath 压缩文件路径
     * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
     * @throws IOException IO错误时抛出
     */
    public static boolean zipFiles(final Collection<String> resFiles,
                                   final String zipFilePath)
            throws IOException {
        return zipFiles(resFiles, zipFilePath, null);
    }

    /**
     * 批量压缩文件
     *
     * @param resFilePaths 待压缩文件路径集合
     * @param zipFilePath  压缩文件路径
     * @param comment      压缩文件的注释
     * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
     * @throws IOException IO错误时抛出
     */
    public static boolean zipFiles(final Collection<String> resFilePaths,
                                   final String zipFilePath,
                                   final String comment)
            throws IOException {
        if (resFilePaths == null || zipFilePath == null) {
            return false;
        }
        ZipOutputStream zos = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFilePath);
            zos = new ZipOutputStream(fileOutputStream);
            //遍历待压缩文件路径集合
            for (String resFile : resFilePaths) {
                File fileByPath = getFileByPath(resFile);
                if (!zipFile(fileByPath, "", zos, comment)) {
                    return false;
                }
            }
            return true;
        } finally {
            if (zos != null) {
                zos.finish();
                AppToolUtils.closeIO(zos);
            }
        }
    }

    /**
     * 批量压缩文件
     *
     * @param resFiles 待压缩文件集合
     * @param zipFile  压缩文件
     * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
     * @throws IOException IO错误时抛出
     */
    public static boolean zipFiles(final Collection<File> resFiles, final File zipFile)
            throws IOException {
        return zipFiles(resFiles, zipFile, null);
    }

    /**
     * 批量压缩文件
     *
     * @param resFiles 待压缩文件集合
     * @param zipFile  压缩文件
     * @param comment  压缩文件的注释
     * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
     * @throws IOException IO错误时抛出
     */
    public static boolean zipFiles(final Collection<File> resFiles,
                                   final File zipFile,
                                   final String comment)
            throws IOException {
        if (resFiles == null || zipFile == null) {
            return false;
        }
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zipFile));
            for (File resFile : resFiles) {
                if (!zipFile(resFile, "", zos, comment)) {
                    return false;
                }
            }
            return true;
        } finally {
            if (zos != null) {
                zos.finish();
                AppToolUtils.closeIO(zos);
            }
        }
    }

    /**
     * 压缩文件
     *
     * @param resFilePath 待压缩文件路径
     * @param zipFilePath 压缩文件路径
     * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
     * @throws IOException IO 错误时抛出
     */
    public static boolean zipFile(final String resFilePath,
                                  final String zipFilePath)
            throws IOException {
        return zipFile(getFileByPath(resFilePath), getFileByPath(zipFilePath), null);
    }

    /**
     * 压缩文件
     *
     * @param resFilePath 待压缩文件路径
     * @param zipFilePath 压缩文件路径
     * @param comment     压缩文件的注释
     * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
     * @throws IOException IO 错误时抛出
     */
    public static boolean zipFile(final String resFilePath,
                                  final String zipFilePath,
                                  final String comment)
            throws IOException {
        return zipFile(getFileByPath(resFilePath), getFileByPath(zipFilePath), comment);
    }

    /**
     * 压缩文件
     *
     * @param resFile 待压缩文件
     * @param zipFile 压缩文件
     * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
     * @throws IOException IO 错误时抛出
     */
    public static boolean zipFile(final File resFile,
                                  final File zipFile)
            throws IOException {
        return zipFile(resFile, zipFile, null);
    }

    /**
     * 压缩文件
     *
     * @param resFile 待压缩文件
     * @param zipFile 压缩文件
     * @param comment 压缩文件的注释
     * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
     * @throws IOException IO 错误时抛出
     */
    public static boolean zipFile(final File resFile,
                                  final File zipFile,
                                  final String comment)
            throws IOException {
        if (resFile == null || zipFile == null) {
            return false;
        }
        ZipOutputStream zos = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fileOutputStream);
            return zipFile(resFile, "", zos, comment);
        } finally {
            if (zos != null) {
                AppToolUtils.closeIO(zos);
            }
        }
    }

    /**
     * 压缩文件
     *
     * @param resFile  待压缩文件
     * @param rootPath 相对于压缩文件的路径
     * @param zos      压缩文件输出流
     * @param comment  压缩文件的注释
     * @return {@code true}: 压缩成功<br>{@code false}: 压缩失败
     * @throws IOException IO 错误时抛出
     */
    private static boolean zipFile(final File resFile,
                                   String rootPath,
                                   final ZipOutputStream zos,
                                   final String comment)
            throws IOException {
        rootPath = rootPath + (AppToolUtils.isSpace(rootPath) ? ""
                : File.separator) + resFile.getName();
        if (resFile.isDirectory()) {
            File[] fileList = resFile.listFiles();
            // 如果是空文件夹那么创建它，我把'/'换为File.separator测试就不成功，eggPain
            if (fileList == null || fileList.length <= 0) {
                ZipEntry entry = new ZipEntry(rootPath + '/');
                entry.setComment(comment);
                zos.putNextEntry(entry);
                zos.closeEntry();
            } else {
                for (File file : fileList) {
                    // 如果递归返回 false 则返回 false
                    if (!zipFile(file, rootPath, zos, comment)) {
                        return false;
                    }
                }
            }
        } else {
            InputStream is = null;
            try {
                FileInputStream fileInputStream = new FileInputStream(resFile);
                is = new BufferedInputStream(fileInputStream);
                ZipEntry entry = new ZipEntry(rootPath);
                entry.setComment(comment);
                zos.putNextEntry(entry);
                byte buffer[] = new byte[BUFFER_LEN];
                int len;
                while ((len = is.read(buffer, 0, BUFFER_LEN)) != -1) {
                    zos.write(buffer, 0, len);
                }
                zos.closeEntry();
            } finally {
                AppToolUtils.closeIO(is);
            }
        }
        return true;
    }

    /**
     * 解压文件
     *
     * @param zipFilePath 待解压文件路径
     * @param destDirPath 目标目录路径
     * @return 文件链表
     * @throws IOException IO 错误时抛出
     */
    public static List<File> unzipFile(final String zipFilePath,
                                       final String destDirPath)
            throws IOException {
        return unzipFileByKeyword(zipFilePath, destDirPath, null);
    }

    /**
     * 解压文件
     *
     * @param zipFile 待解压文件
     * @param destDir 目标目录
     * @return 文件链表
     * @throws IOException IO 错误时抛出
     */
    public static List<File> unzipFile(final File zipFile,
                                       final File destDir)
            throws IOException {
        return unzipFileByKeyword(zipFile, destDir, null);
    }

    /**
     * 解压带有关键字的文件
     *
     * @param zipFilePath 待解压文件路径
     * @param destDirPath 目标目录路径
     * @param keyword     关键字
     * @return 返回带有关键字的文件链表
     * @throws IOException IO 错误时抛出
     */
    public static List<File> unzipFileByKeyword(final String zipFilePath,
                                                final String destDirPath,
                                                final String keyword)
            throws IOException {
        return unzipFileByKeyword(getFileByPath(zipFilePath), getFileByPath(destDirPath), keyword);
    }

    /**
     * 解压带有关键字的文件
     *
     * @param zipFile 待解压文件
     * @param destDir 目标目录
     * @param keyword 关键字
     * @return 返回带有关键字的文件链表
     * @throws IOException IO 错误时抛出
     */
    public static List<File> unzipFileByKeyword(final File zipFile,
                                                final File destDir,
                                                final String keyword)
            throws IOException {
        if (zipFile == null || destDir == null) {
            return null;
        }
        List<File> files = new ArrayList<>();
        ZipFile zf = new ZipFile(zipFile);
        Enumeration<?> entries = zf.entries();
        if (AppToolUtils.isSpace(keyword)) {
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (!unzipChildFile(destDir, files, zf, entry, entryName)) {
                    return files;
                }
            }
        } else {
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.contains(keyword)) {
                    if (!unzipChildFile(destDir, files, zf, entry, entryName)) {
                        return files;
                    }
                }
            }
        }
        return files;
    }

    private static boolean unzipChildFile(final File destDir,
                                          final List<File> files,
                                          final ZipFile zf,
                                          final ZipEntry entry,
                                          final String entryName) throws IOException {
        String filePath = destDir + File.separator + entryName;
        File file = new File(filePath);
        files.add(file);
        if (entry.isDirectory()) {
            if (!AppFileUtils.createOrExistsDir(file)) {
                return false;
            }
        } else {
            if (!AppFileUtils.createOrExistsFile(file)) {
                return false;
            }
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new BufferedInputStream(zf.getInputStream(entry));
                out = new BufferedOutputStream(new FileOutputStream(file));
                byte buffer[] = new byte[BUFFER_LEN];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            } finally {
                AppToolUtils.closeIO(in, out);
            }
        }
        return true;
    }

    /**
     * 获取压缩文件中的文件路径链表
     *
     * @param zipFilePath 压缩文件路径
     * @return 压缩文件中的文件路径链表
     * @throws IOException IO 错误时抛出
     */
    public static List<String> getFilesPath(final String zipFilePath)
            throws IOException {
        return getFilesPath(getFileByPath(zipFilePath));
    }

    /**
     * 获取压缩文件中的文件路径链表
     *
     * @param zipFile 压缩文件
     * @return 压缩文件中的文件路径链表
     * @throws IOException IO 错误时抛出
     */
    public static List<String> getFilesPath(final File zipFile)
            throws IOException {
        if (zipFile == null) {
            return null;
        }
        List<String> paths = new ArrayList<>();
        Enumeration<?> entries = new ZipFile(zipFile).entries();
        while (entries.hasMoreElements()) {
            paths.add(((ZipEntry) entries.nextElement()).getName());
        }
        return paths;
    }

    /**
     * 获取压缩文件中的注释链表
     *
     * @param zipFilePath 压缩文件路径
     * @return 压缩文件中的注释链表
     * @throws IOException IO 错误时抛出
     */
    public static List<String> getComments(final String zipFilePath)
            throws IOException {
        return getComments(getFileByPath(zipFilePath));
    }

    /**
     * 获取压缩文件中的注释链表
     *
     * @param zipFile 压缩文件
     * @return 压缩文件中的注释链表
     * @throws IOException IO 错误时抛出
     */
    public static List<String> getComments(final File zipFile)
            throws IOException {
        if (zipFile == null) {
            return null;
        }
        List<String> comments = new ArrayList<>();
        Enumeration<?> entries = new ZipFile(zipFile).entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            comments.add(entry.getComment());
        }
        return comments;
    }

    private static File getFileByPath(final String filePath) {
        return AppToolUtils.isSpace(filePath) ? null : new File(filePath);
    }

    public static void writeToZip(List<ZipEntrySet> entrySets, File zipfile) {
        if (entrySets == null || entrySets.isEmpty()) {
            return;
        }
        if (zipfile.exists()) {
            zipfile.delete();
        }
        ZipOutputStream zos = null;
        try {
            zipfile.createNewFile();
            zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipfile)));
            for (ZipEntrySet entrySet : entrySets) {
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


    public static boolean writeToZip2(List<ZipEntrySet> entrySets, File zipfile) {
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
            for (ZipEntrySet entrySet : entrySets) {
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

    public static void writeToZip2(File baseDir, List<File> writeFiles, File zipfile) {
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
