package com.yc.apptool.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yc.toolutils.AppLogUtils;

import java.util.ArrayList;

public final class DBTestDao {

    private static volatile DBTestDao instance;
    private final MySqliteHelper dbHelper;
    private static final String TAG = "DBTestDao: ";
    private static String TABLE_NAME = "student";//表名
    private static final String ID = "id";//id自增长
    private static final String NAME = "stu_name";//姓名
    private static final String AGE = "stu_age";//年龄
    private static final String SEX = "stu_sex";//性别
    private static final String GRADE = "stu_grade";//年级

    /**
     * 创建表结构
     */
    static final String SQL_CREATE_TABLE = "create table " + TABLE_NAME + "(" +
            ID + " integer primary key autoincrement," +
            NAME + " text," +
            AGE + " integer," +
            SEX + " varchar(5)," +
            GRADE + " text" +
            ")";



    private DBTestDao(Context context) {
        dbHelper = new MySqliteHelper(context);
    }

    public static DBTestDao getInstance(Context context) {
        if (instance == null) {
            synchronized (DBTestDao.class) {
                if (instance == null) {
                    instance = new DBTestDao(context);
                }
            }
        }
        return instance;
    }

    public void insert(Student student) {
        long startTime = System.currentTimeMillis();
        SQLiteDatabase db = dbHelper.getWriteDb();
        try {
            ContentValues cv = new ContentValues();
            cv.put(NAME, student.getName());
            cv.put(AGE, student.getAge());
            cv.put(SEX, student.getSex());
            cv.put(GRADE, student.getGrade());
            db.insert(TABLE_NAME, null, cv);
            AppLogUtils.d(TAG+"insert插入数据到数据库 : " + cv);
        } finally {
            db.close();
        }
        long endTime = System.currentTimeMillis();
        AppLogUtils.d(TAG+"insert插入数据完成耗时 : " + (endTime - startTime));
    }

    public void insertList(ArrayList<Student> features) {
        long startTime = System.currentTimeMillis();
        SQLiteDatabase db = dbHelper.getWriteDb();
        // 手动设置开始事务
        db.beginTransaction();
        for (Student student : features) {
            ContentValues cv = new ContentValues();
            cv.put(NAME, student.getName());
            cv.put(AGE, student.getAge());
            cv.put(SEX, student.getSex());
            cv.put(GRADE, student.getGrade());
            db.insert(TABLE_NAME, null, cv);
        }
        // 设置事务处理成功，不设置会自动回滚不提交
        db.setTransactionSuccessful();
        // 处理完成
        db.endTransaction();
        long endTime = System.currentTimeMillis();
        AppLogUtils.d(TAG+"insert插入集合数据完成耗时 : " + (endTime - startTime)
                + " , 共" + features.size() + "条数据");
    }

    public Student get(String name) {
        long startTime = System.currentTimeMillis();
        SQLiteDatabase db = dbHelper.getReadDb();
        // 第一个参数：table ：表名。相当于select *** from table语句中的table 。如果是多表联合查询，可以用逗号将两个表名分开。
        // 第二个参数：columns ：要查询出来的列名（字段），全部查询就写null。相当于 select *** from table语句中的 ***部分 。
        // 如果是查询多个参数，可以用逗号将两个表名分开。例：new String[]{“name”,“age”,“sex”}
        // 第三个参数：selection：查询条件子句，相当于select *** from table where && 语句中的&&部分
        // 在条件子句允许使用占位符“?”表示条件值 ，例：“name=?,age=?,sex=?”
        // 第四个参数：selectionArgs ：查询条件对应的内容。对应于 selection参数 占位符的值，
        // 值在数组中的位置与占位符在语句中的位置必须一致，否则就会有异常。 例：与 new String[]{“lisa”,“1”,“女”}
        // 第五个参数：groupBy ：分组。相当于 select *** from table where && group by … 语句中 … 的部分 ，
        // 作用是：将同一列的相同名字的参数合并在一起 例;在name列有两个Jame（name:Jame --salay:100）,
        // 使用…group by name查询后 只显示一个Jame的集合（name:Jame–salay:300）
        // 第六个参数：having ：相当于 select *** from table where && group by …having %%% 语句中 %%% 的部分，
        // 作用于groupBy的条件，例：havig name>2意思是name列相同参数>2
        // 第七个参数：orderBy ：相当于 select ***from ？？ where&& group by …having %%% order by@@语句中的@@ 部分，
        // 如： personid desc（按person 降序）, age asc（按age升序）;
        // 第八个参数：limit：指定偏移量和获取的记录数，查询显示的条数（分页，可以不写，默认全部显示）。
        // 相当于select语句limit关键字后面的部分。

        Cursor cursor = null;
        Student student = new Student();
        try {
            //查询 _id = 1 的数据
            cursor = db.query(TABLE_NAME, null, NAME + "=?",
                    new String[]{name}, null, null, null);
            //移动到首位
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                //通过 Cursor 的 getColumnIndex() 方法获取到某一列在表中对应位置的索引
                //然后将这个索引传入到相应的取值方法中，就可以得到从数据库中读取到的数据
                student.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                student.setGrade(cursor.getString(cursor.getColumnIndex(GRADE)));
                student.setAge(cursor.getInt(cursor.getColumnIndex(AGE)));
                student.setSex(cursor.getString(cursor.getColumnIndex(SEX)));
                cursor.moveToNext();
                AppLogUtils.i(TAG+"query查询单条数据 : " + student);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        long endTime = System.currentTimeMillis();
        AppLogUtils.d(TAG+"query查询单条数据耗时 : " + (endTime - startTime));
        return student;
    }

    public void update(String name, Student student) {
        long startTime = System.currentTimeMillis();
        SQLiteDatabase db = dbHelper.getWriteDb();
        ContentValues cv = new ContentValues();
        cv.put(NAME, student.getName());
        cv.put(AGE, student.getAge());
        cv.put(SEX, student.getSex());
        cv.put(GRADE, student.getGrade());
        //?是一个占位符，通过字符串数组为每个占位符指定相应的内容
        db.update(TABLE_NAME, cv, NAME + "=?", new String[]{name});
        AppLogUtils.i(TAG+"update更新数据 : " + student);
        long endTime = System.currentTimeMillis();
        AppLogUtils.d(TAG+"update更新数据完成耗时 : " + (endTime - startTime));
    }

    public void delete(String name) {
        long startTime = System.currentTimeMillis();
        SQLiteDatabase db = dbHelper.getWriteDb();
        db.delete(TABLE_NAME, name + "=?", new String[]{name});
        long endTime = System.currentTimeMillis();
        AppLogUtils.d(TAG+"delete删除数据完成耗时 : " + (endTime - startTime));
    }

    public ArrayList<Student> getAllData() {
        SQLiteDatabase db = dbHelper.getReadDb();
        ArrayList<Student> list = new ArrayList<>();
        String querySql = "select * from " + TABLE_NAME;
        Cursor cursor = null;
        try {
            //查询表中的所有数据
            cursor = db.rawQuery(querySql, null);
            if (cursor == null) {
                return list;
            }
            //cursor = db.query(TABLE_NAME,null,null,null,null,null,null);
            //通过 Cursor 对象的 moveToNext()方法去遍历查询到的每一行数据。
            while (cursor.moveToNext()) {
                Student student = new Student();
                //通过 Cursor 的 getColumnIndex() 方法获取到某一列在表中对应位置的索引
                //然后将这个索引传入到相应的取值方法中，就可以得到从数据库中读取到的数据
                student.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                student.setGrade(cursor.getString(cursor.getColumnIndex(GRADE)));
                student.setAge(cursor.getInt(cursor.getColumnIndex(AGE)));
                student.setSex(cursor.getString(cursor.getColumnIndex(SEX)));
                list.add(student);
                AppLogUtils.d("query查询数据 : " + student);
            }
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

    public void clear() {
        SQLiteDatabase db = dbHelper.getWriteDb();
        String sql = "delete from " + TABLE_NAME;
        try {
            db.execSQL(sql);
            AppLogUtils.d(TAG+"执行删除数据 : " + sql);
        } finally {
            db.close();
        }
    }

    /**
     * 查找表名称
     */
    public ArrayList<String> tablesInDB() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<String> list = new ArrayList<>();
        String sql = "select name from sqlite_master where type='table'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                String string = cursor.getString(0);
                AppLogUtils.d("查找表名称 : " + string);
                list.add(string);
            } while (cursor.moveToNext());
        }
        return list;
    }

}
