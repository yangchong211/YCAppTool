package com.ns.yc.lifehelper.ui.other.myPicture.view;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;

import butterknife.BindView;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/4
 * 描    述：美图欣赏组
 * 修订历史：
 * ================================================
 */
public class BeautifulGroupActivity extends BaseActivity {

    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.group_toolbar)
    Toolbar groupToolbar;
    private String groupId;
    private int color;
    private BeautifulGroupFragment fragment;
    private int index;

    @Override
    public int getContentView() {
        return R.layout.base_image_group;
    }

    @Override
    public void initView() {
        initToolBar();
        initIntentData();
        initFragment();
    }

    private void initToolBar() {
        setSupportActionBar(groupToolbar);
        groupToolbar.setNavigationIcon(R.drawable.ic_back_white);
        groupToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFinishAfterTransition();
            }
        });
    }

    private void initIntentData() {
        groupId = getIntent().getStringExtra("groupId");
        color = getIntent().getIntExtra("color", getResources().getColor(R.color.gray3));
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    //这里有问题。。还不知道怎么去解决
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        supportStartPostponedEnterTransition();
        index = data.getIntExtra("index", 0);
    }


    public int getIndex() {
        return index;
    }


    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (fragment == null) {
            fragment = new BeautifulGroupFragment(groupId);
        }
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }

}
