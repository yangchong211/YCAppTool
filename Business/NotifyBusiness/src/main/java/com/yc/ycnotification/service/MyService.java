package com.yc.ycnotification.service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class MyService extends BaseService {

    public MyService(String name) {
        //super(name);
        super("MyService");
        Log.d("Service","MyService MyService : " +name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("Service","MyService onHandleIntent");
    }

    @Override
    protected boolean canStopSelf() {
        return false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Service","MyService onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Service","MyService onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Service","MyService onDestroy");
    }

    public static void startRiderService(Context context) {
        Log.d("Service","MyService startRiderService");
        Intent intent = new Intent(context, MyForegroundService.class);
        ContextCompat.startForegroundService(context, intent);
    }

    public static void stopRiderService(Context context) {
        Log.d("Service","MyService stopRiderService");
        Intent intent = new Intent(context, MyForegroundService.class);
        context.stopService(intent);
    }

}
