package com.yc.sqllitelib;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IBaseDao<T> {

    void insert(T item);

    void insert(ArrayList<T> dataList);

    void insertOrUpdate(T item);

    void insertOrUpdate(List<T> dataList);

    Map<Object, T> getMapInfo(String key);

    ArrayList<T> getListInfo();

    ArrayList<T> getListInfo(SQLiteDatabase db, Cursor cursor);

    void deleteAll(SQLiteDatabase db);

    void deleteAll();

}
