package com.yc.tracesdk;

import java.io.IOException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteFullException;
import android.database.sqlite.SQLiteStatement;

import com.yc.tracesdk.LocInfoProtoBuf.CellInfo;
import com.yc.tracesdk.LocInfoProtoBuf.EnvInfo;
import com.yc.tracesdk.LocInfoProtoBuf.ExtraLocInfo;
import com.yc.tracesdk.LocInfoProtoBuf.GpsInfo;
import com.yc.tracesdk.LocInfoProtoBuf.TraceData;
import com.yc.tracesdk.LocInfoProtoBuf.WifiInfo;
import com.squareup.wire.Wire;

public class DBHandler {
    private Context mContext;
    private static volatile DBHandler mInstance;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;
    private DBListener mDBListener;

    public final static String TYPE_CELL = "CELL";
    public final static String TYPE_GPS = "GPS";
    public final static String TYPE_WIFI = "WIFI";
    public final static String TYPE_EXTRA = "EXTRA";
    public final static String TYPE_ENV = "ENV";

    private DBHandler(Context context) {
        this.mContext = context.getApplicationContext();
        this.mDBHelper = new DBHelper(mContext);
        this.mDB = mDBHelper.getReadableDatabase();
    }

    /* package */ static DBHandler getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBHandler.class) {
                if (mInstance == null) {
                    mInstance = new DBHandler(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置数据库监听
     * 
     * @param dBListener
     *            数据库监听
     */
    /* package */void setDBListener(DBListener dBListener) { //#liuc# provide external a interface to notify dbChanges
        mDBListener = dBListener;
    }

    /**
     * 插入一条wifi数据
     * 
     * @param data
     *            wifi数据内容
     */
    /* package */void insertWifiData(byte[] data) {
        insertData(TYPE_WIFI, data);
    }

    /**
     * 插入一条cell数据
     * 
     * @param data
     *            cell数据内容
     */
    /* package */void insertCellData(byte[] data) {
        insertData(TYPE_CELL, data);
    }

    /**
     * 插入一条gps数据
     * 
     * @param data
     *            gps数据内容
     */
    /* package */void insertGpsData(byte[] data) {
        insertData(TYPE_GPS, data);
    }

    /**
     * 插入一条第三方的定位数据
     * 
     * @param data
     *            第三方的定位数据内容
     */
    /* package */void insertExtraLocData(byte[] data) {
        insertData(TYPE_EXTRA, data);
    }

    /**
     * 插入一条环境数据
     */
    void insertEnvData(byte[] data) { insertData(TYPE_ENV, data); }

    /**
     * 取出最新(根据本条数据第一次插入数据库的时间排序)的几条数据
     * 
     * @param size
     *            数据条数
     * @return 数据列表，不为{@code null},列表长度范围为[0, size]
     */
    /* package */synchronized ArrayList<DataWraper> popData(int size) {
        ArrayList<DataWraper> result = new ArrayList<DataWraper>();
        String sql = "select * from " + DBHelper.TABLE_NAME + " order by " + DBHelper.TIME_STAMP + " DESC limit " + size;
        Cursor cursor = mDB.rawQuery(sql, null);
        if (cursor == null) {
            return result;
        }
        int tsIndex = cursor.getColumnIndex(DBHelper.TIME_STAMP);
        int typeIndex = cursor.getColumnIndex(DBHelper.TYPE);
        int dataIndex = cursor.getColumnIndex(DBHelper.BYTE_DATA);

        try {
            while (cursor.moveToNext()) {
                DataWraper data = new DataWraper();
                data.ts = cursor.getLong(tsIndex);
                data.type = cursor.getString(typeIndex);
                data.byteData = cursor.getBlob(dataIndex);
                result.add(data);
            }
        } catch (SQLiteCantOpenDatabaseException e) {}

        cursor.close();

        String deleteSql = "delete from " + DBHelper.TABLE_NAME + " where " + DBHelper.TIME_STAMP + " in (select "
                + DBHelper.TIME_STAMP + " from " + DBHelper.TABLE_NAME + " order by " + DBHelper.TIME_STAMP
                + " DESC LIMIT " + size + ");";
        mDB.execSQL(deleteSql);
        
        mInnerDBListener.onDeleteData();
        return result;
    }

    /**
     * 将多条不同类型的数据转换为PB格式的字节数组
     * 
     * @param dataList
     *            数据列表
     * @return {@code LocInfoProtoBuf.TraceData}对象的字节数组
     */
    /* package */byte[] convertDataList2ByteArray(ArrayList<DataWraper> dataList) {
        TraceData.Builder builder = new TraceData.Builder();
        Wire wire = new Wire();

        builder.cell_list = new ArrayList<CellInfo>();
        builder.wifi_list = new ArrayList<WifiInfo>();
        builder.gps_list = new ArrayList<GpsInfo>();
        builder.env_list = new ArrayList<EnvInfo>();

        for (DataWraper data : dataList) {
            try {
                if (data.type.equals(TYPE_CELL)) {
                    CellInfo cellInfo = wire.parseFrom(data.byteData, CellInfo.class);
                    builder.cell_list.add(cellInfo);
                } else if (data.type.equals(TYPE_WIFI)) {
                    WifiInfo wifiInfo = wire.parseFrom(data.byteData, WifiInfo.class);
                    builder.wifi_list.add(wifiInfo);
                } else if (data.type.equals(TYPE_GPS)) {
                    GpsInfo gpsInfo = wire.parseFrom(data.byteData, GpsInfo.class);
                    builder.gps_list.add(gpsInfo);
                } else if (data.type.equals(TYPE_EXTRA)) {
                    ExtraLocInfo extraLocInfo = wire.parseFrom(data.byteData, ExtraLocInfo.class);
                    builder.extra_loc_list.add(extraLocInfo);
                } else if (data.type.equals(TYPE_ENV)) {
                    EnvInfo envInfo = wire.parseFrom(data.byteData, EnvInfo.class);
                    builder.env_list.add(envInfo);
                }
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
        return builder.build().toByteArray();
    }

    /**
     * 将多条数据插入数据库，用于上传失败时的重新写入
     * 
     * @param dataList
     *            数据列表
     */
    /* package */synchronized void insertData(ArrayList<DataWraper> dataList) {

        if(getDBSize() >= 10*3000) { // 数据库过大，清空本地数据
            mDBHelper.recreateTable(mDB);
        }

        mDB.beginTransaction();
        try {
            for (DataWraper data : dataList) {
                ContentValues cv = new ContentValues();
                cv.put(DBHelper.TIME_STAMP, data.ts);
                cv.put(DBHelper.TYPE, data.type);
                cv.put(DBHelper.BYTE_DATA, data.byteData);
                mDB.insert(DBHelper.TABLE_NAME, null, cv);
                mInnerDBListener.onInsertOldData();
            }
            mDB.setTransactionSuccessful();
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            try {
                mDB.endTransaction();
            } catch (SQLiteFullException e) {}
        }
    }

    /**
     * 插入一条数据
     * 
     * @param type
     *            数据类型（WIFI/GPS/CELL/EXTRA）
     * @param data
     *            数据内容
     */
    private synchronized void insertData(String type, byte[] data) {

        if(getDBSize() >= 10*3000) { // 数据库过大，清空本地数据
            mDBHelper.recreateTable(mDB);
        }

        long ts = System.currentTimeMillis();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.TIME_STAMP, ts);
        cv.put(DBHelper.TYPE, type);
        cv.put(DBHelper.BYTE_DATA, data);
        mDB.insert(DBHelper.TABLE_NAME, null, cv);

        mInnerDBListener.onInsertNewData();
    }

    /**
     * 获取当前数据库（当前只有一张表）的数据条数
     * 
     * @return 数据库的数据条数
     */
    /* packate */synchronized long getDBSize() {
        String sql = "select count(*) from " + DBHelper.TABLE_NAME;
        SQLiteStatement statement = mDB.compileStatement(sql);
        return statement == null ? 0 : statement.simpleQueryForLong();
    }

    /**
     * 检查是否需要写入最早写入时间
     */
    void setEarliestInsertTime() {
        long eTime = TraceManager.getInstance(mContext).getEarliestInsertTime();
        if (eTime == 0) {
            TraceManager.getInstance(mContext).setEarliestInsertTime(System.currentTimeMillis());
        }
    }

    /**
     * 数据库操作的监听
     */
    private DBListener mInnerDBListener = new DBListener() {

        @Override
        public void onInsertNewData() {

            setEarliestInsertTime();

            UploadManager.getInstance(mContext).checkUpload();
            
            if (mDBListener != null) {
                mDBListener.onInsertNewData();
            }
        }

        @Override
        public void onInsertOldData() {
            if (mDBListener != null) {
                mDBListener.onInsertOldData();
            }
        }

        @Override
        public void onDeleteData() {
            if (mDBListener != null) {
                mDBListener.onDeleteData();
            }
        }
    };

    public final class DataWraper {
        public long ts;
        public String type;
        public byte[] byteData;
    }

    public interface DBListener {
        public void onInsertNewData();

        public void onInsertOldData();

        public void onDeleteData();
    }
}
