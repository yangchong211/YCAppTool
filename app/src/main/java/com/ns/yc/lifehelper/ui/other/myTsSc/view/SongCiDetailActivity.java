package com.ns.yc.lifehelper.ui.other.myTsSc.view;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.api.ConstantJsApi;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.ui.other.myTsSc.bean.TangShiDetail;
import com.ns.yc.lifehelper.ui.other.myTsSc.model.TangShiModel;
import com.ns.yc.ycutilslib.loadingDialog.ViewLoading;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/29
 * 描    述：宋词详情页面
 * 修订历史：
 * ================================================
 */
public class SongCiDetailActivity extends BaseActivity implements View.OnClickListener {


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
    private String id;
    private String name;
    private ViewLoading mLoading;


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
        // 添加Loading
        mLoading = new ViewLoading(this, Constant.loadingStyle,"") {
            @Override
            public void loadCancel() {

            }
        };
        if (!mLoading.isShowing()) {
            mLoading.show();
        }
    }

    private void initIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
            name = intent.getStringExtra("name");
        }
    }

    private void initToolBar() {
        if (TextUtils.isEmpty(name)) {
            name = "宋词";
        }
        toolbarTitle.setText(name);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_ts_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {
        getTangShi(id);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }


    private void getTangShi(String id) {
        TangShiModel model = TangShiModel.getInstance(this);
        model.getSongCiDetail(ConstantJsApi.JsAppKey, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TangShiDetail>() {
                    @Override
                    public void onCompleted() {
                        if (mLoading != null && mLoading.isShowing()) {
                            mLoading.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mLoading != null && mLoading.isShowing()) {
                            mLoading.dismiss();
                        }
                    }

                    @Override
                    public void onNext(TangShiDetail tangShiDetail) {
                        if (tangShiDetail != null && tangShiDetail.getResult() != null) {
                            TangShiDetail.ResultBean result = tangShiDetail.getResult();
                            tvTitle.setText(result.getTitle());
                            tvName.setText("作者：" + result.getAuthor() + " / " + result.getType());
                            //tvContent.setText(result.getContent());
                            //tvContent.setText(Html.escapeHtml(result.getContent()));
                            //tvAppreciation.setText(result.getAppreciation());
                            //tvExplanation.setText(result.getExplanation());

                            tvContent.setText(Html.fromHtml(result.getContent()));
                            tvAppreciation.setText(Html.fromHtml(result.getAppreciation()));
                            tvExplanation.setText(Html.fromHtml(result.getExplanation()));
                            if (mLoading != null && mLoading.isShowing()) {
                                mLoading.dismiss();
                            }
                        }
                    }
                });
    }


}
