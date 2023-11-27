package com.yc.sqllitelib.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yc.sqllitelib.abs.AbsBaseDao;

public class StudentDaoImpl extends AbsBaseDao<Student> {
    @Override
    public String getTableName() {
        return "student.db";
    }

    @Override
    public ContentValues getContentValues(Student item) {
        return null;
    }

    @Override
    public Student getItemInfo(Cursor cursor) {
        return null;
    }

    @Override
    public boolean compareItem(Student item1, Student item2) {
        return false;
    }

    @Override
    public void updateItem(SQLiteDatabase db, Student item) {

    }
}
