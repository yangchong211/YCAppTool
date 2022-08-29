package com.yc.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.yc.activitymanager.ActivityLifecycleListener;
import com.yc.activitymanager.ActivityManager;
import com.yc.apploglib.AppLogHelper;
import com.yc.apploglib.config.AppLogFactory;
import com.yc.apploglib.printer.AbsPrinter;
import com.yc.apppermission.PermissionGroup;
import com.yc.apppermission.PermissionHelper;
import com.yc.apppermission.PermissionUtils;
import com.yc.appprocesslib.AppStateLifecycle;
import com.yc.appprocesslib.StateListener;
import com.yc.apprestartlib.RestartAppHelper;
import com.yc.apprestartlib.RestartFactory;
import com.yc.common.reflect.ReflectionActivity;
import com.yc.fragmentmanager.FragmentLifecycleListener;
import com.yc.fragmentmanager.FragmentManager;
import com.yc.intent.log.IntentLogger;
import com.yc.store.BaseDataCache;
import com.yc.store.StoreToolHelper;
import com.yc.toastutils.ToastUtils;
import com.yc.toolutils.AppLogUtils;
import com.yc.toolutils.AppInfoUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommonActivity extends AppCompatActivity implements View.OnClickListener {

    private BaseDataCache dataCache;
    private BaseDataCache mmkvCache;
    private BaseDataCache memoryCache;
    private BaseDataCache lruMemoryCache;
    private BaseDataCache lruDiskCache;

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
        AppStateLifecycle.getInstance().registerStateListener(stateListener);
        FragmentManager.Companion.getInstance().registerActivityLifecycleListener(this,lifecycleListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppStateLifecycle.getInstance().unregisterStateListener(stateListener);
        FragmentManager.Companion.getInstance().unregisterActivityLifecycleListener(this,lifecycleListener);
    }

    private final StateListener stateListener = new StateListener() {
        @Override
        public void onInForeground() {
            AppLogUtils.i("app common state in 前台");
        }

        @Override
        public void onInBackground() {
            AppLogUtils.i("app common state in 后台");
        }
    };

    private final FragmentLifecycleListener lifecycleListener = new FragmentLifecycleListener() {
        @Override
        public void onFragmentCreated(@NotNull androidx.fragment.app.FragmentManager fm, @NotNull Fragment f, @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
            super.onFragmentCreated(fm, f, savedInstanceState);
        }

        @Override
        public void onFragmentDestroyed(@NotNull androidx.fragment.app.FragmentManager fm, @NotNull Fragment f) {
            super.onFragmentDestroyed(fm, f);
        }
    };

    private void initCache() {
        dataCache = StoreToolHelper.getInstance().getSpCache();
        mmkvCache = StoreToolHelper.getInstance().getMmkvDiskCache();
        memoryCache = StoreToolHelper.getInstance().getMemoryCache();
        lruMemoryCache = StoreToolHelper.getInstance().getLruMemoryCache();
        lruDiskCache = StoreToolHelper.getInstance().getLruDiskCache();
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

        findViewById(R.id.btn_restart1).setOnClickListener(this);
        findViewById(R.id.btn_restart2).setOnClickListener(this);
        findViewById(R.id.btn_restart3).setOnClickListener(this);
        findViewById(R.id.btn_restart4).setOnClickListener(this);

        findViewById(R.id.btn_intent).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);

        findViewById(R.id.btn_call).setOnClickListener(this);
        findViewById(R.id.btn_camera).setOnClickListener(this);
        findViewById(R.id.btn_reflection).setOnClickListener(this);
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
        }  else if (id == R.id.btn_sp4) {
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
        } else if (id == R.id.btn_restart1) {
            RestartAppHelper.restartApp(this,RestartFactory.SERVICE);
        }else if (id == R.id.btn_restart2) {
            RestartAppHelper.restartApp(this,RestartFactory.ALARM);
        }else if (id == R.id.btn_restart3) {
            RestartAppHelper.restartApp(this,RestartFactory.LAUNCHER);
        }else if (id == R.id.btn_restart4) {
            RestartAppHelper.restartApp(this,RestartFactory.MANIFEST);
        }else if (id == R.id.btn_intent) {
            intentLog();
        } else if (id == R.id.btn_4) {

        } else if (id == R.id.btn_call) {
            boolean hasPermissions = PermissionHelper.getInstance().hasPermissions(this, PermissionGroup.PHONE);
            if (hasPermissions){
                ToastUtils.showRoundRectToast("打电话");
            } else {
                PermissionHelper.getInstance().requestPermission(this, PermissionGroup.PHONE, 100, new PermissionHelper.PermissionResultListener() {
                    @Override
                    public void onSuccess(int requestCode) {
                        ToastUtils.showRoundRectToast("权限申请成功");
                    }

                    @Override
                    public void onDenied(@NonNull @NotNull List<String> deniedList) {
                        ToastUtils.showRoundRectToast("权限申请拒绝");
                    }

                    @Override
                    public void onCancel(@NonNull @NotNull List<String> cancelList) {
                        ToastUtils.showRoundRectToast("权限申请取消");
                    }
                });
            }
        }else if (id == R.id.btn_camera) {
            boolean granted = PermissionUtils.isGranted(this, PermissionGroup.CAMERA);
            if (granted){
                ToastUtils.showRoundRectToast("打开相机");
            } else {
                PermissionUtils permission = PermissionUtils.permission(this, PermissionGroup.CAMERA);
                permission.callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        ToastUtils.showRoundRectToast("权限申请成功");
                    }

                    @Override
                    public void onDenied() {
                        ToastUtils.showRoundRectToast("权限申请拒绝");
                    }
                });
                permission.request(this);
            }
        } else if (id == R.id.btn_reflection){
            startActivity(new Intent(this, ReflectionActivity.class));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.getInstance().onPermissionResult(this,requestCode,permissions,grantResults);
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
                this, AppInfoUtils.getAppPackageName(),null);
    }

    private void sp1() {
        dataCache.saveBoolean("cacheKey1",true);
        dataCache.saveFloat("cacheKey2",2.0f);
        dataCache.saveInt("cacheKey3",3);
        dataCache.saveLong("cacheKey4",4);
        dataCache.saveString("cacheKey5","doubi5");
        dataCache.saveDouble("cacheKey6",5.20);
    }

    private void sp2() {
        boolean data1 = dataCache.readBoolean("cacheKey1", false);
        float data2 = dataCache.readFloat("cacheKey2", 0);
        int data3 = dataCache.readInt("cacheKey3", 0);
        long data4 = dataCache.readLong("cacheKey4", 0);
        String data5 = dataCache.readString("cacheKey5", "");
        double data6 = dataCache.readDouble("cacheKey6", 0.0);
        AppLogHelper.d("取数据 cacheKey1: ",data1);
        AppLogHelper.d("取数据 cacheKey2: ",data2);
        AppLogHelper.d("取数据 cacheKey3: ",data3);
        AppLogHelper.d("取数据 cacheKey4: ",data4);
        AppLogHelper.d("取数据 cacheKey5: ",data5);
        AppLogHelper.d("取数据 cacheKey6: ",data6);
    }

    private void mmvk1() {
        mmkvCache.saveBoolean("spkey1",true);
        mmkvCache.saveFloat("spkey2",2.0f);
        mmkvCache.saveInt("spkey3",3);
        mmkvCache.saveLong("spkey4",4);
        mmkvCache.saveString("spkey5","doubi5");
        mmkvCache.saveDouble("spkey6",5.20);
    }

    private void mmvk2() {
        boolean spkey1 = mmkvCache.readBoolean("spkey1", false);
        float spkey2 = mmkvCache.readFloat("spkey2", 0);
        int spkey3 = mmkvCache.readInt("spkey3", 0);
        long spkey4 = mmkvCache.readLong("spkey4", 0);
        String spkey5 = mmkvCache.readString("spkey5", "");
        double spkey6 = mmkvCache.readDouble("spkey6", 0.0);
        AppLogHelper.d("mmkvCache取数据 spkey1: ",spkey1);
        AppLogHelper.d("mmkvCache取数据 spkey2: ",spkey2);
        AppLogHelper.d("mmkvCache取数据 spkey3: ",spkey3);
        AppLogHelper.d("mmkvCache取数据 spkey4: ",spkey4);
        AppLogHelper.d("mmkvCache取数据 spkey5: ",spkey5);
        AppLogHelper.d("mmkvCache取数据 spkey6: ",spkey6);
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
        lruDiskCache.saveDouble("spkey6",5.20);
    }

    private void disk2() {
        boolean spkey1 = lruDiskCache.readBoolean("spkey1", false);
        float spkey2 = lruDiskCache.readFloat("spkey2", 0);
        int spkey3 = lruDiskCache.readInt("spkey3", 0);
        long spkey4 = lruDiskCache.readLong("spkey4", 0);
        String spkey5 = lruDiskCache.readString("spkey5", "");
        double spkey6 = lruDiskCache.readDouble("spkey6", 0.0);
        AppLogHelper.d("disk取数据 spkey1: ",spkey1);
        AppLogHelper.d("disk取数据 spkey2: ",spkey2);
        AppLogHelper.d("disk取数据 spkey3: ",spkey3);
        AppLogHelper.d("disk取数据 spkey4: ",spkey4);
        AppLogHelper.d("disk取数据 spkey5: ",spkey5);
        AppLogHelper.d("disk取数据 spkey6: ",spkey6);
    }

    private void disk3() {
        lruDiskCache.clearData();
    }


    private void intentLog() {
        Intent intent = getIntent();
        //打印intent所有的数据
        IntentLogger.print("intent log all: ", intent);
        //打印intent中component
        IntentLogger.printComponentName("intent log component : " , intent);
        //打印intent中extras参数
        IntentLogger.printExtras("intent log extras : ", intent);
        //打印intent中flags参数
        IntentLogger.printFlags("intent log flags : ", intent);
    }

    private void activityManager(){
        //退出应用程序
        ActivityManager.getInstance().appExist();
        //查找指定的Activity
        Activity commonActivity = ActivityManager.getInstance().get(CommonActivity.class);
        //判断界面Activity是否存在
        boolean exist = ActivityManager.getInstance().isExist(CommonActivity.class);
        //移除栈顶的activity
        ActivityManager.getInstance().pop();
        //结束所有Activity
        ActivityManager.getInstance().finishAll();
        //结束指定的Activity
        ActivityManager.getInstance().finish(CommonActivity.this);
        //判断activity任务栈是否为空
        ActivityManager.getInstance().isEmpty();
        //获取栈顶的Activity
        Activity activity = ActivityManager.getInstance().peek();
        //判断activity是否处于栈顶
        ActivityManager.getInstance().isActivityTop(this,"MainActivity");
        //添加 activity 入栈
        ActivityManager.getInstance().add(CommonActivity.this);
        //移除 activity 出栈
        ActivityManager.getInstance().remove(CommonActivity.this);
        //监听某个activity的生命周期，完全解耦合
        ActivityManager.getInstance().registerActivityLifecycleListener(CommonActivity.class, new ActivityLifecycleListener() {
            @Override
            public void onActivityCreated(@Nullable Activity activity, Bundle savedInstanceState) {
                super.onActivityCreated(activity, savedInstanceState);
            }

            @Override
            public void onActivityStarted(@Nullable Activity activity) {
                super.onActivityStarted(activity);
            }
        });
        //移除某个activity的生命周期，完全解耦合
        //ActivityManager.getInstance().registerActivityLifecycleListener(CommonActivity.this,listener);
    }

}
