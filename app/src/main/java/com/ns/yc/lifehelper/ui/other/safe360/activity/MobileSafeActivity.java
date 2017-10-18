package com.ns.yc.lifehelper.ui.other.safe360.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.ui.other.safe360.adapter.MobileSafeAdapter;
import com.ns.yc.lifehelper.ui.weight.itemLine.RecycleViewItemLine;
import com.ns.yc.ycutilslib.loadingDialog.ViewLoading;
import com.pedaily.yc.ycdialoglib.ToastUtil;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2016/9/11
 * 描    述：手机通讯卫士页面
 * 修订历史：
 * ================================================
 */
public class MobileSafeActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    private MobileSafeActivity activity;
    private MobileSafeAdapter adapter;
    private ViewLoading mLoading;
    private EditText etPhoneNumber;
    private CheckBox cbCallIntercept;
    private CheckBox cbSmsIntercept;

    @Override
    public int getContentView() {
        return R.layout.base_easy_recycle_list;
    }

    @Override
    public void initView() {
        activity = MobileSafeActivity.this;
        initToolBar();
        initLoading();
        initRecycleView();
    }

    private void initToolBar() {
        toolbarTitle.setText("通讯卫士");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initLoading() {
        // 添加Loading
        mLoading = new ViewLoading(this, Constant.loadingStyle) {
            @Override
            public void loadCancel() {

            }
        };
        if (!mLoading.isShowing()) {
            mLoading.show();
        }
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {
        getData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mobile_menu_safe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_manually_add:
                showAddDialog(activity);
                break;
            case R.id.m_add_from_contacts:
                AddContacts();
                break;
            case R.id.m_add_from_phone_history:

                break;
            case R.id.m_add_from_sms_history:

                break;
        }
        return true;
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new MobileSafeAdapter(activity);
        final RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#f5f5f7"));
        recyclerView.addItemDecoration(line);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 获取数据，刷新页面
     */
    private void getData() {


        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (mLoading != null && mLoading.isShowing()) {
                    mLoading.dismiss();
                }
            }
        }, 2000);
    }

    /**
     * 手动添加黑名单
     * @param activity
     */
    private void showAddDialog(final MobileSafeActivity activity) {
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_mobile_safe_add, null);
        etPhoneNumber = (EditText) view.findViewById(R.id.et_phone_number);
        cbCallIntercept = (CheckBox) view.findViewById(R.id.cb_call_intercept);
        cbSmsIntercept = (CheckBox) view.findViewById(R.id.cb_sms_intercept);
        final String number = etPhoneNumber.getText().toString().trim();


        new AlertDialog.Builder(this)
                .setTitle(R.string.manually_add)
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(TextUtils.isEmpty(number)){
                            ToastUtil.showToast(activity,"输入内容不能为空");
                            return;
                        }
                        AddDataToSQL(number);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }


    /**
     * 通过联系人添加
     */
    private void AddContacts() {

    }

    /**
     * 将号码添加到数据库
     * @param number
     */
    private void AddDataToSQL(String number) {
        if (!cbCallIntercept.isChecked() && !cbSmsIntercept.isChecked()) {
            ToastUtil.showToast(activity,"添加未生效，您没选择任何拦截");
            return;
        }

    }

}
