package com.ns.yc.lifehelper.ui.other.safe360;

import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.ui.other.safe360.activity.MobileSafeActivity;
import com.ns.yc.lifehelper.ui.other.safe360.adapter.SafeHomeAdapter;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2016/9/12
 * 描    述：我的笔记本页面
 * 修订历史：
 * ================================================
 */
public class SafeHomeActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.gv_home)
    GridView gvHome;

    @Override
    public int getContentView() {
        return R.layout.activity_safe_home;
    }

    @Override
    public void initView() {
        initToolBar();
        initGridView();
    }

    private void initToolBar() {
        toolbarTitle.setText("手机卫士");
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }

    private void initGridView() {
        SafeHomeAdapter adapter = new SafeHomeAdapter(this);
        gvHome.setAdapter(adapter);
        gvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:

                        break;
                    case 1:
                        startActivity(MobileSafeActivity.class);
                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                }
            }
        });
    }


}
