package com.yc.sqllitelib;



import java.util.HashMap;

/**
 * 数据库
 */
public class DbPalmManger implements IDbPalm<String> {

    private static final String TAG = "DbManger";
    /**
     * 建立一个数据库对象
     */
    private final DbSqliteHelper helper;
    //表名
    private final String table_name = "palm_data";

    private DbPalmManger() {
        //由于数据库只需要调用一次，所以在单例中建出来
        helper = new DbSqliteHelper();
    }

    /**
     * 获取单例对象：确保App运行时数据库只被打开一次，避免重复打开引起错误。
     */
    private static volatile DbPalmManger instance;

    public static DbPalmManger getInstance() {
        if (instance == null) {
            synchronized (DbPalmManger.class) {
                if (instance == null) {
                    instance = new DbPalmManger();
                }
            }
        }
        return instance;
    }

    public DbSqliteHelper getHelper() {
        return helper;
    }

    public void close(boolean isRead) {
        helper.closeDb(isRead);
    }


    @Override
    public void add(String featureId, String feature) {

    }

    @Override
    public void addAll(HashMap<String, String> features) {

    }

    @Override
    public String get(String featureId) {
        return null;
    }

    @Override
    public void delete(String featureId) {

    }

    @Override
    public void delete(String[] featureIds) {

    }

    @Override
    public HashMap<String, String> getAllData() {
        return null;
    }

    @Override
    public void clear() {

    }
}
