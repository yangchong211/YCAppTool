package com.yc.monitorapplib.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.yc.monitorapplib.data.AppItem;
import com.yc.monitorapplib.data.IgnoreItem;


public class DbIgnoreExecutor {

    private static DbIgnoreExecutor sInstance;
    private static DbHelper mHelper;

    private DbIgnoreExecutor() {
    }

    public static void init(Context context) {
        mHelper = new DbHelper(context);
        sInstance = new DbIgnoreExecutor();
    }

    public static DbIgnoreExecutor getInstance() {
        return sInstance;
    }

    public void insertItem(AppItem item) {
        if (!exists(item.mPackageName)) {
            ContentValues values = new ContentValues();
            values.put(DbConst.TableIgnore.FIELD_PACKAGE_NAME, item.mPackageName);
            values.put(DbConst.TableIgnore.FIELD_CREATE_TIME, System.currentTimeMillis());
            mHelper.getWritableDatabase().insert(DbConst.TableIgnore.TABLE_NAME, null, values);
        }
    }

    public void insertItem(String packageName) {
        if (!exists(packageName)) {
            ContentValues values = new ContentValues();
            values.put(DbConst.TableIgnore.FIELD_PACKAGE_NAME, packageName);
            values.put(DbConst.TableIgnore.FIELD_CREATE_TIME, System.currentTimeMillis());
            mHelper.getWritableDatabase().insert(DbConst.TableIgnore.TABLE_NAME, null, values);
        }
    }

    public void deleteItem(IgnoreItem item) {
        if (exists(item.mPackageName)) {
            mHelper.getWritableDatabase().delete(
                    DbConst.TableIgnore.TABLE_NAME,
                    DbConst.TableIgnore.FIELD_PACKAGE_NAME + " = ?",
                    new String[]{item.mPackageName}
            );
        }
    }

    public List<IgnoreItem> getAllItems() {
        Cursor cursor = null;
        List<IgnoreItem> items = new ArrayList<>();
        try {
            String[] columns = {
                    DbConst.TableIgnore._ID,
                    DbConst.TableIgnore.FIELD_PACKAGE_NAME,
                    DbConst.TableIgnore.FIELD_CREATE_TIME,
            };
            String orderBy = DbConst.TableIgnore.FIELD_CREATE_TIME + " DESC";
            cursor = mHelper.getReadableDatabase().query(
                    DbConst.TableIgnore.TABLE_NAME,
                    columns,
                    null,
                    null,
                    null,
                    null,
                    orderBy);
            while (cursor.moveToNext()) {
                items.add(cursorToItem(cursor));
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return items;
    }

    private boolean exists(String packageName) {
        SQLiteDatabase database = mHelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = database.query(DbConst.TableIgnore.TABLE_NAME,
                    new String[]{DbConst.TableIgnore._ID},
                    DbConst.TableIgnore.FIELD_PACKAGE_NAME + " = ?",
                    new String[]{packageName},
                    null,
                    null,
                    null);
            if (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(DbConst.TableIgnore._ID));
                return id > 0;
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return false;
    }

    private IgnoreItem cursorToItem(Cursor cursor) {
        IgnoreItem item = new IgnoreItem();
        item.mPackageName = cursor.getString(cursor.getColumnIndex(DbConst.TableIgnore.FIELD_PACKAGE_NAME));
        item.mCreated = cursor.getLong(cursor.getColumnIndex(DbConst.TableIgnore.FIELD_CREATE_TIME));
        return item;
    }
}
