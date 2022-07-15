package com.yc.monitorapplib.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.yc.monitorapplib.data.HistoryItem;


public class DbHistoryExecutor {

    private static DbHistoryExecutor sInstance;
    private static DbHelper mHelper;

    private DbHistoryExecutor() {
    }

    public static void init(Context context) {
        mHelper = new DbHelper(context);
        sInstance = new DbHistoryExecutor();
    }

    public static DbHistoryExecutor getInstance() {
        return sInstance;
    }

    public void clear() {
        mHelper.getWritableDatabase().delete(DbConst.TableHistory.TABLE_NAME, null, null);
    }

    public void insert(HistoryItem historyItem) {
        if (!exists(historyItem)) {
            ContentValues values = itemToContentValue(historyItem);
            mHelper.getWritableDatabase().insert(DbConst.TableHistory.TABLE_NAME, null, values);
        }
    }

    public List<HistoryItem> getAllItems() {
        Cursor cursor = null;
        List<HistoryItem> items = new ArrayList<>();
        try {
            String[] columns = {
                    DbConst.TableHistory._ID,
                    DbConst.TableHistory.FIELD_DATE,
                    DbConst.TableHistory.FIELD_TIMESTAMP,
                    DbConst.TableHistory.FIELD_SYSTEM,
                    DbConst.TableHistory.FIELD_DURATION,
                    DbConst.TableHistory.FIELD_PACKAGE_NAME,
                    DbConst.TableHistory.FIELD_MOBILE_TRAFFIC,
                    DbConst.TableHistory.FIELD_NAME,
            };
            String orderBy = DbConst.TableHistory.FIELD_DURATION + " DESC";
            cursor = mHelper.getReadableDatabase().query(
                    DbConst.TableHistory.TABLE_NAME,
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

    private boolean exists(HistoryItem historyItem) {
        SQLiteDatabase database = mHelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = database.query(DbConst.TableHistory.TABLE_NAME,
                    new String[]{DbConst.TableHistory._ID},
                    DbConst.TableHistory.FIELD_DATE + " = ? AND " + DbConst.TableHistory.FIELD_PACKAGE_NAME + " = ?",
                    new String[]{historyItem.mDate, historyItem.mPackageName},
                    null,
                    null,
                    null);
            if (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(DbConst.TableHistory._ID));
                return id > 0;
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return false;
    }

    private ContentValues itemToContentValue(HistoryItem historyItem) {
        ContentValues values = new ContentValues();
        values.put(DbConst.TableHistory.FIELD_DATE, historyItem.mDate);
        values.put(DbConst.TableHistory.FIELD_DURATION, historyItem.mDuration);
        values.put(DbConst.TableHistory.FIELD_MOBILE_TRAFFIC, historyItem.mMobileTraffic);
        values.put(DbConst.TableHistory.FIELD_NAME, historyItem.mName);
        values.put(DbConst.TableHistory.FIELD_PACKAGE_NAME, historyItem.mPackageName);
        values.put(DbConst.TableHistory.FIELD_SYSTEM, historyItem.mIsSystem);
        values.put(DbConst.TableHistory.FIELD_TIMESTAMP, historyItem.mTimeStamp);
        return values;
    }

    private HistoryItem cursorToItem(Cursor cursor) {
        HistoryItem item = new HistoryItem();
        item.mPackageName = cursor.getString(cursor.getColumnIndex(DbConst.TableHistory.FIELD_PACKAGE_NAME));
        item.mName = cursor.getString(cursor.getColumnIndex(DbConst.TableHistory.FIELD_NAME));
        item.mDate = cursor.getString(cursor.getColumnIndex(DbConst.TableHistory.FIELD_DATE));
        item.mDuration = cursor.getLong(cursor.getColumnIndex(DbConst.TableHistory.FIELD_DURATION));
        item.mTimeStamp = cursor.getLong(cursor.getColumnIndex(DbConst.TableHistory.FIELD_TIMESTAMP));
        item.mIsSystem = cursor.getInt(cursor.getColumnIndex(DbConst.TableHistory.FIELD_SYSTEM));
        item.mMobileTraffic = cursor.getInt(cursor.getColumnIndex(DbConst.TableHistory.FIELD_MOBILE_TRAFFIC));
        return item;
    }
}
