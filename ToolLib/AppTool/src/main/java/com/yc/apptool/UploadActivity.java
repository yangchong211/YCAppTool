package com.yc.apptool;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.yc.apppermission.PermissionUtils;
import com.yc.monitorfilelib.FileExplorerActivity;
import com.yc.toastutils.BuildConfig;
import com.yc.toastutils.ToastUtils;
import com.yc.ycupdatelib.AppUpdateUtils;
import com.yc.ycupdatelib.PermissionDialog;
import com.yc.ycupdatelib.PermissionListener;
import com.yc.ycupdatelib.UpdateFragment;



public class UploadActivity extends AppCompatActivity {

    //这个是你的包名
    private static final String apkName = "yilu.apk";
    private static final String firstUrl = "http://ucan.25pp.com/Wandoujia_web_seo_baidu_homepage.apk";
    private static final String[] mPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_app);
        PermissionDialog.setPermissionDialog(new PermissionDialog() {
            @Override
            protected void showStorageDialog(PermissionListener listener) {
                listener.dialogClickSure();
            }
        });


        findViewById(R.id.tv_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置自定义下载文件路径
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
                        false,firstUrl,apkName,desc, BuildConfig.APPLICATION_ID,null);
            }
        });

        findViewById(R.id.tv_7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = AppUpdateUtils.clearDownload(UploadActivity.this,apkName);
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
                String  desc = getResources().getString(R.string.update_content_info);
                /*
                 * @param isForceUpdate             是否强制更新
                 * @param desc                      更新文案
                 * @param url                       下载链接
                 * @param apkFileName               apk下载文件路径名称
                 * @param packName                  包名
                 */
                UpdateFragment.showFragment(UploadActivity.this,
                        false,firstUrl,apkName,desc,BuildConfig.APPLICATION_ID,"c8b38aeada5eae0aca63831fde204cad");
            }
        });

        findViewById(R.id.tv_8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileExplorerActivity.startActivity(UploadActivity.this);
            }
        });
    }



}
