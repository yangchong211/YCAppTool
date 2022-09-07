package com.yc.apptool;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.yc.apppermission.PermissionUtils;
import com.yc.toastutils.BuildConfig;
import com.yc.toastutils.ToastUtils;
import com.yc.ycupdatelib.AppUpdateUtils;
import com.yc.ycupdatelib.UpdateFragment;

import java.io.File;


public class UploadActivity extends AppCompatActivity {

    //这个是你的包名
    private static final String apkName = "yilu";
    private static final String firstUrl = "http://ucan.25pp.com/Wandoujia_web_seo_baidu_homepage.apk";
    private static final String url = "http://img1.haowmc.com/hwmc/test/android_";
    private static final String[] mPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("MainActivity","onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("MainActivity","onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MainActivity","onPause");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },3000);

        boolean granted = PermissionUtils.isGranted(this,mPermission);
        if(!granted){
            PermissionUtils permission = PermissionUtils.permission(this,mPermission);
            permission.callback(new PermissionUtils.SimpleCallback() {
                @Override
                public void onGranted() {

                }

                @Override
                public void onDenied() {
                    ToastUtils.showRoundRectToast("请允许权限");
                }
            });
            permission.request(this);
        }

        findViewById(R.id.tv_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置自定义下载文件路径
                AppUpdateUtils.APP_UPDATE_DOWN_APK_PATH = "apk" + File.separator + "downApk";
                String  desc = getResources().getString(R.string.update_content_info);
                /*
                 * @param isForceUpdate             是否强制更新
                 * @param desc                      更新文案
                 * @param url                       下载链接
                 * @param apkFileName               apk下载文件路径名称
                 * @param packName                  包名
                 */
                UpdateFragment.showFragment(UploadActivity.this,
                        false,firstUrl,apkName,desc, BuildConfig.APPLICATION_ID,null);
            }
        });


        findViewById(R.id.tv_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  desc = getResources().getString(R.string.update_content_info1);
                UpdateFragment.showFragment(UploadActivity.this,
                        true,firstUrl,apkName,desc, BuildConfig.APPLICATION_ID,null);
            }
        });

        findViewById(R.id.tv_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  desc = getResources().getString(R.string.update_content_info1);
                UpdateFragment.showFragment(UploadActivity.this,
                        false,url,apkName,desc, BuildConfig.APPLICATION_ID,null);
            }
        });

        findViewById(R.id.tv_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = AppUpdateUtils.clearDownload(UploadActivity.this);
                if (b){
                    Toast.makeText(UploadActivity.this,"清除数据成功",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UploadActivity.this,"无数据",Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.tv_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置自定义下载文件路径
                AppUpdateUtils.APP_UPDATE_DOWN_APK_PATH = "apk" + File.separator + "downApk";
                String  desc = getResources().getString(R.string.update_content_info);
                /*
                 * @param isForceUpdate             是否强制更新
                 * @param desc                      更新文案
                 * @param url                       下载链接
                 * @param apkFileName               apk下载文件路径名称
                 * @param packName                  包名
                 */
                UpdateFragment.showFragment(UploadActivity.this,
                        false,firstUrl,apkName,desc,BuildConfig.APPLICATION_ID,"3232");
            }
        });

        findViewById(R.id.tv_6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置自定义下载文件路径
                AppUpdateUtils.APP_UPDATE_DOWN_APK_PATH = "apk" + File.separator + "downApk";
                String  desc = getResources().getString(R.string.update_content_info);
                /*
                 * @param isForceUpdate             是否强制更新
                 * @param desc                      更新文案
                 * @param url                       下载链接
                 * @param apkFileName               apk下载文件路径名称
                 * @param packName                  包名
                 */
                UpdateFragment.showFragment(UploadActivity.this,
                        false,firstUrl,apkName,desc,BuildConfig.APPLICATION_ID,"b291e935d3f5282355192f98306ab489");
            }
        });
    }



}
