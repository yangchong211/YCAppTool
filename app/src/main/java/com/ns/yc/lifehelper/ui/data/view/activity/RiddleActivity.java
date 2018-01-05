package com.ns.yc.lifehelper.ui.data.view.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.ui.find.view.adapter.PicRiddleAdapter;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/31
 * 描    述：谜语页面
 * 修订历史：
 * ================================================
 */
public class RiddleActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.ll_search)
    FrameLayout llSearch;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.gridView)
    GridView gridView;

    private String[] titles = {"字谜","动物谜语","灯谜","物品谜语","儿童谜语","植物谜语","趣味谜语","数字谜语","搞笑谜语","经典谜语","成语谜语"};
    private String[] ids = {"1","2","3","4","5","6","7","8","9","10","11"};

    @Override
    public int getContentView() {
        return R.layout.activity_other_riddle;
    }

    @Override
    public void initView() {
        initToolBar();
        initGridView();
    }

    private void initToolBar() {
        toolbarTitle.setText("谜语大全");
        llSearch.setVisibility(View.VISIBLE);
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        llSearch.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_search:

                break;
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }


    private void initGridView() {
        final PicRiddleAdapter adapter = new PicRiddleAdapter(RiddleActivity.this,titles);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(titles.length>=position){
                    Intent intent = new Intent(RiddleActivity.this,RiddleDetailActivity.class);
                    intent.putExtra("type",titles[position]);
                    intent.putExtra("classId",ids[position]);
                    startActivity(intent);
                }
            }
        });
    }

}
