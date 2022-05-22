package com.yc.toastutils;

import android.app.Application;
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
 *     GitHub: https://github.com/yangchong211/YCDialog
 * </pre>
 */
public final class ToastProvider extends ContentProvider {

    private static final String TAG = "ToastProvider";

    @Override
    public boolean onCreate() {
        try {
            Context context = getContext();
            Context applicationContext = context.getApplicationContext();
            ToastUtils.init((Application) applicationContext);
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
