package com.yc.tracesdk;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public final static int VERSION = 2;
    private final static String DB_NAME = "location_info.db";
    public final static String TABLE_NAME = "location";
    public final static String ID = "_id";
    public final static String TIME_STAMP = "ts";
    public final static String TYPE = "type";
    public final static String BYTE_DATA = "byte_data";
    
    private final static String DB_CREATE = "create table " + TABLE_NAME
            + "(" + ID + " integer primary key autoincrement, "
            + TIME_STAMP + " long, "
            + TYPE + " text not null, "
            + BYTE_DATA + " BLOB );";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /* package */void insertData() {
        
    }

    void recreateTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
