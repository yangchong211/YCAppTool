package com.yc.logupload.config;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/4/17
 *     desc   : 初始化操作
 *     revise :
 * </pre>
 */
public final class UploadInitProvider extends ContentProvider {

    private static final String TAG = "UploadInitProvider";

    @Override
    public boolean onCreate() {
        try {
            Context context = getContext();
        } catch (Exception ex) {
            Log.e(TAG, "Failed to auto initialize InitProvider", ex);
        }
        return false;
    }

    @Override
    public Cursor query(
            @NonNull final Uri uri,
            final String[] projection,
            final String selection,
            final String[] selectionArgs,
            final String sortOrder) {
        return null;
    }

    @Override
    public String getType(@NonNull final Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull final Uri uri, final ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull final Uri uri, final String selection, final String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(
            @NonNull final Uri uri,
            final ContentValues values,
            final String selection,
            final String[] selectionArgs) {
        return 0;
    }
}
