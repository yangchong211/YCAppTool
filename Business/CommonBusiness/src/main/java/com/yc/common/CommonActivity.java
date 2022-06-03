package com.yc.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.activitymanager.ActivityLifecycleListener;
import com.yc.activitymanager.ActivityManager;
import com.yc.apploglib.AppLogHelper;
import com.yc.apploglib.config.AppLogFactory;
import com.yc.apploglib.printer.AbsPrinter;
import com.yc.common.cache.CacheToolHelper;
import com.yc.store.BaseDataCache;
import com.yc.toolutils.AppUtils;

public class CommonActivity extends AppCompatActivity implements View.OnClickListener {

    private BaseDataCache spCache;
    private BaseDataCache mmkvCache;
    private BaseDataCache memoryCache;
    private BaseDataCache lruMemoryCache;
    private BaseDataCache lruDiskCache;
    private BaseDataCache fastSpCache;

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, CommonActivity.class);
            //target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(target);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_main);
        init();
        initCache();
    }

    private void initCache() {
        spCache = CacheToolHelper.getInstance().getSpCache();
        mmkvCache = CacheToolHelper.getInstance().getMkkvDiskCache();
        memoryCache = CacheToolHelper.getInstance().getMemoryCache();
        lruMemoryCache = CacheToolHelper.getInstance().getLruMemoryCache();
        lruDiskCache = CacheToolHelper.getInstance().getLruDiskCache();
        fastSpCache = CacheToolHelper.getInstance().getFastSpCache();
    }

    private void init() {
        findViewById(R.id.btn_log1).setOnClickListener(this);
        findViewById(R.id.btn_log2).setOnClickListener(this);
        findViewById(R.id.btn_activity).setOnClickListener(this);
        findViewById(R.id.btn_sp1).setOnClickListener(this);
        findViewById(R.id.btn_sp2).setOnClickListener(this);
        findViewById(R.id.btn_sp3).setOnClickListener(this);
        findViewById(R.id.btn_sp4).setOnClickListener(this);

        findViewById(R.id.btn_mkkv1).setOnClickListener(this);
        findViewById(R.id.btn_mkkv2).setOnClickListener(this);
        findViewById(R.id.btn_mkkv3).setOnClickListener(this);

        findViewById(R.id.btn_disk1).setOnClickListener(this);
        findViewById(R.id.btn_disk2).setOnClickListener(this);
        findViewById(R.id.btn_disk3).setOnClickListener(this);

        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_log1) {
            testLog1();
        } else if (id == R.id.btn_log2) {
            testLog2();
        } else if (id == R.id.btn_activity) {
            testActivity();
        }  else if (id == R.id.btn_sp1) {
            sp1();
        }  else if (id == R.id.btn_sp2) {
            sp2();
        }  else if (id == R.id.btn_sp3) {
            sp3();
        }  else if (id == R.id.btn_sp4) {
            sp4();
        }  else if (id == R.id.btn_mkkv1) {
            mmvk1();
        } else if (id == R.id.btn_mkkv2) {
            mmvk2();
        } else if (id == R.id.btn_mkkv3) {
            mmvk3();
        }  else if (id == R.id.btn_disk1) {
            disk1();
        } else if (id == R.id.btn_disk2) {
            disk2();
        } else if (id == R.id.btn_disk3) {
            disk3();
        }else if (id == R.id.btn_3) {

        } else if (id == R.id.btn_4) {

        }
    }

    private void testLog1() {
        AppLogHelper.v("MainActivity: ","verbose log");
        AppLogHelper.v("MainActivity log verbose no tag");
        AppLogHelper.d("MainActivity: ","debug log");
        AppLogHelper.d("MainActivity log info no tag");
        AppLogHelper.i("MainActivity: ","info log");
        AppLogHelper.i("MainActivity log info no tag");
        AppLogHelper.w("MainActivity: ","warn log");
        AppLogHelper.w("MainActivity log warn no tag");
        AppLogHelper.e("MainActivity: ","error log");
        AppLogHelper.e("MainActivity log error no tag");
    }

    private void testLog2() {
        //当然，如果不满足你的要求，开发者可以自己实现日志输出的形式。
        AppLogFactory.addPrinter(new AbsPrinter() {
            @NonNull
            @Override
            public String name() {
                return "yc";
            }

            @Override
            public void println(int level, String tag, String message, Throwable tr) {
                //todo 这块全局回调日志，你可以任意实现自定义操作
            }
        });
        AppLogHelper.v("MainActivity: ","verbose log");
        AppLogHelper.v("MainActivity log verbose no tag");
        AppLogHelper.d("MainActivity: ","debug log");
        AppLogHelper.d("MainActivity log info no tag");
        AppLogHelper.i("MainActivity: ","info log");
        AppLogHelper.i("MainActivity log info no tag");
        AppLogHelper.w("MainActivity: ","info log");
        AppLogHelper.w("MainActivity log info no tag");
    }

    private void testActivity() {
        //添加 activity
        ActivityManager.getInstance().add(this);
        //移除 activity
        ActivityManager.getInstance().remove(this);
        //结束指定的Activity
        ActivityManager.getInstance().finish(this);
        //结束所有Activity
        ActivityManager.getInstance().finishAll();
        //退出应用程序。先回退到桌面，然后在杀死进程
        ActivityManager.getInstance().appExist();
        //这个是监听目标Activity的生命周期变化
        ActivityManager.getInstance().registerActivityLifecycleListener(
                CommonActivity.class,new ActivityLifecycleListener(){
                    @Override
                    public void onActivityCreated(@Nullable Activity activity, Bundle savedInstanceState) {
                        super.onActivityCreated(activity, savedInstanceState);
                    }
                });
        //移除栈顶的activity
        ActivityManager.getInstance().pop();
        //获取栈顶的Activity
        Activity activity = ActivityManager.getInstance().peek();
        //判断activity是否处于栈顶
        ActivityManager.getInstance().isActivityTop(this,"MainActivity");
        //返回AndroidManifest.xml中注册的所有Activity的class
        ActivityManager.getInstance().getActivitiesClass(
                this, AppUtils.getAppPackageName(),null);
    }

    private void sp1() {
        spCache.saveBoolean("spkey1",true);
        spCache.saveFloat("spkey2",2.0f);
        spCache.saveInt("spkey3",3);
        spCache.saveLong("spkey4",4);
        spCache.saveString("spkey5","doubi5");
    }

    private void sp2() {
        boolean spkey1 = spCache.readBoolean("spkey1", false);
        float spkey2 = spCache.readFloat("spkey2", 0);
        int spkey3 = spCache.readInt("spkey3", 0);
        long spkey4 = spCache.readLong("spkey4", 0);
        String spkey5 = spCache.readString("spkey5", "");
        AppLogHelper.d("sp取数据 spkey1: ",spkey1);
        AppLogHelper.d("sp取数据 spkey2: ",spkey2);
        AppLogHelper.d("sp取数据 spkey3: ",spkey3);
        AppLogHelper.d("sp取数据 spkey4: ",spkey4);
        AppLogHelper.d("sp取数据 spkey5: ",spkey5);
    }

    private void sp3() {
        fastSpCache.saveBoolean("spkey1",true);
        fastSpCache.saveFloat("spkey2",2.0f);
        fastSpCache.saveInt("spkey3",3);
        fastSpCache.saveLong("spkey4",4);
        fastSpCache.saveString("spkey5","doubi5");
    }

    private void sp4() {
        boolean spkey1 = fastSpCache.readBoolean("spkey1", false);
        float spkey2 = fastSpCache.readFloat("spkey2", 0);
        int spkey3 = fastSpCache.readInt("spkey3", 0);
        long spkey4 = fastSpCache.readLong("spkey4", 0);
        String spkey5 = fastSpCache.readString("spkey5", "");
        AppLogHelper.d("fastSp取数据 spkey1: ",spkey1);
        AppLogHelper.d("fastSp取数据 spkey2: ",spkey2);
        AppLogHelper.d("fastSp取数据 spkey3: ",spkey3);
        AppLogHelper.d("fastSp取数据 spkey4: ",spkey4);
        AppLogHelper.d("fastSp取数据 spkey5: ",spkey5);
    }

    private void mmvk1() {
        mmkvCache.saveBoolean("spkey1",true);
        mmkvCache.saveFloat("spkey2",2.0f);
        mmkvCache.saveInt("spkey3",3);
        mmkvCache.saveLong("spkey4",4);
        mmkvCache.saveString("spkey5","doubi5");
    }

    private void mmvk2() {
        boolean spkey1 = mmkvCache.readBoolean("spkey1", false);
        float spkey2 = mmkvCache.readFloat("spkey2", 0);
        int spkey3 = mmkvCache.readInt("spkey3", 0);
        long spkey4 = mmkvCache.readLong("spkey4", 0);
        String spkey5 = mmkvCache.readString("spkey5", "");
        AppLogHelper.d("mmkvCache取数据 spkey1: ",spkey1);
        AppLogHelper.d("mmkvCache取数据 spkey2: ",spkey2);
        AppLogHelper.d("mmkvCache取数据 spkey3: ",spkey3);
        AppLogHelper.d("mmkvCache取数据 spkey4: ",spkey4);
        AppLogHelper.d("mmkvCache取数据 spkey5: ",spkey5);
    }

    private void mmvk3() {
        mmkvCache.clearData();
    }

    private void disk1() {
        lruDiskCache.saveBoolean("spkey1",true);
        lruDiskCache.saveFloat("spkey2",2.0f);
        lruDiskCache.saveInt("spkey3",3);
        lruDiskCache.saveLong("spkey4",4);
        lruDiskCache.saveString("spkey5","doubi5");
    }

    private void disk2() {
        boolean spkey1 = lruDiskCache.readBoolean("spkey1", false);
        float spkey2 = lruDiskCache.readFloat("spkey2", 0);
        int spkey3 = lruDiskCache.readInt("spkey3", 0);
        long spkey4 = lruDiskCache.readLong("spkey4", 0);
        String spkey5 = lruDiskCache.readString("spkey5", "");
        AppLogHelper.d("disk取数据 spkey1: ",spkey1);
        AppLogHelper.d("disk取数据 spkey2: ",spkey2);
        AppLogHelper.d("disk取数据 spkey3: ",spkey3);
        AppLogHelper.d("disk取数据 spkey4: ",spkey4);
        AppLogHelper.d("disk取数据 spkey5: ",spkey5);
    }

    private void disk3() {
        lruDiskCache.clearData();
    }
}
