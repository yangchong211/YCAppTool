package com.yc.ycnotification.service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class MyService2 extends BaseService {

    public MyService2(String name) {
        //super(name);
        super("MyService2");
        Log.d("Service2","MyService MyService : " +name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("Service2","MyService onHandleIntent");
    }

    @Override
    protected boolean canStopSelf() {
        return false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Service2","MyService onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Service2","MyService onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Service2","MyService onDestroy");
    }

    public static void startRiderService(Context context) {
        Log.d("Service2","MyService startRiderService");
        Intent intent = new Intent(context, MyForegroundService2.class);
        ContextCompat.startForegroundService(context, intent);
    }

    public static void stopRiderService(Context context) {
        Log.d("Service2","MyService stopRiderService");
        Intent intent = new Intent(context, MyForegroundService2.class);
        context.stopService(intent);
    }

}
