package com.ns.yc.lifehelper.ui.other.myTsSc.view;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constantApi.ConstantJsApi;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.ui.other.myTsSc.bean.TangShiDetail;
import com.ns.yc.lifehelper.api.http.tangshi.TangShiModel;
import com.ns.yc.ycutilslib.loadingDialog.ViewLoading;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/29
 * 描    述：唐诗详情页面
 * 修订历史：
 * ================================================
 */
public class TangShiDetailActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.ll_search)
    FrameLayout llSearch;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_explanation)
    TextView tvExplanation;
    @BindView(R.id.tv_appreciation)
    TextView tvAppreciation;
    @BindView(R.id.toolbar)
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
        mLoading = new ViewLoading(this, R.style.Loading,"") {
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
            name = "唐诗";
        }
        toolbarTitle.setText(name);
        toolbar.inflateMenu(R.menu.my_ts_main_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.share:
                        ToastUtils.showShort("分享");
                        break;
                    case R.id.collect:
                        ToastUtils.showShort("收藏");
                        break;
                    case R.id.about:
                        ToastUtils.showShort("关于");
                        break;
                    default:
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
        model.getTangShiDetail(ConstantJsApi.JsAppKey, id)
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
