package com.ns.yc.lifehelper.ui.test.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


public class BankService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BankBinder();
    }
}
