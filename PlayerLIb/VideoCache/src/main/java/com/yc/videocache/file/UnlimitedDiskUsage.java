package com.yc.videocache.file;

import java.io.File;
import java.io.IOException;

/**
 * Unlimited version of {@link DiskUsage}.
 *
 */
public class UnlimitedDiskUsage implements DiskUsage {

    @Override
    public void touch(File file) throws IOException {
        // do nothing
    }
}
