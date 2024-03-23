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

import com.yc.activitymanager.activity.AbsLifecycleListener;
import com.yc.activitymanager.activity.ActivityManager;
import com.yc.apploglib.AppLogHelper;
import com.yc.apploglib.config.AppLogFactory;
import com.yc.apploglib.printer.AbsPrinter;
import com.yc.toolutils.permission.PermissionGroup;
import com.yc.toolutils.permission.PermissionHelper;
import com.yc.toolutils.permission.PermissionResultListener;
import com.yc.appprocesslib.AppStateLifecycle;
import com.yc.appprocesslib.OnStateListener;
import com.yc.apprestartlib.RestartManager;
import com.yc.common.cache.CacheActivity;
import com.yc.common.encypt.EncyptActivity;
import com.yc.common.file.FileActivity;
import com.yc.common.image.ImageActivity;
import com.yc.common.reflect.ReflectionActivity;
import com.yc.common.tab.VpTabActivity;
import com.yc.common.vp.ViewPagerActivity;
import com.yc.common.vp2.ViewPager2Activity;
import com.yc.activitymanager.fragment.FragmentLifecycleListener;
import com.yc.activitymanager.fragment.FragmentManager;
import com.yc.toolutils.intent.IntentLogger;
import com.yc.monitorfilelib.FileExplorerActivity;
import com.yc.toastutils.ToastUtils;
import com.yc.toolutils.system.AppLogUtils;
import com.yc.toolutils.appinfo.AppInfoUtils;
import com.yc.toolutils.bytes.ByteTranslateUtils;
import com.yc.toolutils.permission.PermissionUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommonActivity extends AppCompatActivity implements View.OnClickListener {


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
        AppStateLifecycle.getInstance().registerStateListener(stateListener);
        FragmentManager.Companion.getInstance().registerActivityLifecycleListener(this,lifecycleListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppStateLifecycle.getInstance().unregisterStateListener(stateListener);
        FragmentManager.Companion.getInstance().unregisterActivityLifecycleListener(this,lifecycleListener);
    }

    private final OnStateListener stateListener = new OnStateListener() {
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
        findViewById(R.id.btn_file).setOnClickListener(this);
        findViewById(R.id.btn_vp2).setOnClickListener(this);
        findViewById(R.id.btn_vp).setOnClickListener(this);
        findViewById(R.id.btn_tab).setOnClickListener(this);
        findViewById(R.id.btn_encrypt).setOnClickListener(this);
        findViewById(R.id.btn_image).setOnClickListener(this);

        testUtils();
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
            startActivity(new Intent(this, CacheActivity.class));
        }  else if (id == R.id.btn_sp2) {
        }  else if (id == R.id.btn_sp3) {
            FileExplorerActivity.startActivity(this);
        }  else if (id == R.id.btn_sp4) {
        }  else if (id == R.id.btn_mkkv1) {
        } else if (id == R.id.btn_mkkv2) {
        } else if (id == R.id.btn_mkkv3) {
        }  else if (id == R.id.btn_disk1) {
        } else if (id == R.id.btn_disk2) {
        } else if (id == R.id.btn_disk3) {
        } else if (id == R.id.btn_restart1) {
            RestartManager.getInstance().restartApp(this, RestartManager.ALARM);
        }else if (id == R.id.btn_restart2) {
            RestartManager.getInstance().restartApp(this, RestartManager.SERVICE);
        }else if (id == R.id.btn_restart3) {
            RestartManager.getInstance().restartApp(this, RestartManager.LAUNCHER);
        }else if (id == R.id.btn_restart4) {
            RestartManager.getInstance().restartApp(this, RestartManager.MANIFEST);
        }else if (id == R.id.btn_intent) {
            intentLog();
        } else if (id == R.id.btn_4) {

        } else if (id == R.id.btn_call) {
            boolean hasPermissions = PermissionHelper.getInstance().hasPermissions(this, PermissionGroup.PHONE);
            if (hasPermissions){
                ToastUtils.showRoundRectToast("打电话");
            } else {
                PermissionHelper.getInstance().requestPermission(this, PermissionGroup.PHONE, 100, new PermissionResultListener() {
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
        } else if (id == R.id.btn_file){
            startActivity(new Intent(this, FileActivity.class));
        } else if (id == R.id.btn_vp2){
            startActivity(new Intent(this, ViewPager2Activity.class));
        }  else if (id == R.id.btn_vp){
            startActivity(new Intent(this, ViewPagerActivity.class));
        } else if (id == R.id.btn_tab){
            startActivity(new Intent(this, VpTabActivity.class));
        } else if (id == R.id.btn_encrypt){
            startActivity(new Intent(this, EncyptActivity.class));
        } else if (id == R.id.btn_image){
            startActivity(new Intent(this, ImageActivity.class));
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
                CommonActivity.class,new AbsLifecycleListener(){
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
        ActivityManager.getInstance().registerActivityLifecycleListener(CommonActivity.class, new AbsLifecycleListener() {
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

    private void testUtils(){
        byte[] bytes = {0x12, 0x34, (byte) 0xAB, (byte) 0xCD};
        String toHex1 = ByteTranslateUtils.bytesToHex1(bytes);
        String toHex2 = ByteTranslateUtils.bytesToHex2(bytes);
        String toHex3 = ByteTranslateUtils.bytesToHex3(bytes);
        //bytesToHex : 1234abcd  ----  1234abcd  ----  1234abcd
        AppLogHelper.d("bytesToHex : " + toHex1 + "  ----  " + toHex2 + "  ----  " + toHex3);
        byte[] bytes1 = ByteTranslateUtils.toBytes1(toHex1);
        String str = "687474703A2F2F77656978696E2E71712E636F6D2F722F39307852536366454170703972594D7139786E6C";
        byte[] bytes2 = ByteTranslateUtils.toBytes1(str);
        String toHex4 = ByteTranslateUtils.bytesToHex1(bytes2);
        AppLogHelper.d("bytesToHex toHex4: " + toHex4);
        byte[] bytes3 = ByteTranslateUtils.toBytes2(str);
        String toHex5 = ByteTranslateUtils.bytesToHex1(bytes3);
        AppLogHelper.d("bytesToHex toHex5: " + toHex5);
    }

}
