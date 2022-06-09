package com.yc.videosqllite.disk;


interface InterDiskFactory {

    /** 250 MB of cache. */
    int DEFAULT_DISK_CACHE_SIZE = 250 * 1024 * 1024;
    String DEFAULT_DISK_CACHE_DIR = "disk";

    InterDiskCache build();
}
