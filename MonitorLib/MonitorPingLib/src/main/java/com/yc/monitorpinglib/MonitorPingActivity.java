package com.yc.monitorpinglib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2018/5/6
 *     desc  : ping一下
 *     revise:
 * </pre>
 */
public class MonitorPingActivity extends AppCompatActivity {

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context , String url) {
        try {
            Intent target = new Intent();
            target.setClass(context, MonitorPingActivity.class);
            target.putExtra("url",url);
            context.startActivity(target);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra("url");
        if (savedInstanceState == null){
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            MonitorPingFragment monitorPingFragment = new MonitorPingFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url",url);
            monitorPingFragment.setArguments(bundle);
            fragmentTransaction.replace(android.R.id.content,monitorPingFragment);
            try {
                fragmentTransaction.commit();
            } catch (Exception e){
                fragmentTransaction.commitAllowingStateLoss();
            }
        }
    }

}
