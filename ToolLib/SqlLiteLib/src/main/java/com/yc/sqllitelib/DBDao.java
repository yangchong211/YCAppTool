package com.yc.sqllitelib;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tencent.wx.palmsdk.yt.YTPalmUtils;

import java.util.ArrayList;
import java.util.List;

public final class DBDao {

    public static String TABLE_NAME = "student";//表名
    private static final String ID = "id";//id自增长
    private static final String NAME = "stu_name";//姓名
    private static final String AGE = "stu_age";//年龄
    private static final String SEX = "stu_sex";//性别
    private static final String GRADE = "stu_grade";//年级

    /**
     * 创建表结构
     */
    public static final String SQL_CREATE_TABLE = "create table " + TABLE_NAME + "(" +
            ID + " integer primary key autoincrement," +
            NAME + " text," +
            AGE + " integer," +
            SEX + " varchar(5)," +
            GRADE + " text" +
            ")";


    private DBDao() {

    }

    public static DBDao getInstance() {
        return InnerDB.instance;
    }

    private static class InnerDB {
        private static final DBDao instance = new DBDao();
    }


    /**
     * 数据库插入数据
     *
     * @param bean 实体类
     * @param <T>  T
     */
    public synchronized <T> void insert(T bean) {
        DbSqliteHelper dbHelper = DbPalmManger.getInstance().getHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            if (bean instanceof Student) {
                Student student = (Student) bean;
                ContentValues cv = new ContentValues();
                cv.put(NAME, student.getName());
                cv.put(AGE, student.getAge());
                cv.put(SEX, student.getSex());
                cv.put(GRADE, student.getGrade());
                db.insert(TABLE_NAME, null, cv);
                YTPalmUtils.d("insert插入数据 : " + cv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    /**
     * 删除表中所有的数据
     */
    public synchronized void clearAll() {
        DbSqliteHelper dbHelper = DbPalmManger.getInstance().getHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "delete from " + TABLE_NAME;
        try {
            db.execSQL(sql);
            YTPalmUtils.d("执行删除数据 : " + sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    /**
     * 查询数据
     *
     * @return List
     */
    @SuppressLint("Range")
    public synchronized <T> List<T> query() {
        DbSqliteHelper dbHelper = DbPalmManger.getInstance().getHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<T> list = new ArrayList<>();
        String querySql = "select * from " + TABLE_NAME;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(querySql, null);
            YTPalmUtils.d("query条数 : " + cursor.moveToNext());
            while (cursor.moveToNext()) {
                Student student = new Student();
                student.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                student.setAge(cursor.getInt(cursor.getColumnIndex(AGE)));
                student.setSex(cursor.getString(cursor.getColumnIndex(SEX)));
                student.setGrade(cursor.getString(cursor.getColumnIndex(GRADE)));
                list.add((T) student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return list;
    }

    /**
     * 查找表名称
     */
    public ArrayList<String> tablesInDB() {
        DbSqliteHelper dbHelper = DbPalmManger.getInstance().getHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<String> list = new ArrayList<>();
        String sql = "select name from sqlite_master where type='table'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                String string = cursor.getString(0);
                YTPalmUtils.d("查找表名称 : " + string);
                list.add(string);
            } while (cursor.moveToNext());
        }
        return list;

    }

}
