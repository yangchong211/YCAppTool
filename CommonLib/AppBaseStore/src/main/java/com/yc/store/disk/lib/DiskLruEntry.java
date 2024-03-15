package com.yc.store.disk.lib;

import java.io.File;
import java.io.IOException;

public final class DiskLruEntry {

    protected final String key;
    private final int valueCount;
    /**
     * 条目的文件长度
     */
    protected final long[] lengths;
    protected File[] cleanFiles;
    protected File[] dirtyFiles;

    /**
     * 如果这个条目曾经被发表过，则为真。
     */
    protected boolean readable;

    /**
     * 正在进行的编辑，如果该条目没有被编辑，则为空。
     */
    protected DiskLruCache.Editor currentEditor;

    /**
     * 最近提交到该条目的编辑的序列号
     */
    protected long sequenceNumber;

    public DiskLruEntry(String key , int valueCount , File directory) {
        this.key = key;
        this.valueCount = valueCount;
        this.lengths = new long[valueCount];
        cleanFiles = new File[valueCount];
        dirtyFiles = new File[valueCount];

        // The names are repetitive so re-use the same builder to avoid allocations.
        StringBuilder fileBuilder = new StringBuilder(key).append('.');
        int truncateTo = fileBuilder.length();
        for (int i = 0; i < valueCount; i++) {
            fileBuilder.append(i);
            cleanFiles[i] = new File(directory, fileBuilder.toString());
            fileBuilder.append(".tmp");
            dirtyFiles[i] = new File(directory, fileBuilder.toString());
            fileBuilder.setLength(truncateTo);
        }
    }

    public String getLengths() throws IOException {
        StringBuilder result = new StringBuilder();
        for (long size : lengths) {
            result.append(' ').append(size);
        }
        return result.toString();
    }

    /**
     * Set lengths using decimal numbers like "10123".
     */
    protected void setLengths(String[] strings) throws IOException {
        if (strings.length != valueCount) {
            throw invalidLengths(strings);
        }

        try {
            for (int i = 0; i < strings.length; i++) {
                lengths[i] = Long.parseLong(strings[i]);
            }
        } catch (NumberFormatException e) {
            throw invalidLengths(strings);
        }
    }

    private IOException invalidLengths(String[] strings) throws IOException {
        throw new IOException("unexpected journal line: " + java.util.Arrays.toString(strings));
    }

    public File getCleanFile(int i) {
        return cleanFiles[i];
    }

    public File getDirtyFile(int i) {
        return dirtyFiles[i];
    }

    public File[] getCleanFiles(){
        return cleanFiles;
    }

    public File[] getDirtyFiles(){
        return dirtyFiles;
    }

}
