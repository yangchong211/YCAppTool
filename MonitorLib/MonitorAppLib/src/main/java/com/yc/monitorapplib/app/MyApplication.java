package com.yc.monitorapplib.app;

import android.app.Application;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import com.yc.monitorapplib.BuildConfig;
import com.yc.monitorapplib.data.AppItem;
import com.yc.monitorapplib.data.DataManager;
import com.yc.monitorapplib.db.DbHistoryExecutor;
import com.yc.monitorapplib.db.DbIgnoreExecutor;
import com.yc.monitorapplib.service.AppService;
import com.yc.monitorapplib.util.PreferenceManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.init(this);
        getApplicationContext().startService(new Intent(getApplicationContext(), AppService.class));
        DbIgnoreExecutor.init(getApplicationContext());
        DbHistoryExecutor.init(getApplicationContext());
        DataManager.init();
        addDefaultIgnoreAppsToDB();
    }

    private void addDefaultIgnoreAppsToDB() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> mDefaults = new ArrayList<>();
                mDefaults.add("com.android.settings");
                mDefaults.add(BuildConfig.APPLICATION_ID);
                for (String packageName : mDefaults) {
                    AppItem item = new AppItem();
                    item.mPackageName = packageName;
                    item.mEventTime = System.currentTimeMillis();
                    DbIgnoreExecutor.getInstance().insertItem(item);
                }
            }
        }).run();
    }
}
