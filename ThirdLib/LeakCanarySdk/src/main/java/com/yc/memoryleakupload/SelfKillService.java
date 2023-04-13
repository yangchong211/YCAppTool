package com.yc.memoryleakupload;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;


public class SelfKillService extends Service {

    public static final String TAG = "SelfKillService";

    public static void kill(Context context){
        Intent intent = new Intent(context, SelfKillService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanaryUtils.debug(TAG,"kill self");
        System.exit(0);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
