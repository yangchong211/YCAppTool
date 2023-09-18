package com.yc.sqllitelib;

import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库常见操作
 */
public interface ISqlite {

    /**
     * 读连接可调用SQLiteOpenHelper的getReadableDatabase方法获得
     *
     * @return SQLiteDatabase
     */
    SQLiteDatabase getWriteDb();

    /**
     * 写连接可调用getWritableDatabase获得
     *
     * @return SQLiteDatabase
     */
    SQLiteDatabase getReadDb();

    /**
     * 关闭数据库连接：数据库操作完毕后，应当调用SQLiteDatabase对象的close方法关闭连接。
     *
     * @param isRead 是否是读操作
     */
    void closeDb(boolean isRead);

}
