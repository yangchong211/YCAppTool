package com.yc.sqllitelib.abs;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yc.sqllitelib.DbSqliteHelper;
import com.yc.sqllitelib.SqlLiteManager;
import com.yc.sqllitelib.inter.IBaseDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbsBaseDao<T> implements IBaseDao<T> {

    /**
     * 插入或更新单条数据
     *
     * @param item      item
     */
    public synchronized void insertOrUpdate(T item) {
        ArrayList<T> tmpList = getListInfo();
        DbSqliteHelper dbHelper = SqlLiteManager.getInstance().getSqliteHelper();
        try {
            SQLiteDatabase db = dbHelper.getWriteDb();
            if (!db.isOpen()) {
                return;
            }
            //db是否存在此数据
            boolean isExist = false;
            for (T tmpInfo : tmpList) {
                if (compareItem(item, tmpInfo)) {
                    isExist = true;
                    break;
                }
            }
            if (isExist) {
                updateItem(db, item);
            } else {
                db.insert(getTableName(), null, getContentValues(item));
            }
        } finally {
            dbHelper.closeDb(false);
        }
    }

    /**
     * 插入单条数据
     *
     * @param item      item
     */
    public synchronized void insert(T item) {
        DbSqliteHelper dbHelper = SqlLiteManager.getInstance().getSqliteHelper();
        try {
            SQLiteDatabase db = dbHelper.getWriteDb();
            if (db.isOpen()) {
                db.insert(getTableName(), null, getContentValues(item));
            }
        } finally {
            dbHelper.closeDb(false);
        }
    }

    /**
     * 批量插入（旧数据删除）
     *
     * @param dataList          数据
     */
    public synchronized void insert(ArrayList<T> dataList) {
        DbSqliteHelper dbHelper = SqlLiteManager.getInstance().getSqliteHelper();
        try {
            SQLiteDatabase db = dbHelper.getWriteDb();
            if (db.isOpen()) {
                // 手动设置开始事务
                db.beginTransaction();
                // 删除旧数据
                deleteAll(db);
                for (T item : dataList) {
                    db.insert(getTableName(), null, getContentValues(item));
                }
                // 设置事务处理成功，不设置会自动回滚不提交
                db.setTransactionSuccessful();
                // 处理完成
                db.endTransaction();
            }
        } finally {
            dbHelper.closeDb(true);
        }
    }


    /**
     * 批量插入或更新
     * @param dataList          list数据
     */
    public synchronized void insertOrUpdate(List<T> dataList) {
        ArrayList<T> tmpList = getListInfo();
        DbSqliteHelper dbHelper = SqlLiteManager.getInstance().getSqliteHelper();
        try {
            SQLiteDatabase db = dbHelper.getWriteDb();
            if (!db.isOpen()) {
                return;
            }
            // 手动设置开始事务
            db.beginTransaction();
            for (T item : dataList) {
                //db是否存在此数据
                boolean isExist = false;
                for (T tmpInfo : tmpList) {
                    if (compareItem(item, tmpInfo)) {
                        isExist = true;
                        break;
                    }
                }
                if (isExist) {
                    updateItem(db, item);
                } else {
                    db.insert(getTableName(), null, getContentValues(item));
                }
            }
            // 设置事务处理成功，不设置会自动回滚不提交
            db.setTransactionSuccessful();
            // 处理完成
            db.endTransaction();
        } finally {
            dbHelper.closeDb(true);
        }
    }

    /**
     * 获取map集合
     *
     * @return
     */
    public Map<Object, T> getMapInfo(String key) {
        Map<Object, T> map = new HashMap<>();
        Cursor cursor = null;
        DbSqliteHelper dbHelper = SqlLiteManager.getInstance().getSqliteHelper();
        try {
            SQLiteDatabase db = dbHelper.getReadDb();
            cursor = db.query(getTableName(), null,
                    null, null, null, null, null);
            if (db.isOpen()) {
                while (cursor.moveToNext()) {
                    String id = getString(cursor, key);
                    //int columnIndex = cursor.getColumnIndex(key);
                    //String id = cursor.getString(columnIndex);
                    map.put(id, getItemInfo(cursor));
                }
            }
        } finally {
            if (cursor != null){
                cursor.close();
            }
            dbHelper.closeDb(true);
        }
        return map;

    }

    /**
     * 获取list集合
     *
     * @return
     */
    public ArrayList<T> getListInfo() {
        ArrayList<T> msgList = new ArrayList<>();
        Cursor cursor = null;
        DbSqliteHelper dbHelper = SqlLiteManager.getInstance().getSqliteHelper();
        try {
            SQLiteDatabase db = dbHelper.getReadDb();
            cursor = db.query(getTableName(), null,
                    null, null, null, null, null);
            if (db.isOpen()) {
                while (cursor.moveToNext()) {
                    msgList.add(getItemInfo(cursor));
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dbHelper.closeDb(true);
        }
        return msgList;
    }


    /**
     * 获取list集合（自定义db和cursor）
     *
     * @return
     */
    public ArrayList<T> getListInfo(SQLiteDatabase db, Cursor cursor) {
        ArrayList<T> msgList = new ArrayList<>();
        try {
            if (db.isOpen()) {
                while (cursor.moveToNext()) {
                    msgList.add(getItemInfo(cursor));
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return msgList;
    }

    /**
     * 获取表名
     *
     * @return      表名
     */
    public abstract String getTableName();

    /**
     * item转ContentValues
     *
     * @param item          item
     * @return
     */
    public abstract ContentValues getContentValues(T item);

    /**
     * 获取item
     *
     * @param cursor        cursor指针
     * @return
     */
    public abstract T getItemInfo(Cursor cursor);

    /**
     * 对比两个item是否相同
     *
     * @param item1
     * @param item2
     * @return
     */
    public abstract boolean compareItem(T item1, T item2);

    /**
     * 更新item
     *
     * @param db        db
     * @param item      item
     */
    public abstract void updateItem(SQLiteDatabase db, T item);

    /**
     * 删除所有信息
     *
     * @param db        db
     */
    public void deleteAll(SQLiteDatabase db) {
        try {
            db.delete(getTableName(), null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除所有信息
     */
    public void deleteAll() {
        DbSqliteHelper dbHelper = SqlLiteManager.getInstance().getSqliteHelper();
        try {
            SQLiteDatabase db = dbHelper.getWriteDb();
            if (db.isOpen()) {
                db.delete(getTableName(), null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbHelper.closeDb(false);
        }
    }


    public String getString(Cursor cursor, String name) {
        int columnIndex = cursor.getColumnIndex(name);
        return cursor.getString(columnIndex);
    }

    public int getInt(Cursor cursor, String name) {
        int columnIndex = cursor.getColumnIndex(name);
        return cursor.getInt(columnIndex);
    }

    public long getLong(Cursor cursor, String name) {
        int columnIndex = cursor.getColumnIndex(name);
        return cursor.getLong(columnIndex);
    }

    public float getFloat(Cursor cursor, String name) {
        int columnIndex = cursor.getColumnIndex(name);
        return cursor.getFloat(columnIndex);
    }

    public boolean getBool(Cursor cursor, String name) {
        int columnIndex = cursor.getColumnIndex(name);
        return cursor.getInt(columnIndex) == 1;
    }
}