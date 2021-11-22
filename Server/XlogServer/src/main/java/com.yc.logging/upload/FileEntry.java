package com.yc.logging.upload;

import android.support.annotation.RestrictTo;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件或目录封装
 * Created by zhengtao on 17/3/9.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
@KeepClass
public class FileEntry {

    @SerializedName("name")
    private String name;
    @SerializedName("size")
    private String size;
    @SerializedName("is_dir")
    private boolean isDir;
    @SerializedName("modified")
    private String lastModified;
    @SerializedName("subpaths")
    private List<FileEntry> subPaths;


    public FileEntry(File file) {
        name = file.getName();
        size = String.valueOf(file.length());
        lastModified = String.valueOf(file.lastModified());
        isDir = file.isDirectory();
        if (isDir) {
            subPaths = new ArrayList<>();
            File[] subFiles = file.listFiles();
            for (File subFile : subFiles) {
                subPaths.add(new FileEntry(subFile));
            }
        }
    }
}
