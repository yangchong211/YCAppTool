package com.yc.sqllitelib;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.Nullable;

import com.tencent.wx.palmsdk.PalmServiceApi;

public class DbSqliteHelper extends SQLiteOpenHelper implements ISqlite {

    /**
     * 数据库名称
     */
    private static final String DB_NAME = "palm_sdk_db.db";
    /**
     * 数据库版本
     */
    private static final int VERSION = 1;
    /**
     * table表名
     */
    protected static final String PALM_FEATURE_TABLE = "palm_feature_table";

    public DbSqliteHelper() {
        this(PalmServiceApi.getInstance().getConfig().getContext(),
                DB_NAME, null, VERSION);
    }

    public DbSqliteHelper(@Nullable Context context, @Nullable String name,
                          @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * onCreate()是在数据库首次创建的时候调用
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //初始化数据库
        db.execSQL(DBDao.SQL_CREATE_TABLE);
    }

    /**
     * 更改数据库版本的操作，根据新旧版本号进行表结构变更处理，当打开数据库时传入版本号与当前不同会调用此方法
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 每次成功打开数据库后首先被执行
     *
     * @param db The database.
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //启用数据库的预写日志
            setWriteAheadLoggingEnabled(true);
        } else {
            db.enableWriteAheadLogging();
        }
    }

    @Override
    public synchronized SQLiteDatabase getWriteDb() {
        return getWritableDatabase();
    }

    @Override
    public synchronized SQLiteDatabase getReadDb() {
        return getReadableDatabase();
    }

    @Override
    public synchronized void closeDb(boolean isRead) {
        if (isRead) {
            getReadDb().close();
        } else {
            getWriteDb().close();
        }
    }
}
