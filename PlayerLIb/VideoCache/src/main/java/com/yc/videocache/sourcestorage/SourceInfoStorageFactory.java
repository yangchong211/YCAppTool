package com.yc.videocache.sourcestorage;

import android.content.Context;

/**
 * Simple factory for {@link SourceInfoStorage}.
 *
 */
public class SourceInfoStorageFactory {

    public static SourceInfoStorage newSourceInfoStorage(Context context) {
        return new DatabaseSourceInfoStorage(context);
    }

    public static SourceInfoStorage newEmptySourceInfoStorage() {
        return new NoSourceInfoStorage();
    }
}
