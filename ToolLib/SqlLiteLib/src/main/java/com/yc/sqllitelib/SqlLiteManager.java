package com.yc.sqllitelib;

import android.content.Context;

import com.yc.toolutils.AppCleanUtils;

public final class SqlLiteManager {

    /**
     * 获取单例对象：确保App运行时数据库只被打开一次，避免重复打开引起错误。
     */
    private static volatile SqlLiteManager instance;
    private Context context;
    private DbSqliteHelper dbSqliteHelper;

    public static SqlLiteManager getInstance() {
        if (instance == null) {
            synchronized (SqlLiteManager.class) {
                if (instance == null) {
                    instance = new SqlLiteManager();
                }
            }
        }
        return instance;
    }

    private SqlLiteManager() {

    }

    public Context getContext() {
        return context;
    }

    public DbSqliteHelper getSqliteHelper() {
        return dbSqliteHelper;
    }

    public void clearSql(String dbName) {
        AppCleanUtils.cleanInternalDbByName(dbName);
    }
}
