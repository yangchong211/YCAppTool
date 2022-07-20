package com.yc.privacymonitor.helper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.yc.privacymonitor.config.PrivacyConfig;
import com.yc.privacymonitor.method.NormalMethodList;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCAndroidTool
 *     email : yangchong211@163.com
 *     time  : 2021/05/09
 *     desc  : 初始化操作
 *     revise:
 * </pre>
 */
public class PrivacyProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        Context context = getContext();
        PrivacyConfig config = new PrivacyConfig.Builder(context).build();
        PrivacyHelper.init(config);
        PrivacyHelper.start(new NormalMethodList());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

