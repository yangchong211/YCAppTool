package com.ns.yc.lifehelper.ui.other.myTsSc.view;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/29
 * 描    述：唐诗搜索详情页面
 * 修订历史：
 * ================================================
 */
public class TangShiSearchDetailActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.ll_search)
    FrameLayout llSearch;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.tv_explanation)
    TextView tvExplanation;
    @Bind(R.id.tv_appreciation)
    TextView tvAppreciation;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_translation)
    TextView tvTranslation;
    @Bind(R.id.ll_translation)
    LinearLayout llTranslation;
    private String title;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_my_ts_detail;
    }

    @Override
    public void initView() {
        initIntentData();
        initToolBar();
    }

    private void initIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            String type = intent.getStringExtra("type");
            title = intent.getStringExtra("title");
            String appreciation = intent.getStringExtra("appreciation");
            String author = intent.getStringExtra("author");
            String content = intent.getStringExtra("content");
            String explanation = intent.getStringExtra("explanation");
            String translation = intent.getStringExtra("translation");

            llTranslation.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
            tvName.setText("作者：" + author + " / " + type);
            tvContent.setText(Html.fromHtml(content));
            tvAppreciation.setText(Html.fromHtml(appreciation));
            tvExplanation.setText(Html.fromHtml(explanation));
            tvTranslation.setText(Html.fromHtml(translation));
        } else {
            ToastUtils.showShortSafe("没有数据");
        }
    }

    private void initToolBar() {
        if (TextUtils.isEmpty(title)) {
            title = "唐诗";
        }
        toolbarTitle.setText(title);
        toolbar.inflateMenu(R.menu.my_ts_main_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.share:
                        ToastUtils.showShortSafe("分享");
                        break;
                    case R.id.collect:
                        ToastUtils.showShortSafe("收藏");
                        break;
                    case R.id.about:
                        ToastUtils.showShortSafe("关于");
                        break;
                }
                return true;
            }
        });
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
        switch (v.getId()) {
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }


}
