package com.ycbjie.todo.ui.activity;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.todo.R;
import com.ycbjie.todo.data.BackupManager;
import com.ycbjie.todo.ui.fragment.WorkSettingFragment;

import java.util.Calendar;

import rx.functions.Action1;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/28.
 * 描    述：此版块训练dagger2+MVP
 * 修订历史：
 * ================================================
 */
public class WorkSettingActivity extends BaseActivity implements View.OnClickListener {

    FrameLayout llTitleMenu;
    TextView toolbarTitle;
    FrameLayout fl;
    private ProgressDialog mProgressDialog;
    private int mCurrIndex;

    @Override
    public int getContentView() {
        return R.layout.base_view_fragment;
    }


    @Override
    public void initView() {
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        fl = findViewById(R.id.fl);

        initToolBar();
    }


    private void initToolBar() {
        toolbarTitle.setText("Work设置中心");
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }


    @Override
    public void initData() {
        WorkSettingFragment settingsFragment = new WorkSettingFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fl, settingsFragment)
                .commit();
        Intent intent = getIntent();
        mCurrIndex = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        if (intent != null) {
            mCurrIndex = intent.getIntExtra(Constant.INTENT_EXTRA_SWITCH_TO_INDEX, mCurrIndex);
        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("请稍后");
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_title_menu) {/*Intent intent = new Intent(this, WorkDoActivity.class);
                intent.putExtra(Constant.INTENT_EXTRA_SWITCH_TO_INDEX, mCurrIndex);
                startActivity(intent);*/
            finish();

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent intent = new Intent(this, WorkDoActivity.class);
        intent.putExtra(Constant.INTENT_EXTRA_SWITCH_TO_INDEX, mCurrIndex);
        startActivity(intent);*/
        finish();
    }



    private int option = 0;
    public void backupClick() {
        option = 0;
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setSingleChoiceItems(new String[]{"备份数据", "导出文本"}, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        option = which;
                    }
                })
                .setTitle("请选择操作")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (option) {
                            case 0:
                                backupDataBase();
                                break;
                            case 1:
                                exportToFile();
                                break;
                        }
                    }
                })
                .create();
        dialog.show();
    }


    private void backupDataBase() {
        mProgressDialog.show();
        BackupManager.backup()
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        mProgressDialog.dismiss();
                        ToastUtils.showShort(aBoolean?"备份完成" : "备份失败");
                    }
                });
    }


    private void exportToFile() {
        mProgressDialog.show();
        BackupManager.export()
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        mProgressDialog.dismiss();
                        ToastUtils.showShort(aBoolean?"导出完成" : "导出失败");
                    }
                });
    }



    public void recoveryClick() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("警告")
                .setMessage("还原内容将覆盖当前所有内容, 您确定要继续进行此操作? 还原成功后, 应用将自动关闭")
                .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recoveryDataBase();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }


    private void recoveryDataBase() {
        mProgressDialog.show();
        BackupManager.recovery()
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        mProgressDialog.dismiss();
                        ToastUtils.showShort(aBoolean?"还原成功" : "还原失败");
                        if (aBoolean){
                            System.exit(0);
                        }
                    }
                });
    }



}
