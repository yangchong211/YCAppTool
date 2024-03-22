package com.yc.monitorphone;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.yc.toolutils.context.AppToolUtils;
import com.yc.localelib.locale.service.LocaleService;
import com.yc.privateserver.UserPrivacyInit;
import com.yc.toolutils.permission.PermissionUtils;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2018/5/6
 *     desc  : 查看手机信息
 *     revise:
 * </pre>
 */
public class MonitorPhoneActivity extends AppCompatActivity {

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, MonitorPhoneActivity.class);
            context.startActivity(target);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            fragmentTransaction.replace(android.R.id.content,new MonitorPhoneFragment());
            try {
                fragmentTransaction.commit();
            } catch (Exception e){
                fragmentTransaction.commitAllowingStateLoss();
            }
        }
        LocaleService.getInstance().init(AppToolUtils.getApp());
        UserPrivacyInit.installApp(AppToolUtils.getApp());
        UserPrivacyInit.setIsInitUserPrivacy(true,BuildConfig.DEBUG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!PermissionUtils.isGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ,Manifest.permission.READ_EXTERNAL_STORAGE)){
            PermissionUtils.permission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ,Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

}
