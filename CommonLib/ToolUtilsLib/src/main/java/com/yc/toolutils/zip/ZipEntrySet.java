package com.yc.toolutils.zip;

import java.io.File;
import java.util.List;

public class ZipEntrySet {

    String prefixPath;
    File baseDir;
    List<File> files;

    public ZipEntrySet(String prefix, File baseDir, List<File> files) {
        this.prefixPath = prefix;
        this.baseDir = baseDir;
        this.files = files;
    }


}
